import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        TimableOutput.initStartTimestamp();
        Elevator newElevator = new Elevator(1,0);
        WaitQueue queue = new WaitQueue();
        Scan scan = new Scan(queue,newElevator);
        Modifyer modifyer = new Modifyer(newElevator,queue);
        modifyer.start();
        scan.start();
    }
}
