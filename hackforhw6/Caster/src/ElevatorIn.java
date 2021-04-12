import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class ElevatorIn {
    private final ArrayList<PersonRequest> personRequests;

    public ElevatorIn() {
        personRequests = new ArrayList<>();
    }

    public synchronized void addRequest(PersonRequest request) {
        personRequests.add(request);
    }

    public synchronized ArrayList<PersonRequest> getPersonRequests() {
        return personRequests;
    }

    public synchronized boolean isEmpty() {
        return personRequests.isEmpty();
    }

    public synchronized boolean isFull() {
        return personRequests.size() == 6;
    }
}

