package thread.cas.increment;

public class VolatileInteger implements IncrementInteger {

    volatile private int value; // 캐시 메모리를 무시할 뿐이라 동시성 해결 X
    volatile boolean flag = false;  // 이렇게 원자가 더이상 쪼개지지 않는 경우엔 volatile 유용

    private void setFlag() {
        this.flag = true;
    }

    @Override
    public void increment() {
        value++;
    }

    @Override
    public int get() {
        return value;
    }
}
