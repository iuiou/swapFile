import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.Comparator;

public class RandomPattern extends Schemer {
    private WaitQueue queue;
    private Elevator elevator;

    public RandomPattern(WaitQueue queue, Elevator elevator) {
        this.queue = queue;
        this.elevator = elevator;
    }

    public int getelevatorDis(PersonRequest personRequest) {
        int floor = elevator.getFloor();
        return Math.abs(floor - personRequest.getToFloor());
    }

    public int getqueueDis(PersonRequest personRequest) {
        int floor = elevator.getFloor();
        return Math.abs(floor - personRequest.getFromFloor());
    }

    public ArrayList<PersonRequest> dealwithzeroone(ArrayList<PersonRequest> personRequests) {
        int floor = elevator.getFloor();
        ArrayList<PersonRequest> ansPerson = new ArrayList<>();
        ArrayList<PersonRequest> upPerson = new ArrayList();
        ArrayList<PersonRequest> downPerson = new ArrayList();
        for (PersonRequest item : personRequests) {
            if (item.getToFloor() > floor) {
                upPerson.add(item);
            } else {
                downPerson.add(item);
            }
        }
        upPerson.sort(Comparator.comparing(PersonRequest::getToFloor));
        downPerson.sort(Comparator.comparing(PersonRequest::getToFloor));
        if (!upPerson.isEmpty()) {
            for (int i = 0; i < upPerson.size(); i++) {
                ansPerson.add(upPerson.get(i));
                if (ansPerson.size() + elevator.getSize() == 6) {
                    return ansPerson;
                }
            }
        }
        if (!downPerson.isEmpty()) {
            for (int i = downPerson.size() - 1; i >= 0; i--) {
                ansPerson.add(downPerson.get(i));
                if (ansPerson.size() + elevator.getSize() == 6) {
                    return ansPerson;
                }
            }
        }
        return ansPerson;
    }

    public ArrayList<PersonRequest> dealwithtwo(ArrayList<PersonRequest> personRequests) {
        int floor = elevator.getFloor();
        ArrayList<PersonRequest> ansPerson = new ArrayList<>();
        ArrayList<PersonRequest> upPerson = new ArrayList();
        ArrayList<PersonRequest> downPerson = new ArrayList();
        for (PersonRequest item : personRequests) {
            if (item.getToFloor() > floor) {
                upPerson.add(item);
            } else {
                downPerson.add(item);
            }
        }
        upPerson.sort(Comparator.comparing(PersonRequest::getToFloor));
        downPerson.sort(Comparator.comparing(PersonRequest::getToFloor));
        if (!downPerson.isEmpty()) {
            for (int i = downPerson.size() - 1; i >= 0; i--) {
                ansPerson.add(downPerson.get(i));
                if (ansPerson.size() + elevator.getSize() == 6) {
                    return ansPerson;
                }
            }
        }
        if (!upPerson.isEmpty()) {
            for (int i = 0; i < upPerson.size(); i++) {
                ansPerson.add(upPerson.get(i));
                if (ansPerson.size() + elevator.getSize() == 6) {
                    return ansPerson;
                }
            }
        }
        return ansPerson;
    }

    @Override
    public int makeDirection() {
        int floor = elevator.getFloor();
        int dis = 998244353;
        PersonRequest thecloestPerson = new PersonRequest(0, 0, 0);
        int type = 0;
        int cangoout = elevator.personatFloor().size();
        if (elevator.getSize() - cangoout > 0) { //从这层楼下去后还有空
            for (PersonRequest item : elevator.getPersonSet()) {
                if (item.getToFloor() == floor) {
                    continue;
                }
                if (getelevatorDis(item) < dis) {
                    dis = getelevatorDis(item);
                    thecloestPerson = item;
                    type = 1;
                }
            }
        }
        if (!elevator.isFull()) {
            synchronized (queue) {
                for (PersonRequest item : queue.getPersonList()) {
                    if (item.getFromFloor() == floor) {
                        continue;
                    }
                    if (getqueueDis(item) < dis) {
                        dis = getqueueDis(item);
                        thecloestPerson = item;
                        type = 2;
                    }
                }
            }
        }
        if (type == 0) {
            return 0;
        } else if (type == 1) {
            if (thecloestPerson.getToFloor() > floor) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (thecloestPerson.getFromFloor() > floor) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    @Override
    public boolean isOpen() {
        if (elevator.cangetoutofelevator() ||
                (!elevator.isFull() && queue.haspersonWaiting(elevator.getFloor()))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<PersonRequest> inPerson(int direction) {
        int floor = elevator.getFloor();
        ArrayList<PersonRequest> cangetin = queue.offerPerson(floor);
        if (direction == 0 || direction == 1) {
            return dealwithzeroone(cangetin);
        } else {
            return dealwithtwo(cangetin);
        }
    }

    @Override
    public ArrayList<PersonRequest> outPerson() {
        return elevator.personatFloor();
    }
}
