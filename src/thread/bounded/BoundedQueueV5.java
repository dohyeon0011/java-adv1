package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

/**
 * synchronized를 사용하지 않는 방법.
 * 스레드는 스레드 대기 집합(2차 대기소)에서 빠져 나와도 락 대기 집합(1차 대기소, 자바 객체 인스턴스에서 기본적으로 갖고 있는 락 대기 집합)에서 락을 획득하지 못 하면 1차 대기소에서 또 대기함.
 * synchronized 사용 시: 모니터 락, 락 대기 집합(1차 대기소), 스레드 대기 집합(2차 대기소) -> 2차 1차를 빠져 나와야 임계 영역 수행 가능.
 *
 * 이 3가지 요소는 서로 맞물려 돌아간다.
 * -synchronized 대기
 *      `synchronized` 를 사용한 임계 영역에 들어가려면 모니터 락이 필요하다.
 *      모니터 락이 없으면 락 대기 집합에 들어가서 `BLOCKED` 상태로 락을 기다린다.(BLOCKED 상태는 싱크로나이즈 시에만 있는 상태)
 *      모니터 락을 반납하면 락 대기 잡합에 있는 스레드 중 하나가 락을 획득하고 `BLOCKED -> RUNNABLE` 상태가 된다.
 *      `wait()` 를 호출해서 스레드 대기 집합에 들어가기 위해서는 모니터 락이 필요하다.
 *      스레드 대기 집합에 들어가면 모니터 락을 반납한다.
 *      스레드가 `notify()` 를 호출하면 스레드 대기 집합에 있는 스레드 중 하나가 스레드 대기 집합을 빠져나온다. 그리고 모니터 락 획득을 시도한다.
 *          -모니터 락을 획득하면 임계 영역을 수행한다.
 *          -모니터 락을 획득하지 못하면 락 대기 집합에 들어가서 `BLOCKED` 상태로 락을 기다린다.
 *
 *  -대기1: ReentrantLock 락 획득 대기
 *      `ReentrantLock` 의 대기 큐에서 관리
 *      `WAITING` 상태로 락 획득 대기
 *      `lock.lock()` 을 호출 했을 때 락이 없으면 대기
 *      다른 스레드가 `lock.unlock()` 을 호출 했을 때 대기가 풀리며 락 획득 시도, 락을 획득하면 대기 큐를 빠져나감
 *  -대기2: await() 대기
 *      `condition.await()` 를 호출 했을 때, `condition` 객체의 스레드 대기 공간에서 관리
 *      `WAITING` 상대로 대기
 *      다른 스레드가 `condition.signal()` 을 호출 했을 때 `condition` 객체의 스레드 대기 공간에서 빠져 나감
 *  2차 대기 -> 1차 대기 -> 임계 영역 실행
 */
public class BoundedQueueV5 implements BoundedQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition producerCond = lock.newCondition();    // ReentrantLock을 사용하는 생산자 스레드가 대기하는 스레드 대기 집합(공간)
    private final Condition consumerCond = lock.newCondition();    // ReentrantLock을 사용하는 소비자 스레드가 대기하는 스레드 대기 집합(공간)

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;  // 버퍼 크기

    public BoundedQueueV5(int max) {
        this.max = max;
    }

    // 현재 버전은 큐(버퍼)가 가득 찼을 때, 소비자가 소비해서 큐(버퍼)가 비워질 때까지 대기
    @Override
    public void put(String data) {
        lock.lock();

        try {
            while (queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                try {
                    producerCond.await();   // 생산자 대기 집합에 보관
                    log("[put] 생산자 깨어남.");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            queue.offer(data);
            log("[put] 생산자 데이터 저장, 소비자 signal() 호출");
            consumerCond.signal(); // 소비자 스레드 깨우기
        } finally {
            lock.unlock();
        }
    }
    
    // 현재 버전은 큐(버퍼)가 비워져있을 때, 생산자가 생산해서 큐(버퍼)가 채워질 때까지 대기
    @Override
    public String take() {
        lock.lock();

        try {
            while (queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                try {
                    consumerCond.await();   // 소비자 스레드 대기 집합에 보관
                    log("[take] 소비자 깨어남.");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String data = queue.poll();
            log("[take] 소비자 데이터 소비, 생산자 signal() 호출");
            producerCond.signal();  // 생산자 스레드 깨우기

            return data;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
