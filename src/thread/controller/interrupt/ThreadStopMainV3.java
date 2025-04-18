package thread.controller.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * 자바에서 인터럽트 예외가 한 번 발생하면, 스레드의 인터럽트 상태를 다시 정상(false)으로 돌리는 것은 이런 이유 때문이다.
 * 스레드의 인터럽트 상태를 정상으로 돌리지 않으면, 이후에도 계속 인터럽트가 발생하게 된다.
 * 인터럽트의 목적을 달성하면 인터럽트 상태를 다시 정상으로 돌려두어야 한다.
 */
public class ThreadStopMainV3 {

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
            while (!Thread.currentThread().isInterrupted()) {    // 인터럽트 상태가 아니면(인터럽트 상태 변경 X, 왜냐면 isInterrupted()는 인터럽트 상태만 확인하기 때문, 해당 메서드 타고 들어가면 상태 확인만 하는 메서드임을 알 수 있음.)
                log("작업 중");
            }
            log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted());

            try {   // 결과적으로 자원 정리를 하는 도중에 인터럽트가 발생해서, 자원 정리에 실패한다.
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
