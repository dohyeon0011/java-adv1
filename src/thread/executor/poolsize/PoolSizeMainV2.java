package thread.executor.poolsize;

import thread.executor.RunnableTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;

/**
 * 고정 스레드 방식의 가장 큰 장점은 스레드 수가 고정되어서 CPU, 메모리 리소스가 어느정도 예측 가능하다는 점이다. 따라서 일반적인 상황에 가장 안정적으로 서비스를 운영할 수 있다.
 * 하지만 상황에 따라 장점이 가장 큰 단점이 되기도 한다.
 *
 * 상황1 - 점진적인 사용자 확대
 *  -개발한 서비스가 잘 되어서 사용자가 점점 늘어난다.
 *  -고정 스레드 전략을 사용해서 서비스를 안정적으로 잘 운영했는데, 언젠가부터 사용자들이 서비스 응답이 점점 느려진다고 항의한다.
 *
 * 상황2 - 갑작스런 요청 증가
 *  -마케팅 팀의 이벤트가 대성공 하면서 갑자기 사용자가 폭증했다.
 *  -고객은 응답을 받지 못한다고 항의한다.
 *
 * 요청이 처리되는 시간보다 쌓이는 시간이 더 빠르고(대기 큐의 사이즈가 무제한), 사용자가 늘어나도 CPU, 메모리 사용량이 늘어나지 않고 고정적으로 사용하기 때문에 여유있는 서버 자원만 낭비되는 상황 발생.
 */
public class PoolSizeMainV2 {

    public static void main(String[] args) {

        ExecutorService es = Executors.newFixedThreadPool(2);   // return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        log("pool 생성");
        printState(es);

        for (int i = 1; i <= 6; i++) {
            String taskName = "task" + i;
            es.execute(new RunnableTask(taskName));
            printState(es, taskName);
        }
        es.close();
        log("== shutdown 완료 ==");
    }
}
