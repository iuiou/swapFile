package elevator;

import elevator.console.RideRequest;
import elevator.scheduler.GeneralScheduler;
import elevator.scheduler.IScheduler;
import elevator.scheduler.MorningScheduler;

import java.util.ArrayList;
import java.util.List;

class Handler {
    private final Worker worker;
    private final Thread th;
    private final List<RideRequest> reqStage = new ArrayList<>();
    private IScheduler lastSnapshot;

    Handler(int id, int center, Integer target, boolean isMorning) {
        Cabin cab = CabinFactory.create(id);
        IScheduler sch = isMorning ? new MorningScheduler(cab) :
                new GeneralScheduler(cab, center, target);
        worker = new Worker(sch);
        th = new Thread(worker);
        lastSnapshot = sch.snapshot();
        lastSnapshot.init();
    }

    long evaluate(RideRequest req) {
        IScheduler ss = worker.getSnapshot();
        if (ss != null && ss != lastSnapshot) {
            lastSnapshot = ss;
            reqStage.clear();
        }
        reqStage.add(req);
        long r = Evaluator.evaluate(lastSnapshot.snapshot(), reqStage);
        reqStage.remove(reqStage.size() - 1);
        return r;
    }

    void assign(RideRequest req) {
        reqStage.add(req);
        worker.assign(req);
    }

    void interrupt() {
        worker.interrupt();
    }

    void start() {
        th.start();
    }

    void join() {
        try {
            th.join();
        } catch (InterruptedException e) {
            ;
        }
    }
}
