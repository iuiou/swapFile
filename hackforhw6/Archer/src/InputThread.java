
import com.oocourse.elevator2.ElevatorInput;

import java.io.IOException;
import java.util.ArrayList;

import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.ElevatorRequest;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;
    private ArrayList<ElevatorQueue> elevatorQueues;
    
    public InputThread(WaitQueue waitQueue, ArrayList<ElevatorQueue> elevatorQueues) {
        this.waitQueue = waitQueue;
        this.elevatorQueues = elevatorQueues;
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        String arrivePattern = elevatorInput.getArrivingPattern();
        while (true) {
            Request request = elevatorInput.nextRequest();
            synchronized (waitQueue) {
                if (request == null) {
                    waitQueue.close();
                    waitQueue.notifyAll();
                    try {
                        elevatorInput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    if (request instanceof PersonRequest) {
                        waitQueue.addPassenger(new Passenger(request));
                        waitQueue.notifyAll();
                    } else {
                        int id = Integer.parseInt(((ElevatorRequest) request).getElevatorId());
                        synchronized (elevatorQueues) {
                            for (ElevatorQueue elevatorQueue : elevatorQueues) {
                                synchronized (elevatorQueue) {
                                    if (!elevatorQueue.isChoseSign()) {
                                        elevatorQueue.setElevatorId(id);
                                        elevatorQueue.setChoseSign();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
