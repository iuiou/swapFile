import com.oocourse.TimableOutput;

import java.util.ArrayList;

class MainClass {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        WaitQueue waitQueue = new WaitQueue();
        ArrayList<ElevatorIn> elevatorIns = new ArrayList<>();
        ArrayList<ElevatorWait> elevatorWaits = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            ElevatorIn elevatorIn = new ElevatorIn();
            elevatorIns.add(elevatorIn);
            ElevatorWait elevatorWait = new ElevatorWait();
            elevatorWaits.add(elevatorWait);

            ElevatorThread elevatorThread = new ElevatorThread(elevatorIn, elevatorWait,
                    waitQueue, String.valueOf(i));
            Thread threadElevator = new Thread(elevatorThread);
            threadElevator.start();
        }

        Scheduler scheduler = new Scheduler(waitQueue, elevatorIns, elevatorWaits);
        Thread threadScheduler = new Thread(scheduler);
        threadScheduler.start();

        InputThread inputThread = new InputThread(waitQueue, elevatorIns, elevatorWaits);
        Thread threadInput = new Thread(inputThread);
        threadInput.start();
    }
}