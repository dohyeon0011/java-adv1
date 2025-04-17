package thread.start;

import util.MyLogger;

public class InnerRunnableMainV3 {

    public static void main(String[] args) {
        MyLogger.log("main() start");

        // 익명 클래스 V2 인라인 버전
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MyLogger.log("run()");
            }
        });
        thread.start();

        MyLogger.log("main() end");
    }
}
