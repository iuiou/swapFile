import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Comparator;

public class RandomPattern extends Schemer {
    private WaitQueue queue;
    private Elevator elevator;

    public RandomPattern(WaitQueue queue, Elevator elevator) {
        this.queue = queue;
        this.elevator = elevator;
    }

    @Override
    public int makeDirection() {
        int floor = elevator.getFloor();
        if (elevator.getLastop() == 0) {
            return 1;
        } else if (elevator.getLastop() == 1) {
            if (elevator.gethighestPerson() > floor) {
                return 1;
            } else {
                if (!elevator.isFull() && queue.haspersonHiger(floor)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } else {
            if (elevator.getlowestPerson() < floor) {
                return -1;
            } else {
                if (!elevator.isFull() && queue.haspersonLower(floor)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    @Override
    public boolean isOpen(int direction) {
        int floor = elevator.getFloor();
        if (elevator.cangetoutofelevator()) {
            return true;
        } else {
            if (!elevator.isFull()) {
                if (queue.haspersonWaitingDirection(floor, direction)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<PersonRequest> inPerson(int direction) {
        int floor = elevator.getFloor();
        ArrayList<PersonRequest> cangetin = queue.offerPersonDirection(floor, direction);
        ArrayList<PersonRequest> getin = new ArrayList<>();
        if (cangetin.size() == 0) {
            return getin;
        }
        cangetin.sort(Comparator.comparing(PersonRequest::getToFloor));
        if (direction == 1 || direction == 0) {
            int size = cangetin.size();
            for (int i = size - 1; i >= 0; i--) {
                getin.add(cangetin.get(i));
                if (getin.size() + elevator.getSize() == 6) {
                    break;
                }
            }
        } else {
            int size = cangetin.size();
            for (int i = 0; i < size; i++) {
                getin.add(cangetin.get(i));
                if (getin.size() + elevator.getSize() == 6) {
                    break;
                }
            }
        }
        return getin;
    }

    @Override
    public ArrayList<PersonRequest> outPerson() {
        return elevator.personatFloor();
    }
}
