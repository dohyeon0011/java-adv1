package thread.start;

/**
 * 실행 결과 순서가 항상 다른 이유는 스케줄링 큐에서 스레드를 실행하는 순서가 항상 달라지기 때문에도 있음.
 * + main 스레드가 helloThread.start(); 지시하고 Thread-0 실행 결과 기다려 주지 않고, 바로 다음 줄 코드 실행해서.
 */
public class BadThreadMain {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");    // 자바 기본 main 스레드가 실행

        HelloThread helloThread = new HelloThread();    // 자바 기본 main 스레드가 실행
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");    // 자바 기본 main 스레드가 실행
        helloThread.run();    // run() 직접 실행(이러면 main 스레드가 직접 실행하게 됨.)
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");    // 자바 기본 main 스레드가 실행

        System.out.println(Thread.currentThread().getName() + ": main() end");  // 자바 기본 main 스레드가 실행
    }
}
