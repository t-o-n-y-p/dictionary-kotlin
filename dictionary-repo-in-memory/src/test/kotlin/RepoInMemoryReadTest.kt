import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.InitReadObjects
import com.tonyp.dictionarykotlin.repo.tests.repoReadTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemoryReadTest : FunSpec ({

    include(
        repoReadTest(MeaningRepoInMemory(initObjects = InitReadObjects.initObjects))
    )

})