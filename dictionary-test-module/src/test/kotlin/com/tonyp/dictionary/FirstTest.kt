package com.tonyp.dictionary

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FirstTest : FunSpec({

    test("No meanings found") {
        val dictionary = Dictionary()
        val outputMessage = dictionary.getMeaningsMessage("растение")
        outputMessage shouldBe "Не знаю значений этого слова."
    }

    test("One meaning found") {
        val dictionary = Dictionary()
        val outputMessage = dictionary.getMeaningsMessage("цветок")
        outputMessage shouldBe "цветок:\n\nорган размножения растений с венчиком из лепестков вокруг пестика и тычинок"
    }

    test("Multiple meanings found") {
        val dictionary = Dictionary()
        val outputMessage = dictionary.getMeaningsMessage("трава")
        outputMessage shouldBe """трава:
            |
            |1. травянистые растения, обладающие лечебными свойствами, входящие в лекарственные сборы
            |2. о чем-н. не имеющем вкуса, безвкусном (разг.)""".trimMargin()
    }

})