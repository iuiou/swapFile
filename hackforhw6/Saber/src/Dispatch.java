public class Dispatch {
    private RequestList requestList;
    
    public Dispatch(RequestList requestList) {
        this.requestList = requestList;
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    public boolean findMain(Elevator elevator) {
        if (waitReq(elevator)) {
            return false;
        }
        return true;
    }
    
    private boolean waitReq(Elevator elevator) {
        if (elevator.getNowPeople() == 0) {
            synchronized (requestList) {
                if (requestList.getTrueSize() == 0) {
                    while (!requestList.isDone() && requestList.getTrueSize() == 0) {
                        try {
                            requestList.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (requestList.isDone() && requestList.getTrueSize() == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private int flag = 0;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean pickMain(Elevator elevator) {
        if (requestList.getPattern().equals("Random") ||
                requestList.getPattern().equals("Morning")) {
            if (elevator.getNowPeople() == 0) {
                synchronized (requestList) {
                    int i;
                    for (i = 0; i < requestList.getSize(); i++) {
                        if (requestList.getRequestsList().get(i).getFree() == 0) {
                            elevator.setGoalFloor((requestList.getRequestsList().get(i)).getFrom());
                            requestList.getRequestsList().get(i).setFree(1);
                            requestList.setTrueSize(requestList.getTrueSize() - 1);
                            break;
                        }
                    }
                    if (i == requestList.getSize()) {
                        return false;
                    }
                }
                return true;
            } else {
                elevator.setGoalFloor((elevator.getPeoples().get(0)).getTo());
                return true;
            }
        } else if (requestList.getPattern().equals("Night")) {
            if (flag == 0) {
                int max = 1;
                synchronized (requestList) {
                    for (MyRequest request : requestList.getRequestsList()) {
                        if (request.getFrom() > max) {
                            max = request.getFrom();
                        }
                    }
                }
                elevator.setGoalFloor(max);
                /////轮询？？？？
                flag = 1;
                return true;
            } else {
                elevator.setGoalFloor(1);
                flag = 0;
                return true;
            }
        }
        return false;
    }
}
