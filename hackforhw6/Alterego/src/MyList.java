import com.oocourse.elevator2.PersonRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class MyList {
    private Vector<PersonRequest> requestVector;
    private Vector<PersonRequest> upQueue;
    private Vector<PersonRequest> downQueue;
    private boolean inputEnd;

    private Comparator<PersonRequest> upCmp = new MyComparator();
    private Comparator<PersonRequest> downCmp = new MyRevComparator();

    public MyList() {
        requestVector = new Vector<>();
        upQueue = new Vector<>();
        downQueue = new Vector<>();
        inputEnd = false;
    }

    public Vector<PersonRequest> getUpQueue() {
        return upQueue;
    }

    public Vector<PersonRequest> getDownQueue() {
        return downQueue;
    }

    public synchronized boolean getInputEnd() {
        return inputEnd;
    }

    public synchronized void setInputEnd(boolean inputEnd) {
        this.inputEnd = inputEnd;
    }

    public boolean isEmpty() {
        return requestVector.isEmpty();
    }

    public int size() {
        return requestVector.size();
    }

    public PersonRequest get(int i) {
        return requestVector.get(i);
    }

    public void add(PersonRequest request) {
        requestVector.add(request);
        if (request.getFromFloor() < request.getToFloor()) {
            upQueue.add(request);
            Collections.sort(upQueue, upCmp);
        } else if (request.getFromFloor() > request.getToFloor()) {
            downQueue.add(request);
            Collections.sort(downQueue, downCmp);
        }
    }

    public void remove(PersonRequest request) {
        requestVector.remove(request);
        if (request.getFromFloor() < request.getToFloor()) {
            upQueue.remove(request);
            Collections.sort(upQueue, upCmp);
        } else if (request.getFromFloor() > request.getToFloor()) {
            downQueue.remove(request);
            Collections.sort(downQueue, downCmp);
        }
    }

    public Vector<PersonRequest> findDownFrom(int direction, int nowFloor, int num) {
        Vector<PersonRequest> froms = null;
        int i = 0;
        if (direction == 1) {
            if (downQueue.isEmpty()) {
                return froms;
            } else if (downQueue.get(0).getFromFloor() != nowFloor) {
                return froms;
            } else {
                froms = new Vector<>();
                for (; i < downQueue.size(); i++) {
                    if (downQueue.get(i).getFromFloor() == nowFloor && num + i < 6) {
                        froms.add(downQueue.get(i));
                        //i = i + 1;
                    } else {
                        break;
                    }
                }
                return froms;
            }
        } else if (direction == -1) {
            if (upQueue.isEmpty()) { //相似部分可以写成函数
                return froms;
            } else if (upQueue.get(0).getFromFloor() != nowFloor) {
                return froms;
            } else {
                froms = new Vector<>();
                for (; i < upQueue.size(); i++) {
                    if (upQueue.get(i).getFromFloor() == nowFloor && num + i < 6) {
                        froms.add(upQueue.get(i));
                        //i = i + 1;
                    } else {
                        break;
                    }
                }
                return froms;
            }
        }
        return froms;
    }

    public void getIn(int direction, Vector<PersonRequest> froms) {
        if (direction == 1) {
            for (PersonRequest request : froms) {
                if (upQueue.contains(request)) {
                    upQueue.remove(request);
                } else if (downQueue.contains(request)) {
                    downQueue.remove(request);
                }
                requestVector.remove(request);
            }
        } else if (direction == -1) {
            for (PersonRequest request : froms) {
                if (upQueue.contains(request)) {
                    upQueue.remove(request);
                } else if (downQueue.contains(request)) {
                    downQueue.remove(request);
                }
                requestVector.remove(request);
            }
        }
    }

    public Vector<PersonRequest> findUpFroms(int direction, int nowFloor, int num) {
        Vector<PersonRequest> froms = null;
        int i = 0;
        if (direction == 1) {
            for (; i < upQueue.size(); i++) {
                if (upQueue.get(i).getFromFloor() == nowFloor && num + i < 6) {
                    if (froms == null) {
                        froms = new Vector<>();
                    }
                    froms.add(upQueue.get(i));
                    //i = i + 1;
                }
            }
            return froms;
        } else if (direction == -1) {
            for (; i < downQueue.size(); i++) {
                if (downQueue.get(i).getFromFloor() == nowFloor && num + i < 6) {
                    if (froms == null) {
                        froms = new Vector<>();
                    }
                    froms.add(downQueue.get(i));
                    //i = i + 1;
                }
            }
            return froms;
        }
        return froms;
    }

    public Vector<PersonRequest> findMorning() {
        Vector<PersonRequest> requests = new Vector<>();
        int i = 0;
        if (upQueue.size() > 6) {
            while (i < 6) {
                //System.out.println(request.getPersonId());
                requests.add(upQueue.get(i));
                i = i + 1;
            }
        } else {
            requests.addAll(upQueue);
        }
        return requests;
    }

    public int getMinDownQueue() {
        if (downQueue.isEmpty()) {
            return -1;
        }
        int min = downQueue.get(0).getFromFloor();
        for (PersonRequest request : downQueue) {
            if (request.getFromFloor() < min) {
                min = request.getFromFloor();
            }
        }
        return min;
    }

    public int getMaxUpQueue() {
        if (upQueue.isEmpty()) {
            return 21;
        }
        int max = upQueue.get(0).getFromFloor();
        for (PersonRequest request : upQueue) {
            if (request.getFromFloor() > max) {
                max = request.getFromFloor();
            }
        }
        return max;
    }
}
