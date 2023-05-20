import com.benasher44.uuid.uuid4
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.repo.postgresql.MeaningRepoSql
import com.tonyp.dictionarykotlin.repo.postgresql.SqlProperties
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "dictionary"
    private const val SCHEMA = "dictionary"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        initObjects: Collection<DictionaryMeaning> = emptyList(),
        idUuid: () -> String = { uuid4().toString() },
        versionUuid: () -> String = { uuid4().toString() },
    ): MeaningRepoSql {
        return MeaningRepoSql(
            SqlProperties(url, USER, PASS, SCHEMA, dropDatabase = true),
            initObjects,
            idUuid = idUuid,
            versionUuid = versionUuid
        )
    }
}
