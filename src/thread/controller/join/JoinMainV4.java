
package thread.controller.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV4 {

    public static void main(String[] args) throws InterruptedException {
        log("Start");

        SumTask sumTask1 = new SumTask(1, 50);

        Thread thread1 = new Thread(sumTask1, "thread-1");

        thread1.start();

        // 특정 시간 만큼만 스레드가 종료될 때 까지 대기(TIMED_WAITING)
        log("join(1000) - main 스레드가 thread1 종료까지 1초만 대기");
        thread1.join(1000);
        log("main 스레드 join() 대기 완료");

        int result = sumTask1.result;   // main 스레드 1초만 대기 후 여기로 넘어옴.
        System.out.println("result = " + result);

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
