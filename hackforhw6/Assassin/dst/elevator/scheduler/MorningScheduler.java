package elevator.scheduler;

import elevator.Cabin;
import elevator.console.RideRequest;

public class MorningScheduler implements IScheduler {
    private final Cabin cab;
    private final GeneralScheduler sch;

    public MorningScheduler(Cabin cab) {
        this.cab = cab;
        sch = GeneralScheduler.createCenterFixed(cab, RideRequest.MIN_POS);
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
        return sch.snapshot();
    }

    @Override
    public void assign(RideRequest req) {
        sch.assign(req);
    }

    @Override
    public Status work(boolean interrupted) {
        int pos = cab.getPos();
        if (interrupted) {
            return sch.work(true);
        }
        if (pos != RideRequest.MIN_POS || cab.isFull()) {
            return sch.work(false);
        }
        cab.open();
        if (cab.getCap() <= sch.countPending(pos)) {
            sch.handleAll(pos);
        }
        return Status.WAIT;
    }
}
