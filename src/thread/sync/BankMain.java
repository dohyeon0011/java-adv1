package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankMain {

    public static void main(String[] args) throws InterruptedException {
//        BankAccountV1 account = new BankAccountV1(1000);
//        BankAccount account = new BankAccountV2(1000);
        BankAccount account = new BankAccountV3(1000);

        Thread t1 = new Thread(new WithdrawTask(account, 800), "t1");
        Thread t2 = new Thread(new WithdrawTask(account, 800), "t2");

        t1.start(); // 먼저 락(Lock)을 획득하고 메서드 진입
        t2.start(); // t1이 작업을 끝마칠때 까지 BLOCKED 상태로 대기하다가 -> RUNNABLE

        sleep(500); // 검증 완료까지 잠시 대기
        log("t1 state: " + t1.getState());
        log("t2 state: " + t2.getState());

        t1.join();
        t2.join();

        log("최종 잔액: " + account.getBalance());
    }
}
