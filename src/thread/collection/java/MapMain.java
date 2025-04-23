package thread.collection.java;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static util.ThreadUtils.sleep;

public class MapMain {

    public static void main(String[] args) throws InterruptedException {
        // 순서 보장 X
        // synchronized, Lock(ReentrantLock), CAS, 분할 잠금 기술(segment lock)등 다양한 방법을 섞어서 매우 정교한 동기화를 구현하면서 동시에 성능도 최적화.
        // 각각의 최적화는 매우 어렵게 구현되어 있기 때문에, 자세한 구현을 이해하는 것 보다는, 멀티스레드 환경에 필요한 동시성 컬렉션을 잘 선택해서 사용할 수 있으면 충분하다.
        Map<Integer, String> map1 = new ConcurrentHashMap<>();
//        map1.put(3, "data3");
//        map1.put(2, "data2");
//        map1.put(1, "data1");
//        System.out.println("map1 = " + map1);
//        test(new HashMap<>());  // 이러면 이제 동시성 처리 못하고 하나는 씹힐 수도 있는거
        test(new ConcurrentHashMap<>());

        // 데이터 정렬 순서 유지(Comparator를 인자로 받고 있어서, TreeMap 대안)
        Map<Integer, String> map2 = new ConcurrentSkipListMap<>();
//        map2.put(2, "data2");
//        map2.put(3, "data3");
//        map2.put(1, "data1");
//        System.out.println("map2 = " + map2);
    }

    private static void test(Map<Integer, String> map) throws InterruptedException {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                sleep(100);
                map.put(1, "data 1");
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                sleep(100);
                map.put(2, "data 2");
            }
        };

        Thread t1 = new Thread(r1, "Thread-1");
        Thread t2 = new Thread(r2, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("map = " + map);
    }
}
