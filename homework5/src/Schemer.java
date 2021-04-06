import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public abstract class Schemer {

    public abstract int makeDirection();

    public abstract boolean isOpen();

    public abstract ArrayList<PersonRequest> inPerson(int direction);

    public abstract ArrayList<PersonRequest> outPerson();
}
