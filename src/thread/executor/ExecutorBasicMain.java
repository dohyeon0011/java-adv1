package thread.executor;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ExecutorBasicMain {

    public static void main(String[] args) {
        // corePoolSize(스레드 풀에서 관리되는 기본 스레드 수), maximumPoolSize(스레드 풀에서 관리되는 최대 스레드 수), keepAliveTime(기본 스레드 수를 초과해서 만들어진 스레드가 생존할 수 있는 대기 시간, 이 시간 동안 처리할 작업이 없다면 초과 스레드는 제거.), 작업을 보관할 블로킹 큐
        // ThreadPoolExecutor 생성한 시점에 스레드 풀에 스레드를 미리 만들어두지는 않는다.
        // 기본 스레드 수 만큼 생성된 뒤로부터는 생성된 스레드를 재활용함.
        ExecutorService es = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        log("== 초기 상태 ==");
        printState(es);
        es.execute(new RunnableTask("taskA"));  // 생산자: es.execute(작업)를 호출하면 내부에서 BlockingQueue에 작업을 보관한다. main 스레드가 생산자가 된다.
        es.execute(new RunnableTask("taskB"));  // 소비자: 스레드 풀에 있는 스레드가 소비자이다. 이후에 소비자 중에 하나가 BlockingQueue에 들어있는 작업을 받아 처리한다.(WAITING -> RUNNABLE)
        es.execute(new RunnableTask("taskC"));
        es.execute(new RunnableTask("taskD"));
        log("== 작업 수행 중 ==");
        printState(es);

        sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);

        es.close(); // 자바 19부터 지원. 미만 버전은 shutdown()
        log("== shutdown 완료 ==");
        printState(es);
    }
}
