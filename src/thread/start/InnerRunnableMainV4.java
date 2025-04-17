package thread.start;

import util.MyLogger;

public class InnerRunnableMainV4 {

    public static void main(String[] args) {
        MyLogger.log("main() start");

        // 익명 클래스 람다 버전
        Thread thread = new Thread(() -> MyLogger.log("run()"));
        thread.start();

        MyLogger.log("main() end");
    }
}
