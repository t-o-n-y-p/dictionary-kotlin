import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.repoDeleteInitObjects
import com.tonyp.dictionarykotlin.repo.tests.repoDeleteTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemoryDeleteTest : FunSpec ({

    include(
        repoDeleteTest(MeaningRepoInMemory(initObjects = repoDeleteInitObjects))
    )

})