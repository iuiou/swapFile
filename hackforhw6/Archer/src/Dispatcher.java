import java.util.ArrayList;

public class Dispatcher extends Thread {
    private ArrayList<ElevatorQueue> elevatorQueues;
    private WaitQueue waitQueue;
    
    public Dispatcher(ArrayList<ElevatorQueue> elevatorQueues, WaitQueue waitQueue) {
        this.elevatorQueues = elevatorQueues;
        this.waitQueue = waitQueue;
    }
    
    @Override
    public void run() {
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                    for (ElevatorQueue elevatorQueue : elevatorQueues) {
                        synchronized (elevatorQueue) {
                            elevatorQueue.notifyAll();
                        }
                    }
                    return;
                }
                if (waitQueue.isEmpty()) {
                    try {
                        waitQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    while (!waitQueue.isEmpty()) {
                        ElevatorQueue bestQueue = elevatorQueues.get(0);
                        int leastLength = bestQueue.getLength();
                        synchronized (elevatorQueues) {
                            for (ElevatorQueue elevatorQueue : elevatorQueues) {
                                synchronized (elevatorQueue) {
                                    if ((elevatorQueue.getLength() < leastLength)
                                            && (elevatorQueue.isChoseSign())) {
                                        bestQueue = elevatorQueue;
                                        leastLength = elevatorQueue.getLength();
                                    }
                                }
                            }
                        }
                        synchronized (bestQueue) {
                            bestQueue.addPassenger(waitQueue.getPassenger());
                            bestQueue.notifyAll();
                        }
                        waitQueue.delPassenger(waitQueue.getPassenger());
                    }
                }
            }
        }
    }
}
