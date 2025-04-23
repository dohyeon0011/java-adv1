package thread.collection.simple;

import thread.collection.simple.list.BasicList;
import thread.collection.simple.list.SimpleList;
import thread.collection.simple.list.SyncList;
import thread.collection.simple.list.SyncProxyList;

import static util.MyLogger.log;

public class SimpleListMainV2 {

    public static void main(String[] args) throws InterruptedException {
//        test(new BasicList());
//        test(new SyncList());
        // test()가 클라이언트라 치면 어떤 SimpleList 구현체인 BasicList, SyncList, SynProxyList중 어떤 것을 사용하던 클라이언트의 test() 코드는 수정안해도 됨.
        // 클라이언트인 test() 입장에서는 BasicList가 넘어올지, SyncProxyList가 넘어올 지 상관 없음.
        // 원본 코드에 손대지 않고 프록시 대상인 SyncProxyList를 이용해서 원본 대상에 접근
        test(new SyncProxyList(new BasicList()));
    }

    private static void test(SimpleList list) throws InterruptedException {
        log(list.getClass().getSimpleName());

        // A를 리스트에 저장하는 스레드
        Runnable addA = new Runnable() {
            @Override
            public void run() {
                list.add("A");
                log("Thread-1: list.add(A)");
            }
        };

        // B를 리스트에 저장하는 스레드
        Runnable addB = new Runnable() {
            @Override
            public void run() {
                list.add("B");
                log("Thread-2: list.add(B)");
            }
        };

        Thread thread1 = new Thread(addA, "Thread-1");
        Thread thread2 = new Thread(addB, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        log(list);
    }
}
