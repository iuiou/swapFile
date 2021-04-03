## Datacheck程序使用说明

#### 程序功能

本datacheck程序基于ALS调度策略度量给定输入的电梯运行时间。

#### 使用说明

1. 请将包含输入的文件以`stdin.txt`命名，放置在相同路径下。

2. 在命令行中执行二进制文件。

   例：

   ```bash
   chmod u+x datacheck_student_linux_x86_64
   ./datacheck_student_linux_x86_64
   ```

   对于合法输入，程序将输出$T_{base}$和$T_{max}$；对于非法输入，程序将尽可能定位错误原因。

提示：请根据操作系统使用对应的二进制文件。