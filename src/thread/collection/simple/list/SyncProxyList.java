package thread.collection.simple.list;

/**
 * **프록시 패턴의 주요 목적**
 *  -접근 제어: 실제 객체에 대한 접근을 제한하거나 통제할 수 있다.
 *  -성능 향상: 실제 객체의 생성을 지연시키거나 캐싱하여 성능을 최적화할 수 있다.
 *  -부가 기능 제공: 실제 객체에 추가적인 기능(로깅, 인증, 동기화 등)을 투명하게 제공할 수 있다.
 */
// 이 클래스는 모든 메서드에 synchronized를 대신 걸어줌.
// 그리고 원본 대상에 add(),get()와 같은 호출.
public class SyncProxyList implements SimpleList {  // 프록시 역할을 하는 클래스

    private SimpleList target;  // 프록시 안의 원본 대상

    public SyncProxyList(SimpleList target) {
        this.target = target;
    }

    @Override
    public synchronized int size() {
        return target.size();
    }

    @Override
    public synchronized void add(Object e) {
        target.add(e);
    }

    @Override
    public synchronized Object get(int index) {
        return target.get(index);
    }

    @Override
    public String toString() {
        return target.toString() + " by " + this.getClass().getSimpleName();
    }
}
