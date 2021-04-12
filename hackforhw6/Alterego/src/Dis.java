import com.oocourse.elevator2.PersonRequest;

import java.util.Vector;

public class Dis implements Runnable {
    private Vector<Dispatcher> dispatchers;
    private MyList myList;
    private Vector<MyList> myLists;

    public Dis(Vector<Dispatcher> dispatchers, MyList myList, Vector<MyList> myLists) {
        this.dispatchers = dispatchers;
        this.myList = myList;
        this.myLists = myLists;
    }

    @Override
    public void run() {
        while (true) {
            if (myList.getInputEnd() && myList.isEmpty()) {
                //为何要声明elevators
                for (Dispatcher dispatcher : dispatchers) {
                    dispatcher.getMyList().setInputEnd(true);
                }
                for (MyList myList : myLists) {
                    synchronized (myList) {
                        myList.notifyAll();
                    }
                }
                //System.err.println("dis over");
                return;
            } else if (myList.isEmpty()) {
                synchronized (myList) {
                    try {
                        myList.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < myList.size(); i++) {
                    PersonRequest pr = myList.get(i);
                    int sign = 0;
                    int num = dispatchers.get(0).getMyList().size() +
                            dispatchers.get(0).getEle().getNum() / 2;
                    for (int j = 0; j < dispatchers.size(); j++) {
                        int numj = dispatchers.get(j).getMyList().size() +
                                dispatchers.get(j).getEle().getNum() / 2;
                        if (numj < num) {
                            num = numj;
                            sign = j;
                        }
                    }
                    //EleTime eleTime = new EleTime(elevators.get(0), pr);
                    //int time = eleTime.run();
                    //for (int j = 0; j < elevators.size(); j++) {
                    //    EleTime eleTimei = new EleTime(elevators.get(j), pr);
                    //    int timei = eleTimei.run();
                    //    System.out.println(timei);
                    //    if (timei < time) {
                    //        time = timei;
                    //        sign = j;
                    //    }
                    //}
                    waitToOut(pr, sign); //从wait队列进入out队列
                    i--;
                }
            }
        }
    }

    public void waitToOut(PersonRequest pr, int i) {
        myList.remove(pr);
        synchronized (myList) {
            myList.notifyAll();
        }
        Dispatcher dispatcher = dispatchers.get(i);
        dispatcher.getMyList().add(pr);
        synchronized (dispatcher.getMyList()) {
            dispatcher.getMyList().notifyAll();
        }
    }
}
