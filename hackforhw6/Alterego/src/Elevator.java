import com.oocourse.elevator2.PersonRequest;
import com.oocourse.TimableOutput;

import java.util.Vector;

public class Elevator {
    private int nowFloor;
    private int direction;
    private int state; //变量类型待定
    private boolean doorOpen;
    private int num;
    private Vector<PersonRequest> targetVector;
    private String id;

    public Elevator(String id) {
        nowFloor = 1;
        direction = 0;
        state = 0; //变量名待定
        doorOpen = false;
        num = 0;
        targetVector = new Vector<>();
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getNowFloor() {
        return nowFloor;
    }

    public int getNum() {
        return num;
    }

    public boolean getDoorOpen() {
        return doorOpen;
    }

    public boolean isEmpty() {
        return targetVector.isEmpty();
    }

    public void goUp() {
        nowFloor = nowFloor + 1; //需要判断是否<20吗
        sleep(400);
        arrive();
    }

    public void goDown() {
        nowFloor = nowFloor - 1; //需要判断是否为自然数吗
        sleep(400);
        arrive();
    }

    public void open() {
        doorOpen = true;
        synchronized (TimableOutput.class) {
            TimableOutput.println("OPEN-" + nowFloor + "-" + id);
        }
        //System.err.println("OPEN-" + nowFloor + "-" + id);
        sleep(200);
    }

    public void close() {
        sleep(200);
        synchronized (TimableOutput.class) {
            TimableOutput.println("CLOSE-" + nowFloor + "-" + id);
        }
        //System.err.println("CLOSE-" + nowFloor + "-" + id);
        doorOpen = false;
    }

    public void arrive() {
        synchronized (TimableOutput.class) {
            TimableOutput.println("ARRIVE-" + nowFloor + "-" + id);
        }
        //System.err.println("ARRIVE-" + nowFloor + "-" + id);
    }

    public void sleep(int time) {
        try {
            Thread.sleep(time); //这个是什么意思
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Vector<PersonRequest> findTarget(int nowFloor) {
        Vector<PersonRequest> targets = null;
        for (PersonRequest request : targetVector) {
            if (request.getToFloor() == nowFloor) {
                //assert targets != null;
                if (targets == null) {
                    targets = new Vector<>();
                }
                targets.add(request);
            }
        }
        return targets;
    }

    public void getOut(Vector<PersonRequest> targets) {
        for (PersonRequest request : targets) {
            num = num - 1;
            targetVector.remove(request);
            synchronized (TimableOutput.class) {
                TimableOutput.println("OUT-" + request.getPersonId() + "-" + nowFloor + "-" + id);
            }
            //System.err.println("OUT-" + request.getPersonId() + "-" + nowFloor + "-" + id);
        }
    }

    public void getIn(Vector<PersonRequest> froms) {
        for (PersonRequest request : froms) {
            num = num + 1;
            targetVector.add(request);
            synchronized (TimableOutput.class) {
                TimableOutput.println("IN-" + request.getPersonId() + "-" + nowFloor + "-" + id);
            }
            //System.err.println("IN-" + request.getPersonId() + "-" + nowFloor + "-" + id);
        }
    }

    public int getMin() {
        int min = targetVector.get(0).getToFloor();
        for (PersonRequest request : targetVector) {
            if (request.getToFloor() < min) {
                min = request.getToFloor();
            }
        }
        return min;
    }

    public int getMax() {
        int max = targetVector.get(0).getToFloor();
        for (PersonRequest request : targetVector) {
            if (request.getToFloor() > max) {
                max = request.getToFloor();
            }
        }
        return max;
    }
}
