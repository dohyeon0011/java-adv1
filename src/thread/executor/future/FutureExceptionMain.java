package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class FutureExceptionMain {

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1);
        log("작업 전달");
        Future<Integer> future = es.submit(new ExCallable());
        sleep(1000);    // 잠시 대기(로그 출력 순서 맞추려고)

        Integer result = null;
        try {
            log("future.get() 호출 시도, future.state(): " + future.state());
            result = future.get();
            log("result value = " + result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            log("e = " + e);    // java.util.concurrent.ExecutionException: java.lang.IllegalStateException: ex!
            Throwable cause = e.getCause(); // 예외 원본
            log("cause = " + cause);    // java.lang.IllegalStateException: ex!
        }
        es.close();
    }

    static class ExCallable implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            log("Callable 실행, 예외 발생 ");
            throw new IllegalStateException("ex!");
        }
    }
}
