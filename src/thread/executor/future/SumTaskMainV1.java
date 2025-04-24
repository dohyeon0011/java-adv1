package thread.executor.future;

import java.util.concurrent.*;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class SumTaskMainV1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SumTask task1 = new SumTask(1, 50);
        SumTask task2 = new SumTask(51, 100);

        ExecutorService es = Executors.newFixedThreadPool(2);

        Future<Integer> future1 = es.submit(task1); // Future가 아니었으면 여기서 블로킹이 걸렸을 것.
        Future<Integer> future2 = es.submit(task2);

        // Future가 있기에 요청 스레드가 작업을 맡긴 후, 결과가 나올 때까지 대기하지 않고 다음 코드를 실행할 수 있는 것.
        Integer sum1 = future1.get();   // blocking, 2초 대기
        Integer sum2 = future2.get();   // blocking, 앞에서 2초 대기하는 사이에 연산 끝나고 즉시 반환.

        log("task1.result = " + sum1);
        log("task2.result = " + sum2);

        int sumAll = sum1 + sum2;
        log("task1 + task2 = " + sumAll);
        log("End");

        es.close();
    }

    static class SumTask implements Callable<Integer> {

        int startValue;
        int endValue;

        public SumTask(int startValue, int endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public Integer call() throws Exception {
            log("작업 시작");
            Thread.sleep(2000); // 얘는 부모가 Exception 에러를 던지고 있기 때문에, 자식도 Exception의 자식을 던질 수 있음.
            int sum = 0;

            for (int i = startValue; i <= endValue; i++) {
                sum += i;
            }
            log("작업 완료, result = " + sum);

            return sum;
        }
    }
}
