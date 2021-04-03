import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class EveningPattern extends Schemer {
    private WaitQueue queue;
    private Elevator elevator;

    public EveningPattern(WaitQueue queue, Elevator elevator) {
        this.queue = queue;
        this.elevator = elevator;
    }

    @Override
    public int makeDirection() { // 1 向上，-1向下，0不动
        if (elevator.isFull()) {
            return -1;
        } else {
            if (elevator.getLastop() == 1 || elevator.getLastop() == 0) {
                if (queue.haspersonHiger(elevator.getFloor())) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                if (elevator.getFloor() == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
    }

    @Override
    public boolean isOpen() {
        int floor = elevator.getFloor();
        if (elevator.cangetoutofelevator()) {
            return true;
        } else {
            if (makeDirection() == -1 && !elevator.isFull() && queue.haspersonWaiting(floor)) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public ArrayList<PersonRequest> inPerson(int direction) {
        int floor = elevator.getFloor();
        ArrayList<PersonRequest> getinPerson = new ArrayList<>();
        ArrayList<PersonRequest> cangetin = queue.offerPerson(floor);
        for (PersonRequest item : cangetin) {
            getinPerson.add(item);
            if (getinPerson.size() + elevator.getSize() == 6) {
                break;
            }
        }
        return getinPerson;
    }

    @Override
    public ArrayList<PersonRequest> outPerson() {
        return elevator.personatFloor();
    }

}
