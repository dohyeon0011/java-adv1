package thread.controller.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        log("runFlag = " + task.runFlag);
        t.start();

        sleep(1000);
        log("runFlag를 false로 변경 시도");
        task.runFlag = false;   // main 스레드가 runFlag의 값을 변경해도 CPU 코어1(예시 CPU 코어)이 사용하는 캐시 메모리의 runFlag 값만 false 변경되고, 메인 메모리의 runFlag 값은 true다.
        log("runFlag = " + task.runFlag);   // CPU는 기본적으로 빠른 로딩을 위해 메인 메모리의 값을 읽어오는 것이 아니라, CPU 코어와 거리로도 성능으로도 가까운 캐시 메모리에서 읽어옴.(캐시 메모리에 언제 반영될 지는 CPU 설계 방식과 실행 환경에 따라 다름. 컨텍스트 스위칭 때 갱신이 됨, 아니면 잠깐 Thread.sleep() 이후에 될 지 말지?)
        log("main 종료");                     // 이렇게 메인 메모리에 캐시 메모리의 변경된 값이 반영이 되지 않은 상태를 메모리 가시성이라고 함.
    }

    static class MyTask implements Runnable {

//        boolean runFlag = true;
        volatile boolean runFlag = true;    // volatile을 사용하면 메인 메모리에 직접 반영하고, 메인 메모리에서 직접 데이터를 조회함.(대신, 캐시 메모리를 사용했을 때 보다는 성능이 떨어짐)

        @Override
        public void run() {
            log("task 시작");
            while (runFlag) {
                // runFlag가 false로 바뀌면 탈출
//                System.out.println("hello"); 만약 스레드 실행 도중에 작업을 하게 되면 컨텍스트 스위칭이 발생(출력을 하는 동안 잠깐 대기 상태로 바껴서)해서 메인 메모리의 값이 갱신될 수도 있음.(하지만 100퍼 확실한 경우는 아니라 메모리 가시성이 발생할 확률이 높음)
            }
            log("task 종료"); // work 스레드에서 힙 영역의 runFlag 변수가 false가 되면 while문을 빠져나와 이 로그를 출력되길 기대하지만, while문에서 못 빠져 나오고 계속 무한 루프를 돌고 있음.
        }
    }
}
