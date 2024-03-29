package com.tonyp.dictionarykotlin.meaning.app.base

import com.auth0.jwk.Jwk
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig
import io.ktor.http.auth.*
import java.net.URL
import java.security.interfaces.RSAPublicKey

fun HttpAuthHeader.resolveAlgorithm(authConfig: DictionaryAuthConfig): Algorithm = when {
    authConfig.certUrl != null -> resolveAlgorithmKeycloak(authConfig)
    else -> Algorithm.HMAC256(authConfig.secret)
}

private val keys = mutableMapOf<String, Jwk>()

fun HttpAuthHeader.resolveAlgorithmKeycloak(authConfig: DictionaryAuthConfig): Algorithm {
    val tokenString = this.render().replace(this.authScheme, "").trim()
    if (tokenString.isBlank()) {
        throw IllegalArgumentException("Request contains no proper Authorization header")
    }
    val token = try {
        JWT.decode(tokenString)
    } catch (e: Exception) {
        throw IllegalArgumentException("Cannot parse JWT token from request", e)
    }
    val algo = token.algorithm
    if (algo != "RS256") {
        throw IllegalArgumentException("Wrong algorithm in JWT ($algo). Must be ...")
    }
    val keyId = token.keyId
    val jwk = keys[keyId] ?: run {
        val provider = UrlJwkProvider(URL(authConfig.certUrl))
        val jwk = provider.get(keyId)
        keys[keyId] = jwk
        jwk
    }
    val publicKey = jwk.publicKey
    if (publicKey !is RSAPublicKey) {
        throw IllegalArgumentException("Key with ID was found in KEYS but is not a RSA-key.")
    }
    return Algorithm.RSA256(publicKey, null)
}
