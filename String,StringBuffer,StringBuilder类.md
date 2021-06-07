（1）创建一个字符串有两种方法，可以直接复制，String str="daibin",这种方法"daibin"是存储在公共池的，如果在创建一个String str2="daibin"
	则只是在公共池的"daibin"上在创建一个引用
	2.还可以用String的构造方法来创建，例如：String str=new String("daibin");用new出来的字符串是在堆之中，如果在创建一个
	String str2=new String("daibin");这是创建的两个不同的字符串对象
（2）String中的equesl()，比较的是两个对象的值是否一样，如果一样就返回true，==比较的是两个对象的引用地址是否一样，在公共池的相同字符串引用地址一样，在堆中的相同字符串引用地址不一样，值一样;如果值一样，hashcode()的返回值也一样；
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
（2）StringBuilder与StringBuffer的区别：StringBuilder不是线程安全（不是线程同步）的，意思是在多线程下，用了StringBuilder，可能其他线程的值不能保持一致性等，StringBuffer就是线程安全的；但是StringBuilder的执行速度快与StringBuffer，无特殊情况还是用的StringBuilder；

下面是String，StringBuilder，StringBuffer的父类以及继承的接口关系

> ![img](https://www.runoob.com/wp-content/uploads/2013/12/java-string-20201208.png)

（3）三个类的方法大多数都是差不多的，StringBuilder和StringBuffer基本包括了String的方法

（4）StringBuilder append(Object obj)：把任意类型的数据追加在StringBuilder对象的末尾

（5）StringBuilder reverse()：将StringBuilder对象反转，例如："123"变成“321”

（6）delete(int start, int end)：删除该序列中的从start（包含）到end（不包含）的字符；

（7）StringBuilder insert(int offset,Object obj)：把obj数据（可以是其他数据，很多构造函数）插入到字符串的offset位置

（8）public StringBuilder replace(int start, int end,String str)：把str到start（包含）和end（不包含）之间的字符串替换为str，这个似乎只能把String替换为StringBuilder，没有其他类型的构造函数