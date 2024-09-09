package lesson20240820.locks;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueLocks<T> {

    private final List<T> items = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final int capacity;

    public BlockingQueueLocks(int capacity) {
        this.capacity = capacity;
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (items.size() == capacity) {
                notFull.await();
            }
            items.add(item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (items.isEmpty()) {
                notEmpty.await();
            }
            T item = items.remove(0);
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}
