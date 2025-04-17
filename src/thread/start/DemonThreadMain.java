package thread.start;

/**
 * 데몬 스레드
 *  -백그라운드에서 보조적인 작업을 수행한다.
 *  -모든 user 스레드가 종료되면 데몬 스레드는 자동으로 종료된다.
 *  -JVM은 데몬 스레드의 실행 완료를 기다리지 않고 종료된다. 데몬 스레드가 아닌 모든 스레드가 종료되면, 자바 프로그램도 종료된다.
 */
public class DemonThreadMain {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");
        DemonThread demonThread = new DemonThread();
        demonThread.setDaemon(true);    // 데몬 스레드 여부(기본 값, false)
        demonThread.start();

        System.out.println(Thread.currentThread().getName() + ": main() end");
    }

    static class DemonThread extends Thread {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": run()");

            try {
                Thread.sleep(10000);    // 스레드 10초 대기 상태
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(Thread.currentThread().getName() + ": run() end");   // 데몬 스레드일 경우 이 출력까지 기다리지 않고 그냥 종료됨.
        }
    }
}
