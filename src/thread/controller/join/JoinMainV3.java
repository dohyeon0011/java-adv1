package thread.controller.join;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class JoinMainV3 {

    public static void main(String[] args) throws InterruptedException {
        log("Start");

        SumTask sumTask1 = new SumTask(1, 50);
        SumTask sumTask2 = new SumTask(51, 100);

        Thread thread1 = new Thread(sumTask1, "thread-1");
        Thread thread2 = new Thread(sumTask2, "thread-2");

        thread1.start();
        thread2.start();

        // 각 스레드가 종료될 때 까지 대기
        log("join() - main 스레드가 thread1, thread2 종료까지 대기");
        thread1.join(); // 이 스레드가 끝날 때 까지 여기서 main 스레드가 계속 대기함.(WAITING 상태가 됨, 이 스레드가 TERMINATED 상태가 될 때 까지 무기한 대기하기 때문)
        thread2.join(); // 이 스레드가 끝날 때 까지 여기서 main 스레드가 계속 대기함.(WAITING 상태가 됨, 이 스레드가 TERMINATED 상태가 될 때 까지 무기한 대기하기 때문)
        log("main 스레드 join() 대기 완료");

        int sumAll = sumTask1.result + sumTask2.result;
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
