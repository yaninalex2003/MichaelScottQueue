package org.example

import java.util.concurrent.atomic.AtomicReference

class MichaelScottQueue<T> {

    private class Node<T>(
        val value: T?,
        val next: AtomicReference<Node<T>?> = AtomicReference(null)
    )

    private val dummy = Node<T>(null)
    private val Href = AtomicReference(dummy)
    private val Tref = AtomicReference(dummy)

    fun enqueue(value: T) {
        val newTail = Node(value)
        while (true) {
            val tail = Tref.get()
            val tailNext = tail.next.get()

            if (tailNext != null) {
                Tref.compareAndSet(tail, tailNext)
            } else {
                if (tail.next.compareAndSet(null, newTail)) {
                    Tref.compareAndSet(tail, newTail)
                    return
                }
            }
        }
    }

    fun dequeue(): T? {
        while (true) {
            val head = Href.get()
            val tail = Tref.get()
            val headNext = head.next.get() ?: throw Exception("dequeue from empty queue")

            if (head == tail) {
                Tref.compareAndSet(tail, headNext)
            } else {
                if (Href.compareAndSet(head, headNext)) {
                    return headNext.value
                }
            }
        }
    }
}
