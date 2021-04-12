package elevator.scheduler;

import java.util.Comparator;
import java.util.List;

import elevator.Cabin;
import elevator.console.RideRequest;

public class GeneralScheduler implements IScheduler {
    private final Cabin cab;
    private final ListMap<RideRequest> srcOuter;
    private final ListMap<Integer> dstInner;

    private int cntTodo = 0;
    private int cntEver = 0;
    private double sumSrc = 0;
    private int center;
    private Integer idleTarget;
    private final boolean centerFixed;
    private int dir = 1;

    private static final int MAX_IDLE_MOVE = 4;

    public GeneralScheduler(Cabin cab, int center, Integer target) {
        this(cab, center, target, false);
    }

    private GeneralScheduler(Cabin cab, int center, Integer target, boolean centerFixed) {
        this.cab = cab;
        srcOuter = new ListMap<>(RideRequest.MAX_POS + 1);
        dstInner = new ListMap<>(RideRequest.MAX_POS + 1);
        if (center < 0) {
            this.center = -center;
        } else if (center > 0) {
            this.center = center;
            cntEver = 1;
            sumSrc = center;
        } else {
            this.center = RideRequest.MIN_POS + 1;
        }
        idleTarget = target;
        this.centerFixed = centerFixed;
    }

    static GeneralScheduler createCenterFixed(Cabin cab, int center) {
        return new GeneralScheduler(cab, center, center, true);
    }

    private GeneralScheduler(GeneralScheduler o) {
        cab = o.cab.snapshot();
        srcOuter = o.srcOuter.clones();
        dstInner = o.dstInner.clones();
        cntTodo = o.cntTodo;
        cntEver = o.cntEver;
        sumSrc = o.sumSrc;
        idleTarget = o.idleTarget;
        center = o.center;
        centerFixed = o.centerFixed;
        dir = o.dir;
    }

    @Override
    public void init() {
        cab.init();
    }

    @Override
    public Cabin getCab() {
        return cab;
    }

    @Override
    public GeneralScheduler snapshot() {
        return new GeneralScheduler(this);
    }

    @Override
    public void assign(RideRequest req) {
        ++cntTodo;
        int src = req.getSrc();
        if (!centerFixed) {
            ++cntEver;
            sumSrc += src;
            int c = (int) Math.round(sumSrc / cntEver);
            if (c >= RideRequest.MIN_POS && c <= RideRequest.MAX_POS) {
                center = c;
            }
        }
        srcOuter.add(src, req);
    }

    private void handle(int pos, boolean checkDir) {
        List<Integer> r = dstInner.pop(pos);
        if (r != null) {
            for (Integer id : r) {
                --cntTodo;
                cab.exit(id);
            }
        }
        if (cab.isFull()) {
            return;
        }
        List<RideRequest> reqs = srcOuter.get(pos);
        if (reqs != null) {
            reqs.sort(Comparator.comparing(req -> Math.abs(pos - req.getDst())));
            reqs.removeIf(req -> {
                if (cab.isFull() || (checkDir && req.getDir() != dir)) {
                    return false;
                }
                int id = req.getId();
                cab.enter(id);
                dstInner.add(req.getDst(), id);
                return true;
            });
        }
    }

    public void handleAll(int pos) {
        handle(pos, false);
    }

    public int countPending(int pos) {
        return srcOuter.count(pos);
    }

    private boolean isIdle() {
        return cntTodo <= 0;
    }

    private int findIdleTarget() {
        int pos = cab.getPos();
        int t = pos - MAX_IDLE_MOVE;
        if (center < t) {
            return t;
        }
        return Math.min(center, pos + MAX_IDLE_MOVE);
    }

    private int getIdleTarget() {
        if (centerFixed) {
            return center;
        }
        if (idleTarget == null) {
            return idleTarget = findIdleTarget();
        }
        return idleTarget;
    }

    private boolean moveIdle() {
        int target = getIdleTarget();
        return cab.moveTowards(target) != target;
    }

    private void moveBusy() {
        int pos = cab.getPos();
        if (pos == RideRequest.MIN_POS) {
            dir = 1;
        } else if (pos == RideRequest.MAX_POS) {
            dir = -1;
        } else {
            boolean keep = dstInner.someDirected(pos, dir);
            if (!cab.isFull()) {
                keep = keep || srcOuter.someDirected(pos, dir);
            }
            if (!keep) {
                dir = -dir;
            }
        }
        handle(pos, true);
        if (isIdle()) {
            if (!moveIdle()) {
                return;
            }
        } else {
            idleTarget = null;
            pos = cab.move(dir);
        }
        handle(pos, true);
    }

    @Override
    public Status work(boolean interrupted) {
        if (isIdle()) {
            if (interrupted) {
                cab.close();
                return Status.DEAD;
            }
            if (!moveIdle()) {
                return Status.IDLE;
            }
        } else {
            idleTarget = null;
            moveBusy();
        }
        return Status.BUSY;
    }

    @Override
    public String toString() {
        return String.format("[GScheduler %d]", getId());
    }
}
