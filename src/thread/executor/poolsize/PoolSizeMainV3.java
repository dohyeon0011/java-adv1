package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * ** Executor 캐시 스레드 풀 관리**
 *  1. 작업을 요청하면 core 사이즈 만큼 스레드를 만든다.
 *      core 사이즈가 없다. 바로 core 사이즈를 초과한다.
 *  2. core 사이즈를 초과하면 큐에 작업을 넣는다.
 *      큐에 작업을 넣을 수 없다. (`SynchronousQueue` 는 큐의 저장 공간이 0인 특별한 큐이다.)
 *  3. 큐를 초과하면 max 사이즈 만큼 스레드를 만든다. 임시로 사용되는 초과 스레드가 생성된다.
 *      초과 스레드가 생성된다. 물론 풀에 대기하는 초과 스레드가 있으면 재사용된다.
 *  4. max 사이즈를 초과하면 요청을 거절한다. 예외가 발생한다.
 *      참고로 max 사이즈가 무제한이다. 따라서 초과 스레드를 무제한으로 만들 수 있다.
 *
 *  대신, 적당히 요청이 늘어나는 것이 아닌 갑작스럽게 요청이 증가가 폭주하게 되면 CPU, 메모리 사용량이 100%가 되면서 서버가 터질 수도 있음.(OOM) 스레드 하나가 기본 1MB 이상 먹음.
 */
public class PoolSizeMainV3 {

    public static void main(String[] args) {

        // 모든 작업을 대기하지 않고 작업의 수 만큼 스레드가 생기면서 바로 실행.
        // 캐시 스레드 풀 전략은 매우 빠르고, 유연한 전략이다. 기본 스레드도 없고, 대기 큐에 작업도 쌓이지 않는다. 대신에 작업 요청이 오면 초과 스레드로 작업을 바로바로 처리함.
        // 초과 스레드의 수도 제한이 없기 때문에 CPU, 메모리 자원만 허용한다면 시스템의 자원을 최대로 사용하면서 빠른 처리가 가능.
        // 추가로 초과 스레드는 60초간 생존하기 때문에 작업 수에 맞춰서 적절한 수의 스레드가 재사용됨. 이런 특징 때문에 요청이 갑자기 증가하면 스레드도 갑자기 증가하고, 요청이 줄어들면 스레드도 점점 줄어듬.
        // return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
//        ExecutorService es = Executors.newCachedThreadPool();
        ExecutorService es = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

        log("pool 생성");
        printState(es);

        for (int i = 1; i <= 100; i++) {
            String taskName = "task" + i;
            es.execute(new RunnableTask(taskName));
            printState(es, taskName);
        }

        sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);

        sleep(3000);
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es);

        es.close();
        log("== shutdown 완료 ==");
    }
}
