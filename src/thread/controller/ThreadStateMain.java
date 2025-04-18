package thread.controller;

import util.MyLogger;

import static util.MyLogger.*;

public class ThreadStateMain {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new MyRunnable(), "myThread");
        log("myThread.state1 = " + thread.getState());
        log("myThread.start()");    // NEW
        thread.start();
        Thread.sleep(1000);
        log("myThread.state3 = " + thread.getState());  // TIMED_WAITING(자고 있는 스레드가 직접 찍으면 자고 있기 때문에 깨어나고 난 뒤에 찍게됨. 그러므로 옆에 있는 스레드가 찍어줘야함.)
        Thread.sleep(4000);
        log("myThread.state5 = " + thread.getState());  // TERMINATED
        log("end");
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                log("start");
                log("myThread.state2 = " + Thread.currentThread().getState());  // RUNNABLE
                log("sleep() start");
                Thread.sleep(3000);
                log("sleep() end");
                log("myThread.state4 = " + Thread.currentThread().getState());  // RUNNABLE
                log("end");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
