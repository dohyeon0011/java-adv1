package thread.controller.join;

import util.MyLogger;
import util.ThreadUtils;

import static util.MyLogger.*;
import static util.ThreadUtils.*;

public class JoinMainV1 {

    public static void main(String[] args) {
        log("Start");

        SumTask sumTask1 = new SumTask(1, 50);
        SumTask sumTask2 = new SumTask(51, 100);

        Thread thread1 = new Thread(sumTask1, "thread-1");
        Thread thread2 = new Thread(sumTask2, "thread-2");

        thread1.start();
        thread2.start();

        int sumAll = sumTask1.result + sumTask2.result; // 0이 나옴.(왜냐? main 스레드는 다른 스레드가 종료될 때 까지 기다려주지 않기 때문에)
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
