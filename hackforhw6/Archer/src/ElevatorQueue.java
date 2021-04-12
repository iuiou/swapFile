
import java.util.ArrayList;

public class ElevatorQueue {
    private ArrayList<Passenger> upPassengers = new ArrayList<>(50);
    private ArrayList<Passenger> downPassengers = new ArrayList<>(50);
    private int[] upFloorPersonNumber = {0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] downFloorPersonNumber = {0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int elevatorId;
    private boolean choseSign = false;
    
    public ElevatorQueue(int elevatorId) {
        this.elevatorId = elevatorId;
    }
    
    public void addPassenger(Passenger passenger) {
        if (passenger.getFromFloor() > passenger.getToFloor()) {
            downFloorPersonNumber[passenger.getFromFloor() - 1] += 1;
            downPassengers.add(passenger);
        } else {
            upFloorPersonNumber[passenger.getFromFloor() - 1] += 1;
            upPassengers.add(passenger);
        }
    }
    
    public boolean isEmpty() {
        return upPassengers.isEmpty() && downPassengers.isEmpty();
    }
    
    public void delPersonRequest(Passenger passenger) {
        if (upPassengers.contains(passenger)) {
            upPassengers.remove(passenger);
            upFloorPersonNumber[passenger.getFromFloor() - 1] -= 1;
        } else {
            downPassengers.remove(passenger);
            downFloorPersonNumber[passenger.getFromFloor() - 1] -= 1;
        }
    }
    
    public ArrayList<Passenger> getTheFloorRequest(int fromFloor, int direction) {
        ArrayList<Passenger> requestList = new ArrayList<>(6);
        if (direction > 0) {
            if (upFloorPersonNumber[fromFloor - 1] != 0) {
                for (Passenger passenger : upPassengers) {
                    if (passenger.getFromFloor() == fromFloor) {
                        requestList.add(passenger);
                    }
                }
            }
        } else {
            if (downFloorPersonNumber[fromFloor - 1] != 0) {
                for (Passenger passenger : downPassengers) {
                    if (passenger.getFromFloor() == fromFloor) {
                        requestList.add(passenger);
                    }
                }
            }
        }
        return requestList;
    }
    
    public boolean scan(int fromFloor, int direct) {
        boolean sign = false;
        if (direct > 0) {
            for (Passenger passenger : this.upPassengers) {
                if (passenger.getFromFloor() >= fromFloor) {
                    sign = true;
                }
            }
        } else {
            for (Passenger passenger : this.downPassengers) {
                if (passenger.getFromFloor() <= fromFloor) {
                    sign = true;
                }
            }
        }
        return sign;
    }
    
    public Passenger getReverseRequest(int fromFloor, int direction) {
        int i;
        Passenger bestPassenger = null;
        if (direction > 0) {
            for (i = 20; i >= fromFloor; i--) {
                if (this.downFloorPersonNumber[i - 1] != 0) {
                    for (Passenger passenger : downPassengers) {
                        if (passenger.getFromFloor() == i) {
                            bestPassenger = passenger;
                            break;
                        }
                    }
                }
            }
        } else {
            for (i = 1; i <= fromFloor; i++) {
                if (this.upFloorPersonNumber[i - 1] != 0) {
                    for (Passenger passenger : upPassengers) {
                        if (passenger.getFromFloor() == i) {
                            bestPassenger = passenger;
                            break;
                        }
                    }
                }
            }
        }
        return bestPassenger;
    }
    
    public int getElevatorId() {
        return elevatorId;
    }
    
    public void setElevatorId(int id) {
        this.elevatorId = id;
    }
    
    public void setChoseSign() {
        this.choseSign = true;
    }
    
    public boolean isChoseSign() {
        return choseSign;
    }
    
    public int getLength() {
        return upPassengers.size() + downPassengers.size();
    }
}
