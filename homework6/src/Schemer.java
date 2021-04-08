import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public abstract class Schemer {

    public abstract int makeDirection();

    public abstract boolean isOpen(int direction);

    public abstract ArrayList<PersonRequest> inPerson(int direction);

    public abstract ArrayList<PersonRequest> outPerson();
}
