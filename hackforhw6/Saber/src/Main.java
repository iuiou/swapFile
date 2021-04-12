import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        // please MUST initialize start timestamp at the beginning
        TimableOutput.initStartTimestamp();
        RequestList requestList = new RequestList();
        Dispatch dispatch = new Dispatch(requestList);
        Elevator.getElevators().add(new Elevator(dispatch,requestList,"1"));
        Elevator.getElevators().add(new Elevator(dispatch,requestList,"2"));
        Elevator.getElevators().add(new Elevator(dispatch,requestList,"3"));
        requestList.setDispatch(dispatch);
        new Input(requestList,dispatch).start();
        for (int i = 0;i < 3;i++) {
            Elevator.getElevators().get(i).start();
        }
    }
}

//Random
//1-FROM-1-TO-3
//2-FROM-2-TO-4

//Random
//209-FROM-2-TO-9
//95-FROM-11-TO-15
//285-FROM-20-TO-11
//ADD-97
//368-FROM-10-TO-15

//Random
//115-FROM-3-TO-6
//174-FROM-2-TO-5
//326-FROM-14-TO-7
//384-FROM-13-TO-16
//307-FROM-13-TO-17
//310-FROM-10-TO-16
//109-FROM-15-TO-12
//ADD-431
//178-FROM-12-TO-13
//295-FROM-16-TO-10
//101-FROM-10-TO-19
//29-FROM-1-TO-10
//270-FROM-18-TO-6
//311-FROM-4-TO-8

//Morning
//ADD-4
//111-FROM-1-TO-5
//222-FROM-1-TO-6
//333-FROM-1-TO-4
//444-FROM-1-TO-3

//Random
//1-FROM-1-TO-3
//2-FROM-2-TO-4