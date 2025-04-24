package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class FutureCancelMain {

//    private static boolean mayInterruptIfRunning = true;
    private static boolean mayInterruptIfRunning = false;

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1);
        Future<String> future = es.submit(new MyTask());
        log("Future.state: " + future.state());

        // 일정 시간 후 취소 시도
        sleep(3000);

        // cancel 호출
        // cancel(true): Future를 취소 상태로 바꾸고, 작업이 실행중이라면 Thread.interrupt()를 호출해서 중단.
        // cancel(false): Future를 취소 상태로 바꾸지만, 작업이 이미 실행중이라면 중단하지 않음.(그래도 cancel()을 호출했기 때문에 Future는 CANCEL 상태가 된다. 이후 Future.get()을 호출하면 CancellationException 런타임 예외 발생.)
        boolean cancelResult = future.cancel(mayInterruptIfRunning);
        log("future.cancel(" + mayInterruptIfRunning + ") 호출");
        log("cancel(" + mayInterruptIfRunning + ") result: " + cancelResult);

        // 결과 확인
        try {
            log("Future.result: " + future.get());
        } catch (CancellationException e) {
            log("Future는 이미 취소되었습니다.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        es.close();
    }

    static class MyTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            try {
                for (int i = 0; i < 10; i++) {
                    log("작업 중: " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                log("인터럽트 발생");
                return "interrupted";
            }
            return "Completed";
        }
    }
}
