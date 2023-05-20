import SqlTestCompanion.repoUnderTestContainer
import com.tonyp.dictionarykotlin.repo.tests.*
import io.kotest.core.spec.style.FunSpec

class RepoPostgresCreateTest : FunSpec({
    include(
        repoCreateTest(
            repoUnderTestContainer(
                initObjects = InitCreateObjects.initObjects,
                idUuid = { InitCreateObjects.initId.asString() },
                versionUuid = { InitCreateObjects.initVersion.asString() }
            )
        )
    )
})

class RepoPostgresDeleteTest : FunSpec({
    include(
        repoDeleteTest(repoUnderTestContainer(initObjects = InitDeleteObjects.initObjects))
    )
})

class RepoPostgresReadTest : FunSpec({
    include(
        repoReadTest(repoUnderTestContainer(initObjects = InitReadObjects.initObjects))
    )
})

class RepoPostgresSearchTest : FunSpec({
    include(
        repoSearchTest(repoUnderTestContainer(initObjects = InitSearchObjects.initObjects))
    )
})

class RepoPostgresUpdateTest : FunSpec({
    include(
        repoUpdateTest(
            repoUnderTestContainer(
                initObjects = InitUpdateObjects.initObjects,
                versionUuid = { InitUpdateObjects.initVersion.asString() }
            )
        )
    )
})