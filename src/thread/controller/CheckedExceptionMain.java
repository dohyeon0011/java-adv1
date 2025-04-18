package thread.controller;

import util.ThreadUtils;

public class CheckedExceptionMain {

    public static void main(String[] args) throws Exception {
        throw new Exception();
    }

    static class CheckedRunnable implements Runnable {
        @Override
        public void run() {
//            throw new Exception();    부모가 못 던지는 예외를 자식에서 thr 할 수 없음.
            ThreadUtils.sleep(1000);
        }
    }
}
