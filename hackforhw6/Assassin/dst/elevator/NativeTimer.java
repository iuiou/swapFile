package elevator;

public class NativeTimer implements Timer {
    public long toMillis(long duration) {
        return duration;
    }

    @Override
    public long now() {
        return System.currentTimeMillis();
    }

    @Override
    public void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
