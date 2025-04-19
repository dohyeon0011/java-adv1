package thread.controller.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

/**
 * ** 시점의 차이 **
 *  `main` 스레드가 `flag` 를 `false`로 변경한 시점에 `count` 값은 `1176711196` 이다.
 *  `work` 스레드가 `flag` 값을 `false`로 확인한 시점에 `count` 값은 `1200000000` 이다.
 *
 * 결과적으로 `main` 스레드가 `flag` 값을 `false` 로 변경하고 한참이 지나서야 `work` 스레드는 `flag` 값이 `false`로 변경된 것을 확인한 것이다.
 */
public class VolatileCountMain {

    public static void main(String[] args) {
        MyTask task = new MyTask();
        Thread t = new Thread(task, "work");
        t.start();

        sleep(1000);

        task.flag = false;  // main 스레드가 flag를 false로 변경하는 시점에 count 값은 `1176711196`이다.
        log("flag = " + task.flag + ", count = " + task.count + " in main");
    }

    static class MyTask implements Runnable {

//        boolean flag = true;
//        long count;

        // `volatile` 을 적용하면 캐시 메모리가 아니라 메인 메모리에 항상 직접 접근하기 때문에 성능이 상대적으로 떨어진다.
        // `volatile` 이 없을 때: `1176711196` , 약 11억(정확한 숫자는 아니고 대략적인 수치다)
        // `volatile` 이 있을 때: `222297705` , 약 2.2억
        // 둘을 비교해보면 물리적으로 약 5배의 성능 차이를 확인할 수 있다. 성능은 환경에 따라 차이가 있다.
        volatile long count;
        volatile boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                count++;

                // 1억번에 한 번씩 출력
                if (count % 100_000_000 == 0) {
                    // 여기서 정확히 12억에서 변경된 `flag` 값을 읽을 수 있었던 이유는 12억에서 콘솔에 결과를 출력하기 때문이다.
                    // 콘솔에 결과를 출력하면, 출력하는 동안 스레드가 잠시 대기하며 쉬는데, 이럴 때 컨택스트 스위칭이 발생하면서 캐시 메모리의 값이 갱신된다.
                    // 참고로 이 부분은 주로 그렇다는 것이지 확실하게 캐시의 갱신을 보장하지는 않는다. 따라서 환경에 따라 결과가 달라질 수 있다.
                    // 결국 이 상황에서 메모리 가시성 문제를 확실하게 해결하려면 `volatile` 키워드를 사용해야 한다.
                    log("flag = " + flag + ", count = " + count + " in while()");
                }
            }
            log("flag = " + flag + ", count = " + count + " 종료");   // work 스레드는 이후에 count 값이 1200000000이 되었을 때 flag가 false로 변한 것을 확인할 수 있다.
        }
    }
}
