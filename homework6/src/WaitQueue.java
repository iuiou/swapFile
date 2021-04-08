import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.oocourse.elevator2.PersonRequest;

public class WaitQueue {
    private ArrayList<PersonRequest> personList;
    private String id;
    public boolean endin;

    public WaitQueue(String id) {
        this.personList = new ArrayList<>();
        this.id = id;
        this.endin = false;
    }

    public synchronized ArrayList<PersonRequest> getPersonList() {
        return personList;
    }

    public String getId() {
        return id;
    }

    public synchronized int getSize() {
        return personList.size();
    }

    public synchronized void getpersonRequest(ArrayList<PersonRequest> newPerson) {
        personList.addAll(newPerson);
        notify();
    }

    public synchronized boolean isEndin() {
        return endin;
    }

    public synchronized void endRead() {
        endin = true;
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

    public synchronized ArrayList<PersonRequest> offerPersonDirection(int floor, int direction) {
        ArrayList<PersonRequest> personSet = new ArrayList<>();
        for (PersonRequest item : personList) {
            if (item.getFromFloor() == floor && (item.getToFloor() - item.getFromFloor()) * direction > 0) {
                personSet.add(item);
            }
        }
        return personList;
    }

    public synchronized boolean haspersonWaiting(int floor) {
        for (PersonRequest item : personList) {
            if (item.getFromFloor() == floor) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean haspersonWaitingDirection(int floor, int direction) {
        for (PersonRequest item : personList) {
            if (item.getFromFloor() == floor && (item.getToFloor() - item.getFromFloor()) * direction > 0) {
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

    public synchronized boolean haspersonLower(int floor) {
        for (PersonRequest item : personList) {
            if (item.getFromFloor() < floor) {
                return true;
            }
        }
        return false;
    }

    public synchronized int caltimeforMorning() {
        personList.sort(Comparator.comparing(PersonRequest::getToFloor));
        int size = personList.size();
        int i;
        int time = 0;
        for (i = size - 1; i >= 0; i -= 6) {
            int j = i;
            int nowtime = 0;
            HashSet<Integer> nowset = new HashSet<>();
            while (j >= 0 && j >= i - 5) {
                nowtime = Math.max(nowtime, personList.get(j).getToFloor());
                nowset.add(personList.get(j).getToFloor());
                j--;
            }
            time += (nowtime - 1) * 2 + nowset.size();
        }
        return time;
    }

}
