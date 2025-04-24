package thread.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static util.MyLogger.log;

public abstract class ExecutorUtils {

    public static void printState(ExecutorService executorService) {
        // ExecutorService의 대표 구현체인 ThreadPoolExecutor가 제공하는 기능
        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();  // 스레드 풀에 있는 스레드 개수
            int active = poolExecutor.getActiveCount(); // 실행중인 스레드(RUNNABLE)
            int queued = poolExecutor.getQueue().size();    // 큐에 대기중인 작업 숫자
            long completedTaskCount = poolExecutor.getCompletedTaskCount(); // 스레드가 완료한 작업 개수
            log("[pool=" + pool + ", active=" + active + ", queuedTasks=" + queued + ", completedTask=" + completedTaskCount + "]");
        } else {
            log(executorService);
        }
    }

    public static void printState(ExecutorService executorService, String taskName) {
        // ExecutorService의 대표 구현체인 ThreadPoolExecutor가 제공하는 기능
        if (executorService instanceof ThreadPoolExecutor poolExecutor) {
            int pool = poolExecutor.getPoolSize();  // 스레드 풀에 있는 스레드 개수
            int active = poolExecutor.getActiveCount(); // 실행중인 스레드(RUNNABLE)
            int queued = poolExecutor.getQueue().size();    // 큐에 대기중인 작업 숫자
            long completedTaskCount = poolExecutor.getCompletedTaskCount(); // 스레드가 완료한 작업 개수
            log(taskName + " -> [pool=" + pool + ", active=" + active + ", queuedTasks=" + queued + ", completedTask=" + completedTaskCount + "]");
        } else {
            log(executorService);
        }
    }
}
