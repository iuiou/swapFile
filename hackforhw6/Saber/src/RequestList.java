import java.util.concurrent.CopyOnWriteArrayList;

public class RequestList {
    private String pattern = null;
    private CopyOnWriteArrayList<MyRequest> requestsList = new CopyOnWriteArrayList<>();//请求队列的list
    private CopyOnWriteArrayList<MyRequest>[] requestsMap =
            new CopyOnWriteArrayList[21];//请求队列的from楼层map
    private boolean done;
    private int size = 0;
    private int trueSize = 0;
    private Dispatch dispatch;
    
    public RequestList() {
        for (int i = 0; i < 21; i++) {
            requestsMap[i] = new CopyOnWriteArrayList<>();
        }
    }
    
    public CopyOnWriteArrayList<MyRequest> getRequestsList() {
        return requestsList;
    }
    
    public CopyOnWriteArrayList<MyRequest>[] getRequestsMap() {
        return requestsMap;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public synchronized boolean isDone() {
        return done;
    }
    
    public synchronized void setDone(boolean done) {
        this.done = done;
        notifyAll();
    }
    
    public synchronized int getSize() {
        return size;
    }
    
    public synchronized int getTrueSize() {
        return trueSize;
    }
    
    public synchronized void setTrueSize(int trueSize) {
        this.trueSize = trueSize;
    }
    
    public void setDispatch(Dispatch dispatch) {
        this.dispatch = dispatch;
    }
    
    public synchronized void addre(MyRequest request) {
        trueSize += 1;
        size += 1;
        requestsList.add(request);
        requestsMap[request.getFrom()].add(request);
        notifyAll();
    }
    
    public void rmRe(MyRequest request) {
        size -= 1;
        requestsList.remove(request);
        requestsMap[request.getFrom()].remove(request);
        notifyAll();
    }
}
