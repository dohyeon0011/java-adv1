package thread.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CPU는 다음 두 과정을 묶어서 하나의 원자적인 명령으로 만들어버린다. 따라서 중간에 다른 스레드가 개입할 수 없다.
 *  1. x001의 값을 확인한다.
 *  2. 읽은 값이 0이면 1로 변경한다.
 *  CPU는 두 과정을 하나의 원자적인 명령으로 만들기 위해 1번과 2번 사이에 다른 스레드가 `x001` 의 값을 변경하지 못하게 막는다.
 *  락을 거는 것이 아니지만 하드웨어(CPU)가 접근하지 못하게 기능을 제공함.
 */
public class CasMainV1 {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        System.out.println("start value = " + atomicInteger.get());

        boolean result1 = atomicInteger.compareAndSet(0, 1);    // 기대값(0)이면 1로 set
        System.out.println("result1 = " + result1 + ", value = " + atomicInteger.get());    // true, 1

        boolean result2 = atomicInteger.compareAndSet(0, 1);
        System.out.println("result2 = " + result2 + ", value = " + atomicInteger.get());    // false, 1
    }
}
