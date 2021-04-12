import com.oocourse.TimableOutput;

public class Elevator extends Thread {
    private ProcessQueue processQueue;
    private String elevatorId;
    private String arrivingPattern;
    private WaitQueue waitQueue;
    private int floor = 1;
    
    public Elevator(String elevatorId, String arrivingPattern, WaitQueue waitQueue) {
        this.processQueue = new ProcessQueue();
        this.elevatorId = elevatorId;
        this.arrivingPattern = arrivingPattern;
        this.waitQueue = waitQueue;
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.isEmpty() && waitQueue.getEnd()) {
                    //System.err.println("finish-" + elevatorId);
                    return;
                }
                if (waitQueue.isEmpty()) {
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (processQueue.isEmpty()) {
                synchronized (waitQueue) {
                    if (waitQueue.isEmpty()) {
                        break;
                    } else {
                        switch (arrivingPattern) {
                            case "Random":
                                dealRandomProcess();
                                break;
                            case "Morning":
                                dealRandomProcess();
                                break;
                            case "Night":
                                dealNightProcess();
                                break;
                            default:
                        }
                    }
                }
                elevatorRunToFloor(processQueue.getPersonById(0).getFromFloor());
                openDoor();
                TimableOutput.println(
                        String.format(
                                "IN-" + processQueue.getPersonById(0).getPersonId() + "-" + floor + "-" +
                                        elevatorId));
                elevatorStop();
                if (arrivingPattern.equals("Morning")) {
                    try {
                        sleep(1600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                personIn();
                closeDoor();
            }
            while (!processQueue.isEmpty()) {
                if (processQueue.getPersonById(0).getDirection()) {
                    upStair();
                } else {
                    downStair();
                }
                floorProcess();
            }
        }
    }
    
    public void upStair() {
        floor++;
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println(String.format("ARRIVE-" + floor + "-" + elevatorId));
    }
    
    public void downStair() {
        floor--;
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println(String.format("ARRIVE-" + floor + "-" + elevatorId));
    }
    
    public void openDoor() {
        TimableOutput.println(String.format("OPEN-" + floor + "-" + elevatorId));
    }
    
    public void closeDoor() {
        TimableOutput.println(String.format("CLOSE-" + floor + "-" + elevatorId));
    }
    
    public synchronized void dealRandomProcess() {
        synchronized (waitQueue) {
            int distance = 100;
            int temp = 0;
            for (int i = 0; i < waitQueue.size(); i++) {
                if (Math.abs(waitQueue.getPerson(i).getFromFloor() - floor) < distance) {
                    distance = Math.abs(waitQueue.getPerson(i).getFromFloor() - floor);
                    temp = i;
                }
            }
            processQueue.addPerson(waitQueue.getPerson(temp));
            waitQueue.deletePersonById(temp);
        }
    }
    
    public synchronized void dealNightProcess() {
        synchronized (waitQueue) {
            int max = 0;
            int temp = 0;
            for (int i = 0; i < waitQueue.size(); i++) {
                if (waitQueue.getPerson(i).getToFloor() > max) {
                    max = waitQueue.getPerson(i).getToFloor();
                    temp = i;
                }
            }
            processQueue.addPerson(waitQueue.getPerson(temp));
            waitQueue.deletePersonById(temp);
        }
    }
    
    public void elevatorRunToFloor(int destination) {
        while (floor != destination) {
            if (destination > floor) {
                upStair();
            } else {
                downStair();
            }
        }
    }
    
    public void elevatorStop() {
        try {
            sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void personIn() {
        synchronized (waitQueue) {
            for (int i = 0; i < waitQueue.size(); i++) {
                if (!processQueue.isEmpty() && waitQueue.getPerson(i).getDirection()
                        .equals(processQueue.getPersonById(0).getDirection()) &&
                        !processQueue.full() && waitQueue.getPerson(i).getFromFloor() == floor) {
                    TimableOutput.println(
                            String.format(
                                    "IN-" + waitQueue.getPerson(i).getPersonId() + "-" +
                                            floor + "-" +
                                            elevatorId));
                    processQueue.addPerson(waitQueue.getPerson(i));
                    waitQueue.deletePersonById(i);
                    i--;
                }
            }
        }
    }
    
    public void personOut() {
        for (int i = 0; i < processQueue.size(); i++) {
            if (processQueue.getPersonById(i).getToFloor() == floor) {
                TimableOutput.println(String.format(
                        "OUT-" + processQueue.getPersonById(i).getPersonId() + "-" + floor + "-" +
                                elevatorId));
                processQueue.deletePersonById(i);
                i--;
            }
        }
    }
    
    public Boolean arriveOpen() {
        Boolean open = false;
        for (Person person : processQueue.getPeople()) {
            if (person.getToFloor() == floor) {
                open = true;
                break;
            }
        }
        return open;
    }
    
    public synchronized Boolean takeOpen() {
        synchronized (waitQueue) {
            Boolean open = false;
            for (Person person : waitQueue.getPeople()) {
                if (person.getFromFloor() == floor && !processQueue.isEmpty() &&
                        person.getDirection().equals(processQueue.getPersonById(0).getDirection()) &&
                        !processQueue.full()) {
                    open = true;
                    break;
                }
            }
            return open;
        }
    }
    
    public void floorProcess() {
        if (arriveOpen() || takeOpen()) {
            openDoor();
            personOut();
            elevatorStop();
            personIn();
            closeDoor();
        }
    }
}