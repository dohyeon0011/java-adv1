package thread.bounded;

public interface BoundedQueue { // 버퍼 역할을 하는 큐의 인터페이스
    void put(String data);  // 버퍼에 데이터를 보관(생산자 스레드가 호출하고, 데이터를 생산)

    String take();  // 버퍼에 보관된 값을 가져감(소비자 스레드가 호출하고, 데이터를 소비)
}
