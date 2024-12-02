package org.example

import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test

internal interface MSQueueTest {
    val queue: MichaelScottQueue<Int>

    @Operation
    fun enqueue(x: Int) {
        queue.enqueue(x)
    }

    @Operation
    fun dequeue(): Int? {
        return queue.dequeue()
    }

}

internal class MSQueueTestImpl: MSQueueTest {
    override val queue = MichaelScottQueue<Int>()
}

class MichaelScottQueueTest {
    @Test
    fun stressOptions() =
        StressOptions()
            .check(MSQueueTestImpl::class)

    @Test
    fun stressOptionsSingleThread() =
        StressOptions()
            .threads(1)
            .check(MSQueueTestImpl::class)
}
