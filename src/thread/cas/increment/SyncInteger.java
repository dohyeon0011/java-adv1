package thread.cas.increment;

public class SyncInteger implements IncrementInteger {

    private int value; // 캐시 메모리를 무시할 뿐이라 동시성 해결 X

    @Override
    public synchronized void increment() {
        value++;
    }

    @Override
    public synchronized int get() {
        return value;
    }
}
