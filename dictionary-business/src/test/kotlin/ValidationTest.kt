import DataProvider.empties
import DataProvider.invalidIds
import DataProvider.invalidUsernames
import DataProvider.invalidVersions
import DataProvider.invalidWords
import DataProvider.validApproved
import DataProvider.validIds
import DataProvider.validVersions
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipal
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ValidationTest : FreeSpec ({

    "Create with empty word" - {
        empties.map { (description, word) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = word,
                        value = "value",
                        proposedBy = "proposedBy"
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "WORD_IS_EMPTY",
                        message = "Word must not be empty"
                    )
                )
            }
        }
    }

    "Create with invalid word" - {
        invalidWords.map { (description, word) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = word,
                        value = "value",
                        proposedBy = "proposedBy"
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_WORD",
                        message = "Word must be 1-32 russian letters"
                    )
                )
            }
        }
    }

    "Create with valid word" - {
        DataProvider.validWords.map { (description, word) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = word,
                        value = "value",
                        proposedBy = "proposedBy"
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    word = word.trim(),
                    value = "value",
                    proposedBy = "proposedBy"
                )
            }
        }
    }

    "Create with empty value" - {
        empties.map { (description, value) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = "слово",
                        value = value,
                        proposedBy = "proposedBy"
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "VALUE_IS_EMPTY",
                        message = "Value must not be empty"
                    )
                )
            }
        }
    }

    "Create with invalid value" - {
        DataProvider.validValues.map { (description, value) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = "слово",
                        value = value.repeat(257),
                        proposedBy = "proposedBy"
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_VALUE",
                        message = "Value must be 256 symbols at maximum"
                    )
                )
            }
        }
    }

    "Create with valid value" - {
        DataProvider.validValues.map { (description, value) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = "слово",
                        value = value,
                        proposedBy = "proposedBy"
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    word = "слово",
                    value = value.trim(),
                    proposedBy = "proposedBy"
                )
            }
        }
    }

    "Create with empty username" - {
        empties.map { (description, username) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = "слово",
                        value = "value",
                        proposedBy = username
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    word = "слово",
                    value = "value",
                    proposedBy = ""
                )
            }
        }
    }

    "Create with invalid username" - {
        invalidUsernames.map { (description, username) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = "слово",
                        value = "value",
                        proposedBy = username
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_USERNAME",
                        message = "Username must be 1-12 latin letters or underscores total"
                    )
                )
            }
        }
    }

    "Create with valid username" - {
        DataProvider.validUsernames.map { (description, username) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.CREATE,
                    meaningRequest = DictionaryMeaning(
                        word = "слово",
                        value = "value",
                        proposedBy = username
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    word = "слово",
                    value = "value",
                    proposedBy = username.trim()
                )
            }
        }
    }

    "Create with multiple errors" {
        val context = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = invalidWords[0].b,
                value = empties[1].b,
                proposedBy = invalidUsernames[2].b
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(context)

        context.state shouldBe DictionaryState.FAILING
        context.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "VALUE_IS_EMPTY",
                message = "Value must not be empty"
            ),
            DictionaryError(
                code = "INVALID_WORD",
                message = "Word must be 1-32 russian letters"
            ),
            DictionaryError(
                code = "INVALID_USERNAME",
                message = "Username must be 1-12 latin letters or underscores total"
            )
        )
    }

    "Read with empty ID" - {
        empties.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.READ,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "ID_IS_EMPTY",
                        message = "ID must not be empty"
                    )
                )
            }
        }
    }

    "Read with invalid ID" - {
        invalidIds.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.READ,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_ID",
                        message = "ID must be no more than 64 digits, latin characters, or dashes"
                    )
                )
            }
        }
    }

    "Read with valid ID" - {
        validIds.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.READ,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    id = DictionaryMeaningId(id.trim())
                )
            }
        }
    }

    "Delete with empty ID" - {
        empties.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.DELETE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id),
                        version = DictionaryMeaningVersion("version")
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "ID_IS_EMPTY",
                        message = "ID must not be empty"
                    )
                )
            }
        }
    }

    "Delete with invalid ID" - {
        invalidIds.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.DELETE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id),
                        version = DictionaryMeaningVersion("version")
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_ID",
                        message = "ID must be no more than 64 digits, latin characters, or dashes"
                    )
                )
            }
        }
    }

    "Delete with valid ID" - {
        validIds.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.DELETE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id),
                        version = DictionaryMeaningVersion("version")
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    id = DictionaryMeaningId(id.trim()),
                    version = DictionaryMeaningVersion("version")
                )
            }
        }
    }

    "Delete with empty version" - {
        empties.map { (description, version) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.DELETE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId("0"),
                        version = DictionaryMeaningVersion(version)
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "VERSION_IS_EMPTY",
                        message = "Version must not be empty"
                    )
                )
            }
        }
    }

    "Delete with invalid version" - {
        invalidVersions.map { (description, version) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.DELETE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId("0"),
                        version = DictionaryMeaningVersion(version)
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_VERSION",
                        message = "Version must be mo more than 64 digits, latin chars, or dashes"
                    )
                )
            }
        }
    }

    "Delete with valid version" - {
        validVersions.map { (description, version) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.DELETE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId("0"),
                        version = DictionaryMeaningVersion(version)
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    id = DictionaryMeaningId("0"),
                    version = DictionaryMeaningVersion(version.trim())
                )
            }
        }
    }

    "Delete with multiple errors" {
        val context = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningId(empties[0].b),
                version = DictionaryMeaningVersion(invalidVersions[1].b)
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(context)

        context.state shouldBe DictionaryState.FAILING
        context.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ID_IS_EMPTY",
                message = "ID must not be empty"
            ),
            DictionaryError(
                code = "INVALID_VERSION",
                message = "Version must be mo more than 64 digits, latin chars, or dashes"
            )
        )
    }

    "Update with empty ID" - {
        empties.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.UPDATE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id),
                        approved = DictionaryMeaningApproved.FALSE,
                        version = DictionaryMeaningVersion("version")
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "ID_IS_EMPTY",
                        message = "ID must not be empty"
                    )
                )
            }
        }
    }

    "Update with invalid ID" - {
        invalidIds.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.UPDATE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id),
                        approved = DictionaryMeaningApproved.TRUE,
                        version = DictionaryMeaningVersion("version")
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_ID",
                        message = "ID must be no more than 64 digits, latin characters, or dashes"
                    )
                )
            }
        }
    }

    "Update with valid ID" - {
        validIds.map { (description, id) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.UPDATE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId(id),
                        approved = DictionaryMeaningApproved.FALSE,
                        version = DictionaryMeaningVersion("version")
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    id = DictionaryMeaningId(id.trim()),
                    approved = DictionaryMeaningApproved.FALSE,
                    version = DictionaryMeaningVersion("version")
                )
            }
        }
    }

    "Update with empty version" - {
        empties.map { (description, version) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.UPDATE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId("0"),
                        approved = DictionaryMeaningApproved.FALSE,
                        version = DictionaryMeaningVersion(version)
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "VERSION_IS_EMPTY",
                        message = "Version must not be empty"
                    )
                )
            }
        }
    }

    "Update with invalid version" - {
        invalidVersions.map { (description, version) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.UPDATE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId("0"),
                        approved = DictionaryMeaningApproved.TRUE,
                        version = DictionaryMeaningVersion(version)
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldBe DictionaryState.FAILING
                context.errors shouldContainExactlyInAnyOrder listOf(
                    DictionaryError(
                        code = "INVALID_VERSION",
                        message = "Version must be mo more than 64 digits, latin chars, or dashes"
                    )
                )
            }
        }
    }

    "Update with valid version" - {
        validVersions.map { (description, version) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.UPDATE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId("0"),
                        approved = DictionaryMeaningApproved.FALSE,
                        version = DictionaryMeaningVersion(version)
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    id = DictionaryMeaningId("0"),
                    approved = DictionaryMeaningApproved.FALSE,
                    version = DictionaryMeaningVersion(version.trim())
                )
            }
        }
    }

    "Update with empty approved flag" {
        val context = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningId(validIds[0].b),
                version = DictionaryMeaningVersion("version")
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(context)

        context.state shouldBe DictionaryState.FAILING
        context.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "APPROVED_FLAG_IS_EMPTY",
                message = "Approved flag must not be empty"
            )
        )
    }

    "Update with valid approved flag" - {
        validApproved.map { (description, approved) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.UPDATE,
                    meaningRequest = DictionaryMeaning(
                        id = DictionaryMeaningId("123"),
                        approved = approved,
                        version = DictionaryMeaningVersion("version")
                    ),
                    principal = DictionaryPrincipal(
                        name = "t_o_n_y_p",
                        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningValidated shouldBe DictionaryMeaning(
                    id = DictionaryMeaningId("123"),
                    approved = approved,
                    version = DictionaryMeaningVersion("version")
                )
            }
        }
    }

    "Update with multiple errors" {
        val context = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningId(invalidIds[0].b),
                version = DictionaryMeaningVersion(invalidVersions[2].b)
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(context)

        context.state shouldBe DictionaryState.FAILING
        context.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "INVALID_ID",
                message = "ID must be no more than 64 digits, latin characters, or dashes"
            ),
            DictionaryError(
                code = "APPROVED_FLAG_IS_EMPTY",
                message = "Approved flag must not be empty"
            ),
            DictionaryError(
                code = "INVALID_VERSION",
                message = "Version must be mo more than 64 digits, latin chars, or dashes"
            )
        )
    }

    "Search with empty word" - {
        empties.map { (description, word) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.SEARCH,
                    meaningFilterRequest = DictionaryMeaningFilter(
                        word = word,
                        approved = DictionaryMeaningApproved.FALSE
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningFilterValidated shouldBe DictionaryMeaningFilter(
                    word = "",
                    approved = DictionaryMeaningApproved.FALSE
                )
            }
        }
    }

    "Search with invalid word" - {
        invalidWords.map { (description, word) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.SEARCH,
                    meaningFilterRequest = DictionaryMeaningFilter(
                        word = word,
                        approved = DictionaryMeaningApproved.TRUE
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningFilterValidated shouldBe DictionaryMeaningFilter(
                    word = word,
                    approved = DictionaryMeaningApproved.TRUE
                )
            }
        }
    }

    "Search with valid word" - {
        DataProvider.validWords.map { (description, word) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.SEARCH,
                    meaningFilterRequest = DictionaryMeaningFilter(
                        word = word,
                        approved = DictionaryMeaningApproved.FALSE
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningFilterValidated shouldBe DictionaryMeaningFilter(
                    word = word.trim(),
                    approved = DictionaryMeaningApproved.FALSE
                )
            }
        }
    }

    "Search with empty approved flag" {
        val context = DictionaryContext(
            command = DictionaryCommand.SEARCH,
            meaningFilterRequest = DictionaryMeaningFilter(
                word = "word"
            )
        )
        DataProvider.processor().exec(context)

        context.state shouldNotBe DictionaryState.FAILING
        context.errors shouldBe emptyList()
        context.meaningFilterValidated shouldBe DictionaryMeaningFilter(
            word = "word"
        )
    }

    "Search with valid approved flag" - {
        validApproved.map { (description, approved) ->
            description {
                val context = DictionaryContext(
                    command = DictionaryCommand.SEARCH,
                    meaningFilterRequest = DictionaryMeaningFilter(
                        word = "word",
                        approved = approved
                    )
                )
                DataProvider.processor().exec(context)

                context.state shouldNotBe DictionaryState.FAILING
                context.errors shouldBe emptyList()
                context.meaningFilterValidated shouldBe DictionaryMeaningFilter(
                    word = "word",
                    approved = approved
                )
            }
        }
    }

})