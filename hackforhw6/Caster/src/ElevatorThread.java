import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class ElevatorThread implements Runnable {
    private final WaitQueue waitQueue;
    private final ElevatorIn elevatorIn;
    private final ElevatorWait elevatorWait;

    private final String id;//当前电梯的标号
    private int floor;//当前电梯所在楼层数
    private String direct; //刻画电梯当前运动方向:up为向上,down为向下,fish为停止
    private boolean door; //刻画电梯门当前状态:true为open,false为close

    public ElevatorThread(ElevatorIn elevatorIn, ElevatorWait elevatorWait,
                          WaitQueue waitQueue, String id) {
        this.elevatorIn = elevatorIn;
        this.elevatorWait = elevatorWait;
        this.waitQueue = waitQueue;
        this.id = id;
        this.floor = 1;
        this.direct = "fish";
        this.door = false;
    }

    @Override
    public void run() {
        while (true) {
            if (elevatorIn.isEmpty() && elevatorWait.isEmpty()
                    && waitQueue.getEnd() && waitQueue.isEmpty()) {
                return;
            }

            if (elevatorIn.isEmpty() && elevatorWait.isEmpty()) {
                synchronized (elevatorWait) {
                    try {
                        elevatorWait.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            removePassenger();
            getPassenger();

            changeDirect();
            changeFloor();
        }

    }

    public void changeDirect() {
        if (! elevatorIn.isEmpty()) {
            PersonRequest person = elevatorIn.getPersonRequests().get(0);
            if (person.getFromFloor() < person.getToFloor()) {
                direct = "up";
            }
            else if (person.getFromFloor() > person.getToFloor()) {
                direct = "down";
            }
        }
        else {
            if (! hasRequest()) {
                direct = "fish";
            }
            else {
                if (direct.equals("fish")) {
                    PersonRequest person = elevatorWait.getPersonRequests().get(0);
                    if (floor < person.getFromFloor()) {
                        direct = "up";
                    }
                    else {
                        direct = "down";
                    }
                }
            }
        }
    }

    public void changeDoor() { //改变电梯门当前的状态
        door = ! door;
        if (door) {
            TimableOutput.println("OPEN-" + floor + "-" + id);
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (! door) {
            TimableOutput.println("CLOSE-" + floor + "-" + id);
        }
    }

    public void changeFloor() { //电梯上下运动,改变层数
        if (direct.equals("up")) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            floor = floor + 1;
            TimableOutput.println("ARRIVE-" + floor + "-" + id);
        }
        else if (direct.equals("down")) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            floor = floor - 1;
            TimableOutput.println("ARRIVE-" + floor + "-" + id);
        }
    }

    public void removePassenger() {
        //符合要求的乘客直接下电梯
        //下电梯要求:乘客所到楼层等于当前楼层
        ArrayList<PersonRequest> temp;
        temp = elevatorIn.getPersonRequests();
        for (int i = 0; i < temp.size(); i++) {
            PersonRequest person = temp.get(i);
            if (person.getToFloor() == floor) {
                temp.remove(i);
                if (! door) {
                    changeDoor();
                }
                TimableOutput.println("OUT-" + person.getPersonId() + "-" + floor + "-" + id);
                i--;
            }
        }
    }

    public void getPassenger() {
        ArrayList<PersonRequest> temp;
        temp = elevatorWait.getPersonRequests();

        for (int i = 0; i < temp.size() && ! elevatorIn.isFull(); i++) {
            PersonRequest person = temp.get(i);
            if (direct.equals("fish") && person.getFromFloor() == floor) {
                if (! door) {
                    changeDoor();
                }
                TimableOutput.println("IN-" + person.getPersonId() + "-" + floor + "-" + id);
                elevatorIn.addRequest(person);
                temp.remove(i);
                i--;
            }

            if (person.getFromFloor() == floor
                    && ((person.getFromFloor() < person.getToFloor() && direct.equals("up"))
                    || (person.getFromFloor() > person.getToFloor() && direct.equals("down")))) {
                if (! door) {
                    changeDoor();
                }
                TimableOutput.println("IN-" + person.getPersonId() + "-" + floor + "-" + id);
                elevatorIn.addRequest(person);
                temp.remove(i);
                i--;
            }
        }

        if (door) {
            changeDoor();
        }
    }

    public boolean hasRequest() { //获取当前电梯的运动方向的前方有无等待队列中的请求
        if (! elevatorWait.isEmpty()) {
            for (PersonRequest person : elevatorWait.getPersonRequests()) {
                if (person.getFromFloor() > floor && direct.equals("up")) {
                    return true;
                }
                else if (person.getFromFloor() < floor && direct.equals("down")) {
                    return true;
                }
            }
            return direct.equals("fish");
        }
        return false;
    }
}
