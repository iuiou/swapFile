public class Person {
    private int personId;
    private int fromFloor;
    private int toFloor;
    private Boolean direction;

    public Person(int personId, int fromFloor, int toFloor) {
        this.personId = personId;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        if (toFloor > fromFloor) {
            this.direction = true;
        } else {
            this.direction = false;
        }
    }

    public int getPersonId() {
        return personId;
    }

    public Boolean getDirection() {
        return direction;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }
}
