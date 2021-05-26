### 一、Object类

（1）clone():对象的克隆方法，返回值是对象的拷贝；因为Object类没有实现Cloneable接口，所以在使用clone()的时候需要继承Cloneable接口,不然会抛出异常；
	使用clone()拷贝对象属于浅拷贝就是只是拷贝对象的引用，源对象改变值时浅拷贝对象的值也会跟着变化，并没有创建一个	新的对象的内存空间，返回对象的访问类型为protected,所以在深拷贝重写的	时候需要改为public;；深拷贝就是创建了一个新的对象的地址空间，就算源对象修改了值，深拷贝的对象的值也不会变，这是两个独立的对象了,深拷贝似乎就是public的

```java
protected native Object clone() throws CloneNotSupportedException;
```

（2）equals():比较两个对象是否一样，这里是比较的对象的内存地址（String类重写了equals（）方法，比较的是对象的值），返回值为bool型，一个类要重写Obeject类的equals()的话还需要重写hashCode()方法；这里规定了相等的对象的hasCode值也要相等

```java
  public boolean equals(Object obj) {
        return (this == obj);
    }
```

（3）finalize():Object finalize() 方法用于实例被垃圾回收器回收的时触发的操作。当 GC (垃圾回收器) 确定不存在对该对象的有更多引用时，对象的垃圾回收器就会	调用这个方法

```java
 protected void finalize() throws Throwable { }
```

（4）getClass():返回对象的所属类，例如:String str2;str1.getClass()的返回值就是： class java.lang.String

```java
public final native Class<?> getClass();
```

（5）hashCode():返回对象的hash值，代表在hash表中的位置，是一个整数

```java
    public native int hashCode();
```

（6）toString():如果是一个自定义类去调用的话会返回一个字符串格式是"类名+@+十六进制的hash值"，如果是String、Integer这些去调用的话就会返回该值的字	符串类型

```java
 public String toString() {
     return getClass().getName() + "@" + 				Integer.toHexString(hashCode());
    }
```

（7）notify():唤醒该对象的对象监视器上等待的单个线程

```java
 public final native void notify();
```

（8）notifyAll():唤醒该对象的对象监视器上的所有线程

```java
 public final native void notifyAll();
```

（9）wait()、wait(int timeout)、wait(int timeout,int nanos):

```java
 ////timeout为等待的最大时间，单位是毫秒
public final native void wait(long timeout) throws InterruptedException;
```

```java
//timeout为等待的最大时间，单位是毫秒，nanos为额外的等待时间
public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }
```

```java
 public final void wait() throws InterruptedException {
        wait(0);//等待时间为0表示没有其他线程唤醒的话会一直等待下去
    }
```



### 二、Class类

（1）所属包为java.Lang;每一个类都有一个class对象,类创建并编译成功在.class文件中就产生一个Class对象，表示这个类的类型信息；

（2）使用Class类的三种常用方法：

- getClass（）：调用者是类的对象，返回值是该对象所在类的一个对象（应该是不同的类两个对象）
- Class.forName("类的名字")：是通过Class类的静态方法forName()来获取参数类的对象，返回值是该对象
- getName（）：调用者是类的对象，返回值类对象的名称
- .class：调用者是类，返回值值是该类的对象
- newInstance（）：调用者是类的对象，返回值值是该类的另一个对象
- 还有isEnum()、getTypeName()、getClassLoader()、getFileds()等等方法

### 三、String类

#### 1.String

创建一个字符串有两种方法，可以直接复制，String str="daibin",这种方法"daibin"是存储在公共池的，如果在创建一个String str2="daibin"	；则只是在公共池的"daibin"上在创建一个引用

- 常量池的目的是为了提高字符串的性能，因为String是用final修饰的，为不可变的，为了避免创建同样值得字符串，所以使用了常量池技术，常量池是在JVM的方法区中

- 方法区又叫静态区，里面存放的都是整个程序唯一的元素，比如class对象，static变量，常量池；方法区跟堆一样都是被所有线程栈共享的

- 栈：栈中的数据是线程私有的，每个线程都会有一个栈，栈中只保存基本数据类型和自定义对象的引用（对象本身还是存放在堆中），栈分为三部分：基本类型变量区、执行环境上下文、操作指令区（存放操作指令）；栈里面的数据大小和生命周期是可以确定的，当没有引用指向数据时，这个数据就会自动消失

- 堆：堆中存放的都是对象，不存放基本数据类型和对象的引用；对象由垃圾收集器负责回收，因此生命周期和大小是不确定的

- interm()方法：保证结果来自同一个常量池里，就是字符串是从常量池中获取的

  ```java
  	String s1 = "Hello";
      String s2 = new StringBuffer("He").append("llo").toString();
      String s3 = s2.intern();
      System.out.println("s1 == s2? " + (s1 == s2)); // false
      System.out.println("s1 == s3? " + (s1 == s3)); // true
  ```

- String str1 = new String("A"+"B") ; 会创建多少个对象?

  str1：

  字符串常量池："A","B","AB" : 3个

  堆：new String("AB") ：1个

  引用： str1 ：1个

  总共 ： 5个

2.还可以用String的构造方法来创建，例如：String str=new String("daibin");用new出来的字符串是在堆之中，如果在创建一个
	String str2=new String("daibin");这是创建的两个不同的字符串对象
（2）String中的equesl()，比较的是两个对象的值是否一样，如果一样就返回true，==比较的是两个对象的引用地址是否一样，在公共池的相同字符串引用地址一样，在堆中的相同字符串引用地址不一样，值一样;如果值一样，hashcode()的返回值也一样；

```java
 public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
```

（3）length()：str.length(),返回str字符串的长度，"daibin"返回6；
（4）format();创建格式化字符串，例如：str.format("%.3f,double变量"+"%s",doublevar,string)
(5)compareTo():str1.compareTo(str2),返回值小于0，则str1长度小于str2的负值；返回值等于0，则两个字符串长度和内容相等；大于零，str1比str2大的正值
(6)String类重写了Object类的equesl()，同时也重写了hashCode()方法
（7）split():有一个参数的和两个参数的，一个参数的就是分隔符，是string型；两个参数的第二个参数是分成几段，是int型，返回值都是分隔后的string
	注意：当分隔符是. $ | *等转义字符是需要加上\\
（8）subString():有一个参数和两个参数的，substring(int beginIndex),从起始索引开始到父字符串结束，要包括下标起始下标，从0开始的
		subStrng(int beginIndex,int lastIndex),从起始到第二个参数作为子字符串，不包括结束下标
（9）toCharArray():str.toCharArray()，返回值是str字符串组成的字符数组，打印字符数组的时候，打印数组名称就是打印所有的字符连接起来的字符串
（10）trim():返回该字符串的前导空白和后导空白，可以用在用户的账户输入框和密码输入框等里面，str.trim();
（11）contains():判断字符串中是否包含指定的字符或者字符串，返回值为true或者false，例如：str.contains("ads");str.contains("a");都是用的双引号
（12）isEmpty():判断字符串是否为空，返回值为bool型

StringBuffer与StringBuilder
（1）String的字符串对象本身是不能被修改的，每次被修改都是创建了一个新的字符串对象，而StringBuilder和StringBuffer就是可以修改字符串本身的对象

#### 2.StringBuilder与StringBuffer的区别

StringBuilder不是线程安全（不是线程同步）的，意思是在多线程下，用了StringBuilder，可能其他线程的值不能保持一致性等，StringBuffer就是线程安全的；但是StringBuilder的执行速度快与StringBuffer，无特殊情况还是用的StringBuilder；

- StringBulider与String的区别就是：String的value字符数组是用final修饰的，当要更改字符串时，并且常量池中没有时，就会重新创建一个新的String对象，有很多字符串需要更改时，就会比较影响性能；而StringBuilder的value字符数组是一个变量，变量就可以随便的更改，就比较适合需要大量更改字符串的场景
- StringBuilder与StringBuffer的区别：两个的方法基本都是一致的，只是StringBuffer的方法上都加了synchronized关键字；

下面是String，StringBuilder，StringBuffer的父类以及继承的接口关系

> ![img](https://www.runoob.com/wp-content/uploads/2013/12/java-string-20201208.png)

（3）三个类的方法大多数都是差不多的，StringBuilder和StringBuffer基本包括了String的方法

（4）StringBuilder append(Object obj)：把任意类型的数据追加在StringBuilder对象的末尾

> ![image-20210425170726408](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170726408.png)

（5）StringBuilder reverse()：将StringBuilder对象反转，例如："123"变成“321”

> ![image-20210425170750376](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170750376.png)

（6）delete(int start, int end)：删除该序列中的从start（包含）到end（不包含）的字符；

> ![image-20210425170602538](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170602538.png)

（7）StringBuilder insert(int offset,Object obj)：把obj数据（可以是其他数据，很多构造函数）插入到字符串的offset位置；似乎offset下标表示是数据的第几个后边插入

> ![image-20210425170417802](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170417802.png)

（8）public StringBuilder replace(int start, int end,String str)：用str字符串替代调用字符串的star下标（包括）到end下标（不包括）之间的字符，这个似乎只能把String替换为StringBuilder，没有其他类型的构造函数

> ![image-20210425165245776](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425165245776.png)