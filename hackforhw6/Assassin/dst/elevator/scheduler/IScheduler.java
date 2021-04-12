package elevator.scheduler;

import elevator.Cabin;
import elevator.console.RideRequest;

public interface IScheduler {
    enum Status {
        IDLE,
        BUSY,
        DEAD,
        WAIT
    }

    void assign(RideRequest req);

    Status work(boolean interrupted);

    IScheduler snapshot();

    void init();

    Cabin getCab();

    default int getId() {
        return getCab().getId();
    }
}
