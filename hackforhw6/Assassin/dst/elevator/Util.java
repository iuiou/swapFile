package elevator;

import com.oocourse.TimableOutput;

public abstract class Util {
    private static NativeTimer timer;

    public static NativeTimer getTimer() {
        return timer;
    }

    static {
        TimableOutput.initStartTimestamp();
        timer = new NativeTimer();
    }

    public static long toMillis(long duration) {
        return timer.toMillis(duration);
    }

    private static final Object TIMED_LOCK = new Object();

    private static void timedPrintln(String s) {
        synchronized (TIMED_LOCK) {
            TimableOutput.println(s);
        }
    }

    static void send(String type, int a, int b) {
        timedPrintln(type + '-' + a + '-' + b);
    }

    static void send(String type, int a, int b, int c) {
        timedPrintln(type + '-' + a + '-' + b + '-' + c);
    }
}
