import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        ArrayList<ElevatorQueue> elevatorQueues = new ArrayList<>(5);
        WaitQueue waitQueue = new WaitQueue();
        for (int i = 1; i < 6; i++) {
            ElevatorQueue elevatorQueue = new ElevatorQueue(i);
            if (i < 4) {
                elevatorQueue.setChoseSign();
            }
            elevatorQueues.add(elevatorQueue);
            Elevator elevator = new Elevator(elevatorQueue, waitQueue);
            elevator.start();
        }
        Dispatcher dispatcher = new Dispatcher(elevatorQueues, waitQueue);
        dispatcher.start();
        
        InputThread inputThread = new InputThread(waitQueue, elevatorQueues);
        inputThread.start();
    }
}
