import java.util.Comparator;
import com.oocourse.elevator2.PersonRequest;

public class MyRevComparator implements Comparator<PersonRequest> {
    public int compare(PersonRequest o1, PersonRequest o2) {
        if (o1.getFromFloor() < o2.getFromFloor()) {
            return 1;
        } else if (o1.getFromFloor() == o2.getFromFloor()) {
            if (o1.getToFloor() <= o2.getToFloor()) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
