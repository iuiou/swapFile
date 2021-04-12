package elevator.console;

import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;

import java.io.IOException;
import java.io.InputStream;

public class Client {
    public static final int MAX_REQUEST_NUM = 55;

    private final ElevatorInput in;
    private final String mode;
    private boolean eof = false;

    public Client(InputStream s) {
        in = new ElevatorInput(s);
        mode = in.getArrivingPattern();
    }

    public String getMode() {
        return mode;
    }

    public RequestVariant next() {
        if (eof) {
            return null;
        }
        Request r = in.nextRequest();
        if (r == null) {
            eof = true;
            return null;
        }
        return new RequestVariant(r);
    }

    public void drop() {
        try {
            in.close();
        } catch (IOException ignored) {
            ;
        }
    }
}
