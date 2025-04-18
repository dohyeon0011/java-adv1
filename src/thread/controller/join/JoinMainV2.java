package thread.controller.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV2 {

    public static void main(String[] args) {
        log("Start");

        SumTask sumTask1 = new SumTask(1, 50);
        SumTask sumTask2 = new SumTask(51, 100);

        Thread thread1 = new Thread(sumTask1, "thread-1");
        Thread thread2 = new Thread(sumTask2, "thread-2");

        thread1.start();
        thread2.start();

        log("main 스레드 sleep()");
        sleep(3000);
        log("main 스레드 깨어남");

        int sumAll = sumTask1.result + sumTask2.result; // 각 스레드의 연산 시간이 2초정도가 걸린다 했을 때, main 스레드를 3초 대기 시키고 연산하면 각 스레드의 값들을 받을 수 있게됨.
        System.out.println("sumAll = " + sumAll);

        log("End");
    }

    static class SumTask implements Runnable {

        int startValue;
        int endValue;
        int result = 0;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            log("작업 시작");
            sleep(2000);
            int sum = 0;

            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            result = sum;
            log("작업 완료 result = " + result);
        }
    }
}
