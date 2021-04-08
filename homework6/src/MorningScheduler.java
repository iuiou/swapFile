import com.oocourse.elevator2.PersonRequest;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

public class MorningScheduler extends Scheduler {
    private RequestQueue requestQueue;
    private ArrayList<Pair<Elevator, WaitQueue>> elevators;

    public MorningScheduler(RequestQueue requestQueue, ArrayList<Pair<Elevator, WaitQueue>> elevators) {
        this.requestQueue = requestQueue;
        this.elevators = elevators;
    }

    @Override
    public int caltime(Pair<Elevator, WaitQueue> thisElevator) {
        Elevator nowElevator = thisElevator.getKey();
        WaitQueue nowWaitQueue = thisElevator.getValue();
        return nowElevator.caltimeforMorning() + nowWaitQueue.caltimeforMorning();
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
            ArrayList<ArrayList<PersonRequest>> PersonScheduler;
            synchronized (requestQueue) {
                PersonScheduler = requestQueue.divide();
                elevators.sort(Comparator.comparing(this::caltime));
                int j = 0;
                int elevatorSize = elevators.size();
                int size = PersonScheduler.size();
                for (int i = 0; i < size; i++, j++) {
                    if (j >= elevatorSize) {
                        j = 0;
                    }
                    elevators.get(j).getValue().getpersonRequest(PersonScheduler.get(i));
                }
                requestQueue.clear();
            }
        }
    }
}
