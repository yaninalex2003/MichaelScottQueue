package org.example

import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*

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

internal class MSQueueTestImpl : MSQueueTest {
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

    @Test
    fun stressOptionsSequentialSpecification() =
        StressOptions()
            .sequentialSpecification(SequentialQueue::class.java)
            .check(MSQueueTestImpl::class)

    @Test
    fun modelChecking() =
        ModelCheckingOptions()
            .check(MSQueueTestImpl::class)

    @Test
    fun modelCheckingObstructionFreedom() =
        ModelCheckingOptions()
            .checkObstructionFreedom()
            .check(MSQueueTestImpl::class)

    //region Generated with Explyt. Tests for MichaelScottQueue

    @Test
    fun `enqueueing an item into an empty queue`() {
        val queue = MichaelScottQueue<Int>()
        queue.enqueue(1)
        assertEquals(1, queue.dequeue())
        assertThrows(Exception::class.java) { queue.dequeue() }
    }

    @Test
    fun `enqueueing multiple items into the queue`() {
        val queue = MichaelScottQueue<Int>()
        queue.enqueue(1)
        queue.enqueue(2)
        queue.enqueue(3)

        assertEquals(1, queue.dequeue())
        assertEquals(2, queue.dequeue())
        assertEquals(3, queue.dequeue())
        assertThrows(Exception::class.java) { queue.dequeue() }
    }

    @Test
    fun `dequeueing an item from a queue with multiple items`() {
        val queue = MichaelScottQueue<Int>()
        queue.enqueue(1)
        queue.enqueue(2)
        queue.enqueue(3)

        val removedItem = queue.dequeue()
        assertEquals(1, removedItem)
        assertEquals(2, queue.dequeue())
        assertEquals(3, queue.dequeue())
        assertThrows(Exception::class.java) { queue.dequeue() }
    }

    @Test
    fun `dequeueing from an empty queue`() {
        val queue = MichaelScottQueue<Int>()
        assertThrows(Exception::class.java) { queue.dequeue() }
    }

    @Test
    fun `dequeueing all items from the queue`() {
        val queue = MichaelScottQueue<Int>()
        queue.enqueue(1)
        queue.enqueue(2)
        queue.enqueue(3)

        assertEquals(1, queue.dequeue())
        assertEquals(2, queue.dequeue())
        assertEquals(3, queue.dequeue())
        assertThrows(Exception::class.java) { queue.dequeue() }
    }

    //endregion
}

class SequentialQueue {
    private val queue = LinkedList<Int>()

    fun enqueue(x: Int) {
        queue.add(x)
    }

    fun dequeue(): Int {
        return queue.poll() ?: throw Exception("dequeue from empty queue")
    }
}
