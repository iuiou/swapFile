package elevator;

import elevator.console.RideRequest;
import elevator.scheduler.IScheduler;
import elevator.scheduler.IScheduler.Status;

import java.util.Collection;

abstract class Evaluator {
    static long evaluate(IScheduler sch, Collection<RideRequest> reqs) {
        Cabin cab = sch.getCab();
        long now = cab.now();
        DryTimer timer = new DryTimer(now);
        cab.setTimer(timer);
        sch.work(false);
        for (RideRequest req: reqs) {
            sch.assign(req);
        }
        while (true) {
            Status r = sch.work(true);
            if (r != Status.BUSY) {
                break;
            }
        }
        return timer.now();
    }
}
