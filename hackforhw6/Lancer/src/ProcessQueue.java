import java.util.ArrayList;

public class ProcessQueue {
    private ArrayList<Person> people;
    private final int maxPersonNum = 6;

    public ProcessQueue() {
        this.people = new ArrayList<>();
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void addPerson(Person person) {
        this.people.add(person);
    }

    public void deletePersonById(int i) {
        this.people.remove(i);
    }

    public Boolean isEmpty() {
        return this.people.isEmpty();
    }

    public Person getPersonById(int i) {
        return this.people.get(i);
    }

    public Boolean full() {
        return !(this.people.size() < this.maxPersonNum);
    }

    public int size() {
        return this.people.size();
    }
}
