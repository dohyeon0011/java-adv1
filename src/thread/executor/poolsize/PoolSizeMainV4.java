package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;

/**
 * 1. 일반: 일반적인 상황에는 CPU, 메모리 자원을 예측할 수 있도록 고정 크기의 스레드로 서비스를 안정적으로 운영한다.(1000개 이하의 작업이 큐에 담겨있다. 100개의 기본 스레드가 처리한다.)
 * 2. 긴급: 사용자의 요청이 갑자기 증가하면 긴급하게 스레드를 추가로 투입해서 작업을 빠르게 처리한다.(큐에 담긴 작업이 1000개를 초과한다. 100개의 기본 스레드 + 100개의 초과 스레드가 처리한다.)
 * 3. 거절: 사용자의 요청이 폭증해서 긴급 대응도 어렵다면 사용자의 요청을 거절한다.(초과 스레드를 투입했지만, 큐에 담긴 작업 1000개를 초과하고 또 초과 스레드도 넘어간 상황이다. 이 경우 예외를 발생시킨다.)
 */
public class PoolSizeMainV4 {

//    static final int TASK_SIZE = 1100;  // 1. 일반
    static final int TASK_SIZE = 1200;  // 2. 긴급(긴급 투입한 스레드 덕분에 풀의 스레드 수가 2배가 된다. 따라서 작업을 2배 빠르게 처리한다. 물론 CPU, 메모리 사용을 더 하기 때문에 이런 부분은 감안해서 긴급 상황에 투입할 최대 스레드를 정해야 한다.)
//    static final int TASK_SIZE = 1201;  // 3. 거절

    public static void main(String[] args) {
        // 작업은 1000개까지 받기
        ThreadPoolExecutor es = new ThreadPoolExecutor(100, 200, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));
        printState(es);

        long startMs = System.currentTimeMillis();

        for (int i = 1; i <= TASK_SIZE; i++) {
            String taskName = "task" + i;
            try {
                es.execute(new RunnableTask(taskName));
                printState(es, taskName);
            } catch (RejectedExecutionException e) {    // 작업 처리 거부((기본 스레드 + 초과 스레드) + 대기 큐에 모두 가득 찬 상태)
                log(taskName + " -> " + e);
            }
        }

        es.close();
        long endMs = System.currentTimeMillis();
        log("time: " + (endMs - startMs) + " ms");
        printState(es);
    }
}
