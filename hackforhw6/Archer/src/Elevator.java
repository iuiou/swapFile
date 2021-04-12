import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private ElevatorQueue elevatorQueue;
    private int direction = 1;
    private int floor = 1;
    private WaitQueue waitQueue;
    private ArrayList<Passenger> passengers = new ArrayList<>(6);
    private int[] floorPersonNumber = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private long openTime;
    private long lastTime;
    private boolean reverseSign = false;
    private boolean chooseSign = false;
    
    public Elevator(ElevatorQueue elevatorQueue, WaitQueue waitQueue) {
        this.elevatorQueue = elevatorQueue;
        this.waitQueue = waitQueue;
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (elevatorQueue) {
                synchronized (waitQueue) {
                    if ((waitQueue.isEnd()) && (waitQueue.isEmpty()) && (elevatorQueue.isEmpty())
                            && (this.passengers.isEmpty())) {
                        return;
                    }
                }
                if ((elevatorQueue.isEmpty()) && (this.passengers.isEmpty())) {
                    try {
                        elevatorQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                randomMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void randomMethod() throws InterruptedException {
        if ((this.passengers.size() == 6) && (this.floorPersonNumber[this.floor - 1] == 0)) {
            normalRun1();
        } else {
            if (this.floorPersonNumber[this.floor - 1] != 0) {
                this.openTime = TimableOutput.println("OPEN-" + this.floor
                        + "-" + this.elevatorQueue.getElevatorId());
                out();
                sleep(400 - (this.lastTime - this.openTime));
                in();
                if (this.chooseSign) {
                    this.chooseSign = false;
                    in();
                }
                TimableOutput.println("CLOSE-" + this.floor + "-"
                        + this.elevatorQueue.getElevatorId());
                normalRun2();
            } else {
                ArrayList<Passenger> requestList;
                synchronized (elevatorQueue) {
                    requestList = elevatorQueue.getTheFloorRequest(this.floor, this.direction);
                }
                if (requestList.size() != 0) {
                    this.openTime = TimableOutput.println("OPEN-" + this.floor
                            + "-" + this.elevatorQueue.getElevatorId());
                    sleep(400);
                    in();
                    TimableOutput.println("CLOSE-" + this.floor
                            + "-" + this.elevatorQueue.getElevatorId());
                    normalRun2();
                } else {
                    in();
                    if (this.chooseSign) {
                        this.chooseSign = false;
                        this.openTime = TimableOutput.println("OPEN-" +
                                this.floor + "-" + this.elevatorQueue.getElevatorId());
                        sleep(400);
                        in();
                        TimableOutput.println("CLOSE-" + this.floor + "-"
                                + this.elevatorQueue.getElevatorId());
                    }
                    normalRun2();
                }
            }
        }
    }
    
    private void normalRun1() throws InterruptedException {
        Thread.sleep(400);
        this.floor += this.direction;
        TimableOutput.println("ARRIVE-" + this.floor + "-" + this.elevatorQueue.getElevatorId());
    }
    
    private void normalRun2() throws InterruptedException {
        if (this.reverseSign) {
            this.direction *= -1;
            this.reverseSign = false;
        }
        if (!(passengers.isEmpty() && elevatorQueue.isEmpty())) {
            Thread.sleep(400);
            this.floor += this.direction;
            TimableOutput.println("ARRIVE-" + this.floor + "-"
                    + this.elevatorQueue.getElevatorId());
        }
    }
    
    private void out() {
        ArrayList<Passenger> a = new ArrayList<>();
        for (Passenger passenger : this.passengers) {
            if (passenger.getToFloor() == this.floor) {
                this.floorPersonNumber[this.floor - 1] -= 1;
                a.add(passenger);
                this.lastTime = TimableOutput.println("OUT-" +
                        passenger.getPersonId() + "-" + this.floor +
                        "-" + this.elevatorQueue.getElevatorId());
            }
        }
        for (Passenger passenger : a) {
            this.passengers.remove(passenger);
        }
    }
    
    private void in() {
        ArrayList<Passenger> requestList;
        synchronized (elevatorQueue) {
            requestList = elevatorQueue.getTheFloorRequest(this.floor, this.direction);
        }
        if (requestList.size() != 0) {
            for (Passenger passenger : requestList) {
                if (this.passengers.size() < 6) {
                    this.lastTime = TimableOutput.println("IN-" +
                            passenger.getPersonId() + "-" + this.floor + "-" +
                            this.elevatorQueue.getElevatorId());
                    elevatorQueue.delPersonRequest(passenger);
                    this.passengers.add(passenger);
                    this.floorPersonNumber[passenger.getToFloor() - 1] += 1;
                }
            }
        } else {
            if (this.passengers.size() == 0) {
                synchronized (elevatorQueue) {
                    if (!elevatorQueue.scan(this.floor, this.direction)) {
                        if (elevatorQueue.getReverseRequest(this.floor, this.direction) != null) {
                            if (this.floor == elevatorQueue.
                                    getReverseRequest(this.floor, this.direction).getFromFloor()) {
                                this.direction *= -1;
                                this.chooseSign = true;
                            } else {
                                this.reverseSign = false;
                            }
                        } else {
                            this.reverseSign = true;
                        }
                    }
                }
            }
        }
    }
}
