import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.sun.java.browser.net.ProxyServiceProvider;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

public class Scan extends Thread {
    private RequestQueue requestQueue;
    private ArrayList<Pair<Elevator, WaitQueue>> elevators;

    public Scan(RequestQueue requestQueue, ArrayList<Pair<Elevator, WaitQueue>> elevators) {
        this.requestQueue = requestQueue;
        this.elevators = elevators;
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
        SchedulerFactory factory = new SchedulerFactory();
        Scheduler scheduler = factory.produceScheduler(elevatorType, requestQueue, elevators);
        scheduler.start();
        SchemerFactory schemerFactory = new SchemerFactory();
        for (Pair<Elevator, WaitQueue> item : elevators) {
            Schemer nowSchemer = schemerFactory.produceSchemer(elevatorType, item.getKey(), item.getValue());
            Modifyer modifyer = new Modifyer(item.getKey(), item.getValue(), nowSchemer);
            modifyer.start();
        }
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            } else {
                if (request instanceof ElevatorRequest) {
                    String id = ((ElevatorRequest) request).getElevatorId();
                    Elevator newElevator = new Elevator(1, id);
                    WaitQueue newWaitQueue = new WaitQueue(id);
                    Pair<Elevator, WaitQueue> newPair = new Pair<>(newElevator, newWaitQueue);
                    elevators.add(newPair);
                    Schemer nowSchemer = schemerFactory.produceSchemer(elevatorType, newElevator, newWaitQueue);
                    Modifyer newModifyer = new Modifyer(newElevator, newWaitQueue, nowSchemer);
                    newModifyer.start();
                } else {
                    PersonRequest personRequest = (PersonRequest) request;
                    requestQueue.getpersonRequest(personRequest);
                }
            }
        }
        requestQueue.setEndIn();
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

