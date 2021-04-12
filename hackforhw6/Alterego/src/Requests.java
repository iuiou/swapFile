import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.Request;

import java.io.IOException;
import java.util.Vector;

public class Requests implements Runnable {
    private MyList myList;
    private ElevatorInput elevatorInput;
    private Vector<Dispatcher> dispatchers;
    private String arrivePattern;
    private Vector<MyList> myLists;

    public Requests(MyList myList, ElevatorInput elevatorInput, Vector<Dispatcher> dispatchers,
                    String arrivePattern, Vector<MyList> myLists) {
        this.myList = myList;
        this.elevatorInput = elevatorInput;
        this.dispatchers = dispatchers;
        this.arrivePattern = arrivePattern;
        this.myLists = myLists;
    }

    @Override
    public void run() {
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) { //这表示输入完毕，那如果后面又有输入怎么办
                myList.setInputEnd(true);
                synchronized (myList) {
                    myList.notifyAll();
                }
                break; //这个是全部读完再进行调度，还是一边读一边调度
            } else { //如果另一个进程是只读，那么可以同时执行吗
                if (request instanceof PersonRequest) {
                    myList.add((PersonRequest) request);
                    synchronized (myList) {
                        myList.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) { //待修改
                    synchronized (dispatchers) {
                        if (dispatchers.size() < 5) {
                            MyList myListi = new MyList();
                            myLists.add(myListi);
                            Dispatcher dispatcher = new Dispatcher(myListi, arrivePattern,
                                    ((ElevatorRequest) request).getElevatorId()); //电梯id
                            dispatchers.add(dispatcher);
                            Thread elevatorThread = new Thread(dispatcher, "newdispatcher");
                            elevatorThread.start();
                        }
                    }
                }
            }
        }
        try {
            elevatorInput.close();
            //System.err.println("input over");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
