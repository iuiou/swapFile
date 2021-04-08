import com.oocourse.elevator2.PersonRequest;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class NightScheduler extends Scheduler {
    private RequestQueue requestQueue;
    private ArrayList<Pair<Elevator, WaitQueue>> elevators;

    public NightScheduler(RequestQueue requestQueue, ArrayList<Pair<Elevator, WaitQueue>> elevators) {
        this.requestQueue = requestQueue;
        this.elevators = elevators;
    }

    @Override
    public int caltime(Pair<Elevator, WaitQueue> pair) {
        ArrayList<PersonRequest> elevatorRequest = pair.getKey().getPersonSet();
        ArrayList<PersonRequest> queueRequest = pair.getValue().getPersonList();
        Elevator elevator = pair.getKey();
        WaitQueue queue = pair.getValue();
        int time = 0;
        int maxfloor = 0;
        synchronized (elevator) {
            synchronized (queue) {
                queueRequest.sort(Comparator.comparing(PersonRequest::getFromFloor));
                if (!queue.isEmpty()) {
                    int i = queue.getSize() - 1;
                    maxfloor = queueRequest.get(i).getFromFloor();
                    int num = 0;
                    HashSet<Integer> nowset = new HashSet<>();
                    while (i >= 0 && elevator.getSize() + num <= 6) {
                        i--;
                        num++;
                        nowset.add(queueRequest.get(i).getFromFloor());
                    }
                    time += nowset.size();
                    if (i >= 0) {
                        for (int j = i; j >= 0; j -= 6) {
                            int z = j;
                            int nowtime = 0;
                            HashSet<Integer> newset = new HashSet<>();
                            while (z >= 0 && z >= j - 5) {
                                nowtime = Math.max(nowtime, queueRequest.get(z).getToFloor());
                                newset.add(queueRequest.get(z).getFromFloor());
                                z--;
                            }
                            time += (nowtime - 1) * 2 + nowset.size();
                        }
                    }
                }
                if (elevator.getLastop() == 1) {
                    time += 2 * maxfloor - elevator.getFloor() - 1;
                } else if (elevator.getLastop() == -1) {
                    time += elevator.getFloor() - 1;
                }
            }
        }
        return time;
    }

    public void sleepfor(int time) throws InterruptedException {
        sleep(time);
    }

    @Override
    public void run() {
        while (true) {
            if (requestQueue.isEmpty()) {
                if (requestQueue.isEndIn()) {
                    for (Pair<Elevator, WaitQueue> item : elevators) {
                        item.getValue().endRead();
                    }
                    return;
                } else {
                    synchronized (requestQueue) {
                        try {
                            requestQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (requestQueue.isEmpty()) {
                continue;
            }
            try {
                sleepfor(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (requestQueue) {
                ArrayList<ArrayList<PersonRequest>> personScheduler = requestQueue.divide();
                elevators.sort(Comparator.comparing(this::caltime));
                int j = 0;
                int elevatorSize = elevators.size();
                int size = personScheduler.size();
                for (int i = 0; i < size; i++, j++) {
                    if (j >= elevatorSize) {
                        j = 0;
                    }
                    elevators.get(j).getValue().getpersonRequest(personScheduler.get(i));
                }
                requestQueue.clear();
            }
        }
    }
}
