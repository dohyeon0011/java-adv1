package thread.start;

import util.MyLogger;

public class ManyThreadMainV2 {

    public static void main(String[] args) {
        MyLogger.log("main() start");

        HelloRunnable runnable = new HelloRunnable();

        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }

        MyLogger.log("main() end");
    }
}
