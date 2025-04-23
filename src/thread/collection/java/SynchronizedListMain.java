package thread.collection.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SynchronizedListMain {

    public static void main(String[] args) {
        // SynchronizedRandomAccessList` 는 `synchronized` 를 추가하는 프록시 역할을 한다.
        // 원래는 클라이언트 -> ArrayList
        // 클라이언트 -> SynchronizedRandomAccessList(프록시) -> ArrayList
        // 대신, 각 메서드 호출 시마다 동기화 비용이 추가돼서 성능 저하 발생 가능.
        // 임계 영역을 잠금하는 범위가 넓어지기 때문에 병렬 처리 효율성을 낮춤.(정교한 동기화 불가능)
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        list.add("data1");
        list.add("data1");
        list.add("data2");
        list.add("data3");
        System.out.println(list.getClass());
        System.out.println("list = " + list);
    }
}
