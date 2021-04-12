package elevator;

public class DryTimer implements Timer {
    private long time;

    DryTimer(long time) {
        this.time = time;
    }

    @Override
    public long now() {
        return time;
    }

    @Override
    public void sleep(long duration) {
        time += duration;
    }
}
