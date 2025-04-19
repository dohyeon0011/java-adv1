package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * 이런 동기화를 사용하면 다음 문제들을 해결할 수 있다.
 *  -경합 조건(Race condition): 두 개 이상의 스레드가 경쟁적으로 동일한 자원을 수정할 때 발생하는 문제.
 *  -데이터 일관성: 여러 스레드가 동시에 읽고 쓰는 데이터의 일관성을 유지.
 */
public class BankAccountV3 implements BankAccount {

    private int balance;    // 잔고

    public BankAccountV3(int initialBalance ) {
        this.balance = initialBalance;
    }

    // 모든 객체(인스턴스)는 내부에 자신만의 락(lock)을 가지고 있다.
    // 스레드가 synchronized 키워드가 있는 메서드에 진입하려면 반드시 해당 인스턴스의 락이 있어야 한다.
    // volatile 를 사용하지 않아도 `synchronized` 안에서 접근하는 변수의 메모리 가시성 문제는 해결된다.
    @Override
    public boolean withdraw(int amount) {
        log("거래 시작: " + getClass().getSimpleName());

        // == 임계 영역 시작 ==
        // 임계 영역: 멀티 스레드 환경에서 여러 스레드가 공유 자원에 접근했을 때 문제가 생길 수도 있는 구간(코드)
        // 이러면 불필요한 구간까지 락을 걸지 않고, 정말 필요한 부분만 걸기 때문에 병목 현상을 최대한 최적화 할 수 있음.
        synchronized (this) {   // 락을 획득할 인스턴스의 참조
            log("[검증 시작] 출금액: " + amount + ", 잔액: " + balance);
            // 출금액이 잔고보다 많으면, 진행하면 안됨.
            if (balance < amount) {
                log("[검증 실패] 출금액: " + amount + ", 잔액: " + balance);
                return false;
            }

            // 잔고가 출금액보다 많으면, 진행
            log("[검증 완료] 출금액: " + amount + ", 잔액: " + balance);
            sleep(1000);    // 출금에 걸리는 시간을 가정
            balance -= amount;
            log("[출금 완료] 출금액: " + amount + ", 잔액: " + balance);
        }
        // == 임계 영역 종료 ==

        log("거래 종료");
        return true;    // 락을 반납하면서 리턴.
    }

    @Override
    public synchronized int getBalance() {
        return balance;
    }
}
