import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.*
import io.kotest.core.spec.style.FunSpec

class RepoInMemoryCreateTest : FunSpec({
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

class RepoInMemoryDeleteTest : FunSpec ({
    include(
        repoDeleteTest(MeaningRepoInMemory(initObjects = InitDeleteObjects.initObjects))
    )
})

class RepoInMemoryReadTest : FunSpec ({
    include(
        repoReadTest(MeaningRepoInMemory(initObjects = InitReadObjects.initObjects))
    )
})

class RepoInMemorySearchTest : FunSpec ({
    include(
        repoSearchTest(MeaningRepoInMemory(initObjects = InitSearchObjects.initObjects))
    )
})

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