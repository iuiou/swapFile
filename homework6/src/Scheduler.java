import javafx.util.Pair;

public abstract class Scheduler extends Thread {

    public abstract int caltime(Pair<Elevator, WaitQueue> pair);
}
