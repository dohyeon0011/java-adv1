package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static util.MyLogger.log;

/**
 * 1. AbortPolicy: 새로운 작업을 제출할 때 `RejectedExecutionException` 을 발생시킨다. 기본 정책이다.
 * 2. DiscardPolicy: 새로운 작업을 조용히 버린다.
 * 3. CallerRunsPolicy: 새로운 작업을 제출한 스레드가 대신해서 직접 작업을 실행한다.
 * 4. 사용자 정의(`RejectedExecutionHandler` ): 개발자가 직접 정의한 거절 정책을 사용할 수 있다.
 */
public class RejectMainV1 {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadPoolExecutor.AbortPolicy());    // RejectExecutorHandler(Executor 예외 정책 설정)

        executor.submit(new RunnableTask("task1"));
        try {
            executor.submit(new RunnableTask("task2"));
        } catch (RejectedExecutionException e) {
            log("요청 초과");
            System.out.println("error = " + e.getMessage());
            // 포기, 다시 시도 등 다양한 고민을 하면 됨.
        } finally {
            executor.close();
        }
    }
}
