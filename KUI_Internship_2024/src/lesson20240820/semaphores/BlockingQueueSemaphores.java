package lesson20240820.semaphores;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BlockingQueueSemaphores<T> {

    private final List<T> items = new LinkedList<>();
    private final Semaphore itemsSemaphore;
    private final Semaphore spacesSemaphore;
    private final Object lock = new Object();

    public BlockingQueueSemaphores(int capacity) {
        itemsSemaphore = new Semaphore(0);
        spacesSemaphore = new Semaphore(capacity);
    }

    public void put(T item) throws InterruptedException {
        spacesSemaphore.acquire();
        synchronized (lock) {
            items.add(item);
        }
        itemsSemaphore.release();
    }

    public T get() throws InterruptedException {
        itemsSemaphore.acquire();
        T item;
        synchronized (lock) {
            item = items.remove(0);
        }
        spacesSemaphore.release();
        return item;
    }
}
