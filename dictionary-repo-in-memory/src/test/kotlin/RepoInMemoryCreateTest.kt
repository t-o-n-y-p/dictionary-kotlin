import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.InitCreateObjects
import com.tonyp.dictionarykotlin.repo.tests.repoCreateTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemoryCreateTest : FunSpec ({

    include(
        repoCreateTest(
            MeaningRepoInMemory(
                initObjects = InitCreateObjects.initObjects,
                idUuid = { InitCreateObjects.initId.asString() },
                versionUuid = { InitCreateObjects.initVersion.asString() }
            )
        )
    )

})