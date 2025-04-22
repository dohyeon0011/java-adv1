package thread.cas;

import java.util.concurrent.atomic.AtomicInteger;

import static util.MyLogger.log;

public class CasMainV2 {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println("start value = " + atomicInteger.get());

        /*int result1 = atomicInteger.incrementAndGet();
        System.out.println("result1 = " + result1);

        int result2 = atomicInteger.incrementAndGet();
        System.out.println("result2 = " + result2);*/

        // incrementAndGet 구현
        int resultValue1 = incrementAndGet(atomicInteger);
        System.out.println("resultValue1 = " + resultValue1);

        int resultValue2 = incrementAndGet(atomicInteger);
        System.out.println("resultValue2 = " + resultValue2);
    }

    private static int incrementAndGet(AtomicInteger atomicInteger) {
        int getValue;
        boolean result;

        do {
            getValue = atomicInteger.get(); // thread1 value: 0, 다시 읽어서 value: 1
            log("getValue: " + getValue);
            // thread1이 증가 시키기 직전에 thread2 value -> 1로 증가 시키면, thread1에서 result = false 떠서 다시 반복문 타서 조회
            result = atomicInteger.compareAndSet(getValue, getValue + 1);    // CAS 연산(비교 후, 연산 처리를 하나의 연산으로 묶어버림)
            log("result: " + result);
        } while (!result);

        return getValue + 1;
    }
}
