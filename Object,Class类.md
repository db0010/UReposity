### һ��Object��

��1��clone():����Ŀ�¡����������ֵ�Ƕ���Ŀ�������ΪObject��û��ʵ��Cloneable�ӿڣ�������ʹ��clone()��ʱ����Ҫ�̳�Cloneable�ӿ�,��Ȼ���׳��쳣��
	ʹ��clone()������������ǳ��������ֻ�ǿ�����������ã�Դ����ı�ֵʱǳ���������ֵҲ����ű仯����û�д���һ��	�µĶ�����ڴ�ռ䣬���ض���ķ�������Ϊprotected,�����������д��	ʱ����Ҫ��Ϊpublic;��������Ǵ�����һ���µĶ���ĵ�ַ�ռ䣬����Դ�����޸���ֵ������Ķ����ֵҲ����䣬�������������Ķ�����,����ƺ�����public��

```java
protected native Object clone() throws CloneNotSupportedException;
```

��2��equals():�Ƚ����������Ƿ�һ���������ǱȽϵĶ�����ڴ��ַ��String����д��equals�����������Ƚϵ��Ƕ����ֵ��������ֵΪbool�ͣ�һ����Ҫ��дObeject���equals()�Ļ�����Ҫ��дhashCode()����������涨����ȵĶ����hasCodeֵҲҪ���

```java
  public boolean equals(Object obj) {
        return (this == obj);
    }
```

��3��finalize():Object finalize() ��������ʵ�����������������յ�ʱ�����Ĳ������� GC (����������) ȷ�������ڶԸö�����и�������ʱ������������������ͻ�	�����������

```java
 protected void finalize() throws Throwable { }
```

��4��getClass():���ض���������࣬����:String str2;str1.getClass()�ķ���ֵ���ǣ� class java.lang.String

```java
public final native Class<?> getClass();
```

��5��hashCode():���ض����hashֵ��������hash���е�λ�ã���һ������

```java
    public native int hashCode();
```

��6��toString():�����һ���Զ�����ȥ���õĻ��᷵��һ���ַ�����ʽ��"����+@+ʮ�����Ƶ�hashֵ"�������String��Integer��Щȥ���õĻ��ͻ᷵�ظ�ֵ����	��������

```java
 public String toString() {
     return getClass().getName() + "@" + 				Integer.toHexString(hashCode());
    }
```

��7��notify():���Ѹö���Ķ���������ϵȴ��ĵ����߳�

```java
 public final native void notify();
```

��8��notifyAll():���Ѹö���Ķ���������ϵ������߳�

```java
 public final native void notifyAll();
```

��9��wait()��wait(int timeout)��wait(int timeout,int nanos):

```java
 ////timeoutΪ�ȴ������ʱ�䣬��λ�Ǻ���
public final native void wait(long timeout) throws InterruptedException;
```

```java
//timeoutΪ�ȴ������ʱ�䣬��λ�Ǻ��룬nanosΪ����ĵȴ�ʱ��
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
        wait(0);//�ȴ�ʱ��Ϊ0��ʾû�������̻߳��ѵĻ���һֱ�ȴ���ȥ
    }
```



### ����Class��

��1��������Ϊjava.Lang;ÿһ���඼��һ��class����,�ഴ��������ɹ���.class�ļ��оͲ���һ��Class���󣬱�ʾ������������Ϣ��

��2��ʹ��Class������ֳ��÷�����

- getClass����������������Ķ��󣬷���ֵ�Ǹö����������һ������Ӧ���ǲ�ͬ������������
- Class.forName("�������")����ͨ��Class��ľ�̬����forName()����ȡ������Ķ��󣬷���ֵ�Ǹö���
- getName����������������Ķ��󣬷���ֵ����������
- .class�����������࣬����ֵֵ�Ǹ���Ķ���
- newInstance����������������Ķ��󣬷���ֵֵ�Ǹ������һ������
- ����isEnum()��getTypeName()��getClassLoader()��getFileds()�ȵȷ���

### ����String��

#### 1.String

����һ���ַ��������ַ���������ֱ�Ӹ��ƣ�String str="daibin",���ַ���"daibin"�Ǵ洢�ڹ����صģ�����ڴ���һ��String str2="daibin"	����ֻ���ڹ����ص�"daibin"���ڴ���һ������

- �����ص�Ŀ����Ϊ������ַ��������ܣ���ΪString����final���εģ�Ϊ���ɱ�ģ�Ϊ�˱��ⴴ��ͬ��ֵ���ַ���������ʹ���˳����ؼ���������������JVM�ķ�������

- �������ֽо�̬���������ŵĶ�����������Ψһ��Ԫ�أ�����class����static�����������أ�����������һ�����Ǳ������߳�ջ�����

- ջ��ջ�е��������߳�˽�еģ�ÿ���̶߳�����һ��ջ��ջ��ֻ��������������ͺ��Զ����������ã��������Ǵ���ڶ��У���ջ��Ϊ�����֣��������ͱ�������ִ�л��������ġ�����ָ��������Ų���ָ���ջ��������ݴ�С�����������ǿ���ȷ���ģ���û������ָ������ʱ��������ݾͻ��Զ���ʧ

- �ѣ����д�ŵĶ��Ƕ��󣬲���Ż����������ͺͶ�������ã������������ռ���������գ�����������ںʹ�С�ǲ�ȷ����

- interm()��������֤�������ͬһ��������������ַ����Ǵӳ������л�ȡ��

  ```java
  	String s1 = "Hello";
      String s2 = new StringBuffer("He").append("llo").toString();
      String s3 = s2.intern();
      System.out.println("s1 == s2? " + (s1 == s2)); // false
      System.out.println("s1 == s3? " + (s1 == s3)); // true
  ```

- String str1 = new String("A"+"B") ; �ᴴ�����ٸ�����?

  str1��

  �ַ��������أ�"A","B","AB" : 3��

  �ѣ�new String("AB") ��1��

  ���ã� str1 ��1��

  �ܹ� �� 5��

2.��������String�Ĺ��췽�������������磺String str=new String("daibin");��new�������ַ������ڶ�֮�У�����ڴ���һ��
	String str2=new String("daibin");���Ǵ�����������ͬ���ַ�������
��2��String�е�equesl()���Ƚϵ������������ֵ�Ƿ�һ�������һ���ͷ���true��==�Ƚϵ���������������õ�ַ�Ƿ�һ�����ڹ����ص���ͬ�ַ������õ�ַһ�����ڶ��е���ͬ�ַ������õ�ַ��һ����ֵһ��;���ֵһ����hashcode()�ķ���ֵҲһ����

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

��3��length()��str.length(),����str�ַ����ĳ��ȣ�"daibin"����6��
��4��format();������ʽ���ַ��������磺str.format("%.3f,double����"+"%s",doublevar,string)
(5)compareTo():str1.compareTo(str2),����ֵС��0����str1����С��str2�ĸ�ֵ������ֵ����0���������ַ������Ⱥ�������ȣ������㣬str1��str2�����ֵ
(6)String����д��Object���equesl()��ͬʱҲ��д��hashCode()����
��7��split():��һ�������ĺ����������ģ�һ�������ľ��Ƿָ�������string�ͣ����������ĵڶ��������Ƿֳɼ��Σ���int�ͣ�����ֵ���Ƿָ����string
	ע�⣺���ָ�����. $ | *��ת���ַ�����Ҫ����\\
��8��subString():��һ�����������������ģ�substring(int beginIndex),����ʼ������ʼ�����ַ���������Ҫ�����±���ʼ�±꣬��0��ʼ��
		subStrng(int beginIndex,int lastIndex),����ʼ���ڶ���������Ϊ���ַ����������������±�
��9��toCharArray():str.toCharArray()������ֵ��str�ַ�����ɵ��ַ����飬��ӡ�ַ������ʱ�򣬴�ӡ�������ƾ��Ǵ�ӡ���е��ַ������������ַ���
��10��trim():���ظ��ַ�����ǰ���հ׺ͺ󵼿հף����������û����˻��������������������棬str.trim();
��11��contains():�ж��ַ������Ƿ����ָ�����ַ������ַ���������ֵΪtrue����false�����磺str.contains("ads");str.contains("a");�����õ�˫����
��12��isEmpty():�ж��ַ����Ƿ�Ϊ�գ�����ֵΪbool��

StringBuffer��StringBuilder
��1��String���ַ����������ǲ��ܱ��޸ĵģ�ÿ�α��޸Ķ��Ǵ�����һ���µ��ַ������󣬶�StringBuilder��StringBuffer���ǿ����޸��ַ�������Ķ���

#### 2.StringBuilder��StringBuffer������

StringBuilder�����̰߳�ȫ�������߳�ͬ�����ģ���˼���ڶ��߳��£�����StringBuilder�����������̵߳�ֵ���ܱ���һ���Եȣ�StringBuffer�����̰߳�ȫ�ģ�����StringBuilder��ִ���ٶȿ���StringBuffer����������������õ�StringBuilder��

- StringBulider��String��������ǣ�String��value�ַ���������final���εģ���Ҫ�����ַ���ʱ�����ҳ�������û��ʱ���ͻ����´���һ���µ�String�����кܶ��ַ�����Ҫ����ʱ���ͻ�Ƚ�Ӱ�����ܣ���StringBuilder��value�ַ�������һ�������������Ϳ������ĸ��ģ��ͱȽ��ʺ���Ҫ���������ַ����ĳ���
- StringBuilder��StringBuffer�����������ķ�����������һ�µģ�ֻ��StringBuffer�ķ����϶�����synchronized�ؼ��֣�

������String��StringBuilder��StringBuffer�ĸ����Լ��̳еĽӿڹ�ϵ

> ![img](https://www.runoob.com/wp-content/uploads/2013/12/java-string-20201208.png)

��3��������ķ�����������ǲ��ģ�StringBuilder��StringBuffer����������String�ķ���

��4��StringBuilder append(Object obj)�����������͵�����׷����StringBuilder�����ĩβ

> ![image-20210425170726408](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170726408.png)

��5��StringBuilder reverse()����StringBuilder����ת�����磺"123"��ɡ�321��

> ![image-20210425170750376](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170750376.png)

��6��delete(int start, int end)��ɾ���������еĴ�start����������end�������������ַ���

> ![image-20210425170602538](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170602538.png)

��7��StringBuilder insert(int offset,Object obj)����obj���ݣ��������������ݣ��ܶ๹�캯�������뵽�ַ�����offsetλ�ã��ƺ�offset�±��ʾ�����ݵĵڼ�����߲���

> ![image-20210425170417802](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425170417802.png)

��8��public StringBuilder replace(int start, int end,String str)����str�ַ�����������ַ�����star�±꣨��������end�±꣨��������֮����ַ�������ƺ�ֻ�ܰ�String�滻ΪStringBuilder��û���������͵Ĺ��캯��

> ![image-20210425165245776](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210425165245776.png)