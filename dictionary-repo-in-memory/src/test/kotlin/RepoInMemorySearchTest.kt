import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.repoSearchInitObjects
import com.tonyp.dictionarykotlin.repo.tests.repoSearchTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemorySearchTest : FunSpec ({

    include(
        repoSearchTest(MeaningRepoInMemory(initObjects = repoSearchInitObjects))
    )

})