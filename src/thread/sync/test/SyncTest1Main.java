package thread.sync.test;

import static util.MyLogger.log;

public class SyncTest1Main {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("결과: " + counter.getCount());
    }

    static class Counter {
        private int count = 0;  // 인스턴스의 멤버 변수(필드), 클래스 변수는 공유 자원(스레드1과 스레드2의 스택 영역에서 힙 영역에 있는 Counter 인스턴스에 있는 공유 자원에 같이 접근.)

        public synchronized void increment() {
            count = count + 1;
            log(Thread.currentThread().getName());
        }

        public int getCount() {
            return count;
        }
    }
}