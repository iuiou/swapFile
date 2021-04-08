import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Comparator;

public class RequestQueue {
    private ArrayList<PersonRequest> requests;
    private boolean isEndIn;

    public RequestQueue() {
        this.requests = new ArrayList<>();
        this.isEndIn = false;
    }

    public synchronized void getpersonRequest(PersonRequest newPerson) {
        requests.add(newPerson);
        notify();
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized int getSize() {
        return requests.size();
    }

    public synchronized void clear() {
        requests.clear();
    }

    public synchronized ArrayList<ArrayList<PersonRequest>> divide() {
        ArrayList<ArrayList<PersonRequest>> offerRequest = new ArrayList<>();
        requests.sort(Comparator.comparing(PersonRequest::getToFloor));
        int i;
        int size = requests.size();
        for (i = size - 1; i >= 0; i -= 6) {
            int j = i;
            ArrayList<PersonRequest> personRequests = new ArrayList<>();
            while (j >= 0 && j >= i - 5) {
                personRequests.add(requests.get(j));
                j--;
            }
            offerRequest.add(personRequests);
        }
        return offerRequest;
    }

    public synchronized boolean isEndIn() {
        return isEndIn;
    }

    public synchronized void setEndIn() {
        isEndIn = true;
        notify();
    }
}
