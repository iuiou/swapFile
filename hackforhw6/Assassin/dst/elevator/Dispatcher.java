package elevator;

import elevator.console.Client;
import elevator.console.RequestVariant;
import elevator.console.RideRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Dispatcher implements Runnable {
    private final List<Handler> childs = new ArrayList<>();
    private final BlockingQueue<RequestVariant> queue =
            new ArrayBlockingQueue<>(Client.MAX_REQUEST_NUM);
    private final boolean isMorning;

    Dispatcher(String mode) {
        isMorning = mode.charAt(0) == 'M';
    }

    void interrupt() {
        try {
            queue.put(RequestVariant.nil());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void assign(RequestVariant req) {
        try {
            queue.put(req);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    Handler addHandler(int id, int center, Integer target) {
        Handler ch = new Handler(id, center, target, isMorning);
        childs.add(ch);
        return ch;
    }

    private void dispatch(RideRequest req) {
        Handler ans = null;
        long res = Long.MAX_VALUE;
        for (Handler ch: childs) {
            long r = ch.evaluate(req);
            if (r < res) {
                res = r;
                ans = ch;
            }
        }
        ans.assign(req);
    }

    @Override
    public void run() {
        for (Handler ch: childs) {
            ch.start();
        }
        while (true) {
            RequestVariant req;
            try {
                req = queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if (req.isNil()) {
                break;
            } else if (req.isRider()) {
                dispatch(req.asRider());
            } else {
                addHandler(req.asElevator().getId(), 0, null).start();
            }
        }
        for (Handler ch: childs) {
            ch.interrupt();
        }
        for (Handler ch: childs) {
            ch.join();
        }
    }
}
