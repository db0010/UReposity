### 一、Spring

12月28日：（1）依赖注入中的构造注入，<constructor-arg></constructor-arg>通过的是pojo中的有参构造函数依赖注入中的set注入，<property name="" value=""></property>通过的是pojo中的无参构造函数（最常用的）
2021年1月2日： 

（1）id属性和name属性都是标识一个bean的，一个bean里面只能有一个id，但是可以有多个name
（2）scope属性有prototype（原型），每次获得的bean都不同，hashcode()不一样scope属性singleton（单例），每次获得的bean都是同一个,hashcode()一样
（3）使用@Autowired注解自动装配bean依赖，暂时只知道省略了set()方法，使用的是反射机制
1月18日：（1）在一个maven项目中新建spring时，先配置pom.xml文件，先添加<dependencies>标签，再在标签内部导入spring的依赖包，5.2.0和4.2.6下载了
	下面是pom.xml文件的<dependencies>标签里面



```java
<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.2.0.RELEASE</version>
        </dependency>
```

​	下面是加在appplicationContext文件里的

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd">

<!-- 在这里加入内容 -->
</beans>
```

​	（2）配置文件中的注释是用的<!--内容-->
​	  (3)在resourse的目录右键新建xml里面的spring配置文件，会自动生成bean上面的配置信息
​	（4）ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");参数是字符串，内容是上下文配置文件的名称
​    		Dog dog= (Dog) context.getBean("Dog");通过上下文对象的getBean()方法获取工厂里的Dog对象，可以通过强制转换类型
​		Dog dog= context.getBean("Dog",Dog.class);也可以通过getBean()的两个参数的构造重载函数，第二个参数的反射来变为Dog的类
1月19日：（1）bean标签必须有class属性，指定类的意思
​	bean标签的常用属性：
​	id和name：两个属性都是为了唯一标识一个bean，两者不能一样
​	scope：该属性的值有singleton(获取的bean对象都是同一个)和prototype(获取的bean对象不是同一个，每次都会创建一个新的bean),
​		用法:<bean id="userFactory" class="lzgsea.factory.UserFactory"></bean>	
​			<bean id="user" factory-bean="userFactory" factory-method="createUser"></bean>
​	init-method和destory-method:分别在bean初始化之后调用和在bean销毁之前调用
​	（2）装配bean的方法有三种：都是用在多个bean有依赖关系的情况下使用以下的方式
​	1.手动装配：在People的下面<property>标签上添加Cat和dog，相当于就帮Cat和dog绑定在了People上；这种方法需要添加每个bean的set方法
​			因为似乎property标签就是通过set注入的依赖；主要操作是有property标签的ref属性实现的，引用的意思
​	2.通过bean标签的autowire属性自动装配：该方法还是要每个bean的set方法；该属性有一下几个值
​		byName:在配置文件中还要设置id或者name属性,原理实在上下文配置文件中寻找与自己的set方法名字一样的id或者name
​		byType:通过class类型去寻找bean，不需要设置id或者name属性；类型一样的bean只能有一个，否则无法自动装配
​		constructor:通过People类里面设置参数为cat加dog的构造函数实现的自动装配
​		autodetect：通过bean的内省机制来决定使用byType方式还是constructor方式
​		default：由上级标签的default-autowire属性确定
​		no ： 默认值，不进行自动装配
​	3.通过@Autowired注解自动装配：在People类里面定义其他类的对象的时候使用

​		这种方法最简便的，既不用写set方法，也不用property标签和bean的autowire属性

- 使用Autowired自动装配，必须在Spring的配置文件中使用context:annotation-config/来告诉Spring需要进行自动装配扫描（AutowiredAnnotationBeanPostProcessor不推荐使用）

- Autowired默认按类型进行匹配，当匹配到多个满足条件的bean时，再按照属性名和bean的id进行匹配，如果仍然有多个匹配上或者没有一个匹配上，则抛出异常，提示自动装配失败

- 在使用Autowired时，可以使用Qualifier注解，显式的指定，当冲突发生时，使用那个id对应的bean

- Autowired注解自动装配功能完成的是依赖的自动注入，因此，在一个bean中，它依赖的bean可以通过自动注入的方式完成而不需要显式的为它的属性进行注入。但是这些依赖的bean仍然不能省略，还是要在Spring中进行配置，省略的仅仅是bean属性的注入配置代码

4.@Resource注解在功能和目的上，等效于Autowried+Qualifier注解；默认按名称查找bean，找不到时按类型匹配；Qualifier注解是用来给Autowried（“名称”）注解设置为按名称查找的；

5.几个上下文配置文件中的配置：

- ### <context:component-scan base-package="com.daibin.myOpencv" /> 

  <context:component-scan/>的作用是注解工作起来。 它的base-package属性指定了需要扫描的类包，类包及其递归子包中所有的类都会被处理;不但启用了对类包进行扫描以实施注释驱动 Bean 定义的功能，同时还启用了注释驱动自动注入的功能（即还隐式地在内部注册了 AutowiredAnnotationBeanPostProcessor 和 CommonAnnotationBeanPostProcessor）；有了scan扫描配置，就不需要context:annotation-config配置。

- <mvc:annotation-driven />

使用了annotation-driven后，缺省使用DefaultAnnotationHandlerMapping 来注册handler method和request的mapping关系；AnnotationMethodHandlerAdapter来在实际调用handlermethod前对其参数进行处理。 

> 一开始我在写配置的时候，只写了<context:component-scan/>，并没有使用<mvc:annotation-driven/>，servlet拦截*.do，.do请求可以被正确捕捉和处理。
>
> 后来为了解决静态资源访问的问题，servlet改成了拦截所有请求，即/，并添加了默认的servlet，这时候*.do请求不能被控制器捕捉了，页面错误为404。直到添加了<mvc:annotation-driven/>之后，.do请求才又能被正确捕捉和处理。
>
> ```Java
> <context:component-scan base-package="com"></context:component-scan> 
> <mvc:annotation-driven/>  
> <mvc:resources mapping="/styles/**"
>     location="/WEB-INF/resource/styles/"/>  
> <mvc:default-servlet-handler/>  
> ```

在使用SpringMVC3时，需要对response到页面的数据进行编码设置则需要自定义注解数据格式化类来对页面传过来的字符串进行格式化。

```java
<bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter" >  
        <property name="messageConverters">  
             <list>  
                 <bean class = "org.springframework.http.converter.StringHttpMessageConverter">  
                    <property name = "supportedMediaTypes">  
                         <list>  
                             <value>text/plain;charset=UTF-8</value>  
                         </list>  
                    </property>  
                 </bean>  
             </list>  
        </property>  
</bean> 
```

同时设定**<mvc:message-converters> 标签，**设定字符集和json处理类

6.在配置文件中使用 context 命名空间之前，必须在 <beans> 元素中声明 context 命名空间。

7.@Component("id") 取代<bean class=" " id=" ">

1月20日：（1）要使用注解时，在上下文配置文件中要这样配置：
​	

```java
<beans
	xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd">
<context:annotation-config/>
```

​	（2）@required注解的功能及用法：功能是只检查属性是否已经设置而不会测试属性是否非空；且只能用用在setter方法上（不过好像过时了）
1月21日：（1）ioc容器依赖注入的方式有：xml文件、注解、java配置；java配置用的是AnnotationConfigApplicationContext这个上下文配置类，例子：
​	ApplicationContext context=new AnnotationConfigApplicationContext(MyConfigure.class);//参数很重要
​       	Cat cat1= (Cat) context.getBean("getCat");
​	(2)要自己写个MyConfigurejava类，这个类用@Configuration,类里面配置@bean;例子：
@Configuration
public class MyConfigure {
​    @Bean
​    public Cat getCat(){
​        return new Cat();
​    }
​	（3）pojo类需要用@Component注解，意思是成为了java配置类的一个组件
1月25日：（1）使用spring的AOP来处理程序的横向事务，下面是要使用aop的ApplicationContext的xml配置文件

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop" xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">
    
</beans>
```

​	（2）日志功能：要写在执行方法之前的日志，需要写个java类来继承implements MethodBeforeAdvice，需要重写void before(Method method, Object[] args, Object target)方法，第一个参数是纵向运行的方法，例如add(),delete()等，第二个参数是代理类需要传的参数数组，第三个类是代理类，代理类用的是.getClass()反射来获取的，再用getName()来获取源类的名字；卸载日志方法之后的类，要继承implements AfterReturningAdvice接口，实现的方法是四个参数的，相比于MethodBeforeAdivce要多了第一个参数返回值，void afterReturning(Object returnValue, Method method, Object[] objects, Object target)
​	（3）这个也可以用元数据配置的三种方法中的一个配置，UserService userService= (UserService) context.getBean("UserServiceImpl");在获取bean的时候是用的接口的对象，因为代理机制代理的是接口，而不是类，但是在getBean的参数确是实现类，因为在上下文xml文件中没有配置接口的bean标签；这里也可以使用getBean的第二个参数为反射，但是得是UserService得反射
​	（4）还需要再上下文xml配置文件中配置aop，这样才能让日志类与service类连接起来：

```java
<aop:config>
        <aop:pointcut id="pointcut" expression="execution(* com.xiaobin.servise.UserService.*(..))"/>
        <aop:advisor advice-ref="Log" pointcut-ref="pointcut"/>
        <aop:advisor advice-ref="AfterLog" pointcut-ref="pointcut"/>
    </aop:config>
```

写完pointcut之后要配置advisor，advice-ref是自己写的日志类，例如Log和AfterLog，pointcut-ref是指定切入点的标签，切入点标签pointcut的id属性可以自己定义的，可以有多个pointcut标签，后面的expression属性是表达式，指定用那个路径下的那些方法，可以写死，也可以弄个范围，第一个*号意思是指返回值是任意类型 ，后面接所要使用日志功能的所在类路径，加.*()意思是该类下的任意方法，参数是..表示是任意参数；
	（5）上面的Aop方式是使用的spring的AOP的API实现的；方式二是通过自定义切面类来实现AOP，不过这种方式就没有第一种的API方式那么灵活；
操作过程是通过创建一个普通的java类，类里面填加写准备插入其他类里面的方法，比如before(),after()等等，这个java类充当一个切面类；接着就是上下文xml配置文件有所不同，首先要添加刚才新创建的java类的bean，后面在配置aop:

```java
<aop:config>
        <aop:aspect ref="aop">
            <aop:pointcut id="point" expression="execution(* com.xiaobin.servise.UserServiceImpl.*(..))"/>
            <aop:before method="before" pointcut-ref="point"/>
            <aop:after method="after" pointcut-ref="point"/>
        </aop:aspect>
    </aop:config>
```

ref的值上上面自定义的切面类的bean的id属性值，还是一样的要写切入点，就是要把自定义的切面类里面的方法准备插入在那些类的里面，然后是aop:before属性等，这些标签的method属性值是要插入的方法名，后面是切入点的标签地址；

(6)在spring中项目的任何地方，使用${}来导入resourse目录下的配置文件里面的配置信息；在java类里面需要@PropertySource("jdbc.properties")使用@PropertySource来导入配置文件

例子：注入了配置文件里jdbc的url信息到变量jdbcUrl中

```java
 @Value("${jdbc.url}")
    String jdbcUrl;
```

（7）artifacts的一些配置：

在配置之前先看看web模块有没有配置进来，没有的web的话是没有项目的artifacts的提示的

> ![image-20210220195635673](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210220195635673.png)

> ![image-20210220195703241](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210220195703241.png)

web模块的配置：

> ![image-20210220195811868](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210220195811868.png)

tomacat的配置：

> ![image-20210220195907916](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210220195907916.png)

配置成功的时候要自动在tomcat的webapps目录下生成项目的文件

（8）spring5与spring4的配置不一样，比如Hander的配置类就不一样

mysql8也是个版本临界点，8与8以下的数据库配置都不一样

（9)Spring提供的validation校验框架

- 使用validation只用开启<mvc:annodation-driven/>
- 具体的操作
  - 自定义个实现了validation接口的校验类，重写suports（Class clazz）方法和validation（Object，Errors）方法
  - 在validate（）方法中用ValidationUtils工具类的静态方法来处理校验逻辑；如ValidationUtils,rejectIfEmpty(e,"name","name.empty");用Errors对象的rejectVlue（“字段”，“信息”）；来保存字段

#### SpringAOP

- 核心业务还是要OOP来发挥作用，与AOP的侧重点不一样，前者有种纵向抽象的感觉，后者则是横向抽象的感觉， AOP只是OOP的补充，无替代关系

### 二、Spring Mvc

#### @RequestMapping注解

（1）@RequestMapping注解的使用对象

1. 使用在类上：当使用在类上时视图页面访问的url地址不能到类上的url地址结束，必须接着后面的类里面的url地址，这样视图页面才能访问该控制器的的具体方法
2. 使用在方法上：参数的value属性值表示该方法在上下文中的url地址，method属性表示处理post或者get请求等；似乎可以使用组合注解@PostMapping注解把请求方式确定了，但是有些地方没有这个注解；作用在方法上时，参数value的值就是方法名的字符串类型
3. 该注解都是使用在控制层上的

(2)@RequestMapping与@requestParam配合使用；@requestParam注解使用在RequestMapping作用域里面，作用是接收请求页面传递的参数；注意中文参数传递会发生乱码，需要在webapp目录下的web.xml配置文件里面配置一个设置请求的编码方式为utf-8的过滤器（该过滤器要放在所有的过滤器之前）

(3)在@RequestMapping的方法的返回值：String,void,ModelAndView

1. String:实质上返回的是需要跳转的页面的url地址，只是前缀和后缀在配置文件中提前设定好了的

   ```Java
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
           <property name="prefix" value="${web.view.prefix}"/>
           <property name="suffix" value="${web.view.suffix}"/>
       </bean>
   ```

   prefix和suffix设置在config.properties配置文件中

   ```java
   web.view.prefix=/WEB-INF/view/
   web.view.suffix=.jsp
   ```

2. void:在@RequestMapping的方法里面直接操作HttpSercletResponse,就可以什么也不用返回

   例子：

   ```java
   public ModelAndView download(HttpServletResponse response) {
       byte[] data = ...
       response.setContentType("application/octet-stream");
       OutputStream output = response.getOutputStream();
       output.write(data);
       output.flush();
       return null;
   }
   ```

3. ModelAndView:返回ModelAndView的对象，view就是一个视图文件，Model是一个map的键值对

   ```java
   return new ModelAndView("/profile.html", Map.of("user", user));
   ```

   

#### 启动Spring容器

(1)启动Spring容器可以通过Listener、xml、Servlet、注解启动，最简单的是通过web.xml的配置文件里面启动；dispatcher是Spring-MVC提供的连接Servlet容器（tomcat）和SpringIOC容器

```java
<servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath*:/spring-mvc*.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

1. 初始化参数contextConfigLocation，下面的value是该Spring-mvc配置文件所在的路径；这里要根据自己所用的配置元数据所用到的方法来配置，有xml、纯java这两种方式
2. 在Servlet-mapping种是dispatcher的映射到“/”，即处理所有的Servlet；
3. 在启动Sertvlet容器之后，Servlet首先会加载DispatcherServlet到容器之中；然后根据Spring所用的IOC容器的类型，XML、纯java这两种中的一种来创建IOC容器，接着加载所有的Bean，并将IOC容器绑定在ServletContext上；
4. DispatcherServlet持有IOC容器里面的@Controller的所有Bean，当DispatcherServlet收到Http请求时，Controller配置的url路径会把请求和参数传递到指定的方法；并根据返回的ModelAndView决定如何渲染页面；渲染页面的时候是用的ViewEngined引擎
5. 