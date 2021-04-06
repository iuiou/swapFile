import java.util.ArrayList;

import com.oocourse.elevator1.PersonRequest;

public class WaitQueue {
    private ArrayList<PersonRequest> personList;
    private boolean endReadin;

    public WaitQueue() {
        this.personList = new ArrayList<>();
        this.endReadin = false;
    }

    public synchronized ArrayList<PersonRequest> getPersonList() {
        return personList;
    }

    public synchronized int getSize() {
        return personList.size();
    }

    public synchronized boolean isEndReadin() {
        return endReadin;
    }

    public synchronized void getpersonRequest(PersonRequest newPerson) {
        personList.add(newPerson);
        notify();
    }

    public synchronized boolean isEmpty() {
        return personList.isEmpty();
    }

    public synchronized void deletePerson(ArrayList<PersonRequest> personSet) {
        for (PersonRequest item : personSet) {
            personList.remove(item);
        }
    }

    public synchronized ArrayList<PersonRequest> offerPerson(int floor) {
        ArrayList<PersonRequest> personSet = new ArrayList<>();
        for (PersonRequest item : personList) {
            if (item.getFromFloor() == floor) {
                personSet.add(item);
            }
        }
        return personSet;
    }

    public synchronized boolean haspersonWaiting(int floor) {
        for (PersonRequest item : personList) {
            if (item.getFromFloor() == floor) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean haspersonHiger(int floor) {
        for (PersonRequest item : personList) {
            if (item.getFromFloor() > floor) {
                return true;
            }
        }
        return false;
    }

    public synchronized void endRead() {
        endReadin = true;
    }
}
