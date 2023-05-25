import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipal
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipalRelation
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class AuthTest : FunSpec ({

    test("User creates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe true
        ctx.state shouldNotBe DictionaryState.FAILING
    }

    test("User creates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Admin creates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe true
        ctx.state shouldNotBe DictionaryState.FAILING
    }

    test("Admin creates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe true
        ctx.state shouldNotBe DictionaryState.FAILING
    }

    test("Banned user creates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned user creates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned admin creates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned admin creates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.CREATE,
            meaningRequest = DictionaryMeaning(
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest"
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("User deletes his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[0].id,
                version = DictionaryMeaningStub.getSearchResult()[0].version
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("User deletes not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[1].id,
                version = DictionaryMeaningStub.getSearchResult()[1].version
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Admin deletes his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[0].id,
                version = DictionaryMeaningStub.getSearchResult()[0].version
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe true
        ctx.state shouldNotBe DictionaryState.FAILING
    }

    test("Admin deletes not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[1].id,
                version = DictionaryMeaningStub.getSearchResult()[1].version
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe true
        ctx.state shouldNotBe DictionaryState.FAILING
    }

    test("Banned user deletes his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[0].id,
                version = DictionaryMeaningStub.getSearchResult()[0].version
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned user deletes not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[1].id,
                version = DictionaryMeaningStub.getSearchResult()[1].version
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned admin deletes his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[0].id,
                version = DictionaryMeaningStub.getSearchResult()[0].version
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned admin deletes not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.DELETE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[1].id,
                version = DictionaryMeaningStub.getSearchResult()[1].version
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("User updates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[0].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("User updates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[1].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Admin updates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[0].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe true
        ctx.state shouldNotBe DictionaryState.FAILING
    }

    test("Admin updates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[1].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe true
        ctx.state shouldNotBe DictionaryState.FAILING
    }

    test("Banned user updates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[0].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned user updates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[1].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned admin updates his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[0].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "unittest",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.OWN
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

    test("Banned admin updates not his own meaning") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaningStub.getSearchResult()[1].copy(
                approved = DictionaryMeaningApproved.TRUE
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
            )
        )
        DataProvider.processor().exec(ctx)

        ctx.meaningRepoPrepare.principalRelation shouldBe DictionaryPrincipalRelation.NONE
        ctx.permitted shouldBe false
        ctx.state shouldBe DictionaryState.FAILING
        ctx.errors shouldContainExactlyInAnyOrder listOf(
            DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            )
        )
    }

})