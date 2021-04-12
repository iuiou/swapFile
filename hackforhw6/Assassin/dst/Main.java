import elevator.Listener;

public abstract class Main {
    public static void main(String[] argv) {
        Listener s = new Listener(System.in);
        s.add(1, 5);
        s.add(2, 5);
        s.add(3, 10);
        s.serve();
    }
}
