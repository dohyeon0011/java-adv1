package thread.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * **CAS(Compare-And-Swap)와 락(Lock) 방식의 비교**
 * **락(Lock) 방식**
 *  -비관적(pessimistic) 접근법
 *  -데이터에 접근하기 전에 항상 락을 획득
 *  -다른 스레드의 접근을 막음
 *  -"다른 스레드가 방해할 것이다"라고 가정
 *
 * **CAS(Compare-And-Swap) 방식**(동시 요청이 적은 경우 성능 good, 여러 스레드가 돌기 때문에)
 *  -낙관적(optimistic) 접근법
 *  -락을 사용하지 않고 데이터에 바로 접근
 *  -충돌이 발생하면 그때 재시도
 *  -"대부분의 경우 충돌이 없을 것이다"라고 가정
 */
public class CasMainV3 {

    private static final int THREAD_COUNT = 2;

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                incrementAndGet(atomicInteger);
            }
        };

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int result = atomicInteger.get();
        System.out.println("result = " + result);
    }

    private static int incrementAndGet(AtomicInteger atomicInteger) {
        int getValue;
        boolean result;

        do {
            getValue = atomicInteger.get(); // thread1 value: 0, 다시 읽어서 value: 1
            sleep(100); // 스레드 동시 실행(CAS 연산)을 위해 대기
            log("getValue: " + getValue);
            // thread1이 증가 시키기 직전에 thread2 value -> 1로 증가 시키면, thread1에서 result = false 떠서 다시 반복문 타서 조회
            result = atomicInteger.compareAndSet(getValue, getValue + 1);    // CAS 연산(비교 후, 연산 처리를 하나의 연산으로 묶어버림)
            log("result: " + result);
        } while (!result);

        return getValue + 1;
    }
}
