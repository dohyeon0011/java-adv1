package thread.executor.poolsize;

import thread.executor.ExecutorUtils;
import thread.executor.RunnableTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.*;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * 정리 - Executor 스레드 풀 관리
 *  1. 작업을 요청하면 core 사이즈 만큼 스레드를 만든다.
 *  2. core 사이즈를 초과하면 큐에 작업을 넣는다.
 *  3. 큐를 초과하면 max 사이즈 만큼 스레드를 만든다. 임시로 사용되는 초과 스레드가 생성된다.
 *      큐가 가득차서 큐에 넣을 수도 없다. 초과 스레드가 바로 수행해야 한다.
 *  4. max 사이즈를 초과하면 요청을 거절한다. 예외가 발생한다.
 *      큐도 가득차고, 풀에 최대 생성 가능한 스레드 수도 가득 찼다. 작업을 받을 수 없다.
 */
public class PoolSizeMainV1 {

    public static void main(String[] args) {

        // corePoolSize(기본 스레드 수), maximumPoolSize(최대 스레드 수, 대기 큐와 작업 중인 스레드가 모두 꽉찬 상태일 때 늘어남), keepAliveTime(초과 스레드들의 생명 주기(생성 후 3초가 아닌, 작업을 처리 후 3초))
        // 기본 스레드 수까지 스레드가 생성되지 않은 상태라면, 굳이 대기 큐(BlockingQueue)에 넣지않고 생성한 스레드에 바로 작업 붙여줌)
        // 스레드 풀에 사용할 스레드가 없을 경우에 작업을 대기 큐에 넣음.
        // 기본 스레드 + 대기 중인 작업 큐가 모두 가득찬 경우에 그때서야 초과 스레드를 만듬.
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        ThreadPoolExecutor es = new ThreadPoolExecutor(2, 4, 3000, TimeUnit.MILLISECONDS, workQueue);
        printState(es);

        es.execute(new RunnableTask("task1"));
        printState(es, "task1");

        es.execute(new RunnableTask("task2"));
        printState(es, "task2");

        es.execute(new RunnableTask("task3"));
        printState(es, "task3");

        es.execute(new RunnableTask("task4"));
        printState(es, "task4");

        es.execute(new RunnableTask("task5"));
        printState(es, "task5");

        es.execute(new RunnableTask("task6"));
        printState(es, "task6");

        try {
            es.execute(new RunnableTask("task7"));
            printState(es, "task7");
        } catch (RejectedExecutionException e) {    // 작업 처리 거부((기본 스레드 + 초과 스레드) + 대기 큐에 모두 가득 찬 상태)
            log("task7 실행 거절 예외 발생: " + e);
        }

        sleep(3000);
        log("== 작업 수행 완료 ==");
        printState(es);

        sleep(3000);
        log("== maximumPoolSize 대기 시간 초과 ==");
        printState(es);

        es.close();
        log("== shutdown 완료 ==");
        printState(es);
    }
}
