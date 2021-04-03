public class SchemerFactory {
    public Schemer produceSchemer(int type, Elevator elevator, WaitQueue queue) {
        if (type == 1) {
            return new EveningPattern(queue, elevator);
        } else if (type == 2) {
            return new MorningPattern(queue, elevator);
        } else {
            return new RandomPattern(queue, elevator);
        }
    }
}
