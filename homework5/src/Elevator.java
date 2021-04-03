import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.Iterator;

public class Elevator {
    private ArrayList<PersonRequest> personSet;
    private int floor;
    private int type;
    private int lastop;

    public Elevator(int floor, int type) {
        this.personSet = new ArrayList<>();
        this.floor = floor;
        this.type = type;
        this.lastop = 0;
    }

    public synchronized ArrayList<PersonRequest> getPersonSet() {
        return personSet;
    }

    public synchronized int getFloor() {
        return floor;
    }

    public synchronized int getType() {
        return type;
    }

    public synchronized int getSize() {
        return personSet.size();
    }

    public synchronized int getLastop() {
        return lastop;
    }

    public synchronized void setType(int type) {
        this.type = type;
        notify();
    }

    public synchronized void setLastop(int direction) {
        this.lastop = direction;
    }

    public synchronized ArrayList<PersonRequest> personatFloor() {
        ArrayList<PersonRequest> nowoutofEvevator = new ArrayList<>();
        for (PersonRequest item : personSet) {
            if (item.getToFloor() == floor) {
                nowoutofEvevator.add(item);
            }
        }
        return nowoutofEvevator;
    }

    public synchronized void deletePerson(ArrayList<PersonRequest> outPerson) {
        for (PersonRequest item : outPerson) {
            personSet.remove(item);
        }
    }

    public synchronized boolean cangetoutofelevator() { //can person go out ?
        for (Iterator<PersonRequest> item = personSet.iterator(); item.hasNext(); ) {
            PersonRequest nowPerson = item.next();
            if (nowPerson.getToFloor() == floor) {
                return true;
            }
        }
        return false;
    }

    public synchronized void getinelevator(ArrayList<PersonRequest> inpersonSet) {
        for (PersonRequest item : inpersonSet) {
            personSet.add(item);
        }
        return;
    }

    public synchronized void goUp() {
        floor = floor + 1;
    }

    public synchronized void goDown() {
        floor = floor - 1;
    }

    public synchronized boolean isEmpty() {
        return personSet.isEmpty();
    }

    public synchronized boolean isFull() {
        return personSet.size() == 6;
    }
}
