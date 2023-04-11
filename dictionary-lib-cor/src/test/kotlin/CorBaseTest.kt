import com.tonyp.dictionarykotlin.cor.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CorBaseTest : FunSpec ({

    test("Execute handle with right condition") {
        val worker = CorWorker<TestContext>(
            title = "title",
            blockHandle = { history += "done" }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        ctx.history shouldBe "done"
    }

    test("Execute handle with wrong condition") {
        val worker = CorWorker<TestContext>(
            title = "title",
            blockOn = { status == CorStatuses.ERROR },
            blockHandle = { history += "done" }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        ctx.history shouldBe ""
    }

    test("Execute handle with exception") {
        val worker = CorWorker<TestContext>(
            title = "title",
            blockExcept = { e -> history += e.message },
            blockHandle = { throw RuntimeException("error") }
        )
        val ctx = TestContext()
        worker.exec(ctx)
        ctx.history shouldBe "error"
    }

    test("Execute chain") {
        val chain = CorChain<TestContext>(
            title = "chain",
            handler = ::executeSequential,
            execs = listOf(
                CorWorker(
                    title = "first",
                    blockOn = { status == CorStatuses.NONE },
                    blockHandle = { history += "first; " }
                ),
                CorWorker(
                    title = "second",
                    blockOn = { status == CorStatuses.NONE },
                    blockHandle = { history += "second" }
                )
            )
        )
        val ctx = TestContext()
        chain.exec(ctx)
        ctx.history shouldBe "first; second"
    }

    test("Execute chain created with DSL with right condition") {
        val chain = chain<TestContext> {
            worker {
                title = "title"
                handle {
                    history += "done"
                }
            }
        }.build()
        val ctx = TestContext()
        chain.exec(ctx)
        ctx.history shouldBe "done"
    }

    test("Execute chain with different conditions created with DSL") {
        val chain = chain<TestContext> {
            worker {
                title = "first"
                on { status == CorStatuses.ERROR }
                handle {
                    history += "first; "
                }
            }
            worker {
                title = "second"
                on { status == CorStatuses.NONE }
                handle {
                    history += "second; "
                    status = CorStatuses.FAILING
                }
            }
            worker {
                title = "third"
                on { status == CorStatuses.FAILING }
                handle {
                    history += "third"
                }
            }
        }.build()
        val ctx = TestContext()
        chain.exec(ctx)
        ctx.history shouldBe "second; third"
    }

    test("Execute chain created with DSL with exception") {
        val chain = chain<TestContext> {
            worker {
                title = "title"
                except {
                    history += it.message
                }
                handle {
                    throw RuntimeException("error")
                }
            }
        }.build()
        val ctx = TestContext()
        chain.exec(ctx)
        ctx.history shouldBe "error"
    }

    test("Execute chain created with DSL with rethrown exception") {
        val chain = chain<TestContext> {
            worker {
                title = "title"
                handle {
                    throw RuntimeException("error")
                }
            }
        }.build()
        val ctx = TestContext()
        shouldThrow<RuntimeException> {
            chain.exec(ctx)
        }
    }

    test("Execute complex chains") {
        val chain = chain<TestContext> {
            worker {
                on { status == CorStatuses.NONE }
                except { status = CorStatuses.ERROR }
                handle { status = CorStatuses.RUNNING }
            }
            sequence {
                on { status == CorStatuses.RUNNING }
                worker {
                    title = "Add 1"
                    handle {
                        some += 1
                    }
                }
                worker {
                    title = "Add 2"
                    handle {
                        some += 2
                    }
                }
                worker {
                    title = "Add 3"
                    handle {
                        some += 3
                    }
                }
                worker {
                    title = "Add 4"
                    handle {
                        some += 4
                    }
                }
            }
            parallel {
                on { status == CorStatuses.RUNNING }
                worker {
                    title = "Add 5"
                    handle {
                        some += 5
                    }
                }
                worker {
                    title = "History"
                    handle {
                        history += "parallel"
                    }
                }
            }
        }.build()

        val ctx = TestContext()
        chain.exec(ctx)
        ctx.some shouldBe 15
        ctx.history shouldBe "parallel"
    }

})