import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class WaitQueue {
    private final ArrayList<PersonRequest> personRequests;
    private boolean end;
    private String pattern;

    public WaitQueue() {
        personRequests = new ArrayList<>();
        end = false;
    }

    public synchronized void isEnd() {
        this.end = true;
    }

    public synchronized void addRequest(PersonRequest request) {
        personRequests.add(request);
    }

    public synchronized void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public synchronized String getPattern() {
        return pattern;
    }

    public synchronized ArrayList<PersonRequest> getPersonRequests() {
        return personRequests;
    }

    public synchronized boolean isEmpty() {
        return personRequests.isEmpty();
    }

    public synchronized boolean getEnd() {
        return end;
    }
}

