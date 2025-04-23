package thread.executor.future;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CallableMainV1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1);   // `ExecutorService` 가 제공하는 `submit()` 을 통해 `Callable` 을 작업으로 전달할 수 있다.
        Future<Integer> future = es.submit(new MyCallable());   // `MyCallable` 인스턴스가 블로킹 큐에 전달되고, 스레드 풀의 스레드 중 하나가 이 작업을 실행할 것이다. 이때 작업의 처리 결과는 직접 반환되는 것이 아니라 `Future` 라는 특별한 인터페이스를 통해 반환된다.
        Integer result = future.get();  // future.get()` 을 호출하면 `MyCallable` 의 `call()` 이 반환한 결과를 받을 수 있다.
        log("result value = " + result);

        es.close();
    }

    static class MyCallable implements Callable<Integer> {  // 반환할 자료형
        @Override
        public Integer call() throws Exception {
            log("Callable 시작");
            sleep(2000);
            int value = new Random().nextInt(10);
            log("value = " + value);
            log("Callable 완료");

            return value;
        }
    }
}
