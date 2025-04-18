package thread.controller.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV2 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(4000);
        log("작업 중단 지시 thread.interrupt()");
        thread.interrupt(); // main 스레드에서 해당 스레드를 인터럽트 상태로 바꾸면
        log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted());
    }

    static class MyTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {  // main 스레드가 작업 중단 지시 내리자마자 바로 -catch 문으로 탈출함.(이때는 인터럽트 상태 체크 안함)
                    log("작업 중");
                    Thread.sleep(3000); // 대기 중에 인터럽트 상태가 되면 예외 터짐.(이때 인터럽트 상태를 풀어버림, 해당 메서드를 타고 들어가보면 상태를 변경하는 코드가 나옴.)
                }
            } catch (InterruptedException e) {
                log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());   // 인터럽트 상태가 풀리게 됨.(인터럽트를 걸어놨는데 인터럽트 예외가 터져서)
                log("interrupt message = " + e.getMessage());
                log("state = " + Thread.currentThread().getState());    // 대기 상태에서 깨어나서 TIMED_WAITING -> RUNNABLE 상태가 됨.
            }

            log("자원 정리");
            log("자원 종료");
        }
    }
}
