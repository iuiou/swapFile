package elevator.console;

import com.oocourse.elevator2.PersonRequest;

public class RideRequest {
    public static final int MAX_POS = 20;
    public static final int MIN_POS = 1;

    private final int src;
    private final int dst;
    private final int id;
    private final int dir;

    RideRequest(PersonRequest p) {
        src = p.getFromFloor();
        dst = p.getToFloor();
        id = p.getPersonId();
        dir = dst > src ? 1 : -1;
    }

    @Override
    public String toString() {
        return String.format("<%d: %d to %d>", id, src, dst);
    }

    public int getSrc() {
        return src;
    }

    public int getDst() {
        return dst;
    }

    public int getId() {
        return id;
    }

    public int getDir() {
        return dir;
    }
}
