package thread.start;

/**
 * 실행 결과 순서가 항상 다른 이유는 스케줄링 큐에서 스레드를 실행하는 순서가 항상 달라지기 때문에도 있음.
 * + main 스레드가 helloThread.start(); 지시하고 Thread-0 실행 결과 기다려 주지 않고, 바로 다음 줄 코드 실행해서.
 *
 * 사용자 스레드(non-daemon 스레드)
 *  -프로그램의 주요 작업을 수행한다.
 *  -작업이 완료될 때까지 실행된다.
 *  -모든 user 스레드가 종료되면 JVM도 종료된다.
 */
public class HelloThreadMain {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + ": main() start");    // 자바 기본 main 스레드가 실행

        HelloThread helloThread = new HelloThread();    // 자바 기본 main 스레드가 실행
        System.out.println(Thread.currentThread().getName() + ": start() 호출 전");    // 자바 기본 main 스레드가 실행
        helloThread.start();    // 자바가 스레드를 위한 별도의 스택 공간 할당(main 스레드가 실행하는 것이 아니라, Thread-0이 start() 호출.(main 스레드는 지시만 함)
        System.out.println(Thread.currentThread().getName() + ": start() 호출 후");    // 자바 기본 main 스레드가 실행

        System.out.println(Thread.currentThread().getName() + ": main() end");  // 자바 기본 main 스레드가 실행
    }
}
