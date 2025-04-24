package thread.executor;

import java.util.concurrent.*;

import static thread.executor.ExecutorUtils.*;

public class PrestartPoolMain {

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(1000);
        printState(es);
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) es;
        poolExecutor.prestartAllCoreThreads();  // 설정한 스레드 미리 생성하기(1000개)
        printState(es);
    }
}
