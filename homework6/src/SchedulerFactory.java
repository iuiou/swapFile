import javafx.util.Pair;

import java.util.ArrayList;

public class SchedulerFactory {
    public Scheduler produceScheduler(int type, RequestQueue queue, ArrayList<Pair<Elevator, WaitQueue>> elevators) {
        if (type == 1) {
            return new NightScheduler(queue, elevators);
        } else if (type == 2) {
            return new MorningScheduler(queue, elevators);
        } else {
            return new RandomScheduler(queue, elevators);
        }
    }
}
