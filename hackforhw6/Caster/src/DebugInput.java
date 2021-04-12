import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class DebugInput implements Runnable {

    private final OutputStream outputStream;

    public DebugInput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Timer timer = new Timer(); // 初始化一个定时器
        long currentMillis = System.currentTimeMillis();
        long maxMillis = 0; // 记录最后一条输入的时间
        while (scanner.hasNext()) {
            String[] string = scanner.nextLine().split("]");
            double mill = Double.parseDouble(string[0].substring(1).trim());
            mill = mill * 1000;
            long millis = new Double(mill).longValue(); // 先读取时间
            String input = string[1]; // 读取一行输入
            maxMillis = millis; // 更新maxMillis
            timer.schedule(new TimerTask() { // 创建定时任务
                @Override
                public void run() {
                    try {
                        outputStream.write(input.getBytes());
                        outputStream.write(0x0a); // 换行符
                        outputStream.flush(); // 强制刷新
                    } catch (IOException e) {
                        throw new AssertionError(e);
                    }
                }
            }, new Date(currentMillis + millis));
        }
        // 最后别忘了关闭管道流以及关闭定时器，添加最后一个定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                timer.cancel(); // 关闭定时器，不加这句则定时器可能无限等待
            }
        }, new Date(currentMillis + maxMillis));
    }
}