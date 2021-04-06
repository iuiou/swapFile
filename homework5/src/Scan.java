import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Scan extends Thread {
    private WaitQueue queue;
    private Elevator elevator;

    public Scan(WaitQueue queue, Elevator elevator) {
        this.queue = queue;
        this.elevator = elevator;
    }

    public int getType(String nowType) {
        if (nowType.equals("Night")) {
            return 1;
        } else if (nowType.equals("Morning")) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        String arrivePattern = elevatorInput.getArrivingPattern();
        int elevatorType = getType(arrivePattern);
        elevator.setType(elevatorType);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                break;
            } else {
                queue.getpersonRequest(request);
            }
        }
        queue.endRead();
        synchronized (queue) {
            queue.notify();
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

