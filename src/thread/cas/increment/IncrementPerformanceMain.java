package thread.cas.increment;

public class IncrementPerformanceMain {

    private static final long COUNT = 100_000_000;

    public static void main(String[] args) {
        // 가장 빠르다.
        // CPU 캐시를 적극 사용한다. CPU 캐시의 위력을 알 수 있다.
        // 안전한 임계 영역도 없고, `volatile` 도 사용하지 않기 때문에 멀티스레드 상황에는 사용할 수 없다.
        // 단일 스레드가 사용하는 경우에 효율적이다.
        test(new BasicInteger());

        // `volatile` 을 사용해서 CPU 캐시를 사용하지 않고 메인 메모리를 사용한다.
        //  안전한 임계 영역이 없기 때문에 멀티스레드 상황에는 사용할 수 없다.
        //  단일 스레드가 사용하기에는 `BasicInteger` 보다 느리다. 그리고 멀티스레드 상황에도 안전하지 않다.
        test(new VolatileInteger());

        // `synchronized` 를 사용한 안전한 임계 영역이 있기 때문에 멀티스레드 상황에도 안전하게 사용할 수 있다.
        // `MyAtomicInteger` 보다 성능이 느리다.
        test(new SyncInteger());

        // 자바가 제공하는 `AtomicInteger` 를 사용한다. 멀티스레드 상황에 안전하게 사용할 수 있다.
        // 성능도 `synchronized` , `Lock(ReentrantLock)` 을 사용하는 경우보다 1.5 ~ 2배 정도 빠르다.
        test(new MyAtomicInteger());
    }

    private static void test(IncrementInteger incrementInteger) {
        long startMs = System.currentTimeMillis();

        for (int i = 0; i < COUNT; i++) {
            incrementInteger.increment();
        }

        long endMs = System.currentTimeMillis();
        System.out.println(incrementInteger.getClass().getSimpleName() + ": ms = " + (endMs - startMs));
    }
}
