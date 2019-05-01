package class4;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            System.out.println("We are now in thread: " + Thread.currentThread().getName());
            System.out.println("Priority: " + Thread.currentThread().getPriority());
            throw new RuntimeException("runtime");
        });

        thread.setName("new worker thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Critical exception thread: " + t.getName() + "exception: " + e.getMessage());
        });

        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

        Thread.sleep(1000);
    }
}
