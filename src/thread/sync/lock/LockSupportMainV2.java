package thread.sync.lock;

import java.util.concurrent.locks.LockSupport;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class LockSupportMainV2 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ParkTest(), "Thread-1");
        thread1.start();

        // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다.
        sleep(100);
        log("Thread-1 state: " + thread1.getState());
    }

    static class ParkTest implements Runnable {
        @Override
        public void run() {
            log("park 시작");
            LockSupport.parkNanos(2000_000000); // 1밀리초 = 1,000,000나노초(ns), 2초 = 2,000,000,000나노초(ns)
            log("park 종료, state: " + Thread.currentThread().getState());    // runnable
            log("인터럽트 상태: " + Thread.currentThread().isInterrupted());  // false
        }
    }
}
