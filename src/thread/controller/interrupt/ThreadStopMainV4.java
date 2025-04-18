package thread.controller.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV4 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(100);
        log("작업 중단 지시 thread.interrupt()");
        thread.interrupt(); // main 스레드에서 해당 스레드를 인터럽트 상태로 바꾸면
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {    // 인터럽트 상태 변경 O(interrupted() 메서드는 인터럽트 상태를 바꿔줌, 해당 메서드 타고 들어가보면 상태 변경하는 코드가 나옴.)
                log("작업 중");
            }
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());   // True

            try {
                log("자원 정리 시도");
                Thread.sleep(1000); // 이미 인터럽트 상태인데 이때 인터럽트 예외가 터져서 인터럽트 상태가 False가 됨.
                log("자원 정리 완료");
            } catch (InterruptedException e) {
                log("자원 정리 실패 - 자원 정리 중 인터럽트 발생");
                log("work 스레드 인터럽트 상태 3 = " + Thread.currentThread().isInterrupted());  // RUNNABLE 상태가 됨.
            }
            log("작업 종료");
        }
    }
}
