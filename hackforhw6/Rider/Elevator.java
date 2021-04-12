import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Elevator extends Thread {
    private Queue queue;
    private int nowFloor = 1;
    private int flag; // 1 up -1 down
    private PersonRequest mainRequest = null;
    private ArrayList<PersonRequest> room = new ArrayList<>();
    private boolean judgeOpen = false;
    private boolean mainin = false;
    private Object lock;
    private ReentrantReadWriteLock rwl;
    private int id;

    /*public void setLock(Object lock) {
        this.lock = lock;
    }*/

    public Elevator(Queue queue, Object lock, int id, ReentrantReadWriteLock rwl) {
        this.queue = queue;
        this.lock = lock;
        this.id = id;
        this.rwl = rwl;
    }

    @Override
    public void run() {
        try {
            while (queue.getStop() == false || !queue.getQueue().isEmpty() || !room.isEmpty()) {
                if (!queue.getQueue().isEmpty() || !room.isEmpty()) {
                    if (mainRequest == null) {
                        setMainRequest();
                    }
                    if (mainin == false) {
                        move(mainRequest.getFromFloor());
                    }
                    move(mainRequest.getToFloor());

                } else {
                    synchronized (lock) {
                        lock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMainRequest() {
        if (room.size() == 0) {
            rwl.readLock().lock();
            try {
                if (!queue.getQueue().isEmpty()) {
                    mainRequest = queue.getQueue().get(0);
                }
            } finally {
                rwl.readLock().unlock();
            }
        } else {
            mainRequest = room.get(0);
            mainin = true;
        }
    }

    public int judgeFlag(PersonRequest now) {
        if (now.getFromFloor() > now.getToFloor()) {
            return -1;
        } else {
            return 1;
        }
    }

    public void move(int to) {
        if (nowFloor == to) {
            flag = 0;
            leave();
            pick();
        } else {
            while (nowFloor != to) {
                if (nowFloor > to) {
                    flag = -1;
                    nowFloor--;
                } else {
                    flag = 1;
                    nowFloor++;
                }
                if (nowFloor == to) {
                    flag = 0;
                }
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TimableOutput.println("ARRIVE-" + nowFloor + "-" + id);
                leave();
                pick();
            }
        }
    }

    public void leave() {
        for (int i = 0; i < room.size(); i++) {
            PersonRequest now = room.get(i);
            if (now.getToFloor() == nowFloor) {
                if (!judgeOpen) {
                    TimableOutput.println("OPEN-" + nowFloor + "-" + id);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    judgeOpen = true;
                }
                TimableOutput.println("OUT-" + now.getPersonId() + "-" + nowFloor + "-" + id);
                if (now == mainRequest) {
                    mainRequest = null;
                    mainin = false;
                }
                room.remove(i);
                i--;
            }
        }
        if (mainRequest == null) {
            setMainRequest();
        }
    }

    public void pick() {
        rwl.writeLock().lock();
        try {
            //System.out.println(Id + " get lock");
            for (int i = 0; i < queue.getQueue().size(); i++) {
                //System.out.println(queue.getQueue().size());
                PersonRequest now = queue.getQueue().get(i);
                if (check(now)) {
                    if (!judgeOpen) {
                        TimableOutput.println("OPEN-" + nowFloor + "-" + id);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        judgeOpen = true;
                    }
                    TimableOutput.println("IN-" + now.getPersonId() + "-" + nowFloor + "-" + id);
                    room.add(now);
                    queue.getQueue().remove(i);
                    if (now == mainRequest) {
                        mainin = true;
                    }
                    i--;
                }
            }
        } finally {
            rwl.writeLock().unlock();
        }

        if (judgeOpen) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TimableOutput.println("CLOSE-" + nowFloor + "-" + id);
            judgeOpen = false;
        }
        if (mainRequest != null && nowFloor == mainRequest.getFromFloor() && mainin == false) {
            setMainRequest();
        }
    }

    public boolean check(PersonRequest now) {
        if (room.size() >= 6) {
            return false;
        } else if (now.getFromFloor() != nowFloor) {
            return false;
        } else if (judgeFlag(now) != flag && flag != 0) {
            return false;
        }
        return true;
    }
}
