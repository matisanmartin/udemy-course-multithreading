package class15;

import java.util.Random;

public class Main {

    public static void main(String args[]) {

        Metrics metrics = new Metrics();
        BusinessLogicThread businessLogicThread1 = new BusinessLogicThread(metrics);
        BusinessLogicThread businessLogicThread2 = new BusinessLogicThread(metrics);
        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogicThread1.start();
        businessLogicThread2.start();
        metricsPrinter.start();

    }

    public static class BusinessLogicThread extends Thread {

        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogicThread(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {

            while(true) {
                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {

                }
                long endTime = System.currentTimeMillis();

                metrics.addSample(endTime-startTime);
            }

        }
    }

    public static class MetricsPrinter extends Thread {
        private Metrics metrics;

        private MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }

                double currentAverage = metrics.getAverage();
                System.out.println("Current average: " + currentAverage);
            }
        }
    }

    public static class Metrics {
        private long count = 0;
        private volatile double average = 0.0;

        public synchronized void addSample(long sample) {
            double currentSum = average*count;
            count++;
            average = (currentSum + sample)/count;
        }

        public double getAverage() {
            return average;
        }
    }
}
