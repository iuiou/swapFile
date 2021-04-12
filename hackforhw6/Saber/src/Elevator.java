import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.oocourse.TimableOutput;

public class Elevator extends Thread {
    private static CopyOnWriteArrayList<Elevator> elevators = new CopyOnWriteArrayList<>();
    private int towards = 0;//向上为1，向下为-1，无方向为0
    private int maxFloor = 21;//不算第0层
    private int moveSpeed = 400;
    private int openSpeed = 200;
    private int closeSpeed = 200;
    private int waitTime = 2000;
    private int maxPeople = 6;
    private int nowPeople = 0;
    private int nowFloor = 1;//当前楼层
    private int goalFloor = -1;
    private int nowStatus = 0;//当前状态。0：等待，1：移动，2：开门，3：关门
    private ArrayList<MyRequest> peoples = new ArrayList<>();//当前电梯内的人list
    private ArrayList<MyRequest>[] peoplesMap =
            new ArrayList[maxFloor];//当前电梯内的人to楼层的map
    private Dispatch dispatch;
    private RequestList requestList;
    private String idE;

    public Elevator(Dispatch dispatch,RequestList requestList,String id) {
        super();
        this.dispatch = dispatch;
        this.requestList = requestList;
        for (int i = 0; i < 21; i++) {
            peoplesMap[i] = new ArrayList<>();
        }
        this.idE = id;
    }
    
    public String getIdE() {
        return idE;
    }
    
    public ArrayList<MyRequest> getPeoples() {
        return peoples;
    }
    
    public void setGoalFloor(int goalFloor) {
        this.goalFloor = goalFloor;
    }
    
    public synchronized int getNowPeople() {
        return nowPeople;
    }
    
    public static CopyOnWriteArrayList<Elevator> getElevators() {
        return elevators;
    }
    
    public void rmRe(MyRequest request) {
        nowPeople -= 1;
        peoples.remove(request);
        peoplesMap[request.getTo()].remove(request);
    }
    
    public void addRe(MyRequest request) {
        nowPeople += 1;
        peoples.add(request);
        peoplesMap[request.getTo()].add(request);
    }
    
    @Override
    public void run() {
        while (dispatch.findMain(this)) {
            if (!dispatch.pickMain(this)) {
                goalFloor = -1;
                continue;
            }
            try {
                towards = 0;
                if (nowFloor != goalFloor) {
                    this.towards = nowFloor > goalFloor ? -1 : 1;
                }
                if (goalFloor > 0) {
                    runMain();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            goalFloor = -1;
        }
    }
    
    public void runMain() throws InterruptedException {
        /////////电梯的运行，根据测试样例来写
        if (nowFloor == goalFloor) {
            if (requestList.getPattern().equals("Morning") && nowFloor == 1) {
                open();
                synchronized (requestList) {
                    while (requestList.getSize() > 0 && nowPeople < maxPeople) {
                        inOut();
                        sleep(waitTime);
                    }
                }
                close();
                return;
            } else {
                open();
                inOut();
                sleep(openSpeed + closeSpeed);
                close();
                return;
            }
        }
        while (workOneFloor()) {
            if (nowFloor == goalFloor) {
                towards = 0;
            }
            synchronized (requestList) {
                if (peoplesMap[nowFloor].size() != 0 ||
                        (requestList.getRequestsMap())[nowFloor].size() != 0) {
                    int flag = 0;
                    for (int i = 0; nowPeople < maxPeople &&
                            i < (requestList.getRequestsMap())[nowFloor].size(); i++) {
                        MyRequest re = (requestList.getRequestsMap())[nowFloor].get(i);
                        if (towards == 0 || re.getTowards() == towards) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 1 || peoplesMap[nowFloor].size() != 0) {
                        open();
                        inOut();
                        sleep(openSpeed + closeSpeed);
                        close();
                    }
                }
            }
        }
    }
    
    private boolean workOneFloor() throws InterruptedException {
        if (nowFloor == goalFloor) {
            return false;
        }
        if (nowFloor < goalFloor) {
            nowFloor += 1;
        } else {
            nowFloor -= 1;
        }
        sleep(moveSpeed);
        TimableOutput.println("ARRIVE-" + nowFloor + "-" + idE);
        return true;
    }
    
    private void inOut() {
        while (peoplesMap[nowFloor].size() != 0) {
            MyRequest re = peoplesMap[nowFloor].get(0);
            TimableOutput.println("OUT-" + re.getId() + "-" + nowFloor + "-" + idE);
            rmRe(re);
        }
        synchronized (requestList) {
            if (nowFloor == goalFloor) {
                for (int i = 0; nowPeople < maxPeople
                        && i < (requestList.getRequestsMap())[nowFloor].size(); i++) {
                    MyRequest re = (requestList.getRequestsMap())[nowFloor].get(i);
                    if (re.getFree() == 1 && (towards == 0 || re.getTowards() == towards)) {
                        TimableOutput.println("IN-" + re.getId() + "-" + nowFloor + "-" + idE);
                        requestList.rmRe(re);
                        i--;
                        addRe(re);
                        break;
                    }
                }
            }    ////在目标楼层接到主请求
            
            
            for (int i = 0; ((nowFloor == goalFloor && nowPeople < maxPeople) ||
                    (nowFloor != goalFloor && nowPeople < maxPeople - 1))
                    && i < (requestList.getRequestsMap())[nowFloor].size(); i++) {
                MyRequest re = (requestList.getRequestsMap())[nowFloor].get(i);
                if (towards == 0 || re.getTowards() == towards) {
                    TimableOutput.println("IN-" + re.getId() + "-" + nowFloor + "-" + idE);
                    requestList.rmRe(re);
                    i--;
                    addRe(re);
                    if (re.getFree() == 0) {
                        requestList.setTrueSize(requestList.getTrueSize() - 1);
                    }  //若不为被主请求的，则真实队列-1
                }
            }
        }
    }
    
    private void open() {
        TimableOutput.println("OPEN-" + nowFloor + "-" + idE);
    }
    
    private void close() {
        TimableOutput.println("CLOSE-" + nowFloor + "-" + idE);
    }
}
