package elevator;

public class Cabin {
    private final int id;

    private final int moveTime;
    private final int transactionTime;
    private final int capacity;

    private int pos;
    private int cnt = 0;
    private boolean opened = false;

    private Timer timer;

    private long arriveMoment;
    private long openMoment;
    private long closeMoment;

    private boolean muted = false;

    public Cabin(
        int id,
        Timer timer,
        int moveTime,
        int transactionTime,
        int capacity,
        int initialPos
    ) {
        this.id = id;
        this.timer = timer;
        this.moveTime = moveTime;
        this.transactionTime = transactionTime;
        this.capacity = capacity;
        pos = initialPos;
    }

    public Cabin snapshot() {
        Cabin r = new Cabin(id, timer, moveTime, transactionTime, capacity, pos);
        r.cnt = cnt;
        r.opened = opened;
        r.arriveMoment = arriveMoment;
        r.openMoment = openMoment;
        r.closeMoment = closeMoment;
        r.muted = true;
        return r;
    }

    public void setTimer(Timer t) {
        timer = t;
    }

    public int getId() {
        return id;
    }

    public void init() {
        arriveMoment = openMoment = closeMoment = timer.now();
    }

    public int getPos() {
        return pos;
    }

    public int getCap() {
        return capacity - cnt;
    }

    public long now() {
        return timer.now();
    }

    private void until(long start, long start2, int duration) {
        long dt = Math.max(start, start2) + duration;
        dt -= now();
        if (dt > 0) {
            timer.sleep(dt);
        }
    }

    private void send(String type, int a) {
        if (!muted) {
            Util.send(type, a, id);
        }
    }

    private void send(String type, int a, int b) {
        if (!muted) {
            Util.send(type, a, b, id);
        }
    }

    private int arrive(int pos) {
        close();
        this.pos = pos;
        until(arriveMoment, closeMoment, moveTime);
        send("ARRIVE", pos);
        arriveMoment = now();
        return pos;
    }

    public int move(int dir) {
        if (dir > 0) {
            return arrive(pos + 1);
        }
        return arrive(pos - 1);
    }

    public int moveTowards(int dst) {
        if (pos == dst) {
            return pos;
        }
        if (pos < dst) {
            return arrive(pos + 1);
        }
        return arrive(pos - 1);
    }

    public void enter(int id) {
        open();
        ++cnt;
        send("IN", id, pos);
    }

    public void exit(int id) {
        open();
        --cnt;
        send("OUT", id, pos);
    }

    public boolean isFull() {
        return cnt >= capacity;
    }

    public void open() {
        if (!opened) {
            opened = true;
            send("OPEN", pos);
            openMoment = now();
        }
    }

    public void close() {
        if (opened) {
            opened = false;
            until(arriveMoment, openMoment, transactionTime);
            send("CLOSE", pos);
            closeMoment = now();
        }
    }
}
