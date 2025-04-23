package thread.cas.spinlock;

import java.util.concurrent.atomic.AtomicBoolean;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

// 대신 CAS 연산을 사용하게 되면 스레드가 BLOCKED, WAITING 상태로 빠지지는 않지만,
// RUNNABLE 상태로 락을 계속 획득하려고 대기하기 때문에 락을 기다리는 스레드가 CPU 자원을 계속 사용하는 상화이 발생함.
// 임계 영역이 연산이 매우 짧은 경우에만 효과적.(데이터베이스의 결과를 대기한다거나, 다른 서버의 요청을 기다린다거나 하는 것 처럼 오래 기다리는 작업에 사용하면 CPU 자원 낭비)
public class SpinLock {

    private final AtomicBoolean lock = new AtomicBoolean(false);

    public void lock() {
        log("락 획득 시도");
        // 이 두 연산을 하나의 원자적인 연산으로 만들어줌.
        // 1. 락 사용 여부 확인: lock의 값이 false이면
        // 2. 락의 값 변경: lock의 값을 true로 변경
        while (!lock.compareAndSet(false, true)) {  // !false(락을 획득하기 전에, 먼저 락이 false 여야 함.)
            // 락을 획득할 때 까지 스핀 대기(바쁜 대기) 한다.
            log("락 획득 실패 - 스핀 대기");
        }
        log("락 획득 완료");
    }

    public void unlock() {
        lock.set(false);
        log("락 반납 완료");
    }
}
