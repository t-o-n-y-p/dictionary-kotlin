import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipal
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipalRelation
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RepoTest : FunSpec ({

    test("Repo create success (own)") {
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
        DataProvider.processor(DataProvider.successRepo).exec(ctx)

        ctx.meaningRepoPrepare shouldBe DictionaryMeaning(
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t_o_n_y_p",
            approved = DictionaryMeaningApproved.FALSE,
            principalRelation = DictionaryPrincipalRelation.OWN
        )
        ctx.state shouldBe DictionaryState.FINISHING
        ctx.meaningResponse shouldBe DictionaryMeaning(
            id = DictionaryMeaningId("10000000000000000000000000000001"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t_o_n_y_p",
            approved = DictionaryMeaningApproved.FALSE
        )
    }

    test("Repo create success (not own)") {
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
        DataProvider.processor(DataProvider.successRepo).exec(ctx)

        ctx.meaningRepoPrepare shouldBe DictionaryMeaning(
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.FALSE
        )
        ctx.state shouldBe DictionaryState.FINISHING
        ctx.meaningResponse shouldBe DictionaryMeaning(
            id = DictionaryMeaningId("10000000000000000000000000000001"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.FALSE
        )
    }

    test("Repo create error") {
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
        DataProvider.processor(DataProvider.createReadSearchErrorRepo).exec(ctx)

        ctx.meaningRepoPrepare shouldBe DictionaryMeaning(
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.FALSE
        )
        ctx.state shouldBe DictionaryState.FAILING
        ctx.meaningResponse.isEmpty() shouldBe true
        ctx.errors shouldBe IMeaningRepository.Errors.RESULT_ERROR_ALREADY_EXISTS.errors
    }

    test("Repo read success") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.READ,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[0].id
            )
        )
        DataProvider.processor(DataProvider.successRepo).exec(ctx)

        ctx.state shouldBe DictionaryState.FINISHING
        ctx.meaningResponse shouldBe DictionaryMeaningStub.getSearchResult()[0]
    }

    test("Repo read error") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.READ,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningStub.getSearchResult()[0].id
            )
        )
        DataProvider.processor(DataProvider.createReadSearchErrorRepo).exec(ctx)

        ctx.state shouldBe DictionaryState.FAILING
        ctx.meaningResponse.isEmpty() shouldBe true
        ctx.errors shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID.errors
    }

    test("Repo delete success") {
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
        DataProvider.processor(DataProvider.successRepo).exec(ctx)

        ctx.state shouldBe DictionaryState.FINISHING
        ctx.meaningResponse shouldBe DictionaryMeaningStub.getSearchResult()[1]
    }

    test("Repo delete error at the read state") {
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
        DataProvider.processor(DataProvider.createReadSearchErrorRepo).exec(ctx)

        ctx.state shouldBe DictionaryState.FAILING
        ctx.meaningResponse.isEmpty() shouldBe true
        ctx.errors shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID.errors
    }

    test("Repo delete error at the delete state") {
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
        DataProvider.processor(DataProvider.updateDeleteErrorRepo).exec(ctx)

        ctx.state shouldBe DictionaryState.FAILING
        ctx.meaningResponse.isEmpty() shouldBe true
        ctx.errors shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_VERSION.errors
    }

    test("Repo update success") {
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
        DataProvider.processor(DataProvider.successRepo).exec(ctx)

        ctx.meaningRepoPrepare shouldBe DictionaryMeaningStub.getSearchResult()[1].copy(
            approved = DictionaryMeaningApproved.TRUE
        )
        ctx.state shouldBe DictionaryState.FINISHING
        ctx.meaningResponse shouldBe ctx.meaningRequest.copy(version = DictionaryMeaningVersion("qwerty"))
    }

    test("Repo update error at the read state") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningId("456"),
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "t-o-n-y-p",
                approved = DictionaryMeaningApproved.TRUE,
                version = DictionaryMeaningVersion("version")
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor(DataProvider.createReadSearchErrorRepo).exec(ctx)

        ctx.meaningRepoPrepare.isEmpty() shouldBe true
        ctx.state shouldBe DictionaryState.FAILING
        ctx.meaningResponse.isEmpty() shouldBe true
        ctx.errors shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID.errors
    }

    test("Repo update error at the update state") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            meaningRequest = DictionaryMeaning(
                id = DictionaryMeaningId("456"),
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "t-o-n-y-p",
                approved = DictionaryMeaningApproved.TRUE,
                version = DictionaryMeaningVersion("version")
            ),
            principal = DictionaryPrincipal(
                name = "t_o_n_y_p",
                groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
            )
        )
        DataProvider.processor(DataProvider.updateDeleteErrorRepo).exec(ctx)

        ctx.meaningRepoPrepare shouldBe DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.TRUE,
            version = DictionaryMeaningVersion("version")
        )
        ctx.state shouldBe DictionaryState.FAILING
        ctx.meaningResponse.isEmpty() shouldBe true
        ctx.errors shouldBe IMeaningRepository.Errors.RESULT_ERROR_CONCURRENT_MODIFICATION.errors
    }

    test("Repo search success") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.SEARCH,
            meaningFilterRequest = DictionaryMeaningFilter(
                word = "обвал",
                approved = DictionaryMeaningApproved.TRUE
            )
        )
        DataProvider.processor(DataProvider.successRepo).exec(ctx)

        ctx.state shouldBe DictionaryState.FINISHING
        ctx.meaningsResponse shouldBe DictionaryMeaningStub.getSearchResult()
    }

    test("Repo search error") {
        val ctx = DictionaryContext(
            command = DictionaryCommand.SEARCH,
            meaningFilterRequest = DictionaryMeaningFilter(
                word = "обвал",
                approved = DictionaryMeaningApproved.TRUE
            )
        )
        DataProvider.processor(DataProvider.createReadSearchErrorRepo).exec(ctx)

        ctx.state shouldBe DictionaryState.FAILING
        ctx.meaningsResponse shouldBe emptyList()
        ctx.errors shouldBe listOf(DictionaryMeaningStub.getSearchError())
    }

})