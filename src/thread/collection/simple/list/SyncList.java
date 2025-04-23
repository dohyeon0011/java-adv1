package thread.collection.simple.list;

import java.util.Arrays;

import static util.ThreadUtils.sleep;

public class SyncList implements SimpleList {

    private static final int DEFAULT_CAPATICY = 5;

    private Object[] elementData;
    private int size = 0;


    public SyncList() {
        elementData = new Object[DEFAULT_CAPATICY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public synchronized void add(Object e) {
        elementData[size] = e;
        sleep(100); // 멀티스레드 문제를 쉽게 확인하는 코드(스레드1이 add()를 다 마치고 스레드2가 add()를 실행할 가능성도 있어서)
        size++;
    }

    @Override
    public synchronized Object get(int index) {
        return elementData[index];
    }

    @Override
    public synchronized String toString() {
        return Arrays.toString(Arrays.copyOf(elementData, size)) + " size = " + size + ", capaticy = " + elementData.length;
    }
}
