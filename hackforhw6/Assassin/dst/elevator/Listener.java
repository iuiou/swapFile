package elevator;

import elevator.console.Client;
import elevator.console.RequestVariant;

import java.io.InputStream;

public class Listener {
    private final Client client;
    private final Dispatcher worker;

    public Listener(InputStream in) {
        client = new Client(in);
        worker = new Dispatcher(client.getMode());
    }

    public void add(int id, int center) {
        worker.addHandler(id, center, center);
    }

    public void serve() {
        Thread th = new Thread(worker);
        th.start();
        for (RequestVariant req; (req = client.next()) != null; ) {
            worker.assign(req);
        }
        worker.interrupt();
        client.drop();
        try {
            th.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
