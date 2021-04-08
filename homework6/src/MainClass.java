import com.oocourse.TimableOutput;
import javafx.util.Pair;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        TimableOutput.initStartTimestamp();
        ArrayList<Pair<Elevator, WaitQueue>> elevatorSet = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Elevator elevator = new Elevator(1, Integer.toString(i));
            WaitQueue waitQueue = new WaitQueue(Integer.toString(i));
            elevatorSet.add(new Pair<>(elevator, waitQueue));
        }
        RequestQueue requestQueue = new RequestQueue();
        Scan scan = new Scan(requestQueue, elevatorSet);
        scan.start();
    }
}
