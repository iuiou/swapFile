import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Modifyer extends Thread {
    private Elevator elevator;
    private WaitQueue queue;
    private Schemer schemer;

    public Modifyer(Elevator elevator, WaitQueue queue) {
        this.elevator = elevator;
        this.queue = queue;
    }

    public void coutOpen(int floor) {
        TimableOutput.println(String.format("OPEN-%d", floor));
    }

    public void coutClose(int floor) {
        TimableOutput.println(String.format("CLOSE-%d", floor));
    }

    public void coutGetout(ArrayList<PersonRequest> personRequestSet, int floor) {
        for (PersonRequest item : personRequestSet) {
            TimableOutput.println(String.format("OUT-%d-%d", item.getPersonId(), floor));
        }
    }

    public void coutGetin(ArrayList<PersonRequest> personRequestSet, int floor) {
        for (PersonRequest item : personRequestSet) {
            TimableOutput.println(String.format("IN-%d-%d", item.getPersonId(), floor));
        }
    }

    public void coutArrive(int floor) {
        TimableOutput.println(String.format("ARRIVE-%d", floor));
    }

    public void sleepfor(int time) throws InterruptedException {
        sleep(time);
    }

    public void opentheDoor(int direction) {
        int floor = elevator.getFloor();
        coutOpen(floor);
        try {
            sleepfor(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<PersonRequest> outPerson = schemer.outPerson();
        coutGetout(outPerson, floor);
        elevator.deletePerson(outPerson);
        ArrayList<PersonRequest> inPerson = schemer.inPerson(direction);
        coutGetin(inPerson, floor);
        queue.deletePerson(inPerson);
        elevator.getinelevator(inPerson);
        coutClose(elevator.getFloor());
    }

    @Override
    public void run() {
        if (elevator.getType() == 0) {
            synchronized (elevator) {
                try {
                    elevator.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        SchemerFactory workFactory = new SchemerFactory();
        schemer = workFactory.produceSchemer(elevator.getType(), elevator, queue);
        while (true) {
            if (queue.isEmpty() && elevator.isEmpty()) {
                if (queue.isEndReadin()) {
                    return;
                } else {
                    synchronized (queue) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (queue.isEmpty() && elevator.isEmpty()) {
                continue;
            }
            int direction = schemer.makeDirection();
            if (schemer.isOpen()) {
                opentheDoor(direction);
            } else {
                try {
                    sleepfor(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (direction == 1) {
                    elevator.goUp();
                    coutArrive(elevator.getFloor());
                } else if (direction == -1) {
                    elevator.goDown();
                    coutArrive(elevator.getFloor());
                }
                elevator.setLastop(direction);
            }
        }
    }
}
