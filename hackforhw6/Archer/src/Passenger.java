import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

public class Passenger {
    private PersonRequest personRequest;
    private boolean sign = false;
    
    public Passenger(Request personRequest) {
        this.personRequest = (PersonRequest) personRequest;
    }
    
    public void setSign() {
        this.sign = true;
    }
    
    public int getFromFloor() {
        return personRequest.getFromFloor();
    }
    
    public int getToFloor() {
        return personRequest.getToFloor();
    }
    
    public boolean isChosen() {
        return this.sign;
    }
    
    public int getPersonId() {
        return personRequest.getPersonId();
    }
}
