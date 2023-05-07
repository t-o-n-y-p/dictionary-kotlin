import com.tonyp.dictionarykotlin.log.v1.dictionaryLogger
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LoggerTest : FunSpec ({

    val logId = "test-logger"

    fun invokeLogger(block: suspend () -> Unit): ByteArrayOutputStream {
        val outputStreamCaptor = ByteArrayOutputStream().apply {
            System.setOut(PrintStream(this))
        }

        try {
            runBlocking {
                dictionaryLogger(this::class).doWithLogging(logId) {
                    block()
                }
            }
        } catch (ignore: RuntimeException) {
        }

        return outputStreamCaptor
    }

    fun invokeErrorLogger(block: suspend () -> Unit): ByteArrayOutputStream {
        val outputStreamCaptor = ByteArrayOutputStream().apply {
            System.setOut(PrintStream(this))
        }

        try {
            runBlocking {
                dictionaryLogger(this::class).doWithErrorLogging(logId) {
                    block()
                }
            }
        } catch (ignore: RuntimeException) {
        }

        return outputStreamCaptor
    }

    test("Do something with logger") {
        val output = invokeLogger {
            println("Some action")
        }.toString()

        output shouldContain "Started kotlinx.coroutines.BlockingCoroutine $logId"
        output shouldContain "Some action"
        output shouldContain "Finished kotlinx.coroutines.BlockingCoroutine $logId"
    }

    test("Do something with logger: exception") {
        val output = invokeLogger {
            throw RuntimeException("Some action")
        }.toString()

        output shouldContain "Started kotlinx.coroutines.BlockingCoroutine $logId"
        output shouldContain "Some action"
        output shouldContain "Failed kotlinx.coroutines.BlockingCoroutine $logId"
    }

    test("Do something with error logger") {
        val output = invokeErrorLogger {
            println("Some action")
        }.toString()

        output shouldNotContain "Started kotlinx.coroutines.BlockingCoroutine $logId"
        output shouldContain "Some action"
        output shouldNotContain "Finished kotlinx.coroutines.BlockingCoroutine $logId"
    }

    test("Do something with error logger: exception") {
        val output = invokeErrorLogger {
            throw RuntimeException("Some action")
        }.toString()

        output shouldNotContain "Started kotlinx.coroutines.BlockingCoroutine $logId"
        output shouldContain "Some action"
        output shouldContain "Failed kotlinx.coroutines.BlockingCoroutine $logId"
    }

})