package elevator.console;

import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ElevatorRequest;

public class RequestVariant {
    private final Request body;

    RequestVariant(Request body) {
        this.body = body;
    }

    public static RequestVariant nil() {
        return new RequestVariant(null);
    }

    public boolean isNil() {
        return body == null;
    }

    public boolean isRider() {
        return body instanceof PersonRequest;
    }

    public RideRequest asRider() {
        return new RideRequest((PersonRequest) body);
    }

    public LiftRequest asElevator() {
        return new LiftRequest((ElevatorRequest) body);
    }
}
