package thread.sync;

public interface BankAccount {

    // 계좌 출금
    boolean withdraw(int amount);

    // 계좌 잔액 조회
    int getBalance();
}
