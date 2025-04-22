package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static util.MyLogger.log;

/**
 * synchronized를 사용하지 않는 방법.
 */
public class BoundedQueueV4 implements BoundedQueue {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();    // ReentrantLock을 사용하는 스레드가 대기하는 스레드 대기 집합(공간)

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;  // 버퍼 크기

    public BoundedQueueV4(int max) {
        this.max = max;
    }

    // 현재 버전은 큐(버퍼)가 가득 찼을 때, 소비자가 소비해서 큐(버퍼)가 비워질 때까지 대기 -> 근데 누가 깨어날 지 모름.
    @Override
    public void put(String data) {
        lock.lock();

        try {
            while (queue.size() == max) {
                log("[put] 큐가 가득 참, 생산자 대기");
                try {
                    condition.await();  // 지정한 condition에 현재 스레드를 대기(WAITING)상태로 보관. 이때 ReentrantLock에서 획득한 락을 반납 하고 대기 상태로 보냄.
//                    wait(); // RUNNABLE -> WAITING, 락을 다른 스레드에게 반납.(자바에서 기본으로 제공하는 모든 객체 인스턴스가 가지고 있는 Object.wait())
                    log("[put] 생산자 깨어남.");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            queue.offer(data);
            log("[put] 생산자 데이터 저장, signal() 호출");
            condition.signal(); // 지정한 condition에서 대기중인 스레드를 하나 깨움. 깨어난 스레드는 condition에서 빠져 나옴.
        } finally {
            lock.unlock();
        }
    }
    
    // 현재 버전은 큐(버퍼)가 비워져있을 때, 생산자가 생산해서 큐(버퍼)가 채워질 때까지 대기 -> 근데 누가 깨어날 지 모름.
    @Override
    public String take() {
        lock.lock();

        try {
            while (queue.isEmpty()) {
                log("[take] 큐에 데이터가 없음, 소비자 대기");
                try {
                    condition.await();
                    log("[take] 소비자 깨어남.");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            String data = queue.poll();
            log("[take] 소비자 데이터 소비, signal() 호출");
            condition.signal();

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
