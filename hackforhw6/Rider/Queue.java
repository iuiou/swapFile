import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Queue {
    private ArrayList<PersonRequest> queue = new ArrayList<>();
    private boolean stop = false;

    public synchronized ArrayList<PersonRequest> getQueue() {
        return queue;
    }

    public synchronized void setStop(boolean stop) {
        this.stop = stop;
    }

    public synchronized boolean getStop() {
        return stop;
    }
}
