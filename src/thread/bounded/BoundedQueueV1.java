package thread.bounded;

import java.util.ArrayDeque;
import java.util.Queue;

import static util.MyLogger.log;

public class BoundedQueueV1 implements BoundedQueue {

    private final Queue<String> queue = new ArrayDeque<>();
    private final int max;  // 버퍼 크기

    public BoundedQueueV1(int max) {
        this.max = max;
    }

    // 현재 버전은 큐(버퍼)가 가득 찼을 때, 소비자가 소비하기 전까지 기다렸다가 소비하고 비워지고 난 후 추가하지 않아서, 해당 데이터는 버려지게 됨.
    @Override
    public synchronized void put(String data) {
        if (queue.size() == max) {
            log("[put] 큐가 가득 참, 버림: " + data);
        }
        queue.offer(data);
    }

    // 현재 버전은 큐(버퍼)가 비워져있을 때, 생산자가 생산하기 전까지 기다렸다가 소비하지 않고, 바로 null을 얻어서 소비하지 못하는 소비자가 발생.
    @Override
    public synchronized String take() {
        if (queue.isEmpty()) {
            return null;
        }

        return queue.poll();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
