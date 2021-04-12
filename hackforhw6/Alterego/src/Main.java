import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.TimableOutput;

import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        //System.out.println("a");
        try {
            TimableOutput.initStartTimestamp();
            ElevatorInput elevatorInput = new ElevatorInput(System.in);
            String arrivePattern = elevatorInput.getArrivingPattern();

            MyList myList1 = new MyList();
            MyList myList2 = new MyList();
            MyList myList3 = new MyList();
            Dispatcher dispatcher1 = new Dispatcher(myList1, arrivePattern, "1");
            Dispatcher dispatcher2 = new Dispatcher(myList2, arrivePattern, "2");
            Dispatcher dispatcher3 = new Dispatcher(myList3, arrivePattern, "3");

            Thread consumer1 = new Thread(dispatcher1, "consumer1");
            Thread consumer2 = new Thread(dispatcher2, "consumer2");
            Thread consumer3 = new Thread(dispatcher3, "consumer3");
            consumer1.start();
            consumer2.start();
            consumer3.start();
            Vector<Dispatcher> dispatchers = new Vector<>();
            dispatchers.add(dispatcher1);
            dispatchers.add(dispatcher2);
            dispatchers.add(dispatcher3);
            MyList myList = new MyList();
            Vector<MyList> myLists = new Vector<>();
            myLists.add(myList1);
            myLists.add(myList2);
            myLists.add(myList3);
            Dis dis = new Dis(dispatchers, myList, myLists);
            Thread disall = new Thread((Runnable) dis, "dis");

            Thread producer = new Thread(new Requests(myList, elevatorInput,
                    dispatchers, arrivePattern, myLists), "producer");
            producer.start();
            disall.start();
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
