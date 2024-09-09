package lesson20240820.semaphores;

public class TestSemaphores {
    public static void main(String[] args) throws InterruptedException {

        BlockingQueueSemaphores<String> queue = new BlockingQueueSemaphores<>(1);

        new Thread(() -> {
            System.out.println("Waiting for a message in the queue...");
            try {
                String message = queue.get();
                System.out.println("Received: " + message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        Thread.sleep(5000);
        queue.put("Message 1");
        Thread.sleep(1000);
        queue.put("Message 2");
        Thread.sleep(1000);
        queue.put("Message 3");
    }
}
