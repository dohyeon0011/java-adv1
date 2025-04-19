package thread.sync.test;

import static util.MyLogger.log;

public class SyncTest2Main {
    public static void main(String[] args) throws InterruptedException {
        MyCounter myCounter = new MyCounter();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                myCounter.count();
            }
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();
    }

    static class MyCounter {

        public void count() {
            // 지역 변수는 각각의 스레드가 가지는 스택 영역에 지역 변수가 올라가기 때문에, 스레드끼리 공유하는 자원이 아니라서 동시성 문제가 발생하지 않음.
            int localValue = 0;
            for (int i = 0; i < 1000; i++) {
                localValue = localValue + 1;
            }
            log("결과: " + localValue);
        }
    }

}