public class Person {
    private int id;
    private int from;
    private int to;

    public Person(int id,int from,int to) {
        this.id = id;
        this.from = from;
        this.to = to;
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
}
