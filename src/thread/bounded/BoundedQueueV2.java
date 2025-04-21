package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BoundedQueueV2 implements BoundedQueue {

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;  // 버퍼 크기

    public BoundedQueueV2(int max) {
        this.max = max;
    }

    // 현재 버전은 큐(버퍼)가 가득 찼을 때, 소비자가 소비해서 큐(버퍼)가 비워질 때까지 1초 대기 반복.(하지만 얘는 생산자와 소비자가 같은 BoundedQueueV2 인스턴스(x001)를 공유하기 때문에 누가 먼저 실행하냐에 따라서 한 쪽이 락을 풀지 않는 이상 무기한 접근 불가임.(BLOCKED))
    @Override
    public synchronized void put(String data) {
        while (queue.size() == max) {
            log("[put] 큐가 가득 참, 생산자 대기");
            sleep(1000);
        }
        queue.offer(data);
    }
    
    // 현재 버전은 큐(버퍼)가 비워져있을 때, 생산자가 생산해서 큐(버퍼)가 채워질 때까지 1초 대기 반복.(하지만 얘는 생산자와 소비자가 같은 BoundedQueueV2 인스턴스(x001)를 공유하기 때문에 누가 먼저 실행하냐에 따라서 한 쪽이 락을 풀지 않는 이상 무기한 접근 불가임.(BLOCKED)
    @Override
    public synchronized String take() {
        while (queue.isEmpty()) {
            log("[take] 큐에 데이터가 없음, 소비자 대기");
            sleep(1000);
        }

        return queue.poll();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
