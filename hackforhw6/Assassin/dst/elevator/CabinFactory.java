package elevator;

public abstract class CabinFactory {
    static Cabin create(int id) {
        return new Cabin(id, Util.getTimer(), 400, 400, 6, 1);
    }
}
