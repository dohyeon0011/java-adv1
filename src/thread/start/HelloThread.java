package thread.start;

public class HelloThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": run()");   // 실행중인 스레드 이름
    }
}
