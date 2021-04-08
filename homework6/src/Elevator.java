import com.oocourse.elevator2.PersonRequest;
import com.sun.org.apache.xpath.internal.objects.XString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Elevator {
    private ArrayList<PersonRequest> personSet;
    private int floor;
    private int lastop;
    private String id;

    public Elevator(int floor, String id) {
        this.personSet = new ArrayList<>();
        this.floor = floor;
        this.lastop = 0;
        this.id = id;
    }

    public synchronized ArrayList<PersonRequest> getPersonSet() {
        return personSet;
    }

    public synchronized int getFloor() {
        return floor;
    }

    public synchronized int getSize() {
        return personSet.size();
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized int getLastop() {
        return lastop;
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

    public synchronized int gethighestPerson() {
        int floor = 0;
        for (PersonRequest item : personSet) {
            floor = Math.max(floor, item.getToFloor());
        }
        return floor;
    }

    public synchronized int getlowestPerson() {
        int floor = 998244353;
        for (PersonRequest item : personSet) {
            floor = Math.min(floor, item.getToFloor());
        }
        return floor;
    }

    public synchronized int caltimeforMorning() {
        int time = 0;
        if (getLastop() == 1) {
            if (isEmpty()) {
                time += getFloor();
            } else {
                time += 2 * gethighestPerson() - floor - 1;
                HashSet<Integer> nowset = new HashSet<>();
                for (PersonRequest item : personSet) {
                    if (item.getToFloor() > floor) {
                        nowset.add(item.getToFloor()); //算上开门时间
                    }
                }
                time += nowset.size();
            }
        } else if (getLastop() == -1) {
            time += floor - 1;
        } else {
            time = 0;
        }
        return time;
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
