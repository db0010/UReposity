### 一、面向对象

#### 访问控制修饰符

- **default** (即默认，什么也不写）: 在同一包内可见，不使用任何修饰符。使用对象：类、接口、变量、方法。

- **private** : 在同一类内可见。使用对象：变量、方法。 注意：不能修饰类（外部类）

- **public** : 对所有类可见。使用对象：类、接口、变量、方法

- **protected** : 对同一包内的类和所有子类可见。使用对象：变量、方法。 注意：不能修饰类（外部类）

- ### synchronized 修饰符

  synchronized 关键字声明的方法同一时间只能被一个线程访问。synchronized 修饰符可以应用于四个访问修饰符

  ```java
  public synchronized void showDetails(){
  }
  ```

- transient 修饰符:序列化的对象包含被 transient 修饰的实例变量时，java 虚拟机(JVM)跳过该特定的变量；该修饰符包含在定义变量的语句中，用来预处理类和变量的数据类型

  ```java
  public transient int limit = 55;   // 不会持久化
  public int b; // 持久化
  ```

- volatile修饰符：volatile 修饰的成员变量在每次被线程访问时，都强制从共享内存中重新读取该成员变量的值。而且，当成员变量发生变化时，会强制线程将变化值回写到共享内存。这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值

  ```java
  private volatile boolean active;
  ```

#### 方法参数

- 方法不能修改基本数据类型的参数（即数值型个布尔型）
- 方法可以改变对象参数的状态
- 方法参数为对象引用，引用的交换不能实现方法外边的对象的交换
- 在方法中的局部变量必须明确的初始化，但是类中的成员变量没有初始化会有对应类型的默认值
- 方法里的变量为局部变量，不能用static修饰

#### 构造器

- 如果类中没有构造器，那么默认会有一个无参构造器；但是如果设置了有参构造器，但是没有手动设置无参构造器，那么就不会自动创建无参构造器
- this(String str,double d)该语句表示调用该类的其他构造器，该语句需要放在方法的第一行

#### 初始化块

- 初始化数据字段的方法
  - 在构造器中设置值
  - 在声明中赋值
  - 在初始化块中赋值，只要构造这个类的对象，那么这些代码块就会按顺序依次执行；先允许初始化块，再允许构造器
- 静态代码块：如果类的静态字段需要很复杂的初始化代码块，那么可以使用静态的代码块；在类第一次加载的时候，将会进行静态字段的初始化，静态字段也会有默认值

#### 包

- 有一个静态导入包，导入的是包里面的静态方法和静态字段，下面的代码使用时就不用加上类名

  ```java
  Math.sqrt(Math.pow(x,2)+Math.pow(y,2)
  //sqrt(pow(x,2)+pow(y,2))
  ```

#### java编译器与解释器

- javac.exe是java编译器；java.exe是解释器

  ```java
  //命令行命令
  //编译器前的目录用/分隔
  javac com/mycompany/Test.java
  //解释器中的目录用.分隔
  java com.mycompany.Test.java
  ```


#### 散列码与equals

- equals方法的设计规则

  - 自反性：x.equasl(x)应返回true
  - 对称性：x.equals(y)==y.equals(x)
  - 传递性：x.equals(y),y.equals(s),那么x.equals(s)为相同结果
  - 一致性：如果x,y引用的对象没有发生变化，那反复调用x.equals(y)应该都是相同的结果
  - 对于非空引用x，x.equals(null)应该返回null

- 散列码

  - 要把类的所有字段的hashcode组合为散列码，其中引用类型用Objects.hashCode(Object object)来返回一个整型值，如果object为null，那么返回0，否则返回object.hashCode()；基本数据类型可用封装类型的hashCode方法

    ```java
    public int hashCode(){
        return 7*Objects.hashCode(name)+11*Double.hashCode(salary)+13*Objects.hashCode(hireDay);
    }
    ```

  - 还可以使用Objects类的hash(Object... objects)方法

    ```java
    pubulic int hashCode(){
        retrn Objects.hash(name,salary,hireDay);
    }
    ```

  - 对于数组可使用Arrays.hashCode(xxx[] a)方法，来获取数组的散列码

#### toString()方法

- 一般格式：类名+[里面是各个字段对应的值]

  ```java
  public String toString(){
  	return getClass.getName()
          +"[name="+name
          +",salary="+salary
          +",hireDay="+hireDay
          +"]";
  }
  ```

- 子类需要重写父类的toString()方法，可以通过super.toString()方法减少代码的重写，在加上子类独有的字段就行了

- 当使用""+person时，就会自动调用Person类的toString()方法

#### supper

- 父类没有无参的构造函数，所以子类需要在自己的构造函数中显式调用父类的构造函数，使用supper();

#### 重载与重写

- java多态有两种情况：重载和覆写

  在覆写中，运用的是动态单分配，是根据new的类型确定对象，从而确定调用的方法；

  在重载中，运用的是静态多分派，即根据静态类型确定对象，因此不是根据new的类型确定调用的方法

#### switch语句

- 在switch（expr1）中，expr1只能是一个**整数表达式或者枚举常量（更大字体）**，整数表达式可以是int基本类型或Integer包装类型，由于，byte,short,char都可以隐含转换为int，所以，这些类型以及这些类型的包装类型也是可以的。显然，long、float、double类型不符合switch的语法规定，并且不能被隐式转换成int类型，所以，它们不能作用于swtich语句中。
  注意：String类型是Java7开始支持的

#### webService

- Web service顾名思义是基于web的服务，它是一种跨平台，跨语言的服务。

  我们可以这样理解它，比如说我们可以调用互联网上查询天气信息的web服务，把它嵌入到我们的B/S程序中，当用户从我们的网点看到天气信息时，会认为我们为他提供很多的服务，但其实我们什么也没做，只是简单的调用了一下服务器上的一端代码而已。Web service 可以将你的服务发布到互联网上让别人去调用，也可以调用别人发布的web service，和使用自己的代码一样。

  它是采用XML传输格式化的数据，它的通信协议是SOAP(简单对象访问协议).

#### 面向对象设计基本原则

- 五个基本原则： 
  单一职责原则（Single-Resposibility Principle）：一个类，最好只做一件事，只有一个引起它的变化。单一职责原则可以看做是低耦合、高内聚在面向对象原则上的引申，将职责定义为引起变化的原因，以提高内聚性来减少引起变化的原因。 
  开放封闭原则（Open-Closed principle）：软件实体应该是可扩展的，而不可修改的。也就是，对扩展开放，对修改封闭的。 
  Liskov替换原则（Liskov-Substituion Principle）：子类必须能够替换其基类。这一思想体现为对继承机制的约束规范，只有子类能够替换基类时，才能保证系统在运行期内识别子类，这是保证继承复用的基础。 
  依赖倒置原则（Dependecy-Inversion Principle）：依赖于抽象。具体而言就是高层模块不依赖于底层模块，二者都同依赖于抽象；抽象不依赖于具体，具体依赖于抽象。 
  接口隔离原则（Interface-Segregation Principle）：使用多个小的专门的接口，而不要使用一个大的总接口

#### 类初始化流程

- 初始化过程是这样的： 

  1.首先，初始化父类中的静态成员变量和静态代码块，按照在程序中出现的顺序初始化； 

  2.然后，初始化子类中的静态成员变量和静态代码块，按照在程序中出现的顺序初始化； 

  3.其次，初始化父类的普通成员变量和代码块，在执行父类的构造方法；

  4.最后，初始化子类的普通成员变量和代码块，在执行子类的构造方法；
  
- 注意点

  - #### 当调用父类的成员方法时，如果子类重写了父类的该方法，那么会调用子类的方法，而不是父类的，有可能这时子类的成员变量还没有加载，为null

#### JDK的一些工具命令

- 1、jps：查看本机java进程信息。

  2、jstack：打印线程的**栈**信息，制作线程dump文件。

  3、jmap：打印内存映射，制作**堆**dump文件

  4、jstat：性能监控工具

  5、jhat：内存分析工具

  6、jconsole：简易的可视化控制台

  7、jvisualvm：功能强大的控制台
  
  java.exe是java虚拟机
  
  javadoc.exe用来制作java文档
  
  jdb.exe是java的调试器
  
  javaprof.exe是剖析工具
  
  javap -c -v Bank会对Bank.class文件经行反编译

### 二、抽象类

- 抽象类不能被实例化(初学者很容易犯的错)，如果被实例化，就会报错，编译无法通过。只有抽象类的非抽象子类可以创建对象。
-  抽象类中不一定包含抽象方法，但是有抽象方法的类必定是抽象类。
- 抽象类中的抽象方法只是声明，不包含方法体，就是不给出方法的具体实现也就是方法的具体功能。
- 构造方法，类方法（用 static 修饰的方法）不能声明为抽象方法；在main方法中无法调用父类的抽象方法，因为main方法为static方法，只能使用静态的属性和方法
-  抽象类的子类必须给出抽象类中的抽象方法的具体实现，除非该子类也是抽象类

### 三、接口

- ### 接口特性

  - ### 接口中每一个方法也是隐式抽象的,接口中的方法会被隐式的指定为 **public abstract**（只能是 public abstract，其他修饰符都会报错）。

  - ### 接口中可以含有变量，但是接口中的变量会被隐式的指定为 **public static final** 变量（并且只能是 public，用 private 修饰会报编译错误）。

  - ### 接口中的方法是不能在接口中实现的，只能由实现接口的类来实现接口中的方法。

  ### 抽象类和接口的区别

  - 抽象类：在Java中被abstract关键字修饰的类称为抽象类，被abstract关键字修饰的方法称为抽象方法，抽象方法只有方法的声明，没有方法体。抽象类的特点：

    a、抽象类不能被实例化只能被继承；

    b、包含抽象方法的一定是抽象类，但是抽象类不一定含有抽象方法；

    c、抽象类中的抽象方法的修饰符只能为public或者protected，默认为public；

    d、一个子类继承一个抽象类，则子类必须实现父类抽象方法，否则子类也必须定义为抽象类；

    e、抽象类可以包含属性、方法、构造方法，但是构造方法不能用于实例化，主要用途是被子类调用。

    *接口*：Java中接口使用interface关键字修饰，特点为:

    a、接口可以包含变量、方法；变量被隐士指定为public static final，方法被隐士指定为public abstract（JDK1.8之前）；

    b、接口支持多继承，即一个接口可以extends多个接口，间接的解决了Java中类的单继承问题；

    c、一个类可以实现多个接口；

    d、JDK1.8中对接口增加了新的特性：

    ​    （1）、默认方法（default method）：JDK 1.8允许给接口添加非抽象的方法实现，但必须使用default关键字修饰；定义了default的方法可以不被实现子类所实现，但只能被实现子类的对象调用；如果子类实现了多个接口，并且这些接口包含一样的默认方法，则子类必须重写默认方法；

    ​    （2）、静态方法（static method）：JDK 1.8中允许使用static关键字修饰一个方法，并提供实现，称为接口静态方法。接口静态方法只能通过接口调用（接口名.静态方法名）。

    ​    注意：**jdk1.9是允许接口中出现private修饰的默认方法和静态方法**。

  - 

  - ### 抽象类中的方法可以有方法体，就是能实现方法的具体功能，但是接口中的方法不行。

  - ###  抽象类中的成员变量可以是各种类型的，而接口中的成员变量只能是 **public static final** 类型的。 

  - ### 接口中不能含有静态代码块以及静态方法(用 static 修饰的方法)，而抽象类是可以有静态代码块和静态方法。

  - ### 一个类只能继承一个抽象类，而一个类却可以实现多个接口。
  
- 函数式接口

  - 对于只有一个抽象方法（JDK1.8之后接口可以有私有方法）的接口，就叫做函数式接口，是用于提供lambda表达式的

- lambda表达式

  - 参数，（参数）->{代码块}；大概意思是把lambda表达式的值赋值给原本对象

  - 注意事项

    - 代码块里面的返回范围是完整的；例如下面只有return -1的情况的话，就是不合法的

      ```java
      (String first,String second)->{
          if(first.lemgth()<second.length()) return -1;
          else if(first.length()>second.length()) return 1;
          else return 0;
      }
      ```

    - lambda表达式即使没有参数，任然要提供空括号

      ```java
      ()->{for(int i=100;i>=0;i--) System.out.println(i);}
      ```

    - 如果可以推导出lambda表达式的参数类型，那么可以省略类型

      ```java
      Comparator comp=(first,second)->first.length()-second.length();
      ```

    - 如果参数只有一个，并且可以推断出参数类型，那么还可以省略小括号

  - 例子

    ```java
       String[] planets=new String[]{"Mercury","Tom","Earth","Mars","Jump","Saturn","Nepture"};
            System.out.println(Arrays.toString(planets));
            System.out.println("根据字典排序：");
            Arrays.sort(planets);
            System.out.println(Arrays.toString(planets));
            System.out.println("根据长度排序：");
    Arrays.sort(planets,(String first,String second)->first.length()-second.length());
            System.out.println(Arrays.toString(planets));
    ```

    ![image-20210520232347824](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210520232347824.png)

### 四、枚举

```java
//枚举的定义在类外面
enum Color 
{ 
    //枚举值，每个枚举值就是Color的一个实例化对象
    RED, GREEN, BLUE; //省略了public static final，跟接口的变量一样
     // 构造函数，当调用一个枚举值时，有多少个枚举值，就会调用多少次构造函数
    private Color() 
    { 
        System.out.println("Constructor called for : " + 			      this.toString()); 
    } 
  
    public void colorInfo() 
    { 
        System.out.println("Universal Color"); 
    } 
} 
  
public class Test 
{ 
    public static void main(String[] args) 
    { 
        // 调用 values() 
        Color[] arr = Color.values(); 
        // 迭代枚举
        for (Color col : arr) 
        { 
            // 查看索引
            System.out.println(col + " at index " + col.ordinal()); 
        } 
  
        // 使用 valueOf() 返回枚举常量，不存在的会报错IllegalArgumentException 
        System.out.println(Color.valueOf("RED")); 
        // System.out.println(Color.valueOf("WHITE")); 
    } 
} 
```

```Java
输出结果
Constructor called for : RED
Constructor called for : GREEN
Constructor called for : BLUE
RED at index 0
GREEN at index 1
BLUE at index 2
RED
```

- values() 返回枚举类中所有的值。
- ordinal()方法可以找到每个枚举常量的索引，就像数组索引一样。
- valueOf()方法返回指定字符串值的枚举常量
- 所有的枚举值都是 public static final 的

```java
enum Color{
    RED{
        public String getColor(){//枚举对象实现抽象方法
            return "红色";
        }
    },
    GREEN{
        public String getColor(){//枚举对象实现抽象方法
            return "绿色";
        }
    },
    BLUE{
        public String getColor(){//枚举对象实现抽象方法
            return "蓝色";
        }
    };
    public abstract String getColor();//定义抽象方法
}
```

- 枚举中定义了抽象方法之后，需要为每个枚举对象都实现抽象方法

### 五、异常处理

> <img src="http://uploadfiles.nowcoder.com/images/20151010/214250_1444467985224_6A144C1382BBEF1BE30E9B91BC2973C8" alt="img" style="zoom:50%;" />

- 粉红色的是受检查的异常(checked exceptions),其必须被 try{}catch语句块所捕获,或者在方法签名里通过throws子句声明.受检查的异常必须在编译时被捕捉处理,命名为 Checked Exception 是因为Java编译器要进行检查,Java虚拟机也要进行检查,以确保这个规则得到遵守；RuntimeException和error都是非检查型异常

  2. 绿色的异常是运行时异常(runtime exceptions),需要程序员自己分析代码决定是否捕获和处理,比如 空指针,被0除...

  3. 而声明为Error的，则属于严重错误，如系统崩溃、虚拟机错误、动态链接失败等，这些错误无法恢复或者不可能捕捉，将导致应用程序中断，Error不需要捕捉。

- 1、throws出现在方法头，throw出现在方法体 2、throws表示出现异常的一种可能性，并不一定会发生异常；throw则是抛出了异常，执行throw则一定抛出了某种异常。

- Exception中除了RuntimeException及其子类，其他的异常都需要捕获

- 子类中声明的检查型异常不能比父类声明的异常更通用；父类方法没有抛出任何检查型异常时，子类也不能抛出任何检查型异常

- 捕获到异常并再次抛出时，一定要留住原始异常，否则很难定位第一案发现场

  ```java
  public class Main {
      public static void main(String[] args) {
          try {
              process1();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  
      static void process1() {
          try {
              process2();
          } catch (NullPointerException e) {
              throw new IllegalArgumentException(e);
          }
      }
  
      static void process2() {
          throw new NullPointerException();
      }
  }
  java.lang.IllegalArgumentException: java.lang.NullPointerException
      at Main.process1(Main.java:15)
      at Main.main(Main.java:5)
  Caused by: java.lang.NullPointerException
      at Main.process2(Main.java:20)
      at Main.process1(Main.java:13)
  ```

  ```java
  public class Main {
      public static void main(String[] args) {
          try {
              process1();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  
      static void process1() {
          try {
              process2();
          } catch (NullPointerException e) {
              throw new IllegalArgumentException();
          }
      }
  
      static void process2() {
          throw new NullPointerException();
      }
  }
  java.lang.IllegalArgumentException 
  at Main.process1(Main.java:15) 
  at Main.main(Main.java:5) 
  at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) 
  at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:64) 
  at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) 
  at java.base/java.lang.reflect.Method.invoke(Method.java:564) 
  at jdk.compiler/com.sun.tools.javac.launcher.Main.execute(Main.java:415) 
  at jdk.compiler/com.sun.tools.javac.launcher.Main.run(Main.java:192) 
  at jdk.compiler/com.sun.tools.javac.launcher.Main.main(Main.java:132) 
  ```

  没把异常对象作为参数继续往上面传递的话，就找不到根源的异常了

  - 通常不要在finally中抛出异常。如果在finally中抛出异常，应该原始异常加入到原有异常中。调用方可通过Throwable.getSuppressed()获取所有添加的Suppressed Exception

    ```java
    public class Main {
        public static void main(String[] args) throws Exception {
            Exception origin = null;
            try {
                System.out.println(Integer.parseInt("abc"));
            } catch (Exception e) {
                origin = e;
                throw e;
            } finally {
                Exception e = new IllegalArgumentException();
                if (origin != null) {
                    e.addSuppressed(origin);
                }
                throw e;
            }
        }
    }
    ```

  - 自定义异常：项目中自定义异常建议从RuntimeException继承，并且提供多种构造方法

    ```java
    public class BaseException extends RuntimeException {
        public BaseException() {
            super();
        }
    
        public BaseException(String message, Throwable cause) {
            super(message, cause);
        }
    	//message默认带有详细描述信息
        public BaseException(String message) {
            super(message);
        }
    
        public BaseException(Throwable cause) {
            super(cause);
        }
    }
    public class UserNotFoundException extends BaseException {
    }
    
    public class LoginFailedException extends BaseException {
    }
    ```

  - finally一定会在return之前执行，但是如果finally使用了return或者throw语句，将会使trycatch中的return或者throw失效；try代码块中出现异常会执行catch代码块，try异常后面的代码不会在执行

  - try-with-Resources语句

    - 当需要关闭多个资源时，i这个语法比较方便，能避免多层嵌套的try，finaly语句；try(获取i资源的代码，多条语句用；隔开)
    - 虽然可以省略finaly语句，但是也可有；catch字句和finaly字句都是在try-with-Resources语句关掉资源之后在执行
    - 注意：Scanner这些都是需要关闭资源的

  - 异常处理的规则

    - 捕获你知道如何处理的异常，传播那些不知道怎么处理的异常；早传播，晚捕获；用throw或者throws传播异常
    - 在子类方法中不能throws抛出父类方法中没有throws抛出的异常
    - 不要finaly中放改变控制流的语句，如return，throw，break，continue；因为用了return语句会吞噬掉try中的异常
    - 捕获异常挺耗费时间的；因此只在需要用时用
    - 最好创建自定义的异常类

  - Assert断言：用于调试程序，不可用于正式的开发代码中

    Java断言的特点是：断言失败时会抛出AssertionError，导致程序结束退出。因此，断言不能用于可恢复的程序错误，只应该用于开发和测试阶段

    ```java
    assert i>0;//如果不为true，则会停止程序，抛出异常
    ```

  - java.Exception.NullPointerException异常，可使用Objects工具的方法，还是会抛出异常，返回默认对象的重载方法似乎没有；java.utils.Objects

    ```java
    		String str=null;
            Objects.requireNonNull(str,"对象为null！");
    		//当两个对象用equals方法比较时，如果两个都可能为null；用Objects的equals方法，如果a,b都为null，那么返回true，其中一个为null，返回false，两个都不为null在比较a.equals(b)
    		Objects.equals(a,b)  
    ```
    
    ![image-20210514204039871](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210514204039871.png)
    
    
    
  - java.Exception.ClassCastException类型转换异常；发生在类型的强制转换中；

    - 只能在继承层次内进行强制类型转换

    - 在将超类转换为子类之前，应该使用instanceof关键字检查

      ```java
      if(stff[1] instanceof Manager){
          boss=(Manager)stff[1];
      }
      ```

  - JDK自带的Logger日志

    ```java
    import java.util.logging.Logger;
    public class Test extends AbstTes{
    	public static void main(String[] args) {
    	Logger lo=Logger.getGlobal();//注意它没有构造方法
    	lo.info("ssss");
    	lo.severe("sever");
    	}
    }
    ```

  - 在开发阶段，始终使用Commons Logging接口来写入日志，并且开发阶段无需引入Log4j。如果需要把日志写入文件， 只需要把正确的配置文件和Log4j相关的jar包放入classpath，就可以自动把日志切换成使用Log4j写入，无需修改任何代码

  - log4j的xml配置

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <Configuration>
    	<Properties>
            <!-- 定义日志格式 -->
    		<Property name="log.pattern">%d{MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36}%n%msg%n%n</Property>
            <!-- 定义文件名变量 -->
    		<Property name="file.err.filename">log/err.log</Property>
    		<Property name="file.err.pattern">log/err.%i.log.gz</Property>
    	</Properties>
        <!-- 定义Appender，即目的地 -->
    	<Appenders>
            <!-- 定义输出到屏幕 -->
    		<Console name="console" target="SYSTEM_OUT">
                <!-- 日志格式引用上面定义的log.pattern -->
    			<PatternLayout pattern="${log.pattern}" />
    		</Console>
            <!-- 定义输出到文件,文件名引用上面定义的file.err.filename -->
    		<RollingFile name="err" bufferedIO="true" fileName="${file.err.filename}" filePattern="${file.err.pattern}">
    			<PatternLayout pattern="${log.pattern}" />
    			<Policies>
                    <!-- 根据文件大小自动切割日志 -->
    				<SizeBasedTriggeringPolicy size="1 MB" />
    			</Policies>
                <!-- 保留最近10份 -->
    			<DefaultRolloverStrategy max="10" />
    		</RollingFile>
    	</Appenders>
    	<Loggers>
    		<Root level="info">
                <!-- 对info级别的日志，输出到console -->
    			<AppenderRef ref="console" level="info" />
                <!-- 对error级别的日志，输出到err，即上面定义的RollingFile -->
    			<AppenderRef ref="err" level="error" />
    		</Root>
    	</Loggers>
    </Configuration>
    ```

  - | Commons Logging                       | SLF4J                   |
    | :------------------------------------ | :---------------------- |
    | org.apache.commons.logging.Log        | org.slf4j.Logger        |
    | org.apache.commons.logging.LogFactory | org.slf4j.LoggerFactory |

- commons-logging+log4j是配套使用的，slf4j+logback也是配套使用的，commons logging和slf4j都是日志接口，log4j和logback就是日志

### 六、注解

- 使用注解：注解可以使用在类、方法、字段、构造方法上，注解是一种用作标注的“元数据”，注释是会自动被编译器跳过的，而注解是会被编译器编译进class文件的；注解可以配置参数，没有指定配置的参数使用默认值

- 注解的分类

  - 第一类：编译器上的注解，比如@Override覆写注解，@SuppressWarning表示编译器忽略此处的产生的警告；这类注解不会被写进.class文件
  - 第二类：是由工具处理/class文件使用的注解，是一些工具对class文件做动态修改，实现一些特殊的功能，这不会加载到内存中，这类注解一般被底层库使用，不用我们设置
  - 第三类：程序运行期能够读取的注解，它们在加载后一直存在于JVM中，这也是最常用的注解

- 自定义注解

  - ```java
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    //自定义注解@Report，用@interface关键字，上面的为元注解
    public @interface Report {
        int type() default 0;
        String level() default "info";
        String value() default "";
    }
    ```

  - @target注解的参数值

    类或接口：ElementType.TYPE；
    字段：ElementType.FIELD；
    方法：ElementType.METHOD；
    构造方法：ElementType.CONSTRUCTOR；
    方法参数：ElementType.PARAMETER。

  - @Retention注解的参数值，对应注解的三种类型

    仅编译期：RetentionPolicy.SOURCE；
    仅class文件：RetentionPolicy.CLASS；
    运行期：RetentionPolicy.RUNTIME

### 七、内部类

- 内部类的作用
  - 当内部类使用private修饰符修饰时，可以对同一个包中的其他类隐藏；可通过外部类来访问内部类
  - 当外部类不是public，内部类是public时，外部类就是内部类的辅助类，内部类可以访问外部类的所有属性和方法；内部类对象有一个隐式引用，总是指向外部类的对象（this）
  - 应用场景
    - 外部类中的某个字段需要另一个类计算时，又不想提供该字段的公共的get方法；那么就可以用内部类来实现；
    - 设置类时，为了避免类的命名冲突，就看能不能设置为内部类
- 内部类的限制
  - 内部类中声明的所有静态字段都必须是final
  - Java规范中表明内部类不要有static方法，虽然语法允许，但只能访问外围类的静态字段和静态方法

- 成员内部类

  - 成员内部类前面可以修饰public,protected和private

- 静态内部类

  - ![img](https://uploadfiles.nowcoder.com/images/20170830/5248791_1504089837512_39944D8036786CB820F9D9873950ABA5)

- 局部内部类

  - 局部内部类是定义在方法内部的类；该类的作用域是该方法体中
  - 可以把局部内部类当做一个局部变量，所以它是不需要加任何修饰符的局部内部类前不能用修饰符public和private,protected
  - 优势
    - 局部内部类对于方法外部完全隐藏，除了该方法内部，其他地方都不能访问该内部类
    - 还可访问外部类的字段，以及包含局部内部类的方法的参数（局部变量，但是必须是final）

- 匿名内部类

  - 应用场景

    - 如果只想创建该类的对象，甚至不需要指定类名字时

  - 语法

    ```java
    new SuperType(构造器参数){
    	内部类的方法和字段
    }
    ```

    - 其中SuperType可以是接口、类，如果是接口，那么该匿名内部类就是实现的该接口；如果是类，那么就是继承的该类

  - 匿名内部类的限制

    - 因为匿名内部类没有类名，因此不能有构造器；如果需要初始化字段，可以使用初始化块

      ```java
      new SuperType(构造器参数){
          {初始化操作}
      }
      ```

      

    - 因为构造参数要传递给父类的构造器，因此如果匿名内部类实现的是接口的话，那么就不能设置构造参数，不过任然要有一个空的小括号

    - 在使用匿名内部类的过程中，我们需要注意如下几点：

  **1、**使用匿名内部类时，我们必须是继承一个类或者实现一个接口，但是两者不可兼得，同时也只能继承一个类或者实现一个接口。

  **2、**匿名内部类中是不能定义构造函数的。

  **3、**匿名内部类中不能存在任何的静态成员变量和静态方法。

  **4、**匿名内部类为局部内部类，所以局部内部类的所有限制同样对匿名内部类生效。

  **5、**匿名内部类不能是抽象的，它必须要实现继承的类或者实现的接口的所有抽象方法。

  - 静态内部类

    - 应用场景

      - 当外部类为工具类作用时，里面的静态方法需要使用内部类，这时就要求内部类为静态的；
      - 当需要在一个方法中返回两个值时，这时就可创建包含该两个字段的内部类，在该方法中返回该内部类的对象

    - 注意事项

      - 只有内部类可以用static修饰，外部类不行
      - 静态内部类可以有静态字段和方法，其他内部类不行
      - 在接口中的内部类自动是static和public

      - 静态内部类没有外部类的引用，就不能访问外部类的方法和字段；不需要访问外部类数据的内部类都应该设置为静态内部类

  **1. 静态内部类本身可以访问外部的静态资源，包括静态私有资源。但是不能访问非静态资源，可以不依赖外部类实例而实例化。**

  **2. 成员内部类：**

    **1. 成员内部类本身可以访问外部的所有资源，但是自身不能定义静态资源，因为其实例化本身就还依赖着外部类。**

  **3. 局部内部类：**

    **1. 局部内部类就像一个局部方法，不能被访问修饰符修饰，也不能被static修饰。**

    **2. 局部内部类只能访问所在代码块或者方法中被定义为final的局部变量。**

  **4. 匿名内部类：**

    **1. 没有类名的内部类，不能使用class，extends和implements，没有构造方法。**

    **2. 多用于GUI中的事件处理。**

    **3. 不能定义静态资源**

    **4. 只能创建一个匿名内部类实例。**

    **5. 一个匿名内部类一定是在new后面的，这个匿名类必须继承一个父类或者实现一个接口。**

    **6. 匿名内部类是局部内部类的特殊形式，所以局部内部类的所有限制对匿名内部类也有效**

### 八、编码

- 数据单位；1k和1kb是不一样的！

  ![image-20210607114525962](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607114525962.png)

### 九、I/O流

- #### ![img](http://uploadfiles.nowcoder.com/images/20150328/138512_1427527478646_1.png)

- RandomAccessFile：该类的实例支持读取和写入随机访问文件。 随机访问文件的行为类似于存储在文件系统中的大量字节，可以附加或更新文件

  ![image-20210506134429807](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210506134429807.png)



### 十、基本数据类型和关键字

#### 关键字

- super关键字在子类重写父类方法的时候，但是还想调用父类的方法，那么就可用super.父类方法,来调用父类的方法，这个语句必须写在子类该方法的第一行

| 访问控制                      | 类、方法和变量修饰符    | 程序控制语句          | 基本类型         |
| :---------------------------- | :---------------------- | :-------------------- | :--------------- |
| private 私有的                | abstract 声明抽象       | break 跳出循环        | boolean 布尔型   |
| protected 受保护的            | class 类                | continue 继续         | byte 字节型      |
| public 公共的                 | extends 扩允, 继承      | return 返回           | char 字符型      |
|                               | final 终极, 不可改变的  | do 运行               | double 双精度    |
| 包相关                        | implements 实现         | while 循环            | float 浮点       |
| import 引入                   | interface 接口          | if 如果               | int 整型         |
| package 包                    | native 本地             | else 反之             | long 长整型      |
|                               | new 新, 创建            | for 循环              | short 短整型     |
| 错误处理                      | static 静态             | instanceof 实例       | null 空          |
| catch 处理异常                | strictfp 严格, 精准     | switch 开关           | true 真          |
| finally 有没有异常都执行      | synchronized 线程, 同步 | case 返回开关里的结果 | false 假         |
| throw 抛出一个异常对象        | transient 短暂          | default 默认          |                  |
| throws 声明一个异常可能被抛出 | volatile 易失           |                       | 变量引用         |
| try 捕获异常                  |                         |                       | super 父类. 超类 |
|                               |                         |                       | this 本类        |
| 保留字                        | null                    |                       | void 无返回值    |
| byValue                       | generic                 | outer                 |                  |
| cast                          | inner                   | rest                  | goto             |
| false、ture                   | operator                | var                   | const            |

#### 八大基本数据类型

- #### byte、short、char、int、long、float、double、boolean；所占大小分别是1、2、2、4、8、4、8、1（但是JVM里面使用的4字节）

- char类型既可以表示ascll，也可以用一个Unicode表示，Unicode编码不管中文还是英文都是两个字节

- int类型占四个字节，这个整型是有符号位的，最大值为2147483647（一个0，31个1），2的31次方为2147483648，因为是从0开始的，最小值为-2147483648，是从-1开始的；超过最大值就会溢出，变成一个固定的负数

- float类型占4个字节，值得后面要加个f，浮点型的意思是，比如1234.5可以表示成12.345x102，也可以表示成1.2345x103，所以称为浮点数,float类型可最大表示3.4e38，而double类型可最大表示1.79e308

  ```java
  	float f=2.0e2f;//200.0
  	float g=1.011e1f;//10.11
  ```

- boolean型，不能写成bool，在eclipse下会编译出错；只有false、true两个值；Java语言对布尔类型的存储并没有做规定，因为理论上存储布尔类型只需要1 bit，但是通常JVM内部会把boolean表示为4字节整数

- 数值类型的默认值都是0，char字符类型的默认值是‘/u0000’空字符，但是eclipse输出是一个正方形的图形，boolean类型是false

#### 基本封装类型

- Integer；

- Character

- Short

- Byte：大小一个字节，取值范围是-128~127，属于小整型，超过范围会java.lang.NumberFormatException异常

- Long

- Double

- Float

- Boolean

- 7种类型都有两种构造方法（Chracter没有），分别是本类型的参数和String类型的参数

  如public Double(String s)，public Double(double value)

- 基本类型与封装类型的主动转换（拆箱与装箱）

  - 基本类型转为封装类型（装箱）

    - 通过public static Integer valueOf(int i)， public static Long valueOf(long l)，public static Character valueOf(char c)

      ![image-20210509221242236](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509221242236.png)

    - Integer in=1（自动装箱）

  - 封装类型转为基本类型（拆箱）

    -   public double doubleValue()，public char charValue()，public int intValue()

      ![image-20210509221120586](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509221120586.png)

    - 强制转换

      ![image-20210509220651944](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509220651944.png)

- 注意事项：在用System.out.print()来输出八大基本类型数组引用时，只有char数组会输出数组的全部值，其他的都是数组的地址；但是当输出char[]时变成了print方法的字符串的重载方法，那么还是输出的char数组的地址

  ![image-20210509210033968](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509210033968.png)

  ![image-20210509210116853](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509210116853.png)

- 数值封装类型都有个1字节大小的缓存，如果新初始化的值小于一个字节，那么是从缓存中取的，如果值一样，那么两个变量就是同一个对象

  - Integer类型

  ![image-20210509215501167](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509215501167.png)

  ![image-20210509215528247](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509215528247.png)

  - Chracter类型

    ![image-20210509215837610](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509215837610.png)

    ![image-20210509215859885](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509215859885.png)

#### JDK常用类

##### String

-  private final byte[] value：String用的final修饰的byte[]数组存储字符；public final class String，String类也是用的final修饰

- 字符串类型转换为八大基本数据类型，当有转换异常是java.lang.NumberFormatException数值格式异常，注意转换的parse方法都是对应基本类型的封装类型的静态方法

  ![image-20210509210539033](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509210539033.png)

- String的12种常用方法

  - 用字符数组创建一个String

    **public** **String**(**char**[] **value**)

    **public** **String**(**char** chars[], **int** x, **int** n)

    ![image-20210510140931715](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510140931715.png)

  - 获取字符串长度

    **public** **int** **length**()

    ![image-20210510141019906](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141019906.png)

  - 获取字符串某一位置的字符

    ![image-20210510141049044](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141049044.png)

  - 获取字符串的子串

    **public** String **substring**(**int** beginIndex)
    //该方法从beginIndex位置起，
    *//从当前字符串中取出剩余的字符作为一个新的字符串返回*

    **public** String **substring**(**int** beginIndex, intendIndex)
    *//该方法从beginIndex位置起，从当前字符串中*
    //取出到endIndex-1位置的字符作为一个新的字符串返回

    ![image-20210510141159269](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141159269.png)

  - 字符串的比较

    **public** int compareTo(String str)
    //该方法是对字符串内容按字典顺序进行大小比较，
    //通过返回的整数值指明当前字符串与参数字符串的大小关系。
    //若当前对象比参数大则返回正整数，反之返回负整数，相等返回0

    **public** int compareToIgnoreCase (String str)
    //与compareTo方法相似，但忽略大小写

    **public** boolean equals(Object obj)
    //比较当前字符串和参数字符串，在两个字符串相等的时候返回true，否则返回false

    **public** boolean equalsIgnoreCase(String str)
    //与equals方法相似，但忽略大小写

    ![image-20210510141304131](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141304131.png)

  - 查找子串在字符串中的位置

    **public** **int** **indexOf**(String str)
    *//用于查找当前字符串中字符或子串，返回字符或*
    *//子串在当前字符串中从左边起首次出现的位置，若没有出现则返回-1。*

    **public** **int** **indexOf**(String str, intfromIndex)
    *//改方法与第一种类似，区别在于该方法从fromIndex位置向后查找。*

    **public** **int** **lastIndexOf**(String str)
    *//该方法与第一种类似，区别在于该方法从字符串的末尾位置向前查找。*

    **public** **int** **lastIndexOf**(String str, intfromIndex)
    //该方法与第二种方法类似，区别于该方法从fromIndex位置向前查找

    ![image-20210510141351892](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141351892.png)

  - 字符串中字符的大小写转换

    **public** String toLowerCase()
    *//返回将当前字符串中所有字符转换成小写后的新串*

    **public** String toUpperCase()
    //返回将当前字符串中所有字符转换成大写后的新串

    ![image-20210510141424373](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141424373.png)

  - 字符串两端去空格

    String trim()
    //去除字符串两端的空格，中间的空格不变，一般用于登陆注册时

    ![image-20210510141458205](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141458205.png)

  - 将字符串分割成字符串数组

    String[] split(String str)

    ![image-20210510141840208](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510141840208.png)

  - 基本类型转换为字符串

    **static** String valueOf(xxx xx)

    ![](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509211906288.png)

  - 替换字符串

    **public** String replace(char oldChar, charnewChar)
    *//用字符newChar替换当前字符串中所有的oldChar字符，*
    *//并返回一个新的字符串。*

    **public** String replaceFirst(String regex,String replacement)
    *//该方法用字符replacement的内容替换当前字符串中遇到的*
    *//第一个和字符串regex相匹配的子串，应将新的字符串返回。*

    **public** String replaceAll(String regex,String replacement)
    *//该方法用字符replacement的内容替换当前字符串中遇到的所有*
    //和字符串regex相匹配的子串，应将新的字符串返回

    ![image-20210510142026812](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510142026812.png)

  - 转化为字符数组

    public char[] toCharArray() 

    ![image-20210510142120404](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510142120404.png)

- 八大基本数据类型转化为字符串类型

  - 可以通过+链接一个""空串

  - 可通过String.valueOf(E e)方法，public static String valueOf(Object obj)，每种数据类型的重载函数都有

    ![image-20210509211906288](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509211906288.png)

  - 通过类型包装类的toString(E e)方法，这是包装类的带参数的静态方法，不带参数的toString()方法是普通的成员方法，用对象调用的

    ![image-20210509212107054](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509212107054.png)
    
  - 字符串的创建题
  
    ```java
    String s1 = "HelloWorld";
     String s2 = new String("HelloWorld");
    ```
  
    这种情况下s1!=s2；s1是在常量池中，s2是在堆中，有常量池中的引用？
  
    ```java
    String s1="HelloWorld";
    String s2="HelloWorld";
    ```
  
    这中情况下s1==s2，这时都是指向常量池的同一个字符串对象

##### StringBuilder

- StringBuilder是继承的AbstractStringBuilder，AbstractStringBuilder使用的是byte[]数组来存储的字符

- 构造方法；public StringBuilder(String str)：构造一个初始化为指定字符串内容的字符串构建器。 字符串构建器的初始容量为16加上字符串参数的长度；public StringBuilder()：构造一个没有字符的字符串构建器，初始容量为16个字符

- 插入

  - public StringBuilder insert(int offset,String str)：在偏移量位置插入字符串，String参数的String按顺序插入到指定偏移量的该序列中，向上移动原始位于该位置的任何字符，并将该序列的长度增加到参数的长度。 如果str是null ，则四个字符"null"被插入到该序列中；其他的重载方法都是偏移量+要插入的类型，除了字符和字符数组

  ![image-20210513131316871](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210513131316871.png)

  - public StringBuilder insert(int index,//在原字符串中要插入的位置
                                char[] str,//插入的字符数组
                                int offset,//插入的字符数字的起始位置
                                int len//要插入的长度

  -  public StringBuilder append(String str)：在字符串的尾部增加字符串；该方法几种参数类型都有重载方法；

    ![image-20210513133718667](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210513133718667.png)

- 转化为String和截取为子串返回

  - public String toString()：StringBuilder转化为String；
  - public String substring(int start,int end)：截取StringBuilder的字串，左闭右开区间；public CharSequence subSequence(int start,int end)跟subString一样的，只是实现了CharSequens接口

- public StringBuilder reverse()：reverse()反转方法，可以用来反转String，需要用StringBuilder的带String参数的构造函数转换成StringBuilder，在调用reverse()

  ![image-20210513122437757](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210513122437757.png)

  

- java.util.Random

  - Random()：构造一个新的随机数生成器
  - int nextInt(int n)：返回一个0~n-1之间的随机数



### 十二、运算符

- 算术运算符：+加、-减、*乘、/除、%求余、++自增、--自减

- 逻辑运算符：&&逻辑与（也叫短路与）、||逻辑或、！逻辑非

- 位运算符：<<左移运算（相当于乘2的次方）、>>右移运算（相当于除2的次方）、>>>（除以2的次方并且符号位变成正数）、&按位与（对应位全1为1）、|按位或（对应位有1则1）、^按位异或（对应位相同为0，不同为1）、~按位补（每位翻转，0变1，1变0，结果就是原值+1）

  例如：1011，左边是2的3次方，右边是从2的0次方开始的，1011>>1=101左边的数会把最右边的覆盖了，变小了

- 关系运算符：>、<、=、>=、<=、==、!=

- 赋值运算符：=、+=、-=、/=、*=、（%）=、&=、|=、^=、<<=、>>=

- #### 其他运算符：逻辑表达式？值1：值2（三目运算符或条件运算符）、instanceof（返回值为boolean类型）

  - 三元操作符类型的转换规则：
    1.若两个操作数不可转换，则不做转换，返回值为Object类型
    2.若两个操作数是明确类型的表达式（比如变量），则按照正常的二进制数字来转换，int类型转换为long类型，long类型转换为float类型等。
    3.若两个操作数中有一个是数字S,另外一个是表达式，且其类型标示为T，那么，若数字S在T的范围内，则转换为T类型；若S超出了T类型的范围，则T转换为S类型。

    4.若两个操作数都是直接量数字，则返回值类型为范围较大者

    #### 例如：

    ```java
    //o1为1.0，被自动转换为了double类型
    Object o1 = true ? new Integer(1) : new Double(2.0);
    ```

    

- 运算符的优先级

  | 类别     | 操作符                                     | 关联性   |
  | :------- | :----------------------------------------- | :------- |
  | 后缀     | () [] . (点操作符)                         | 左到右   |
  | 一元     | + + - ！〜                                 | 从右到左 |
  | 乘性     | * /％                                      | 左到右   |
  | 加性     | + -                                        | 左到右   |
  | 移位     | >> >>>  <<                                 | 左到右   |
  | 关系     | >> = << =                                  | 左到右   |
  | 相等     | == !=                                      | 左到右   |
  | 按位与   | ＆                                         | 左到右   |
  | 按位异或 | ^                                          | 左到右   |
  | 按位或   | \|                                         | 左到右   |
  | 逻辑与   | &&                                         | 左到右   |
  | 逻辑或   | \| \|                                      | 左到右   |
  | 条件     | ？：                                       | 从右到左 |
  | 赋值     | = + = - = * = / =％= >> = << =＆= ^ = \| = | 从右到左 |
  | 逗号     | ，                                         | 左到右   |

- #### 只有%取余操作，只适用于整型;虽然能用，都是结果不一定是正确的;因为一些小数无法用二进制表示

### 十三、环境配置

- JDK环境变量配置，需要配置JAVA_HOME，然后再系统变量那里添加path，%表示引用系统变量里面的key，相当于键值对，path需要到bin目录；也可以直接在path中添加路径到JDK的bin目录

  ![image-20210509165458367](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509165458367.png)

![image-20210509165530406](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509165530406.png)

### 十四、集合

#### 数组

- 创建数组的三种方式

  - ```java
  		int[] a={1,2,3,};
    		String[] str=new String[]{"aaa","bbb","ccc"};
  		double[] db=new double[10];
    ```

  - 创建数值数组时，所有元素为 0除了float和double为0.0（byte、short、int、long、float、double），boolean数组为false，引用类型数组为null（比如封装类型和String等）

    ![image-20210513205814751](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210513205814751.png) <img src="C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210513210058430.png" alt="image-20210513210058430" style="zoom:50%;" />

    #### 数组的使用

    - 数组的定义：一组类型相同的元素，如果是基本数据类型的数组，那么内存地址是连续的，如果是引用类型数组，那么数组里存放是的真实对象的引用，引用是连续的，但是真实对象是分散的；可以通过下标访问数组中的元素，下标从0开始

      ![image-20210509151219236](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509151219236.png)

    - 数组的遍历

      - 使用for each循环，使用的是迭代器

        ```java
      for(int i:a){
        		System.out.print(i+"\t");
      }
        ```

      - 使用Arrays.toString(数组)打印数组

        ```java
      System.out.println(Arrays.toString(a));
        ```

      - 下标遍历

        ```java
      //正序遍历，注意int[]数组的长度是a.length，其他的一些集合是size()方法，或者length()方法
        for(int i=0;i<a.length;i++){
      		System.out.print(a[i]+"\t");
        }
      //逆序遍历
        for(int i=a.length-1;i>=0;i--){
      		System.out.print(a[i]+"\t");
        }
        ```
    ```
      
    ```
  
  - 数组的拷贝

    - 可以直接通过数组变量之间的拷贝，这样两个变量将引用同一个数组
  
  - 还可以通过Arrays中的public static int[] copyOf(int[] original, int newLength),方法来拷贝，这种方式比较灵活，可以拷贝一部分数组，也可以把数组扩容，多的部分为元素类型的默认值
  
  - 数组的排序
    
    - 冒泡排序：思路就是通过打擂台的形式，先让最大的数移动到最后面，然后是第二大，第三大；降序就是把最小的移到最后面
    
        ```java
      //升序排序
        int temp=0;
      	for(int i=0;i<a.length;i++){
        		for(int j=i+1;j<a.length;j++){
      			if(a[i]>a[j]){//降序的话改成if(a[i]<a[j])
        				temp=a[j];
      				a[j]=a[i];
        				a[i]=temp;
      			}
        		}
      	}
        	System.out.print(Arrays.toString(a));
  //也可以直接使用JDK中Arrays提供的sort方法来排序
        	Arrays.sort(a);
  	System.out.print(Arrays.toString(a));
      ```

    - 快速排序

      - Arrays.sort(数组)就是使用的优化的快速排序（quickSort）
  
    - 插入排序
    
  - 数组是一个对象，不同类型的数组具有不同的类，数组是一个连续的存储结构
    
  - 多维数组：打印多维数组的时候可用Arrays.deepToString(数组)；
    
      ```java
       int[][] scores = {
                  { 82, 90, 91 },
                  { 68, 72, 64 },
                  { 95, 91, 89 },
                  { 67, 52, 60 },
                { 79, 81, 85 },
          };
       System.out.println(Arrays.deepToString(scores));
    ```
  
  - 命令行参数，根据可以通过Sting数组来获取命令行的参数，根据参数的不同用if条件判断来执行不同的代码；另外JDK14可以直接用java命令来执行JAVA文件，不用生成.class字节码文件
    
    注意：在编辑器上改变了代码之后要保存之后才能生效
    
      ```java
      public class Main {
      public static void main(String[] args) {
      	 for (String arg : args) {
               if ("-version".equals(arg)) {
                   System.out.println("v 1.0");
                   break;
             }
           }
    }
      ```
  
    ![image-20210509164952224](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210509164952224.png)
    
  - java.util.Arrays类：提供对数组操作的方法，比如排序、查找、数组比较、批量赋值、数组拷贝、打印数组
    
      （2）List<T> asList(T... a)：返回由指定数组支持的固定大小的列表，例如：List<String> stooges = Arrays.asList("Larry", "Moe", "Curly"); 
      （3)int binarySearch(byte[] a,byte key):使用二进制搜索算法搜索指定值的指定字节数组。 在进行此调用之前，数组必须按照sort(byte[])方法进行排序；如果找到则返回该值的下标，反之则返回一个负数
      （4）T[] copyOfRange(T[] original, int from,int to)：参数分别为要复制的数组，初始索引（包括），最终索引（可在源数组外）；该拷贝用的是浅拷贝，如果复制的是对象数组，则只是复制了对象的引用，并没有new每一个对象；因为该方法内部用了System.ayyarcopy(original, from, copy, 0, Math.min(original.length - from, newLength)),为浅拷贝
      （5） boolean equals(Object[] a, Object[] a2)：比较两个数组是否相等，可以是基本类型的数组，也可以是对象数组
      （6）void fill(long[] a, int fromIndex, int toIndex, long val)：对数组从起始索引到结束索引填充数据val，参数分别056是要填充的数组、起始索引（包括）、结束索引（不包括）、填充的数据
      （7）byte[] copyOf(byte[] original, int newLength)：也是浅拷贝，不过初始索引固定从0开始，第二个参数是从0开始拷贝的长度
    （8） int hashCode(int[] a)：返回hashcode值，参数可以是任意类型的数组；
      （9）void sort(Object[] a)、void sort(T[] a, Comparator<? super T> c)：第一种是直接按照默认的升序排序，第二种可以通过重写compareTo()方法自定义排序方式；此外第一种排序还可以在后面加上void sort(Object[] a,int fromIndex, int toIndex)包括开始索引，不包括结束索引，只排序这个范围内的数据
    （10）String toString(int[] a)：返回该数组的字符串形式，例如：[1, 2, 3, 4, 5]；参数数组是任意类型的
    （11）void parallelSort(int[] a)：并行计算排序，内部原理是利用Fork/join对大数组进行拆分进行并行排序，比用一个单线程来计算大数组要高效得多；应用场景是需要排序得数组长度非常的大
    
  - 练手，在抽彩游戏中抽取从 1到49的随机数字，抽取6个，要求没有重复的，并且每次抽取都是随机的
    
      ```java
      int[] number=new int[49];
      		for(int i=0;i<number.length;i++){
      			number[i]=i+1;
      		}
      		int[] result=new int[6];
      		int r=0;
      		for(int i=0;i<result.length;i++){
                  //Math.random()的范围是[0,1),乘上n就是[0,n);实质上是数组下标的随机，而不是数组值的随机
      			r=(int) (Math.random()*number.length);
      			result[i]=number[r];
      			number[r]=number[number.length-1];//保证不会重复，把最后一位赋值给该数；但是如果本该数就是数组最后一位呢？
      		}
      		Arrays.sort(result);
      		System.out.print(Arrays.toString(result));
    ```
    
      

- ArrayList的常用方法

  - add添加方法，有尾插入方式和头插入方式

    public boolean add(E e)：默认是从尾部插入

    public void add(int index,E element)

    index参数表示设置插入数组的下标位置，设为0的话就是头部插入元素

    Collections.reverse(listStr)：结合Collectons的reverse翻转(数组)方法，可以把list集合的顺序颠倒

    ![image-20210510214807405](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510214807405.png) ![image-20210510214941297](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510214941297.png)

  - 删除元素

    public E remove(int index)：按照下标删除指定的对象

    public boolean remove(Object o)：删除指定的对象

    public void clear()：删除list中的所有元素

    protected void removeRange(int fromIndex,int toIndex)：删除该下标范围的元素，左闭右开区间

    public boolean removeAll(Collection<?> c)：把原集合中的包含参数集合c的元素删除

    public boolean retainAll(Collection<?> c)：把原集合中的除了参数集合c，其他的元素全部删除

    ![image-20210510220556824](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510220556824.png) ![image-20210510221519564](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510221519564.png)

    

  - 构造方法

    public ArrayList()：返回一个初始容量为10的数组

    public ArrayList(Collection<? extends E> c)：创建一个包含指定的集合的列表

    public ArrayList(int initialCapacity)：构建一个包含初始容量的列表

  - 获取元素

    public E get(int index)：获取指定下标位置的对象

  - 修改元素

    public E set(int index,E element)：修改指定下标的元素，用element参数覆盖

  - 获取list列表中元素的个数

    public int size()

  - 是否为空、是否包含的判断

    public boolean contains(Object o)：如果为true，表示列表中至少有一个该对象

    public boolean isEmpty()：如果列表中不包含元素则返回true

  - 位置下标判断

    public int indexOf(Object o)：判断某元素在列表中第一次出现所在的下标

    public int lastIndexOf(Object o)：判断某元素在列表中最后一次出现所在的下标

  - 切割子列表

    public List<E> subList(int fromIndex,int toIndex)：切割下标范围内的作为一个新列表，[fromIndex,toIndex)左闭右开区间

    ![image-20210510231033511](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510231033511.png)

  - 列表和数组之间的相互转换

    列表转化为数组：调用列表的toArray()方法，如果是列表是String类型的话，不能把Object[]数组强转为String[]，会报ClassCastException异常，就用Object[]数组来接收

    public Object[] toArray()

    ![image-20210510230244557](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510230244557.png)

    数组转化为列表：调用Arrays的asList()，java.util.Arrays

    public static <T> List<T> asList(T... a)

    ![image-20210510230806136](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210510230806136.png)

### 十五、时间和日期

- java.util.Date：用来表示时间点的

  - 创建对象

    ```java
     Date date=new Date();
    ```

- java.time.LocalDate：使用日历表示法表示日期

  - 创建对象

    ```java
    //使用public static LocalDate of(int year, Month month, int dayOfMonth)创建指定年月日的对象
    LocalDate localDate=LocalDate.of(1999,2,20);
    //使用静态方法now()来创建当前日期的对象public static LocalDate now()
    LocalDate date1=LocalDate.now();
    ```

  - ```java
     //获取对象的年
    public int getYear() {
            return year;
        }
    //获取对象的月
        public int getMonthValue() {
            return month;
        }
    //获取对象的日
        public int getDayOfMonth() {
            return day;
        }
    //获取对象的DayOfWeek实例，在调用getValue()方法返回1~7之间的整数，对应星期一到星期日
         public DayOfWeek getDayOfWeek() {
            int dow0 = (int)Math.floorMod(toEpochDay() + 3, 7);
            return DayOfWeek.of(dow0 + 1);
        }
    //生成当前日期之后的日期
        public LocalDate plusDays(long daysToAdd) {
            if (daysToAdd == 0) {
                return this;
            }
            long mjDay = Math.addExact(toEpochDay(), daysToAdd);
            return LocalDate.ofEpochDay(mjDay);
        }
    //生成当前日期之前的日期
        public LocalDate minusDays(long daysToSubtract) {
            return (daysToSubtract == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-daysToSubtract));
        }
    ```

    

  ```java
   	LocalDate date=LocalDate.now();
          int month=date.getMonthValue();
          int today=date.getDayOfMonth();
  		//toda-1表示这个月的第一天，today-2表示这个月的第二天
          date=date.minusDays(today-1);
          int value=date.getDayOfWeek().getValue();
          System.out.println("Mon"+"\t\t"+"Tue"+"\t\t"+"Wed"+"\t\t"+"Thu"+"\t\t"+"Fri"+"\t\t"+"Sat"+"\t\t"+"Sun");
          for(int i=0;i<value;i++){
              System.out.print("    ");
          }
          while (date.getMonthValue()==month){
              System.out.printf("%3d",date.getDayOfMonth());
              if(date.getDayOfMonth()==today){
                  System.out.print("*");
              }
              else{
                  System.out.print("  ");
              }
              if(date.getDayOfWeek().getValue()==7){
                  System.out.println();
              }
              date=date.plusDays(1);
          }
          if(date.getDayOfWeek().getValue()!=1) System.out.println();
      }
  ```

  ![image-20210514200054438](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210514200054438.png)

### 十六、Linux命令

- tar

  -c: 建立压缩档案
  -x：解压
  -t：查看内容
  -r：向压缩归档文件末尾追加文件
  -u：更新原压缩包中的文件

  这五个是独立的命令，压缩解压都要用到其中一个，可以和别的命令连用但只能用其中一个。下面的参数是根据需要在压缩或解压档案时可选的。

  -z：有gzip属性的
  -j：有bz2属性的
  -Z：有compress属性的
  -v：显示所有过程
  -O：将文件解开到标准输出

  下面的参数-f是必须的

  -f: 使用档案名字，切记，这个参数是最后一个参数，后面只能接档案名。

- 解压

  - tar –xvf file.tar 解压 tar包
  - tar -xzvf file.tar.gz 解压tar.gz
  - tar -xjvf file.tar.bz2  解压 tar.bz2
  - tar –xZvf file.tar.Z  解压tar.Z
  - unrar e file.rar 解压rar
  - unzip file.zip 解压zip

- 压缩

  - tar –cvf jpg.tar *.jpg 将目录里所有jpg文件打包成tar.jpg
  - tar –czf jpg.tar.gz *.jpg  将目录里所有jpg文件打包成jpg.tar后，并且将其用gzip压缩，生成一个gzip压缩过的包，命名为jpg.tar.gz
  - tar –cjf jpg.tar.bz2 *.jpg 将目录里所有jpg文件打包成jpg.tar后，并且将其用bzip2压缩，生成一个bzip2压缩过的包，命名为jpg.tar.bz2
  - tar –cZf jpg.tar.Z *.jpg  将目录里所有jpg文件打包成jpg.tar后，并且将其用compress压缩，生成一个umcompress压缩过的包，命名为jpg.tar.Z
  - rar a jpg.rar *.jpg rar格式的压缩，需要先下载rar for linux
  - zip jpg.zip *.jpg  zip格式的压缩，需要先下载zip for linux 

### 十七、lambda表达式和流

- java.util.Comparator接口专门可用于lambda表达式使用

- 流和集合的区别

  - 流没有关闭时不能被多次使用，不然会抛出异常
  - 流不存储数据，数据还是存储在底层集合中
  - 流的操作不会修改其的数据源，流的更改方法都不是在原有流上更改，更是创建的新的流来操作
  - 流的操作会延迟执行，直到其需要流操作的结果时才会执行

- 创建流的方式

  - Collection接口有stream()和paralleStram()方法，所以集合中的数据结构都可以调用这两个方法来转换成流

  - static <T> Stream<T> of(T... values)：创建一个给定元素的流

  - static <T> Stream<T> empty()：创建一个空流

  - static <T> Stream<T> generate(Supplier<T> s)：创建一个无限流，值是通过反复调用s函数而构建的流

  - static <T> Stream<T> iterate(T seed,UnaryOperator<T> f)：创建一个无限流，在seed上调用f产生的值

    ![image-20210606164752713](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210606164752713.png)

    这样如果不调用limit方法的话，那么会产生一个从1开始自增1的无限流，1，2，3...

- 方法引用

  - 方法引用只能是该类的静态方法才能直接使用，如果是实例方法，那么只能使用lambda表达式，在表达式的方法体里面new一个该类的实例对象，在调用实例方法

    ```java
    Stream<Integer> randoms = Stream.generate(() ->new Random().nextInt(100));//产生一个包含大于等于0小于100的整数的无限流；可以使用Stream的limit（int n）方法来控制长度
    Stream.generate(Math::random);//Math类的random方法是静态的，返回的是0~1之间的小数，是double类型的
    ```

### 十八、泛型

- Java类库使用E表示集合的元素类型，K，V表示键和值的类型，T表示任意类型

- 练手程序：根据实现一个传入一个String[]，返回一个包含字符串的最大值和最小值的泛型对象

  ```java
  public class PairTest {
  	public static void main(String[] args) {
  		String[] strs={"Mary","had","A","little","lamb"};
  		Pair<String> m=ArrayAlg.minMax(strs);
  		System.out.println("最小值："+m.getMin());
  		System.out.println("最大值："+m.getMax());
  	}
  }
  class ArrayAlg{
  	public static <T extends Comparable> Pair<T> minMax(T[] strs){
  		if(strs==null||strs.length==0) return null;
  		T min=strs[0];
  		T max=strs[0];
  		for(int i=1;i<strs.length;i++) {
  			if(min.compareTo(strs[i])>0) min=strs[i];
  			if(max.compareTo(strs[i])<0) max=strs[i];
  		}
  		return new Pair<T>(min, max);
  	}
  }
  class Pair<T> {
  	private T min;
  	private T max;
  	public T getMin() {
  		return min;
  	}
  	public void setMin(T min) {
  		this.min = min;
  	}
  	public T getMax() {
  		return max;
  	}
  	public void setMax(T max) {
  		this.max = max;
  	}
  	public Pair(T min, T max) {
  		this.min = min;
  		this.max = max;
  	}
  }
  ```

  - 泛型需要通过类型擦除的步骤，在虚拟机中的类型都是普通类型；桥方法

  - 注意事项

    - 虚拟机中没有泛型，只有普通方法和类
    - 所有的类型参数都会替换为他们的限定类型；比如<T extends Comparable & Serializable>这样的，下面出现的T泛型都会替换为限定类型Comparable
    - 会合成桥方法来保持多态
    - 为保持类型的安全性，必要时会插入强制类型转换

  - 泛型的限制

    - 不能用基本类型实例化类型参数（<类型>这叫类型变量）
    - 运行时类型查询只适用于原始类型
    - 不能创建参数化类型的数组
    - 不能实例化类型变量
    - 不能构造泛型数组
    - 泛型类的静态上下文中类型变量无效
    - 不能抛出或捕获泛型类的实例

  - 通配符

    - ？问号为通配符，pair<? extends Employee>表示传入的类型必须为Employee的子类；Pair<? super Manager>表示传入的类型必须为Manager的父类 ；跟<T extends Comparable & Serializable>一样的用法

    - 通配符与T泛型的区别

      ```java
      public static <T> boolean hasNulls(Pair<T> p){}
      public static boolean hasNulls(pari<?> p){}
      //两种泛型表达方式都是等价的
      ```

  - 练手程序：兼容Employee与Manager父子类的泛型计算，打印Manager数组中的最高工资、最低工资的姓名

    ```java
    public class PairTest2 {
    	public static void main(String[] args) {
    		Manager ceo=new Manager("Dai bin", 10000, 2000, 12, 15);
    		Manager cfo=new Manager("Yu huan", 900000, 2030, 2, 25);
    		Pair<Manager> buddies=new Pair<Manager>(ceo,cfo);
    		printBuddies(buddies);
    		ceo.setSalary(400000);
    		cfo.setSalary(30000);
    		Manager[] managers= {ceo,cfo};
    		Pair<Employee> result=new Pair<Employee>();
    		minMaxSalary(managers, result);
    		System.out.println("工资最低者姓名："+result.getMin().getName()+" ,工资最高者姓名："+result.getMax().getName());
    	}
    	public static void minMaxSalary(Manager[] a,Pair<? super Manager> result) {
    		if(a.length==0) return;
    		Manager min=a[0];
    		Manager max=a[0];
    		for(int i=1;i<a.length;i++) {
    			if(min.getSalary()>a[i].getSalary()) min=a[i];
    			if(max.getSalary()<a[i].getSalary()) max=a[i];
    		}
    		result.setMin(min);
    		result.setMax(max);
    	}
    	public static void maxMinSalary(Manager[] a,Pair<? super Manager> result) {
    		minMaxSalary(a, result);
    		PairAlg.swapHelper(result);
    	}
    	public static void printBuddies(Pair<? extends Employee> p) {
    		Employee min=p.getMin();
    		Employee max=p.getMax();
    		System.out.println(min.getName()+" and "+max.getName()+" are buddies.");
    	}
    }
    class PairAlg{
    	public static boolean hasNull(Pair<?> p) {
    		return p.getMin()==null||p.getMax()==null;
    	}
    	public static void swap(Pair<?> p) {
    		swapHelper(p);
    	}
    	public static <T> void swapHelper(Pair<T> p) {
    		T t=p.getMin();
    		p.setMin(p.getMax());
    		p.setMax(t);
    	}
    }
    ```

    

### 十九、Vim使用

- **第一部分：一般模式可用的光标移动、复制粘贴、搜索替换等**

  | 移动光标的方法     |                                                              |
  | :----------------- | ------------------------------------------------------------ |
  | h 或 向左箭头键(←) | 光标向左移动一个字符                                         |
  | j 或 向下箭头键(↓) | 光标向下移动一个字符                                         |
  | k 或 向上箭头键(↑) | 光标向上移动一个字符                                         |
  | l 或 向右箭头键(→) | 光标向右移动一个字符                                         |
  | [Ctrl] + [f]       | 屏幕『向下』移动一页，相当于 [Page Down]按键 (常用)          |
  | [Ctrl] + [b]       | 屏幕『向上』移动一页，相当于 [Page Up] 按键 (常用)           |
  | [Ctrl] + [d]       | 屏幕『向下』移动半页                                         |
  | [Ctrl] + [u]       | 屏幕『向上』移动半页                                         |
  | +                  | 光标移动到非空格符的下一行                                   |
  | -                  | 光标移动到非空格符的上一行                                   |
  | n< space>          | 那个 n 表示『数字』，例如 20 。按下数字后再按空格键，光标会向右移动这一行的 n 个字符。 |
  | 0 或功能键[Home]   | 这是数字『 0 』：移动到这一行的最前面字符处 (常用)           |
  | $ 或功能键[End]    | 移动到这一行的最后面字符处(常用)                             |
  | H                  | 光标移动到这个屏幕的最上方那一行的第一个字符                 |
  | M                  | 光标移动到这个屏幕的中央那一行的第一个字符                   |
  | L                  | 光标移动到这个屏幕的最下方那一行的第一个字符                 |
  | G                  | 移动到这个档案的最后一行(常用)                               |
  | nG                 | n 为数字。移动到这个档案的第 n 行。例如 20G 则会移动到这个档案的第 20 行(可配合 :set nu) |
  | gg                 | 移动到这个档案的第一行，相当于 1G 啊！(常用)                 |
  | n< Enter>          | n 为数字。光标向下移动 n 行(常用)                            |

  | 搜索替换 |                                                              |
  | :------- | ------------------------------------------------------------ |
  | /word    | 向光标之下寻找一个名称为 word 的字符串。例如要在档案内搜寻 vbird 这个字符串，就输入 /vbird 即可！(常用) |
  | ?word    | 向光标之上寻找一个字符串名称为 word 的字符串。               |
  | n        | 这个 n 是英文按键。代表重复前一个搜寻的动作。举例来说， 如果刚刚我们执行 /vbird 去向下搜寻 vbird 这个字符串，则按下 n 后，会向下继续搜寻下一个名称为 vbird 的字符串。如果是执行 ?vbird 的话，那么按下 n 则会向上继续搜寻名称为 vbird 的字符串！ |
  | N        | 这个 N 是英文按键。与 n 刚好相反，为『反向』进行前一个搜寻动作。例如 /vbird 后，按下 N 则表示『向上』搜寻 vbird 。 |

  | 删除、复制与粘贴 |                                                              |
  | :--------------- | ------------------------------------------------------------ |
  | x, X             | 在一行字当中，x 为向后删除一个字符 (相当于 [del] 按键)， X 为向前删除一个字符(相当于 [backspace] 亦即是退格键) (常用) |
  | nx               | n 为数字，连续向后删除 n 个字符。举例来说，我要连续删除 10 个字符， 『10x』。 |
  | dd               | 删除游标所在的那一整行(常用)                                 |
  | ndd              | n 为数字。删除光标所在的向下 n 行，例如 20dd 则是删除 20 行 (常用) |
  | d1G              | 删除光标所在到第一行的所有数据                               |
  | dG               | 删除光标所在到最后一行的所有数据                             |
  | d$               | 删除游标所在处，到该行的最后一个字符                         |
  | d0               | 那个是数字的 0 ，删除游标所在处，到该行的最前面一个字符      |
  | yy               | 复制游标所在的那一行(常用)                                   |
  | nyy              | n 为数字。复制光标所在的向下 n 行，例如 20yy 则是复制 20 行(常用) |
  | y1G              | 复制游标所在行到第一行的所有数据                             |
  | yG               | 复制游标所在行到最后一行的所有数据                           |
  | y0               | 复制光标所在的那个字符到该行行首的所有数据                   |
  | y$               | 复制光标所在的那个字符到该行行尾的所有数据                   |
  | p, P             | p 为将已复制的数据在光标下一行贴上，P 则为贴在游标上一行！举例来说，我目前光标在第 20 行，且已经复制了 10 行数据。则按下 p 后， 那 10 行数据会贴在原本的 20 行之后，亦即由 21 行开始贴。但如果是按下 P 呢？那么原本的第 20 行会被推到变成 30 行。(常用) |
  | J                | 将光标所在行与下一行的数据结合成同一行                       |
  | c                | 重复删除多个数据，例如向下删除 10 行，[ 10cj ]               |
  | u                | 复原前一个动作。(常用)                                       |
  | [Ctrl]+r         | 重做上一个动作。(常用)                                       |

  **第二部分：一般模式切换到编辑模式的可用的按钮说明**

  | 进入输入或取代的编辑模式 |                                                              |
  | :----------------------- | ------------------------------------------------------------ |
  | i, I                     | 进入输入模式(Insert mode)：i 为『从目前光标所在处输入』， I 为『在目前所在行的第一个非空格符处开始输入』。(常用) |
  | a, A                     | 进入输入模式(Insert mode)：a 为『从目前光标所在的下一个字符处开始输入』， A 为『从光标所在行的最后一个字符处开始输入』。(常用) |
  | o, O                     | 进入输入模式(Insert mode)：这是英文字母 o 的大小写。o 为『在目前光标所在的下一行处输入新的一行』；O 为在目前光标所在处的上一行输入新的一行！(常用) |
  | r, R                     | 进入取代模式(Replace mode)：r 只会取代光标所在的那一个字符一次；R会一直取代光标所在的文字，直到按下 ESC 为止；(常用) |
  | [Esc]                    | 退出编辑模式，回到一般模式中(常用)                           |

  **第三部分：一般模式切换到指令行模式的可用的按钮说明**

  | 指令行的储存、离开等指令                                     |                                                              |
  | :----------------------------------------------------------- | ------------------------------------------------------------ |
  | :w                                                           | 将编辑的数据写入硬盘档案中(常用)                             |
  | :w!                                                          | 若文件属性为『只读』时，强制写入该档案。不过，到底能不能写入， 还是跟你对该档案的档案权限有关啊！ |
  | :q                                                           | 离开 vi (常用)                                               |
  | :q!                                                          | 若曾修改过档案，又不想储存，使用 ! 为强制离开不储存档案。    |
  | 注意一下啊，那个惊叹号 (!) 在 vi 当中，常常具有『强制』的意思～ |                                                              |
  | :wq                                                          | 储存后离开，若为 :wq! 则为强制储存后离开 (常用)              |
  | ZZ                                                           | 这是大写的 Z 喔！若档案没有更动，则不储存离开，若档案已经被更动过，则储存后离开！ |
  | :w [filename]                                                | 将编辑的数据储存成另一个档案（类似另存新档）                 |
  | :r [filename]                                                | 在编辑的数据中，读入另一个档案的数据。亦即将 『filename』 这个档案内容加到游标所在行后面 |
  | :n1,n2 w [filename]                                          | 将 n1 到 n2 的内容储存成 filename 这个档案。                 |
  | :! command                                                   | 暂时离开 vi 到指令行模式下执行 command 的显示结果！例如 『:! ls /home』即可在 vi 当中看 /home 底下以 ls 输出的档案信息！ |
  | :set nu                                                      | 显示行号，设定之后，会在每一行的前缀显示该行的行号           |
  | :set nonu                                                    | 与 set nu 相反，为取消行号！                                 |

![image-20210607113637450](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607113637450.png)