package class13;

public class Main {

    public static void main(String args[]) throws InterruptedException {

        InventoryCounter counter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(counter);
        DecrementingThread decrementingThread = new DecrementingThread(counter);

//        incrementingThread.start();
//        incrementingThread.join();
//
//        decrementingThread.start();
//        decrementingThread.join();

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("Items: " + counter.getItems());

    }

    private static class InventoryCounter {

        private int items = 0;
        Object lock = new Object();

        // Primer approach con metodos synchronized (monitor)
//        public synchronized void increment() {
//            items++;
//        }
//
//        public synchronized void decrement() {
//            items--;
//        }
//        public synchronized int getItems() {
//            return items;
//        }

        // Approach con lock objects
        public void increment() {
            synchronized (lock) {
                items++;
            }
        }

        public void decrement() {
            synchronized (lock) {
                items--;
            }
        }

        public int getItems() {
            synchronized (lock) {
                return items;
            }

        }


    }

    public static class IncrementingThread extends Thread {

        private InventoryCounter counter;

        public IncrementingThread(InventoryCounter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for(int i = 0; i < 10000000; i++) {
                counter.increment();
            }
        }
    }

    public static class DecrementingThread extends Thread {

        private InventoryCounter counter;

        public DecrementingThread(InventoryCounter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for(int i = 0; i < 10000000; i++) {
                counter.decrement();
            }
        }
    }
}
