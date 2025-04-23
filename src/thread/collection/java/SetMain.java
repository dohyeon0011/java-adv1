package thread.collection.java;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class SetMain {

    public static void main(String[] args) {
        Set<Integer> copySet = new CopyOnWriteArraySet<>(); // HashSet 대안
        copySet.add(1);
        copySet.add(2);
        copySet.add(3);
        System.out.println("copySet = " + copySet);

        // 데이터 정렬 됨.(comparator를 인자로 받고 있어서)
        Set<Object> skipSet = new ConcurrentSkipListSet<>();
        skipSet.add(2);
        skipSet.add(1);
        skipSet.add(3);
        System.out.println("skipSet = " + skipSet);
    }
}
