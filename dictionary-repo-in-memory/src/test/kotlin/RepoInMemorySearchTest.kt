import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.InitSearchObjects
import com.tonyp.dictionarykotlin.repo.tests.repoSearchTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemorySearchTest : FunSpec ({

    include(
        repoSearchTest(MeaningRepoInMemory(initObjects = InitSearchObjects.initObjects))
    )

})