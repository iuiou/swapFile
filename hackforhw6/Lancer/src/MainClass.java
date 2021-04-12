import com.oocourse.TimableOutput;
import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

public class MainClass {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        String arrivingPattern = elevatorInput.getArrivingPattern();
        WaitQueue waitQueue = new WaitQueue();
        Elevator elevator1 = new Elevator("1", arrivingPattern, waitQueue);
        elevator1.start();
        Elevator elevator2 = new Elevator("2", arrivingPattern, waitQueue);
        elevator2.start();
        Elevator elevator3 = new Elevator("3", arrivingPattern, waitQueue);
        elevator3.start();
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                synchronized (waitQueue) {
                    waitQueue.notifyAll();
                    waitQueue.close();
                }
                break;
            } else {
                if (request instanceof PersonRequest) {
                    synchronized (waitQueue) {
                        Person person = new Person(((PersonRequest) request).getPersonId(),
                                ((PersonRequest) request).getFromFloor(),
                                ((PersonRequest) request).getToFloor());
                        waitQueue.addPerson(person);
                        waitQueue.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) {
                    Elevator elevator;
                    synchronized (waitQueue) {
                        elevator = new Elevator(((ElevatorRequest) request).getElevatorId(),
                                arrivingPattern, waitQueue);
                    }
                    elevator.start();
                }
            }
        }
        //System.err.println("inputfinish");
        elevatorInput.close();
    }
}
