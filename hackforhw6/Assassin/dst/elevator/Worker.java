package elevator;

import elevator.console.Client;
import elevator.console.RideRequest;
import elevator.scheduler.IScheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Worker implements Runnable {
    private volatile boolean interrupted = false;

    private final BlockingQueue<RideRequest> queue =
            new ArrayBlockingQueue<>(Client.MAX_REQUEST_NUM);
    private final IScheduler scheduler;

    private volatile IScheduler snapshot;
    private final Object ssLock = new Object();

    Worker(IScheduler scheduler) {
        this.scheduler = scheduler;
    }

    IScheduler getSnapshot() {
        synchronized (ssLock) {
            return snapshot;
        }
    }

    private void updateSnapshot() {
        synchronized (ssLock) {
            IScheduler m = scheduler.snapshot();
            snapshot = m;
        }
    }

    synchronized void interrupt() {
        interrupted = true;
        notify();
    }

    private synchronized boolean isInterrupted() {
        return interrupted;
    }

    synchronized void assign(RideRequest req) {
        try {
            queue.put(req);
            notify();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "[Worker " + scheduler.getId() + ']';
    }

    @Override
    public void run() {
        scheduler.init();
        updateSnapshot();

        while (true) {
            try {
                boolean f = isInterrupted();
                boolean assigned = false;
                while (!queue.isEmpty()) {
                    scheduler.assign(queue.take());
                    assigned = true;
                }
                if (assigned) {
                    updateSnapshot();
                }
                IScheduler.Status r = scheduler.work(f);
                updateSnapshot();
                switch (r) {
                    case IDLE:
                        synchronized (this) {
                            if (!interrupted) {
                                wait();
                            }
                        }
                        break;
                    case DEAD:
                        return;
                    case WAIT:
                        synchronized (this) {
                            if (!interrupted) {
                                wait(Util.toMillis(2000));
                            }
                        }
                        break;
                    default:
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
