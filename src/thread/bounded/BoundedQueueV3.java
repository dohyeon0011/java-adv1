package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BoundedQueueV3 implements BoundedQueue {

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;  // 버퍼 크기

    public BoundedQueueV3(int max) {
        this.max = max;
    }

    // 현재 버전은 큐(버퍼)가 가득 찼을 때, 소비자가 소비해서 큐(버퍼)가 비워질 때까지 대기
    @Override
    public synchronized void put(String data) {
        while (queue.size() == max) {
            log("[put] 큐가 가득 참, 생산자 대기");
            try {
                wait(); // RUNNABLE -> WAITING, 락을 다른 스레드에게 반납.
                log("[put] 생산자 깨어남.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        queue.offer(data);
        log("[put] 생산자 데이터 저장, notify() 호출");
        notify();   // 대기 스레드 깨워 락 획득 기회 주기(어떤 스레드가 깨어날 지 모름), WAITING -> BLOCKED
    }
    
    // 현재 버전은 큐(버퍼)가 비워져있을 때, 생산자가 생산해서 큐(버퍼)가 채워질 때까지 대기
    @Override
    public synchronized String take() {
        while (queue.isEmpty()) {
            log("[take] 큐에 데이터가 없음, 소비자 대기");
            try {
                wait(); // RUNNABLE -> WAITING, 락을 다른 스레드에게 반납.
                log("[take] 소비자 깨어남.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String data = queue.poll();
        log("[take] 소비자 데이터 소비, notify() 호출");
        notify();   // 대기 스레드 깨워 락 획득 기회 주기(어떤 스레드가 깨어날 지 모름), WAITING -> BLOCKED

        return data;
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
