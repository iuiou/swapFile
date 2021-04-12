import com.oocourse.TimableOutput;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Mainclass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Queue queue = new Queue();
        Object lock = new Object();
        ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
        Elevator elevator1 = new Elevator(queue, lock, 1, rwl);
        elevator1.start();
        Elevator elevator2 = new Elevator(queue,lock,2, rwl);
        elevator2.start();
        Elevator elevator3 = new Elevator(queue,lock,3, rwl);
        elevator3.start();
        Input input = new Input(queue,lock, rwl);
        input.start();
    }
}
