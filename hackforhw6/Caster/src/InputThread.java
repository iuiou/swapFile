import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

public class InputThread implements Runnable {
    private final WaitQueue waitQueue;
    private final ArrayList<ElevatorIn> elevatorIns;
    private final ArrayList<ElevatorWait> elevatorWaits;
    private static final boolean DEBUG = false;

    public InputThread(WaitQueue waitQueue, ArrayList<ElevatorIn> elevatorIns,
                       ArrayList<ElevatorWait> elevatorWaits) {
        this.waitQueue = waitQueue;
        this.elevatorIns = elevatorIns;
        this.elevatorWaits = elevatorWaits;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput;
        // 实例化一对管道流
        PipedOutputStream myPipeOut = new PipedOutputStream();
        PipedInputStream myPipeIn = new PipedInputStream();
        // 将二者连接起来, PipedOutputStream的connect方法会抛出IOException
        try {
            myPipeOut.connect(myPipeIn);
        } catch (IOException e) {
            throw new AssertionError(e); // Never happen
        }

        if (DEBUG) { // 调试开关, 提交评测时务必关掉它！
            elevatorInput = new ElevatorInput(myPipeIn);
            new Thread(new DebugInput(myPipeOut)).start();
        }
        else {
            elevatorInput = new ElevatorInput(System.in);
        }

        String pattern = elevatorInput.getArrivingPattern();
        waitQueue.setPattern(pattern);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                waitQueue.isEnd();
                synchronized (waitQueue) {
                    waitQueue.notifyAll();
                }
                break;
            }
            else if (request instanceof PersonRequest) {
                waitQueue.addRequest((PersonRequest) request);
                synchronized (waitQueue) {
                    waitQueue.notifyAll();
                }
            }
            else if (request instanceof ElevatorRequest) {
                String id = ((ElevatorRequest) request).getElevatorId();
                ElevatorIn elevatorIn = new ElevatorIn();
                elevatorIns.add(elevatorIn);
                ElevatorWait elevatorWait = new ElevatorWait();
                elevatorWaits.add(elevatorWait);

                ElevatorThread elevatorThread = new ElevatorThread(elevatorIn, elevatorWait,
                        waitQueue, id);
                Thread threadElevator = new Thread(elevatorThread);
                threadElevator.start();
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
