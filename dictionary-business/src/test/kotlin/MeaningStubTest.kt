import DataProvider.processor
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MeaningStubTest : FunSpec ({

    test("Create stub success") {
        val successStubContext = DictionaryContext(
            command = DictionaryCommand.CREATE,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.SUCCESS
        )
        processor.exec(successStubContext)

        successStubContext.state shouldBe DictionaryState.FINISHING
        successStubContext.meaningResponse shouldBe DictionaryMeaningStub.getPending()
    }

    test("Create stub error") {
        val errorStubContext = DictionaryContext(
            command = DictionaryCommand.CREATE,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.CANNOT_CREATE
        )
        processor.exec(errorStubContext)

        errorStubContext.state shouldBe DictionaryState.FAILING
        errorStubContext.errors shouldBe listOf(DictionaryMeaningStub.getCreateError())
    }

    test("Read stub success") {
        val successStubContext = DictionaryContext(
            command = DictionaryCommand.READ,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.SUCCESS
        )
        processor.exec(successStubContext)

        successStubContext.state shouldBe DictionaryState.FINISHING
        successStubContext.meaningResponse shouldBe DictionaryMeaningStub.getApproved()
    }

    test("Read stub error") {
        val errorStubContext = DictionaryContext(
            command = DictionaryCommand.READ,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.CANNOT_READ
        )
        processor.exec(errorStubContext)

        errorStubContext.state shouldBe DictionaryState.FAILING
        errorStubContext.errors shouldBe listOf(DictionaryMeaningStub.getReadError())
    }

    test("Update stub success") {
        val successStubContext = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.SUCCESS
        )
        processor.exec(successStubContext)

        successStubContext.state shouldBe DictionaryState.FINISHING
        successStubContext.meaningResponse shouldBe DictionaryMeaningStub.getApproved()
    }

    test("Update stub error") {
        val errorStubContext = DictionaryContext(
            command = DictionaryCommand.UPDATE,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.CANNOT_UPDATE
        )
        processor.exec(errorStubContext)

        errorStubContext.state shouldBe DictionaryState.FAILING
        errorStubContext.errors shouldBe listOf(DictionaryMeaningStub.getUpdateError())
    }

    test("Delete stub success") {
        val successStubContext = DictionaryContext(
            command = DictionaryCommand.DELETE,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.SUCCESS
        )
        processor.exec(successStubContext)

        successStubContext.state shouldBe DictionaryState.FINISHING
        successStubContext.meaningResponse shouldBe DictionaryMeaningStub.getPending()
    }

    test("Delete stub error") {
        val errorStubContext = DictionaryContext(
            command = DictionaryCommand.DELETE,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.CANNOT_DELETE
        )
        processor.exec(errorStubContext)

        errorStubContext.state shouldBe DictionaryState.FAILING
        errorStubContext.errors shouldBe listOf(DictionaryMeaningStub.getDeleteError())
    }

    test("Search stub success") {
        val successStubContext = DictionaryContext(
            command = DictionaryCommand.SEARCH,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.SUCCESS
        )
        processor.exec(successStubContext)

        successStubContext.state shouldBe DictionaryState.FINISHING
        successStubContext.meaningsResponse shouldBe DictionaryMeaningStub.getSearchResult()
    }

    test("Search stub error") {
        val errorStubContext = DictionaryContext(
            command = DictionaryCommand.SEARCH,
            workMode = DictionaryWorkMode.STUB,
            stubCase = DictionaryStub.CANNOT_SEARCH
        )
        processor.exec(errorStubContext)

        errorStubContext.state shouldBe DictionaryState.FAILING
        errorStubContext.errors shouldBe listOf(DictionaryMeaningStub.getSearchError())
    }

})