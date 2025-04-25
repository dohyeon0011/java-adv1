package thread.executor.reject;

import thread.executor.RunnableTask;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;

/**
 * 1. AbortPolicy: 새로운 작업을 제출할 때 `RejectedExecutionException` 을 발생시킨다. 기본 정책이다.
 * 2. DiscardPolicy: 새로운 작업을 조용히 버린다.
 * 3. CallerRunsPolicy: 새로운 작업을 제출한 스레드가 대신해서 직접 작업을 실행한다.
 * 4. 사용자 정의(`RejectedExecutionHandler` ): 개발자가 직접 정의한 거절 정책을 사용할 수 있다.
 *
 * **고정 스레드 풀 전략**: 트래픽이 일정하고, 시스템 안전성이 가장 중요
 * **캐시 스레드 풀 전략**: 일반적인 성장하는 서비스
 * **사용자 정의 풀 전략**: 다양한 상황에 대응
 *
 * 가장 좋은 최적화는 최적화 하지 않는 것이다. 미래에는 어떤 상황이 일어날 지 모르기 때문에, 현재 상황에 맞는 최적화를 하다가 상황이 바뀌면 그때가서 개선하는 방향이 나음.
 */
public class RejectMainV4 {

    public static void main(String[] args) {
        // 참고로 `ThreadPoolExecutor` 를 `shutdown()` 을 하면 이후에 요청하는 작업을 거절하는데, 이때도 같은 정책이 적용된다.
        // CallerRunsPolicy 정책은 shutdown() 이후에도 작업을 수행함. 따라서 shutdown() 조건을 체크해서 이 경우에는 작업을 수행하지 않도록 한다.
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new MyRejectedExecutionHandler());    // RejectExecutorHandler(Executor 예외 정책 설정)

        executor.submit(new RunnableTask("task1"));
        executor.submit(new RunnableTask("task2"));
        executor.submit(new RunnableTask("task3"));

        executor.close();
    }

    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

        static AtomicInteger count = new AtomicInteger();

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            int i = count.incrementAndGet();
            log("[경고] 거절된 누적 작업 수: " + i + " 개");
        }
    }
}


