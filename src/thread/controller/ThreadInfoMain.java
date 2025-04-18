package thread.controller;

import thread.start.HelloRunnable;
import util.MyLogger;

import static util.MyLogger.*;

/**
 * ** 자바 스레드의 상태 전이 과정 **
 *  1. New → Runnable: `start()` 메서드를 호출하면 스레드가 `Runnable` 상태로 전이된다.
 *  2. Runnable → Blocked/Waiting/Timed Waiting: 스레드가 락을 얻지 못하거나, `wait()` 또는 `sleep()` 메서드를 호출할 때 해당 상태로 전이된다.
 *  3. Blocked/Waiting/Timed Waiting → Runnable: 스레드가 락을 얻거나, 기다림이 완료되면 다시 `Runnable` 상태로 돌아간다.
 *  4. Runnable → Terminated: 스레드의 `run()` 메서드가 완료되면 스레드는 `Terminated` 상태가 된다.
 */
public class ThreadInfoMain {

    public static void main(String[] args) {
        // main 스레드
        Thread mainThread = Thread.currentThread();
        log("mainThread = " + mainThread);
        log("mainThread.threadId() = " + mainThread.threadId());    // 스레드 id는 자바가 자동으로 만들어주고 중복되지 않음.
        log("mainThread.getName() = " + mainThread.getName());  // 스레드 이름은 중복 가능.
        log("mainThread.getPriority() = " + mainThread.getPriority());  // 우선순위(1 ~ 10, 높으면 먼저 실행)
        log("mainThread.getThreadGroup() = " + mainThread.getThreadGroup());    // 스레드가 속한 스레드 그룹을 반환(모든 스레드는 부모 스레드와 같은 그룹에 속함)
        log("mainThread.getState() = " + mainThread.getState());

        // myThread 스레드
        Thread myThread = new Thread(new HelloRunnable(), "myThread");
        log("myThread = " + myThread);
        log("myThread.threadId() = " + myThread.threadId());    // 스레드 id는 자바가 자동으로 만들어주고 중복되지 않음.
        log("myThread.getName() = " + myThread.getName());  // 스레드 이름은 중복 가능.
        log("myThread.getPriority() = " + myThread.getPriority());  // 우선순위(1 ~ 10, 높으면 먼저 실행)
        log("myThread.getThreadGroup() = " + myThread.getThreadGroup());    // 스레드가 속한 스레드 그룹을 반환(모든 스레드는 부모 스레드와 같은 그룹에 속함, 얘는 그럼 main 스레드가 부모)
        log("myThread.getState() = " + myThread.getState());
    }
}
