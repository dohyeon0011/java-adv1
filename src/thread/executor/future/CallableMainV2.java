package thread.executor.future;

import java.util.Random;
import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class CallableMainV2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1);   // `ExecutorService` 가 제공하는 `submit()` 을 통해 `Callable` 을 작업으로 전달할 수 있다.
        log("submit() 호출");
        Future<Integer> future = es.submit(new MyCallable());   // `MyCallable` 인스턴스가 블로킹 큐에 전달되고, 스레드 풀의 스레드 중 하나가 이 작업을 실행할 것이다. 이때 작업의 처리 결과는 직접 반환되는 것이 아니라 `Future` 라는 특별한 인터페이스를 통해 반환된다.
        log("future 즉시 반환, future = " + future);
        // Future는 내부에 MyCallable 작업의 완료 여부와, 작업의 결과 값을 가진다.(Future 객체가 만들어지는 것 까지는 생성한 즉시 반환하기 때문에 요청 스레드(main)는 대기하지 않고 다음 코드를 호출.

        log("future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING");
        Integer result = future.get();  // future.get()` 을 호출하면 `MyCallable` 의 `call()` 이 반환한 결과를 받을 수 있다.(요청 스레드: RUNNABLE -> WAITING), 스레드의 작업이 완료되고 Future에 완료 상태와 결과가 담기면 대기하던 요청 스레드를 깨워줌.
        log("future.get() [블로킹] 메서드 호출 완료 -> main 스레드 RUNNABLE");

        log("result value = " + result);
        log("future 완료, future = " + future);

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
