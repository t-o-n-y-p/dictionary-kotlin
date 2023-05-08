import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.repoCreateInitObjects
import com.tonyp.dictionarykotlin.repo.tests.repoCreateTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemoryCreateTest : FunSpec ({

    include(
        repoCreateTest(MeaningRepoInMemory(initObjects = repoCreateInitObjects))
    )

})