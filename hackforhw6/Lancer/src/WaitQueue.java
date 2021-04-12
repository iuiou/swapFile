import java.util.ArrayList;

public class WaitQueue {
    private ArrayList<Person> people;
    private Boolean isEnd;

    public WaitQueue() {
        this.people = new ArrayList<>();
        this.isEnd = false;
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public Boolean getEnd() {
        return isEnd;
    }

    public void close() {
        isEnd = true;
    }

    public boolean isEmpty() {
        return people.isEmpty();
    }

    public int size() {
        return this.people.size();
    }

    public Person getPerson(int i) {
        return this.people.get(i);
    }

    public void deletePersonById(int i) {
        people.remove(i);
    }

    @Override
    public String toString() {
        return "WaitQueue{" +
            "people=" + people +
            ", isEnd=" + isEnd +
            '}';
    }
}
