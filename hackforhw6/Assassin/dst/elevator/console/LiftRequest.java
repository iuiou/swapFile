package elevator.console;

import com.oocourse.elevator2.ElevatorRequest;

public class LiftRequest {
    private final int id;

    LiftRequest(ElevatorRequest p) {
        id = Integer.parseInt(p.getElevatorId());
    }

    public int getId() {
        return id;
    }
}
