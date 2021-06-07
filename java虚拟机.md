### 一、JMM

> ![img](https://pic2.zhimg.com/80/v2-bd607bd9a5598a8330ad329033e04b91_720w.jpg)

（1）JMM分为堆（heap）和线程栈（Tread Stack）；JMM目的是规范线程与内存之间的关系；线程栈中的私有变量对其他线程是不可见的，比如上图中的Local varible 1就是私有变量（感觉是局部变量；不是new初始化的）；Local variable 2是对象的引用，指向存放在堆中的Object3对象，该对象有两个同样是new出来的成员变量，所以两个线程都可以通过Localvariable 2来访问Object2和Object4；所有new出来的对象都是放在堆之中；

（2）两个线程执行method Two()的同样的代码时，会实例化两个Interger对象存放在堆上，两个对象的hashcode不同，值相同，同时每个线程都有对应的实例化对象的拷贝；

（3）两个线程都有Object3实例化对象的拷贝，该对象还有两个不是new出来的long变量，在Object3实例化之后也一样存放在堆之中（感觉这就像全局变量一样）；被static修饰的静态变量存储在堆中

（4）堆之中的数据对所有线程都是可见的，一个线程有一个线程栈且对其他线程是不可见的；

示例代码：

```java
public class MyRunnable implements Runnable() {

    public void run() {
        methodOne();
    }

    public void methodOne() {
        int localVariable1 = 45;

        MySharedObject localVariable2 =
            MySharedObject.sharedInstance;

        //... do more with local variables.

        methodTwo();
    }

    public void methodTwo() {
        Integer localVariable1 = new Integer(99);

        //... do more with local variable.
    }
}

public class MySharedObject {

    //static variable pointing to instance of MySharedObject

    public static final MySharedObject sharedInstance =
        new MySharedObject();

    //member variables pointing to two objects on the heap

    public Integer object2 = new Integer(22);
    public Integer object4 = new Integer(44);

    public long member1 = 12345;
    public long member1 = 67890;
}
```

> ![å¾ç](http://mmbiz.qpic.cn/mmbiz/7SKDAZlEYA6YSia4U2N9gCibfCBiapvIgstj79SgibwFvohYFconUntBY7YKvYg6rq0oJfKtZkvWBVFmCaniaTOTImQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

（5）JMM模型与硬件内存结构的关系；堆中的对象与线程栈之中的数据（局部变量和对象的引用）在内存的三层结构（主存、cpu缓存、cpu寄存器）之中都可能存在，线程栈和堆中的数据主要都存在主存中；cpu都是从寄存器中取数据来计算的，需要先从主存移到缓存，在从缓存移动到寄存器中；

（6）JMM模型的主要解决两个问题：

1. 在堆中的共享变量的的可见性
   - 可以通过synchrinized和volatile关键字来修饰共享变量；两种关键字的可见性原理不一样
   - synchrinized是通过设置“内存屏障”（Memory Barrier）：一是把所有cpu操作的结果都刷新在主存中，实现了可见性；二是让先获得锁的操作都happens-before随后获得锁的操作，就是前面一个的操作的结果对后面操作都是可见的
   - volatile就是把在寄存器中的变量写到主存中取，保证所有线程的可见性；该关键字只能修饰字段；并且不能控制读取变量的顺序，使用的时候需要保证只有单个线程读变量；
2. 共享变量被多个线程竞争
   - 使用synchrinized来锁住字段、方法、类这些代码块；同一时间只能有一个获取到锁的线程访问同步代码，保证了所有的变量都从主存中去读，多有的操作完的变量都写会主存；是重量级锁

### 二、JVM

- #### <img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210516222725077.png" alt="image-20210516222725077" style="zoom:200%;" />

- JVM规范

  #### ![img](https://uploadfiles.nowcoder.com/images/20190314/242025553_1552556718666_11CD8DF4C9693369E94F5F84238EBBC6)

- java平台就是JDK主要分为java虚拟机和API类库；java虚拟机（JVM）分为三大模块：类加载器、运行时数据区、字节码执行引擎

- 简单的来说 java的堆内存分为两块:permantspace（持久带） 和 heap space。

  持久带中主要存放用于存放静态类型数据，如 Java Class, Method 等， 与垃圾收集器要收集的Java对象关系不大。

  而heapspace分为年轻带和年老带 

  年轻代的垃圾回收叫 Young GC， 年老代的垃圾回收叫 Full GC。

  在年轻代中经历了N次（可配置）垃圾回收后仍然存活的对象，就会被复制到年老代中。因此，可以认为年老代中存放的都是一些生命周期较长的对象

  年老代溢出原因有 循环上万次的字符串处理、创建上千万个对象、在一段代码内申请上百M甚至上G的内存，既A B D选项

  持久代溢出原因 动态加载了大量Java类而导致溢出
  
- 方法区是什么？
  方法区是广义上的概念，是一个定义、标准，可以理解为Java中的接口，在Jdk6、7方法区的实现叫永久代；Jdk8之后方法区的实现叫元空间，并从JVM内存中移除，放到了直接内存中；
  方法区是被所有方法线程共享的一块内存区域.

  运行时常量池是什么？
  运行时常量池是每一个类或接口的常量池的运行时表示形式.

  具体体现就是在Java编译后生成的.class文件中，会有class常量池，也就是静态的运行时常量池；
  运行时常量池存放的位置？
  在不同版本的JDK中，运行时常量池所处的位置也不一样.
  JDK1.7及之前方法区位于永久代.由于一些原因在JDK1.8之后彻底祛除了永久代,用元空间代替.所以运行时常量池一直是方法区的一部分。

  运行时常量池存放什么？
  存放编译期生成的各种字面量和符号引用；（字面量和符号引用不懂的同学请自行查阅）
  运行时常量池中包含多种不同的常量，包括编译期就已经明确的数值字面量，也包括到运行期解析后才能够获得的方法或者字段引用。 此时不再是常量池中的符号地址了，这里换为真实地址。

  运行时常量池与字符串常量池？（可能有同学把他俩搞混）
  字符串常量池：在JVM中，为了减少相同的字符串的重复创建，为了达到节省内存的目的。会单独开辟一块内存，用于保存字符串常量，这个内存区域被叫做字符串常量池.

  字符串常量池位置？

  JDK1.6时字符串常量池，被存放在方法区中（永久代），而到了JDK1.7，因为永久代垃圾回收频率低；而字符串使用频率比较高，不能及时回收字符串，会导致导致永久代内存不足，就被移动到了堆内存中


### 三、类加载器

（1）类加载的五个阶段（类的生命周期）：加载、验证、准备、解析、初始化；其中除了解析阶段可能发生在初始化之后，其他四个阶段的顺序都是固定了的；验证、准备和解析阶段合在一起又叫连接；类加载是指类的.class文件以二进制的形式读到内存中去，就是运行时数据区的方法区；然后在堆中创建java.Lang.Class对象用来封装类在方法区的数据结构，给外界提供了使用类的接口

（2）.class文件加载的五种方式：

1. 从本地系统直接加载
2. 从网上联网下载
3. 将源程序动态编译为为.class文件加载
4. 从zip,jar等归档文件中加载.class文件
5. 从专有数据库中提取.class文件

（3）验证阶段；验证的目的是为了保证类加载的正确性以及不会危害JVM，从以下四方面验证

- 验证字节流是否符合Class文件格式的规范；例如：是否以0xCAFEBABE开头、主次版本号是否在当前虚拟机的处理范围之内、常量池中的常量是否有不被支持的类型。
- 元数据验证：对字节码描述的信息进行语义分析（注意：对比javac编译阶段的语义分析），以保证其描述的信息符合Java语言规范的要求；例如：这个类是否有父类，除了java.lang.Object之外。
- 字节码验证：通过数据流和控制流分析，确定程序语义是合法的、符合逻辑的。
- 符号引用验证：确保解析动作能正确执行。

（4）准备阶段；目的是为类的静态变量（static）分配内存，并且初始化为默认值，这时还没有赋值

该阶段有四点需要注意的：

- 对于基本数据类型：如果是类变量（static）和全局变量，该变量被设为该类型的默认零值（如0、0L、null、false等）；而局部变量在使用前必须对其赋值，不然编译不会通过
- 对于同时被static和final修饰的常量：在使用前必须对其赋值，系统不会对其默认赋值；如果对其赋了初值，则static final常量在编译期就将其结果放入了调用它的类的常量池中，就可以直接使用
- 对于引用类型：没设值系统会设置默认值
- 对于数组类型：会根据数组里的元素类型为其设置对应类型的零值

（5）解析阶段；是虚拟机把常量池中类中的符号引用解析为直接引用；主要是七大符号引用：类或接口、字段、类方法、接口方法、方法类型、方法句柄、调用点限定符；

- 符号引用就是用来描述目标的一组符号，可以是任何字面值；
- 直接引用就是直接指向目标的指针、相对偏移量、间接定位到目标的句柄
- 句柄一般指获取一个对象的手段，具体表现形式可以是一个对象、指针、整数等

（6）初始化；为类的静态变量赋予正确的初始值

JVM负责对类进行初始化，主要对类变量进行初始化。在Java中对类变量进行初始值设定有两种方式：

- 声明类变量是指定初始值
- 使用静态代码块为类变量指定初始值

注意事项：

- 装载一个不存在的类的时候，因为采用的双亲加载模式，所以强制加载会直接报错，

  java.lang.SecurityException: Prohibited package name: java.lang

- #### 所有ClassLoader装载的类都来自CLASSPATH环境指定的路径：错误；自定义类加载器实现 继承ClassLoader后重写了findClass方法加载指定路径上的class

- #### 一个类，由不同的类加载器实例加载的话，会在方法区产生两个不同的类，彼此不可见，并且在堆中生成不同Class实例



JVM初始化步骤

1、假如这个类还没有被加载和连接，则程序先加载并连接该类

2、假如该类的直接父类还没有被初始化，则先初始化其直接父类

3、假如类中有初始化语句，则系统依次执行这些初始化语句



类初始化时机

只有当对类的主动使用的时候才会导致类的初始化，类的主动使用包括以下六种：

– 创建类的实例，也就是new的方式

– 访问某个类或接口的静态变量，或者对该静态变量赋值

– 调用类的静态方法

– 反射（如Class.forName(“com.shengsiyuan.Test”)）

– 初始化某个类的子类，则其父类也会被初始化

– Java虚拟机启动时被标明为启动类的类（Java Test），直接使用java.exe命令来运行某个主类



（7）结束生命周期

在如下几种情况下，Java虚拟机将结束生命周期

– 执行了System.exit()方法

– 程序正常执行结束

– 程序在执行过程中遇到了异常或错误而异常终止

– 由于操作系统出现错误而导致Java虚拟机进程终止

（8）类加载器的分类

> ![å¾ç](http://mmbiz.qpic.cn/mmbiz_jpg/eZzl4LXykQxqialtFyQIAA8Mp2SfxSalH4LhZBYWxvUJiaiaTqAibgfFf0JKsLFZRuV59DgfSiazibKZyp4GCVic4GxLQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

- HotSpot虚拟机的启动类加载器（Bootstrap ClassLodaer）是用c语言写的，其他的虚拟机是用java写的；用c语言写的就不能够直接调用；所有java.*的java类都是该类加载器加载的；负责加载存放在JDK\jre\lib
- 扩展类加载器（ExtClassLoader）是加载javax.*的类库；它负责加载DK\jre\lib\ext目录中
- 应用类加载器（system classloader）是加载用户类的；通过ClassLoader.getSystemClassLoader()来获取它，它负责加载用户类路径（ClassPath）CLASSPATH/-Djava.class.path所指定的类和jar包的装入工作
- 上图中的箭头继承关系，是组合关系，通过getParent()方法获取上级类加载器；通过开发人员可以通过继承java.lang.ClassLoader类的方式实现自己的类加载器，从而进行动态加载class文件，以满足一些特殊的需求，这体现java动态实时类装入特性

（9）类加载的三种方式

- Class.forName("类名")：加载类到JVM中并执行类的静态代码块和静态变量
- ClassLorder.load(“类名”)：只把加载类到JVM中；Class.forName(name, initialize, loader)带参函数也可控制是否加载static块
- 在命令行启动应用时由JVM初始化加载

示例：

```java
public class loaderTest { 

        public static void main(String[] args) throws ClassNotFoundException { 

                ClassLoader loader = HelloWorld.class.getClassLoader(); 

                System.out.println(loader); 

                //使用ClassLoader.loadClass()来加载类，不会执行初始化块 

                loader.loadClass("Test2"); 

                //使用Class.forName()来加载类，默认会执行初始化块 

                //Class.forName("Test2"); 

                //使用Class.forName()来加载类，并指定ClassLoader，初始化时不执行静态块 

                //Class.forName("Test2", false, loader); 

        } 
}
```

（9）双亲委派机制

（双亲委派模式是在Java 1.2后引入的，其工作原理的是，如果一个类加载器收到了类加载请求，它并不会自己先去加载，而是把这个请求委托给父类的加载器去执行，如果父类加载器还存在其父类加载器，则进一步向上委托，依次递归，请求最终将到达顶层的启动类加载器，如果父类加载器可以完成类加载任务，就成功返回，倘若父类加载器无法完成此加载任务，子加载器才会尝试自己去加载，这就是双亲委派模式）

工作流程：当要加载一个类的时候，首先会去寻找它的父加载器所管目录是否有该类的.class文件，如果还没有就会继续找父父加载器，如果不存在父父加载器，则会检查是否是启动类加载器加载的类；如果都不是，则会抛出异常，异常的代码块里面才调用自身的加载功能

ClassLoader源码：

```java
public Class<?> loadClass(String name)throws ClassNotFoundException {
            return loadClass(name, false);
    }
    protected synchronized Class<?> loadClass(String name, boolean resolve)throws ClassNotFoundException {

            // 首先判断该类型是否已经被加载

            Class c = findLoadedClass(name);

            if (c == null) {

                //如果没有被加载，就委托给父类加载或者委派给启动类加载器加载

                try {

                    if (parent != null) {

                         //如果存在父类加载器，就委派给父类加载器加载

                        c = parent.loadClass(name, false);

                    } else {

                    //如果不存在父类加载器，就检查是否是由启动类加载器加载的类，通过调用本地方法native Class findBootstrapClass(String name)

                        c = findBootstrapClass0(name);

                    }

                } catch (ClassNotFoundException e) {

                 // 如果父类加载器和启动类加载器都不能完成加载任务，才调用自身的加载功能
                    c = findClass(name);

                }

            }

            if (resolve) {

                resolveClass(c);

            }

            return c;

        }
```

双亲委派模型意义：

- 系统类防止内存中出现多份同样的字节码

- 保证Java程序安全稳定运行

（10）需要自定义类加载器的情况和操作

通常情况下，我们都是直接使用系统类加载器。但是，有的时候，我们也需要自定义类加载器。比如应用是通过网络来传输 Java 类的字节码，为保证安全性，这些字节码经过了加密处理，这时系统类加载器就无法对其进行加载，这样则需要自定义类加载器来实现。自定义类加载器一般都是继承自 ClassLoader 类，从上面对 loadClass 方法来分析来看，我们只需要重写 findClass 方法即可

### 四、垃圾回收机制

> https://www.jianshu.com/p/5261a62e4d29

（1）根搜索算法

- 标记阶段：从各个GCRoots对象开始遍历根对象引用的其他遍历，比如实例变量；遍历到的可达的对象标记为存活，GC判断对象是否可达看的是强引用；没有遍历到的对象就是垃圾对象，会在接下来的阶段在做判断筛选之后清理垃圾对象
  - 强引用；比如Object obj=new Object（）；这样直接new出来的就是强引用
  - 弱引用；引用的值被修改过
- 垃圾回收回收的是垃圾对象的内存，内存被回收释放之后对象也就没有了；垃圾收集器主要活动的区域就是堆中，因为new的对象都是在堆中；
- 可以作为GCRoots根对象的有：
  1. 虚拟机栈帧中的引用对象（线程栈中的本地变量表）
  2. 方法区中的常量引用的对象
  3. 方法区中类静态属性引用的对象
  4. 本地方法栈（native）中引用的对象
  5. 活跃线程
- 标记阶段的特点
  - 在标记之前会暂停应用线程，如果线程在运行的话，检测到的对象都是变化的；这时叫安全点（safe point），这会触发一次Stop The World（STW）暂停
  - 暂停的时间（标记的时间）是由当前存活对象的数量决定的，并不取决于堆内对象的多少和堆的大小
  - 要真正宣告一个对象的死亡，至少要经历两次标记过程

（2）Tracing（标记-清除算法算法）

> ![img](https://upload-images.jianshu.io/upload_images/3789193-05ac8d99f632c6c7.png?imageMogr2/auto-orient/strip|imageView2/2/w/485/format/webp)

- 分为标记和清除两个阶段，标记阶段就是使用根搜索算法的过程，标记的存活对象；清除的是未标记的对象
- 优点：不需要进行对象的移动；特点是仅对未标记的对象清理，在存活对象较多的时候极为高效
- 缺点：标记和清除的效率都不高；
  - 因为需要用一个空闲表来记录空闲区域以及大小，对空闲表的管理降低了效率
  - 清除垃圾对象之后会让整个内存区域产生不连续的内存碎片

（3）Compacting（标记-整理算法）

> ![img](https://upload-images.jianshu.io/upload_images/3789193-27c645c7700f687b.png?imageMogr2/auto-orient/strip|imageView2/2/w/511/format/webp)

- 大体过程还是标记-清除；只是在清理垃圾之前会把存活的内存向一端移动，然后让除了这些对象之外的内存都清理掉；使用该算法一般增加句柄和句柄表
- 优点：
  - 经过整理之后，新对象的分配只需要通过指针碰撞便能完成（Pointer Bumping），相当简单
  - 解决了内存碎片的问题
- 缺点：GC暂停的时间较长，因为需要把所有对象都拷贝到一个地方，还需要更新他们的引用地址

（4）Copying（复制算法）

> ![img](https://upload-images.jianshu.io/upload_images/3789193-f3564647800ab93c.png?imageMogr2/auto-orient/strip|imageView2/2/w/490/format/webp)

- 该算法是为了克服句柄的开销和解决内存碎片的问题；把堆内存分为两块，先只用一块，这块用完了就把这块的存活对象那个复制到另一块上去，再把该块的内存全部清理掉；复制算法适用于新生代（短生存周期的对象），老年代（长生存周期的对象）中的对象存活周期长，就会执行较多的复制操作，效率会变低；老年代都是使用的标记-整理算法

- 优点：
  - 标记阶段和复制阶段可以同时进行
  - 每次只对一块内存进行回收，运行高效
  - 只需按照栈顶指针按序分配内存，操作简单
  - 没有内存碎片（前提是存活对象的内存空间之间没有间隔）
  
- 缺点：算法的运行过程始终需要一半空闲的内存空间，因此可分配的内存空间缩小一半

  

#### 垃圾收集器

- CMS

  CMS全称 **Concurrent Mark Sweep**，是一款并发的、使用标记-清除算法的垃圾回收器，以牺牲吞吐量为代价来获得最短回收停顿时间的垃圾回收器，对于要求服务器响应速度的应用上，这种垃圾回收器非常适合。

  CMS的基础算法是：标记—清除。

  它的过程可以分为以下6个步骤：

  1. 初始标记(STW initial mark)：没用户线程参与
  2. 并发标记(Concurrent marking)
  3. 并发预清理(Concurrent precleaning)
  4. 重新标记(STW remark)：没用户线程参与
  5. 并发清理(Concurrent sweeping)
  6. 并发重置(Concurrent reset)

  ![img](https://uploadfiles.nowcoder.com/images/20200502/9374535_1588428365082_2D3DED829E7D27716B630299E48DDD87)

  1. **初始标记：**在这个阶段，需要虚拟机停顿正在执行的任务，官方的叫法STW(Stop The Word)。这个过程从垃圾回收的"根对象"开始，只扫描到能够和"根对象"直接关联的对象，并作标记。所以**这个过程虽然暂停了整个JVM，但是很快就完成了**。
  2. **并发标记：**这个阶段紧随初始标记阶段，在初始标记的基础上继续向下追溯标记。并发标记阶段，***应用程序的线程和并发标记的线程并发执行，所以\******用户不会感受到停顿\******。\***
  3. **并发预清理**：并发预清理阶段仍然是并发的。在这个阶段，虚拟机查找在执行并发标记阶段新进入老年代的对象(可能会有一些对象从新生代晋升到老年代， 或者有一些对象被分配到老年代)。通过重新扫描，减少下一个阶段"重新标记"的工作，因为下一个阶段会Stop The World。
  4. **重新标记**：这个阶段会暂停虚拟机，收集器线程扫描在CMS堆中剩余的对象。扫描从"根对象"开始向下追溯，并处理对象关联。
  5. **并发清理：**清理垃圾对象，***这个阶段收集器线程和应用程序线程并发执行\***。
  6. **并发重置：**这个阶段，重置CMS收集器的数据结构，等待下一次垃圾回收。

  ​    CMS不会整理、压缩堆空间，这样就带来一个问题：经过CMS收集的堆会产生空间碎片，CMS不对堆空间整理压缩节约了垃圾回收的停顿时间，但也带来的堆空间的浪费。

  ​    为了解决堆空间浪费问题，CMS回收器不再采用简单的指针指向一块可用堆空 间来为下次对象分配使用。；而是把一些未分配的空间汇总成一个列表，当JVM分配对象空间的时候，会搜索这个列表找到足够大的空间来hold住这个对象。

  ​    从上面的图可以看到，为了让应用程序不停顿，CMS线程和应用程序线程并发执行，这样就需要有更多的CPU，单纯靠线程切 换是不靠谱的。并且，重新标记阶段，为空保证STW快速完成，也要用到更多的甚至所有的CPU资源。

### 五、Java锁

#### synchronized锁

- synchronized关键字是用来实现在多线程环境下，字段、方法的同步操作，实现原子性

- 使用的位置不一样，锁住的对象也不一样

  - 使用在静态方法上时，锁住的是class对象
  - 使用在普通方法上时，锁住的是类对象（this）
  - 使用在方法块上时，锁住的是synchronized(Object)里的object对象

- synchronized同步的原理；每个对象都有一个monitor与之关联，JVM基于进入Monitor对象和退出Monitor来实现方法同步和代码块同步，在同步代码的开始执行monitor enter指令，线程就会尝试获取该对象的monitor，在同步代码结束处或者异常处执行monitor exit指令，释放对该对象的monitor的所有权，即释放锁

- 锁的存储结构

  - 对象头：Java的每一个对象都可以作为锁，就是因为每个对象都要对象头，对象头里面就存储有锁信息和其他信息；非数组对象头为两个字宽（Word），数组对象头为三个字宽，32位JVM中一字宽为4个字节，64位JVM中一字宽为8字节

    | 长度     | 内容                   | 说明                                 |
    | -------- | ---------------------- | ------------------------------------ |
    | 32/64bit | Mark Word              | 存储对象的hashcode或锁信息           |
    | 32/64bit | Class Metadata Address | 存储到JVM方法区的class类的引用地址   |
    | 32/32bit | Array length           | 数组的长度（如果当前对象是数组的话） |

    - 32位JVM的对象头结构

      ![img](https://img2018.cnblogs.com/blog/597777/201907/597777-20190707103937984-1421540495.png)

    - 64位JVM的对象头结构

      ![img](https://img2018.cnblogs.com/blog/597777/201907/597777-20190707104505038-1623488275.png)

    - 运行期间对象头的变化时的结构

      ![img](https://img2018.cnblogs.com/blog/597777/201907/597777-20190707104231154-1431922531.png)

    - 上图名称的解释

      - 无锁状态：当线程处于不活动状态，比如wait阻塞状态，那么就会把同步代码锁对象的对象头设置为无锁状态
      - 对象分代年龄：每个对象都是存储在堆中的，对象每次在年轻代中被MonitorGC垃圾回收线程执行回收扫描一次，分代年龄就会加1，加到15后就会被放在老年代存储区中
      - 栈中的锁记录：Monitor 是线程私有的数据结构，每一个线程都有一个可用 monitor record 列表，同时还有一个全局的可用列表。每一个被锁住的对象都会和一个 monitor 关联，同时 monitor 中有一个 Owner 字段存放拥有该锁的线程的唯一标识，表示该锁被这个线程占用
      - 重量级锁：指向互斥量的指针，依赖于操作系统 Mutex Lock 所实现的锁我们称之为重量级锁；竞争重量级锁的线程会处于阻塞状态，竞争到重量级锁之后会用CAS操作修改对象头的锁标志位，但是不会修改对象头的线程id；线程的切换是在用户态和核心态之间切换，是很耗费时间的
      - 偏向锁：指的是在对象头中存储经常访问该对象的线程id值，避免了之后该线程进出同步代码块时都要用CAS操作来加锁和解锁；JDK 1.6中默认是开启偏向锁和轻量级锁的，我们也可以通过-XX:-UseBiasedLocking=false来禁用偏向锁
      - 轻量级锁：又叫自旋锁，当多个线程竞争一个偏向锁时，偏向锁就会升级为轻量级锁；或者通过配置JVM参数来禁用了偏向锁，那么就使用的是轻量级锁；多个线程竞争一个偏向锁时，线程就会在CPU里面自旋来争取锁，争取到锁后会把锁的持有者改为该线程的id，这样提高了响应速度，但是降低了吞吐量；当有线程的自旋次数超过了10次，就会升级为重量级锁

- 锁的升级过程

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200603161323889.png)

  ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200606123648335.png)

- 锁的比较

  | 锁       | 优点                                                         | 缺点                                           | 适用场景                           |
  | -------- | ------------------------------------------------------------ | ---------------------------------------------- | ---------------------------------- |
  | 偏向锁   | 加锁和解锁不需要额外的消耗，和执行非同步方法相比仅存在纳秒级的差距 | 如果线程间存在锁竞争，会带来额外的锁撤销的消耗 | 适用于只有一个线程访问同步块场景   |
  | 轻量级锁 | 竞争的线程不会阻塞，提高了程序的响应速度                     | 如果始终得不到索竞争的线程，使用自旋会消耗CPU  | 追求响应速度，同步块执行速度非常快 |
  | 重量级锁 | 线程竞争不使用自旋，不会消耗CPU                              | 线程阻塞，响应时间缓慢                         | 追求吞吐量，同步块执行速度较慢     |

#### Lock锁

##### Lock接口的方法

- void lock()：获取锁
- boolean tryLock()：尝试非阻塞的获取锁
- void unLock()：释放锁
- boolean tryLock(long time,TimeUnit timeUnit) Throws InterruptedException：超时的获取锁
- Condition newCondition()：创建一个condition对象，Condition是个等待通知组件，里面有配合Lock的线程控制的方法
- void lockInterruptibly() Throws InterruptedException：在获取锁的时候可以被中断，也是获取锁

##### ReetrantLock可重入锁

- 可重入锁：同一个线程可以多次获取对象的锁而不会发送阻塞，synchronized和ReetrantLock都是可重入锁；Mutex就不是可重入锁，同一个线程获取锁之后，第二次在尝试获取锁时就会阻塞

- ReetrantLock是最常用的Lock接口的实现类

  ![image-20210429143128363](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210429143128363.png)

##### 公平锁与非公平锁

- 在获取锁的顺序上涉及到公不公平，如果是公平的话，那么先请求获取锁的线程先获得锁

- 公平的获取锁，就是在CAS操作之前，判断hasQueuedPrecessors()方法是否为假，就说明没有线程比当前线程更早的请求获取锁，那么就可以用CAS操作来修改锁状态，把当前线程设为锁的占有线程

  ```java
   protected final boolean tryAcquire(int acquires) {
              final Thread current = Thread.currentThread();
              int c = getState();
              if (c == 0) {
                  if (!hasQueuedPredecessors() &&
                      compareAndSetState(0, acquires)) {
                      setExclusiveOwnerThread(current);
                      return true;
                  }
              }
              else if (current == getExclusiveOwnerThread()) {
                  int nextc = c + acquires;
                  if (nextc < 0)
                      throw new Error("Maximum lock count exceeded");
                  setState(nextc);
                  return true;
              }
              return false;
          }
  ```

- 不公平的获取锁，ReetrantLock的tryLock()方法默认使用的就是不公平方式，这种方式相比公平式的获取锁线程切换的次数更少，数据量在10万次时，比公平式的获取锁快94倍，吞吐量大，但是可能会造成饥饿现象

  ```java
   final boolean nonfairTryAcquire(int acquires) {
              final Thread current = Thread.currentThread();
              int c = getState();
              if (c == 0) {
                  if (compareAndSetState(0, acquires)) {
                      setExclusiveOwnerThread(current);
                      return true;
                  }
              }
              else if (current == getExclusiveOwnerThread()) {
                  int nextc = c + acquires;
                  if (nextc < 0) // overflow
                      throw new Error("Maximum lock count exceeded");
                  setState(nextc);
                  return true;
              }
              return false;
          }
  ```

##### 读写锁

![image-20210429143448570](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210429143448570.png)

- JDK的并发包提供了ReetrantReadWriteLock读写锁，每个对象都有个读锁和写锁，目的是提高并发，支持选择公平性，并且属于可重入锁、写锁可以降为读锁；Mutex和ReetrantLock都是排他锁，指同一时刻只允许一个线程进行访问，而读写锁允许同一时刻多个读线程同时访问

- 通过readLock()、writeLock()来分别获取读锁和写锁；可以使用ReetrantReadWriteLock结合HashMap来实现一个线程安全的缓存Cache

  ```java
  	static Map<String key,Object value> map=new HashMap<String key,Object value>();
  	static ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
  	static	Lock readLock= lock.readLock();
  	static	Lock writeLock=lock.writeLock();
  	public static final Object get(String key){
  		readLock.lock();
  		try{
  			return map.get(key);	
  		}finally{
  			readLock.unlock();
  		}
  	}
  	public static final void put(String key,Object value){
  		writeLock.lock();
  		try{
  			map.put(key, value);
  		}finally{
  			writeLock.unlock();
  		}
  	}
  ```

- 读锁和写锁都是可重入的，写锁降级为读锁，先获取写锁，在获取读锁，在释放写锁，这时执行一些操作，最后释放读锁；

#### volatile

- Java 语言中的 volatile 变量可以被看作是一种 “程度较轻的 synchronized”；与synchronized 块相比，volatile 变量所需的编码较少，并且运行时开销也较少，但是它所能实现的功能也仅是 synchronized 的一部分。本文介绍了几种有效使用 volatile 变量的模式，并强调了几种不适合使用 volatile 变量的情形。

  锁提供了两种主要特性：*互斥（mutual exclusion）* 和*可见性（visibility）*。互斥即一次只允许一个线程持有某个特定的锁，因此可使用该特性实现对共享数据的协调访问协议，这样，一次就只有一个线程能够使用该共享数据。可见性要更加复杂一些，它必须确保释放锁之前对共享数据做出的更改对于随后获得该锁的另一个线程是可见的 —— 如果没有同步机制提供的这种可见性保证，线程看到的共享变量可能是修改前的值或不一致的值，这将引发许多严重问题。

  Volatile 变量

  Volatile 变量具有 synchronized 的可见性特性，但是不具备原子特性。这就是说线程能够自动发现 volatile 变量的最新值。Volatile 变量可用于提供线程安全，但是只能应用于非常有限的一组用例：多个变量之间或者某个变量的当前值与修改后值之间没有约束。因此，单独使用 volatile 还不足以实现计数器、互斥锁或任何具有与多个变量相关的不变式（Invariants）的类（例如 “start <=end”）。

  出于简易性或可伸缩性的考虑，您可能倾向于使用 volatile 变量而不是锁。当使用 volatile 变量而非锁时，某些习惯用法（idiom）更加易于编码和阅读。此外，volatile 变量不会像锁那样造成线程阻塞，因此也很少造成可伸缩性问题。在某些情况下，如果读操作远远大于写操作，volatile 变量还可以提供优于锁的性能优势。

  正确使用 volatile 变量的条件

  您只能在有限的一些情形下使用 volatile 变量替代锁。要使 volatile 变量提供理想的线程安全，必须同时满足下面两个条件：

  - 对变量的写操作不依赖于当前值。
  - 该变量没有包含在具有其他变量的不变式中。

  

  实际上，这些条件表明，可以被写入 volatile 变量的这些有效值独立于任何程序的状态，包括变量的当前状态。

  第一个条件的限制使 volatile 变量不能用作线程安全计数器。虽然增量操作（x++）看上去类似一个单独操作，实际上它是一个由读取－修改－写入操作序列组成的组合操作，必须以原子方式执行，而 volatile 不能提供必须的原子特性。实现正确的操作需要使x 的值在操作期间保持不变，而 volatile 变量无法实现这点。（然而，如果将值调整为只从单个线程写入，那么可以忽略第一个条件。）

  大多数编程情形都会与这两个条件的其中之一冲突，使得 volatile 变量不能像 synchronized 那样普遍适用于实现线程安全。

#### Condition

- Condition newCondition()：创建Condition对象需要调用Lock的实例方法newConditon()
- void await() throws InterruptedException：将该线程放在这个条件的等待集中
- void signalAll()：解除该等待集中所有线程的阻塞状态
- void signal()：从该条件中随机选择一个线程，解除其阻塞状态

#### final

- 使用final关键字也可以安全的访问一个共享字段



### 六、synchronized与volatile的区别

- #### 修饰的目标不同

  - #### synchonized可以修饰类、方法、代码块，但是不能修饰变量；volatile只能修饰变量

