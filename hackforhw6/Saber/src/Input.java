import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
//import com.oocourse.TimableOutput;
import java.io.IOException;

public class Input extends Thread {
    private RequestList requestList;
    private Dispatch dispatch;
    
    public Input(RequestList requestList, Dispatch dispatch) {
        super();
        this.requestList = requestList;
        this.dispatch = dispatch;
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        String arrivingPattern = elevatorInput.getArrivingPattern();
        requestList.setPattern(arrivingPattern);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest personRequest = (PersonRequest) request;
                    MyRequest re = new MyRequest(personRequest.getPersonId(),
                            personRequest.getFromFloor(), personRequest.getToFloor());
                    requestList.addre(re);
                } else if (request instanceof ElevatorRequest) {
                    ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                    Elevator elevator = new Elevator(dispatch, requestList,
                            elevatorRequest.getElevatorId());
                    Elevator.getElevators().add(elevator);
                    elevator.start();
                }
            }
        }
        requestList.setDone(true);
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
}
