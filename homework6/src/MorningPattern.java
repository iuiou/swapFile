import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Comparator;

public class MorningPattern extends Schemer {
    private WaitQueue queue;
    private Elevator elevator;

    public MorningPattern(WaitQueue queue, Elevator elevator) {
        this.queue = queue;
        this.elevator = elevator;
    }

    @Override
    public int makeDirection() { // 1 向上，-1向下，0不动
        if (elevator.isFull() || (queue.isEndin() && elevator.getFloor() == 1)) {
            return 1;
        } else {
            if (elevator.getLastop() == 0) {
                return 0;
            } else if (elevator.getLastop() == 1) {
                if (elevator.isEmpty()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (elevator.getFloor() != 1) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    @Override
    public boolean isOpen(int direction) {
        if (elevator.cangetoutofelevator()) {
            return true;
        } else {
            if (elevator.isFull()) {
                return false;
            }
            if (elevator.getFloor() == 1) {
                if ((queue.isEndin() && queue.getSize() > 0) || queue.getSize() >= 6) {
                    return true;
                } else {
                    return false;
                }
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
        cangetin.sort(Comparator.comparing(PersonRequest::getToFloor));
        int size = cangetin.size();
        for (int i = size - 1; i >= 0; i--) {
            getinPerson.add(cangetin.get(i));
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
