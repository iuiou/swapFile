package elevator;

public interface Timer {
    long now();

    void sleep(long time);
}
