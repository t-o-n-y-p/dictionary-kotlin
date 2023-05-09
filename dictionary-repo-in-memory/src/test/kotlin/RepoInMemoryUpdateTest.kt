import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.InitUpdateObjects
import com.tonyp.dictionarykotlin.repo.tests.repoUpdateTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemoryUpdateTest : FunSpec ({

    include(
        repoUpdateTest(
            MeaningRepoInMemory(
                initObjects = InitUpdateObjects.initObjects,
                versionUuid = { InitUpdateObjects.initVersion.asString() }
            )
        )
    )

})