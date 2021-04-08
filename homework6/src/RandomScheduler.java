import com.oocourse.elevator2.PersonRequest;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

public class RandomScheduler extends Scheduler {
    private RequestQueue requestQueue;
    private ArrayList<Pair<Elevator, WaitQueue>> elevators;

    public RandomScheduler(RequestQueue requestQueue, ArrayList<Pair<Elevator, WaitQueue>> elevators) {
        this.requestQueue = requestQueue;
        this.elevators = elevators;
    }

    @Override
    public int caltime(Pair<Elevator, WaitQueue> pair) {
        return pair.getKey().getSize() + pair.getValue().getSize();
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
