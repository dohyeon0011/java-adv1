package thread.controller.yield;

import thread.start.HelloRunnable;

import static util.ThreadUtils.sleep;

public class YieldMain {

    static final int THREAD_COUNT = 1000;

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(new MyRunnable());
            thread.start();
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " - " + i);

                // 1. empty
//                sleep(1);   // 2. sleep (1000개의 스레드가 보통은 평균적으로 1초 대기하면서 번갈아 가면서 실행하게 됨.(RUNNABLE <-> TIMED_WAITING 상태가 반복되기 때문(스케줄링 큐에 넣었다 뺐다 해서), +다른 스레드가 다 대기 상태라 내 스레드는 잠깐 실행해도 되는 짜투리 시간이 있음에도 불구하고 무조건적으로 대기하게 되어버려서))
                Thread.yield(); // 3. yield (얘는 상대적으로 하나의 스레드가 쭉 연달아 실행되다가 다른 스레드로 넘어감.(RUNNABLE 상태를 유지하면서 다른 스레드에게 CPU를 양보함, RUNNABLE 상태를 유지하기 때문에 양보해줘야 할 스레드가 없으면 본인 스레드가 계속 실행 될 수 있기 때문))
            }
        }
    }
}
