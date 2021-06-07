### 一、System类

- 主要的静态变量有，in,out,err(标准输入流，标准输出流，错误流)，这三个还是final修饰的；所有的成员都是静态的

- 方法

  1. public static void exit(int status);作用是终止当前正在运行的虚拟机，status状态标志非0表示异常终止

  2. ```java
     public static void arraycopy(Object src,
                                 int srcPos,
                                  Object dest,
                                  int destPos,
                                  int length) 
     ```

     赋值src数组的从srcpos索引位置开始,length个数值；到目标数组dest的destPos索引位置开始

  3. gc();主动运行垃圾回收器

  4. public static long currentTimeMillis();用来获取当前时间，以毫秒为单位；可以用来测量一段程序的运行时间；具体操作是在被测量的代码开头与结尾都执行一次，并且用两个不同的long型变量存储起来，最后两个变量值相减就是中间代码运行的时间

  5. public static long nanoTime();也是用来获取当前时间，但单位是毫微秒

### 二、Runtime类

- Runtime类封装了java虚拟机进程，一个java虚拟机对应一个Runtime实例对象。不能通过new创建Runtime的实例对象，只能通过Runtime的静态方法getRuntime()来获取该实例对象

- 常用的方法

  1. public Process exec(String command);作用是启动这个子进程，参数是命令字符串

     例子：

     ```java
     Process p=null;
     	try {
     		p=Runtime.getRuntime().exec("notepad.exe D:\\zuo_mian");
     		Thread.sleep(8000);
     		p.destroy();
     	} catch (Exception e) {
     		// TODO Auto-generated catch block
     		e.printStackTrace();
     	}
     ```

  2. public long freeMemory();返回java虚拟机中的空闲内存，单位是字节

     1. public long totalMemory();返回java虚拟机中的内存容量，单位是字节

### 三、Calendar类

- calendar类是一个抽象类，不能直接通过new来创建实列化对象，使用Calendar.getInstance()来返回一个实例化对象；主要作用是获取和设置时间、日期之类的
- 还可以与Date类相互转化，Date类可以直接new来创建对象，打印对象就是时间

### 四、Random类

- 两种构造方法；无参的，和参数为long seed；seed为种子，种子相同，生成的随机数也相同

- 可以通过nextInt(int n),nextDouble()等方法来设置随机数的范围，设置数字的几率等

  ```java
  int n=new Random().nextInt(100);//n的范围为[0,100)
  double d=new Random().nextDouble();//d的范围为[0,1)
  Integer n=new Random().nextInt(9000)+1000;//范围是[1000,10000)四位数
  ```
```
  
  




```