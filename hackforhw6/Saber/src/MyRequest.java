public class MyRequest {
    //每个请求类
    private int id;
    private int from;
    private int to;
    private int towards;
    private int free;
    
    public MyRequest(int id, int from, int to) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.towards = from > to ? -1 : 1;
        this.free = 0;
    }
    
    public int getId() {
        return id;
    }
    
    public int getFrom() {
        return from;
    }
    
    public int getTo() {
        return to;
    }
    
    public int getTowards() {
        return towards;
    }
    
    public int getFree() {
        return free;
    }
    
    public void setFree(int free) {
        this.free = free;
    }
}
