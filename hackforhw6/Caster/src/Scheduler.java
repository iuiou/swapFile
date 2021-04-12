import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Scheduler implements Runnable {
    private final WaitQueue waitQueue;
    private final ArrayList<ElevatorIn> elevatorIns;
    private final ArrayList<ElevatorWait> elevatorWaits;

    public Scheduler(WaitQueue waitQueue, ArrayList<ElevatorIn> elevatorIns,
                     ArrayList<ElevatorWait> elevatorWaits) {
        this.waitQueue = waitQueue;
        this.elevatorIns = elevatorIns;
        this.elevatorWaits = elevatorWaits;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.getEnd() && waitQueue.isEmpty()) {
                break;
            }

            if (waitQueue.isEmpty()) {
                synchronized (waitQueue) {
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (waitQueue.getPattern().equals("Morning")) {
                morningDeal();
            }
            else {
                randomDeal();
            }

        }


        for (ElevatorWait elevatorWait : elevatorWaits) {
            synchronized (elevatorWait) {
                elevatorWait.notifyAll();
            }
        }
    }

    public void morningDeal() {
        int id = 0;
        while (true) {
            int num = waitQueue.getPersonRequests().size();
            synchronized (waitQueue) {
                try {
                    waitQueue.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (waitQueue.getPersonRequests().size() == 6) {
                for (PersonRequest person : waitQueue.getPersonRequests()) {
                    elevatorWaits.get(id).addRequest(person);
                }
                waitQueue.getPersonRequests().clear();
                synchronized (elevatorWaits.get(id)) {
                    elevatorWaits.get(id).notifyAll();
                }
                id = (id + 1) % elevatorWaits.size();
            }
            else if (num == waitQueue.getPersonRequests().size()) {
                break;
            }
        }

        while (! waitQueue.isEmpty()) {
            for (PersonRequest person : waitQueue.getPersonRequests()) {
                elevatorWaits.get(id).addRequest(person);
            }
            waitQueue.getPersonRequests().clear();
            synchronized (elevatorWaits.get(id)) {
                elevatorWaits.get(id).notifyAll();
            }
        }
    }

    public void randomDeal() {
        while (! waitQueue.isEmpty()) {
            int id = - 1;
            int num = - 1;
            for (ElevatorWait elevatorWait : elevatorWaits) {
                if (id == - 1) {
                    id = elevatorWaits.indexOf(elevatorWait);
                    num = elevatorWait.getPersonRequests().size();
                }
                else if (elevatorWait.getPersonRequests().size() < num) {
                    id = elevatorWaits.indexOf(elevatorWait);
                    num = elevatorWait.getPersonRequests().size();
                }
            }
            elevatorWaits.get(id).addRequest(waitQueue.getPersonRequests().get(0));
            waitQueue.getPersonRequests().remove(0);
        }

        for (ElevatorWait elevatorWait : elevatorWaits) {
            if (! elevatorWait.getPersonRequests().isEmpty()) {
                synchronized (elevatorWait) {
                    elevatorWait.notifyAll();
                }
            }
        }
    }
}