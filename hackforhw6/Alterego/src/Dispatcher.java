import com.oocourse.elevator2.PersonRequest;

import java.util.Vector;

public class Dispatcher implements Runnable {
    private MyList myList;
    private Elevator ele;
    private String arrivePattern; //night morning
    private String id;

    public Dispatcher(MyList myList, String arrivePattern, String id) {
        this.myList = myList;
        this.ele = new Elevator(id);
        this.arrivePattern = arrivePattern;
        this.id = id;
    }

    public void setArrivePattern(String arrivePattern) {
        this.arrivePattern = arrivePattern;
    }

    public MyList getMyList() {
        return myList;
    }

    public Elevator getEle() {
        return ele;
    }

    @Override
    public void run() {
        while (true) {
            //已经将inputEnd相关方法同步，这里应该不用再加同步块了吧
            if (myList.isEmpty() && ele.isEmpty() && myList.getInputEnd()) {
                //System.err.println("ele" + id + "over");
                break;
            } else {
                while (myList.isEmpty() && ele.isEmpty()) {
                    try {
                        ele.setState(0);
                        ele.setDirection(0); //这个地方需要吗
                        synchronized (myList) {
                            myList.wait();
                        }
                        if (myList.getInputEnd()) {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if ("Night".equals(arrivePattern)) {
                runNight();
            } else if ("Morning".equals(arrivePattern)) {
                while (myList.getUpQueue().size() < 9 && !myList.getInputEnd() && ele.isEmpty()) {
                    ele.setState(0);
                    ele.setDirection(0);
                    synchronized (myList) {
                        try { myList.wait(); }
                        catch (InterruptedException e) { e.printStackTrace(); } }
                }
                runMorning();
            } else if ("Random".equals(arrivePattern)) {
                if (ele.getNowFloor() == 20 && ele.getDirection() == 1) {
                    ele.setDirection(-1);
                } else if (ele.getNowFloor() == 1 && ele.getDirection() == -1) {
                    ele.setDirection(1);
                } else {
                    runRandom();
                }
            }
        }
    }

    public void runNight() {
        if (ele.getState() == 1) {
            if (ele.getDirection() == 1) {
                ele.goUp();
                Vector<PersonRequest> downFromsWhenUp = myList.findDownFrom(ele.getDirection(),
                        ele.getNowFloor(), ele.getNum());
                //可以观察一下这个方法能否正确实现
                if (downFromsWhenUp != null) {
                    ele.open();
                    ele.getIn(downFromsWhenUp);
                    myList.getIn(1, downFromsWhenUp);
                    ele.close();
                    ele.setDirection(-1);
                }
                //看看这种情况下，我写的以上代码是否解决得足够全面
            } else {
                ele.goDown();
                if (ele.getNowFloor() > 1) {
                    if (ele.getNum() < 6) {
                        Vector<PersonRequest> upFromsWhenDown = myList
                                .findUpFroms(ele.getDirection(), ele.getNowFloor(), ele.getNum());
                        if (upFromsWhenDown != null) {
                            ele.open();
                            ele.getIn(upFromsWhenDown);
                            myList.getIn(ele.getDirection(), upFromsWhenDown);
                            ele.close();
                        }
                    }
                } else {
                    Vector<PersonRequest> targets = ele.findTarget(ele.getNowFloor());
                    if (targets != null) {
                        ele.open();
                        ele.getOut(targets);
                        ele.close();
                        ele.setDirection(1);
                    }
                }
            }
        } else {
            if (!myList.getDownQueue().isEmpty()) {
                if (ele.getNowFloor() > myList.getDownQueue().get(0).getFromFloor()) {
                    ele.setState(1);
                    ele.setDirection(-1);
                } else if (ele.getNowFloor() == myList.getDownQueue().get(0).getFromFloor()) {
                    Vector<PersonRequest> downFromsWhenUp =
                            myList.findDownFrom(1, ele.getNowFloor(), ele.getNum());
                    //if (downFromsWhenUp != null) 这个语句是必须的吗
                    ele.open();
                    ele.getIn(downFromsWhenUp);
                    myList.getIn(1, downFromsWhenUp);
                    ele.close();
                    ele.setState(1);
                    ele.setDirection(-1);
                } else {
                    ele.setState(1);
                    ele.setDirection(1);
                }
            }
        }
    }

    public void runMorning() {
        if (ele.getState() == 1) {
            if (ele.getDirection() == 1) {
                ele.goUp();
                Vector<PersonRequest> targets = ele.findTarget(ele.getNowFloor());
                if (targets != null) {
                    ele.open();
                    ele.getOut(targets);
                    ele.close();
                    if (ele.isEmpty()) {
                        ele.setDirection(-1);
                    }
                }
            } else {
                ele.goDown();
                if (ele.getNowFloor() == 1) {
                    Vector<PersonRequest> requests = myList.findMorning();
                    ele.open();
                    ele.getIn(requests);
                    myList.getIn(ele.getDirection(), requests);
                    ele.close();
                    ele.setDirection(1);
                }
            }
            //如果ele不在1层，就向下走
        } else {
            //if (myList.getInputEnd())
            if (!myList.getUpQueue().isEmpty()) {
                if (myList.getUpQueue().size() >= 9 || myList.getInputEnd()) {
                    if (ele.getNowFloor() > 1) {
                        ele.setState(1);
                        ele.setDirection(-1);
                    } else {
                        Vector<PersonRequest> requests = myList.findMorning();
                        ele.open();
                        ele.getIn(requests);
                        myList.getIn(-1, requests);
                        ele.close();
                        ele.setState(1);
                        ele.setDirection(1);
                    }
                }
            }
        }
    }

    public void runRandom() {
        if (ele.getState() == 1) {
            if (ele.getDirection() == 1) {
                runRandomUp();
                if (ele.getDoorOpen()) {
                    ele.close();
                }
            } else if (ele.getDirection() == -1) {
                runRandomDown();
                if (ele.getDoorOpen()) {
                    ele.close();
                }
            }
        } else {
            if (!myList.getUpQueue().isEmpty()) {
                if (myList.getUpQueue().get(0).getFromFloor() < ele.getNowFloor()) {
                    ele.setState(1);
                    ele.setDirection(-1);
                } else if (myList.getUpQueue().get(0).getFromFloor() == ele.getNowFloor()) {
                    //System.out.println(myList.getUpQueue().get(0).getFromFloor());
                    Vector<PersonRequest> downFromsWhenUp =
                            myList.findDownFrom(-1, ele.getNowFloor(), ele.getNum());
                    //System.out.println(downFromsWhenUp.size());
                    ele.open();
                    ele.getIn(downFromsWhenUp);
                    myList.getIn(-1, downFromsWhenUp);
                    ele.close();
                    ele.setState(1);
                    ele.setDirection(1);
                } else {
                    ele.setState(1);
                    ele.setDirection(1);
                }
            } else if (!myList.getDownQueue().isEmpty()) {
                if (myList.getDownQueue().get(0).getFromFloor() > ele.getNowFloor()) {
                    ele.setState(1);
                    ele.setDirection(1);
                } else if (myList.getDownQueue().get(0).getFromFloor() == ele.getNowFloor()) {
                    Vector<PersonRequest> downFromsWhenUp = myList
                            .findDownFrom(1, ele.getNowFloor(), ele.getNum());
                    ele.open();
                    ele.getIn(downFromsWhenUp);
                    myList.getIn(1, downFromsWhenUp);
                    ele.close();
                    ele.setState(1);
                    ele.setDirection(-1);
                } else {
                    ele.setState(1);
                    ele.setDirection(-1);
                }
            }
        }
    }

    public void runRandomUp() {
        ele.goUp(); //到顶层了怎么办
        Vector<PersonRequest> targets = ele.findTarget(ele.getNowFloor());
        if (targets != null) {
            ele.open();
            ele.getOut(targets);
            //isArrive
            //这个地方讨论情况多了，因为本来是抽出来单独作为一个方法的
            //(has wrong?) 继续研究一下，当运行方向上没有请求层时可以调转方向
            isArrive();
        }
        if (ele.getNum() < 6) { //如果满6人（保证了电梯内乘客方向一致，那么不会有问题？）
            Vector<PersonRequest> downFromsWhenUp = myList
                    .findDownFrom(ele.getDirection(), ele.getNowFloor(), ele.getNum());
            Vector<PersonRequest> upFromsWhenUp = myList
                    .findUpFroms(ele.getDirection(), ele.getNowFloor(), ele.getNum());
            if (downFromsWhenUp != null) {
                if (!ele.getDoorOpen()) {
                    ele.open();
                }
                ele.getIn(downFromsWhenUp);
                myList.getIn(ele.getDirection(), downFromsWhenUp);
                ele.close();
                ele.setDirection(-1);
            } else if (upFromsWhenUp != null) {
                if (!ele.getDoorOpen()) {
                    ele.open();
                }
                ele.getIn(upFromsWhenUp);
                myList.getIn(ele.getDirection(), upFromsWhenUp);
                ele.close();
            }
        }
    }

    public void runRandomDown() {
        ele.goDown();
        Vector<PersonRequest> targets = ele.findTarget(ele.getNowFloor());
        if (targets != null) {
            ele.open();
            ele.getOut(targets);
            //isArrive
            isArrive();
        }
        if (ele.getNum() < 6) {
            //方法命名不和谐
            Vector<PersonRequest> downFromsWhenDown = myList
                    .findDownFrom(ele.getDirection(), ele.getNowFloor(), ele.getNum());
            Vector<PersonRequest> upFromsWhenDown = myList
                    .findUpFroms(ele.getDirection(), ele.getNowFloor(), ele.getNum());
            if (downFromsWhenDown != null) {
                if (!ele.getDoorOpen()) {
                    ele.open();
                }
                ele.getIn(downFromsWhenDown);
                myList.getIn(ele.getDirection(), downFromsWhenDown);
                ele.close();
                ele.setDirection(1);
            } else if (upFromsWhenDown != null) {
                if (!ele.getDoorOpen()) {
                    ele.open();
                }
                ele.getIn(upFromsWhenDown);
                myList.getIn(ele.getDirection(), upFromsWhenDown);
                ele.close();
            }
        }
    }

    public void isArrive() {
        if (ele.isEmpty()) {
            if ((ele.getDirection() == -1 && myList.getDownQueue().isEmpty()
                    && !myList.getUpQueue().isEmpty()) &&
                    (myList.getUpQueue().get(0).getFromFloor() > ele.getNowFloor())) {
                ele.setDirection(1);
            } else if ((ele.getDirection() == -1 && !myList.getDownQueue().isEmpty()
                    && myList.getUpQueue().isEmpty()) &&
                    (myList.getMinDownQueue() > ele.getNowFloor())) {
                ele.setDirection(1);
            } else if ((ele.getDirection() == -1 && !myList.getDownQueue().isEmpty()
                    && !myList.getUpQueue().isEmpty()) &&
                    (myList.getUpQueue().get(0).getFromFloor() > ele.getNowFloor() &&
                            myList.getMinDownQueue() > ele.getNowFloor())) {
                ele.setDirection(1);
            } else if ((ele.getDirection() == 1 && !myList.getDownQueue().isEmpty() &&
                    myList.getUpQueue().isEmpty()) && (myList.
                    getDownQueue().get(0).getFromFloor() < ele.getNowFloor())) {
                ele.setDirection(-1);
            } else if ((ele.getDirection() == 1 && myList.getDownQueue().isEmpty() &&
                    !myList.getUpQueue().isEmpty()) &&
                    (myList.getMaxUpQueue() < ele.getNowFloor())) {
                ele.setDirection(-1);
            } else if ((ele.getDirection() == 1 && !myList.getDownQueue().isEmpty() &&
                    !myList.getUpQueue().isEmpty()) &&
                    (myList.getDownQueue().get(0).getFromFloor() < ele.getNowFloor() &&
                            myList.getMaxUpQueue() < ele.getNowFloor())) {
                ele.setDirection(-1);
            }
        } else {
            if (ele.getDirection() == -1) {
                if (ele.getMin() > ele.getNowFloor()) {
                    ele.setDirection(1);
                }
            } else if (ele.getDirection() == 1) {
                if (ele.getMax() < ele.getNowFloor()) {
                    ele.setDirection(-1);
                }
            }
        }
    }
}
