import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.repoUpdateInitObjects
import com.tonyp.dictionarykotlin.repo.tests.repoUpdateTest
import io.kotest.core.spec.style.FunSpec

class RepoInMemoryUpdateTest : FunSpec ({

    include(
        repoUpdateTest(MeaningRepoInMemory(initObjects = repoUpdateInitObjects))
    )

})