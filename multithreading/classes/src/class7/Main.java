package class7;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String args[]) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(1000000000L, 3435L, 35435L, 2324L, 4656L, 23L, 2435L, 5566L);

        List<FactorialThread> threads = inputNumbers.stream().map(FactorialThread::new).collect(Collectors.toList());

        threads.forEach( factorialThread -> {
            factorialThread.setDaemon(true);
            factorialThread.start();
        });

        for (FactorialThread factorialThread : threads) {
            factorialThread.join(2000);
        }

        for(int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread thread = threads.get(i);
            if(thread.isFinished()) {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + thread.getResult());
            } else {
                System.out.println("Calculation for " + inputNumbers.get(i) + " is still in progress");
            }
        }

    }

    public static class FactorialThread extends Thread {
        private Long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = Boolean.FALSE;

        public FactorialThread(Long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        public BigInteger factorial(Long inputNumber) {
            BigInteger tempResult = BigInteger.ONE;

            for(Long i = inputNumber; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }

            return tempResult;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
