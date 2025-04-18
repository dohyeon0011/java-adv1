package thread.controller.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV1 {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread thread = new Thread(task, "work");
        thread.start();

        sleep(4000);
        log("작업 중단 지시 runFlag = false");
        task.runFlag = false;
    }

    static class MyTask implements Runnable {

        volatile boolean runFlag = true;

        @Override
        public void run() {
            while (runFlag) {
                log("작업 중");    // 총 2번 로그 찍히고
                sleep(3000);
            }
            log("자원 정리");   // main 스레드가 4초 후 runFlag를 false로 바꾸자마자 while문 탈출하는 것이 아닌, 이 스레드가 3초 대기 후 while 조건문을 다시 탈 때 빠져나옴.
            log("자원 종료");
        }
    }
}
