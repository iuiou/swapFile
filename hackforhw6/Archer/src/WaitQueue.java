import java.util.ArrayList;

public class WaitQueue {
    private ArrayList<Passenger> passengerList = new ArrayList<>(50);
    private boolean end = false;
    
    public void close() {
        this.end = true;
    }
    
    public boolean isEnd() {
        return this.end;
    }
    
    public boolean isEmpty() {
        return passengerList.isEmpty();
    }
    
    public void  addPassenger(Passenger passenger) {
        passengerList.add(passenger);
    }
    
    public void delPassenger(Passenger passenger) {
        passengerList.remove(passenger);
    }
    
    public ArrayList<Passenger> getPassengers() {
        return passengerList;
    }
    
    public Passenger getPassenger() {
        return passengerList.get(0);
    }
}
