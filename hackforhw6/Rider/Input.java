import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Input extends Thread {
    private Queue queue;
    private Request request = null;
    private Object lock;
    private boolean flag = false;
    private ReentrantReadWriteLock rwl;
    private int elevatorNum = 3;

    public void setLock(Object lock) {
        this.lock = lock;
    }

    public Input(Queue queue, Object lock, ReentrantReadWriteLock rwl) {
        this.queue = queue;
        this.lock = lock;
        this.rwl = rwl;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        String temp = elevatorInput.getArrivingPattern();
        while ((request = elevatorInput.nextRequest()) != null) {
            if (request instanceof PersonRequest) {
                rwl.writeLock().lock();
                try {
                    if (queue.getQueue().isEmpty()) {
                        flag = true;
                    }
                    //System.out.println(flag);
                    queue.getQueue().add((PersonRequest) request);
                    if (flag == true) {
                        flag = false;
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                } finally {
                    rwl.writeLock().unlock();
                }
            } else if (request instanceof ElevatorRequest) {
                if (elevatorNum == 3) {
                    int id = Integer.parseInt(((ElevatorRequest) request).getElevatorId());
                    Elevator elevator4 = new Elevator(queue, lock, id, rwl);
                    elevatorNum++;
                    elevator4.start();
                } else if (elevatorNum == 4) {
                    int id = Integer.parseInt(((ElevatorRequest) request).getElevatorId());
                    Elevator elevator5 = new Elevator(queue, lock, id, rwl);
                    elevatorNum++;
                    elevator5.start();
                }
            }
        }

        queue.setStop(true);
        synchronized (lock) {
            lock.notifyAll();
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
