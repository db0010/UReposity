### 一、集成Spring MVC框架

- 把Spring MVC所需要的Maven依赖包和相关属性值（如包的版本值设置在properties标签中）填加在pox.xml文件中；导入spring-mvc,javax.servlet-api,jstl三个jar包

- 在web.xml配置文件中添加DispatcherServlet配置和ContextLoaderListener监听器

  ```java
  <listener>
          <listener-class>
              org.springframework.web.context.ContextLoaderListener
          </listener-class>
  </listener>
      <servlet>
          <servlet-name>dispatcher</servlet-name>       
              <servletclass>          			org.springframework.web.servlet.DispatcherServlet
              </servlet-class>
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

  DispatcherServlet：前置控制器，根据配置的url-pattern标签的url匹配规则拦截请求；在init-param标签里面初始化spring-mvc配置文件

  ContextLoaderListener：该监听器实现了ServletContextListener接口；其作用是在启动web容器（tomcat）时自动装配ApplicationContex上下文的配置信息

  要使用@Resource注解需要在pom.xml中加入以下依赖

  ```xml
  
      <!--注解依赖 https://mvnrepository.com/artifact/javax.annotation/javax.annotation-api -->
      <dependency>
          <groupId>javax.annotation</groupId>
          <artifactId>javax.annotation-api</artifactId>
          <version>1.3.2</version>
      </dependency>
  ```

  

- 在resource目录下创建配置文件spring-mvc.xml;

  ```java
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context" xmlns:mvc="http://www.springframework.org/schema/mvc"
         xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
  		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
  		http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd">
  <context:component-scan base-package="com.myOpenCV.controller"/>
      <mvc:annotation-driven/>
          <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
              <property name="viewClass" value="org.springfrmework.web.servlet.view.JstlView"/>
              <property name="prefix" value="/WEB-INF/views"/>
              <property name="suffix" value=".jsp"/>
              </bean>
  ```

  context:component-scan：扫描包里的controller类，并把类注册成bean；也可以通过扫描全部包，在下面的属性里面配置只要controller类，其他的都忽略

  mvc:annotation-driven：该注解会自动注册RequestMappingHandlerMapping和RequestMappingHandlerAdapter两个bean，是SpringMVC为@Controller分发请求所必须的，并提供了Json、XML、数据绑定等的支持

  InternalResourceViewResovler：最常用的视图解析器，当controller类的方法的返回类型是String时，该解析器会给返回值自动加上路径前缀和后缀

1. 常用注解

   -  请求映射注解

     - @Controller
     - @RequestMapping
     - @GetMapping、@PostMapping

   - 参数绑定注解

     - @RequestParam

     - @PathVariable：注入动态参数

     - @RequestHeader：获取请求头的各个key的值（可能是int、String、String[]）;例如：

       ```java
       @RequestHeader("Accept-Encoding") 
       String[] accept
       ```

     - @CookieValue：获取请求的Cookie的value，@CookieValue("Cookie的key")注入的参数获得的就是对应key的value

     - @ModelAttribute：在控制器中首先执行带有该注解的方法，可以在方法中添加Model或者初始化操作

     - @SessionAttribute、@SessionAttributes：@SessionAttributes("value")注解只能使用在类上，表示把参数value添加到HttpSession域之中，可以在多个请求之间使用，与redirect重定向配合使用；这样在重定向之后modelMap.get("value")来获取数据；@SessionAttribute注解使用在方法参数里面，把HttpSession域中对应的字段注入

     - @ResponseBody、@RequestBody：@ResponseBody注解使用在控制器的方法上；把return的类型不会解析为视图，而是对应值的字符串。然后直接写入HTTP响应头返回给浏览器；@RequestBody注解先用系统默认配置的HttpMessageConverter把Http请求的body数据解析，然后注入到控制器的参数中去

   - 信息转换

2. web中的几个作用域

   - request域
   - HttpSession域：作用域是在多个请求间

### 二、集成MyBatis-spring框架

1. 在pom.xml中添加

   - musql-connector-java

   - druid数据连接池包

   - spring-jdbc

   - mybatis

   - mybatis-spring

     1.spring的jar包及spring的依赖包

     2.springmvc的jar包及springmvc的依赖包

     3.mybaties的jar包及mybaties的依赖包

     4.mybaties与spring的整合包和数据库驱动包
     
   - 注意错误

     - Maven里不配置下面的话，可能会导致资源加载错误问题

       ![image-20210703210217675](D:\zuo_mian\java小知识\image\image-20210703210217675.png)

2. 在resource目录下创建jdbc.properties配置文件

   - jdbc.driverClassName

   - jdbc.url

   - jdbc.username

   - jdbc.password

   - 在mapper.xml文件的配置头

     ```xml
     <?xml version="1.0" encoding="UTF-8" ?>
     <!DOCTYPE mapper
             PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
             "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     ```

   - 导入数据源；注意里面引用jdbc配置文件里数据时使用的是${}

     ```xml
     <bean id="properties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
             <property name="location" value="jdbc.properties"/>
         </bean>
         <bean id="SqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
             <property name="dataSource" ref="dataSource"></property>
              <property name="mapperLocations" value="classpath:mapper/*.xml"/>
         </bean>
         <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
             <property name="driverClassName" value="${jdbc.driverClassName}"/>
             <property name="url" value="${jdbc.url}"/>
             <property name="password" value="${jdbc.password}"/>
             <property name="username" value="${jdbc.username}"/>
         </bean>
     ```

     

3. 在applicationContext.xml上下文配置文件中配置导入jdbc.properties配置文件、配置数据源、配置SqlSessionFactory对象、配置sqlSession、扫描base-package下所有以@MyBatisDao注解的接口

   ```
   
   ```

   contignore-unresolvableext:property-placeholder：目的是为了引入其他的配置文件；location属性表示引入文件的位置；ignore-unresolvable属性表示是否忽略解析不到的属性，这里设置为true

   SqlSessionFactoryBean：在基本的MyBatis中，Session工厂可以使用SQLSessionFactoryBuilder来创建；而在Mybatis-Spring中，则使用SqlSessionFactoryBean来替代

   MapperScannerConfigurer：查找类路径下的映射器并将他们创建成MapperFactoryBean

4. 映射器mapper的主要元素

   - select,insert,update,delete元素就是xml里面配置增删改查的sql语句
   - selectKey元素主要是用来指定主键的，一是id主键，需要在sql语句中用max（id）+1聚集函数来达到id主键自增的目的
   - resultMap元素（结果集映射）目的是为了实现JavaBean与数据库表的对应转换
   - sql元素与<include refid="">元素的组合使用，可以达到对字段封装的目的
   - 配置里面${},#{}的区别，#{11}=="11",${11}=11
   - 动态sql中if，choose，when，otherwise，trim，where，set，foreach，bind元素可以到达一些逻辑判断，这些都是标签

5. MyBatis中的注解配置

   - @select,@insert,@update,@delete注解就对应xml配置中的元素一样，注解的参数是sql语句字符串，所有的注解都是用在DAO层类的方法上面

   - @param注解，是用在DAO的接口参数里面，是根据名称找到传入的参数，注解的参数是数据库表对应字段的字符串；使用@Param注解的四种情况

     - 当有多个（同名）参数时，可使用@param配置不同的名称
     - 需要修改（指定）传入XML中的参数名称时；参数为基本数据类型和String类型时加上
     - 当mapper.XML中使用了动态SQL时，就需要用@Param
     - 当需要使用${}符时，${}有SQL注入问题，但是当传入的参数是表明个列名时必须使用${}，因此必须使用动态SQL来判断传入的${}是否为null和""，则必须使用@param传递这两种参数

     > 参考网页：https://zhuanlan.zhihu.com/p/74853451

   - 简单的配置可以用注解配置，但是有些复杂的配置还是需要使用XMl配置

6. MyBatis的关联映射（MyBatis重点和难点）

   - 一对一：比如一个人只能有一张对应的身份证，需要在人这个类中定义身份证类的对象，在resultMap元素需要多配置个<association>元素作为子元素，子元素也需要配置该对象的属性，如<id>,<result property="" column="">；还需要配置方法引用地址，javaType属性这些
   - 一对多：在“一”所在类的mapper.xml中配置<collection javaType/>,还需要在“一”所在类添加“多”类的对象集合属性；“多”类配置就更一对一类的配置一样，用<association>元素配置
   - 多对对：两个类都添加另一个类的对象集合，并且在mapper.xml配置文件中用<collection>配置
   - 可以在<collection fetchType="lazy">，表示使用懒加载方式加载数据，这需要在mybatis-config.xml配置文件中配置开启懒加载

7. RowBounds分页类与分页插件PageHelper；使用PageHelper时可以查看他的官网文档

   - RowBounds类是直接使用的SqlSession的原生sql方法，其中有个select方法传入RowBounds对象

     ```
     RowBounds rowBounds=new RowBound(int statIndex,int pageSize);
     ```

     

   - RowBounds类使用的时候需要在DAO类中的方法的参数上作为一个对象参数，在使用的时候，new一个RowBounds的对象传给DAO的方法，参数（0，5）表示从第0行开始读取数据，读5条记录；这种分页是逻辑分页，是在数据返回全部数据的基础上只显示设定的多行数据

   - PageHelper是一个外部的包，需要在Maven中导入包的依赖，以及在applicationContex.xml上下文文件中配置插件；该插件的分页是物理分页，效率要比逻辑分页的效率高很多

   - 使用的步骤

     ```java
     //第0行，读5条数据
     PageHelper.startPage(0,5);
     List<Teacher> teacherList=TeacherDao.findAll();
     PageInfo pageInfo=new PageInfo(teacherList);
     ```

     在调用startPaget(0,5)方法之后，紧跟的第一个MyBatis查询方法会被进行分页，分页的信息会被封装到pageInfo对象之中，比如当前页，每页的数量，前一页，下一页，当前页面在数据库中的行号；

8. MyBatis的缓存机制

   1. 一级查询缓存；SqlSession级别的缓存（作用域是SqlSession范围），也叫会话缓存和本地缓存，localCache；

      > ![img](https://awps-assets.meituan.net/mit-x/blog-images-bundle-2018a/bb851700.png)

      - 一个Session会话创建一个SqlSession对象，一个SqlSession对象创建一个Executor对象，Executor对象有一个PerpetualCache对象，这是一级缓存
      - 在一个Session周期内，如果没有刷新缓存的操作发生，比如调用clearCache()方法或者执行DML操作（insert，update，Delete），那么下一次查询的数据是PerpetualCache缓存里面有的，那么就从缓存里面获取数据
      - SqlSession的close()方法是释放整个PerpetualCache缓存，后面也是不可继续使用的；clearCache()方法是清楚缓存中的数据，后面还可以继续使用缓存
   - 每当一个新 session 被创建，MyBatis 就会创建一个与之相关联的本地缓存。任何在 session 执行过的查询结果都会被保存在本地缓存中，所以，当再次执行参数相同的相同查询时，就不需要实际查询数据库了。本地缓存将会在做出修改、事务提交或回滚，以及关闭 session 时清空
   
2. 创建SqlSession对象的过程
   
      ```java
      SqlSessionFactory sqlsessionFacotry=sqlSessionFactoryBean.getObject();
      SqlSession sqlSession=sqlSessionFactory.openSession();
      //后面的调用teacherDao接口的方法都是用下行通过sqlSession获取的teacherDao
      TeacherDao teacherDao=sqlSession.getMapper(TeacherDao.class);
   ```
   
3. 二级查询缓存；Mapper级别的缓存（是以namespace为单位的），也叫second level cache；二级查询缓存默认是不开启的，开启需要在ApplicationContex.xml,mybatis-config.xml,以及每个mapper.xml文件中都要配置开启二级查询缓存
   
      - 每个mapper都有一个二级缓存，其中可能有多个mapper的缓存是指向同一个缓存的引用（mapper标签的cache-ref=""属性来配置）；
   - 二级缓存是事务性的。这意味着，当 SqlSession 完成并提交时，或是完成并回滚，但没有执行 flushCache=true 的 insert/delete/update 语句时，缓存会获得更新这样在其中一个mapper的数据更新之后，另一个mapper在查询原来的数据，那就会读取到脏数据；因此使用二级缓存需谨慎
      - 使用一级缓存在更新数据之后，缓存中的数据会清空，开启二级缓存之后就先会去二级缓存里面获取相应的数据

   4. MyBatis缓存原理；在mapper.xml中用cache标签、或者在mapper标签设置了cache-ref属性那么就表示开启了二级缓存；
   
      > ![img](https://awps-assets.meituan.net/mit-x/blog-images-bundle-2018a/28399eba.png)
   >
      > ![img](https://awps-assets.meituan.net/mit-x/blog-images-bundle-2018a/cdb21712.jpg)
   
      - 二级缓存（全局缓存）开启之后，查找的顺序是二级缓存->一级缓存->数据库
      - 这里使用了装饰器模式；Cache为Component（抽象构件），PerpetualCache为ConcreteComponent（具体构件），其他的类都是ConcreteDecorator（具体装饰类）；只有PerpetualCache为Cache接口的实现类
      - 装饰器模式：动态的给一个对象增加一些额外的职责

- 单独的MyBatis

  - 生命周期与作用域；设置作用域就是设置定义类对象的位置；作用域出现问题会出现并发问题

    - SqlSessionFactoryBuilder：使用建造者模式创建SqlSession工厂，只需要一个工厂就需要把建造者销毁，因此作用域是在方法作用域（设置为方法里的局部变量）
    - SqlSessionFactory：整个应用的SqlSession都需要该工厂来创建，因此定义的工厂作用域应设置为应用作用域，可以通过设置单例模式和静态单例模式
    - SqlSession：一个会话对于一个SqlSession，因此作用域为方法作用域或者请求作用域，例如设置为跟Servlet中的Request请求一个作用域；这个需要显示的关闭（最好用try-with-resource语法）
    - Mapper：一个SqlSession对应一个或者多个Mapper，Mapper的作用域应设置为方法作用域；这个不需要显示的关闭

  - 配置文件例子

    ```
    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <configuration>
        <settings>
            <setting name="logImpl" value="STDOUT_LOGGING"/>
        </settings>
        <environments default="development">
            <environment id="development">
                <transactionManager type="JDBC"/>
                <dataSource type="POOLED">
                    <property name="driver" value="com.mysql.jdbc.Driver"/>
                    <property name="url" value="jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf8&amp;useSSL=false&amp;serverTimezone=UTC&amp;rewriteBatchedStatements=true"/>
                    <property name="username" value="root"/>
                    <property name="password" value="12345678"/>
                </dataSource>
            </environment>
            
            <environment id="dev">
                <transactionManager type="JDBC"/>
                <dataSource type="POOLED">
                    <property name="driver" value="com.mysql.jdbc.Driver"/>
                    <property name="url" value="jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf8&amp;useSSL=false&amp;serverTimezone=UTC&amp;rewriteBatchedStatements=true"/>
                    <property name="username" value="root"/>
                    <property name="password" value="12345678"/>
                </dataSource>
            </environment>
        </environments>
    
        <mappers>
            <mapper class="com.kuang.mapper.UserMapper"/>
        </mappers>
    </configuration>
    
    ```

    

  - Mybatis环境的配置

    ```
    <environments default="development">		//在MyBatis的多环境下通过引用id名称来使用；一个sqlSessionFactory只能使用一个environment，多个环境就配置多个environment标签
      <environment id="development">
        <transactionManager type="JDBC">	//JDBC的事务管理器是最常用的（另一个是MANAGED），MyBatis和Spring整个后不用配置了，会被Spring的事务管理器覆盖
          <property name="..." value="..."/>	//这个可在MyBatis文档中的Settings部分查看全部配置
        </transactionManager>
        <dataSource type="POOLED">	//数据源有POOLED(池化连接，适用于并发web)、JDNI（适用于应用服务器）、UNPOOLED（无池）
          <property name="driver" value="${driver}"/>
          <property name="url" value="${url}"/>
          <property name="username" value="${username}"/>
          <property name="password" value="${password}"/>
        </dataSource>
      </environment>
    </environments>
    ```

  - 映射配置；mapers标签配置，可以使用相对于类路径的资源引用，或完全限定资源定位符（包括 `file:///` 形式的 URL），或类名和包名等；

    ```
    <!-- 将包内的映射器接口实现全部注册为映射器 -->
    <mappers>
      <package name="org.mybatis.builder"/>	//这个适用于类比较多时，但是这个不能配置别名；设置别名可结合@Alisa
    </mappers>
    <!-- 使用映射器接口实现类的完全限定类名 -->
    <mappers>
      <mapper class="org.mybatis.builder.AuthorMapper"/>
      <mapper class="org.mybatis.builder.BlogMapper"/>
      <mapper class="org.mybatis.builder.PostMapper"/>
    </mappers>
    ```

  - Java实体类属性与数据库表字段不相同问题；有两种解决方法；

    - 一是在select语句中查询的数据库列名用as取个别名（对应JavaBean中的属性），这样返回的对象就可以完美的转化为JavaBean了

      ```
      <select id="getUserById" resultType="com.kuang.pojo.User">
          select id,name,pwd as password from mybatis.user where id = #{id}
      </select>
      ```

    - 二是使用resultmap标签；手动把对应的JavaBean和数据库字段对应起来

      ```
      <!--结果集映射-->
      <resultMap id="UserMap" type="User">
          <!--column数据库中的字段，property实体类中的属性-->
          <result column="id" property="id"/>
          <result column="name" property="name"/>
          <result column="pwd" property="password"/>
      </resultMap>
      
      <select id="getUserById" resultMap="UserMap">
          select * from mybatis.user where id = #{id}
      </select>
      ```

    - map方法；上面的解决办法都是针对的select语句，通过在返回时设置别名；当遇见传入参数作为set子句的遍历时就无法解决，需要用把所有参数封装进map中，mybatis可以直接获取map的键（用#{key}）

      ```
        Map map=new HashMap<String,Object>();
              map.put("id","2");
              map.put("name","谢磊");
              map.put("pwd","666");
              userMapper.updateUser(map);
      ```

      

  - Mapper.xml编写的注意事项

  ```
  public interface UserMapper {
      List<User> getUserList();	//这里的返回值为列表，但是在下面的mapper中返回类型为User，不是ArrayList
  }
  //xml中的sql语句
   <select id="getUserList" resultType="com.db.mybatisTest.bean.User">
          select * from user
    </select>
  ```

  - 模糊查询；有拼接字符串和使用concat()两种方法

    ```
    //拼接字符串
    <select id="findUserLike" resultType="com.db.mybatisTest.bean.User">
            select id,name,pwd as password from user where name like "%"#{name}
        </select
    //使用concat()
    <select id="findUserLike" resultType="com.db.mybatisTest.bean.User">
            select id,name,pwd as password from user where name like concat("%",#{name})
        </select
    ```

  - Mybatis在insert记录的时候可以使用useGeneratedKeys参数为true，keyProperty="id"；前提是数据库表中id字段设置为了auto_increment属性，不然MyBatis执行会报错；当有很多类都需要设置自动主键自增时，可以在environment的配置文件中配置Settings属性的useGeneratedKeys，这个作用域就是全局；如果局部和全局都设置了，那么依局部的配置

  - 易错点：

    - 在mybatisconfig.xml中配置Environment时，每中属性的上下顺序不能错，不然会报错，按着他提示的顺序修改顺序就好了

### 三、继承Log4j日志框架

1. Log4j中的三个组件：Logger（记录器）、Appender（输出端）、Layout（布局）；三个的作用为记录器为日志的级别，输出端为输出的地方，布局为输出的格式

2. 在pom.xml中导入依赖包
   - log4j
   - slf4j-api
   - slf4j-log4j12
   
3. Log4j的配置文件可以是XMl文件形式，也可以是log4j.properties形式；下面是Log4j在MyBatis中的日志配置

   ```
   #将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
   log4j.rootLogger=DEBUG,console,file
   
   #控制台输出的相关设置
   log4j.appender.console = org.apache.log4j.ConsoleAppender
   log4j.appender.console.Target = System.out
   log4j.appender.console.Threshold=DEBUG
   log4j.appender.console.layout = org.apache.log4j.PatternLayout
   log4j.appender.console.layout.ConversionPattern=[%c]-%m%n
   
   #文件输出的相关设置
   log4j.appender.file = org.apache.log4j.RollingFileAppender
   log4j.appender.file.File=./log/kuang.log
   log4j.appender.file.MaxFileSize=10mb
   log4j.appender.file.Threshold=DEBUG
   log4j.appender.file.layout=org.apache.log4j.PatternLayout
   log4j.appender.file.layout.ConversionPattern=[%p][%d{yy-MM-dd}][%c]%m%n
   
   #日志输出级别
   log4j.logger.org.mybatis=DEBUG
   log4j.logger.java.sql=DEBUG
   log4j.logger.java.sql.Statement=DEBUG
   log4j.logger.java.sql.ResultSet=DEBUG
   log4j.logger.java.sql.PreparedStatement=DEBUG
   ```

   

### 四、集成JUnit测试框架

1. JUnit测试是程序员测试，也叫白盒测试

2. 在pom.xml文件中导入junit的Maven依赖

3. 在/src/main/test/下在创建对应的main文件目录，每个类都应该有个对应的测试类；在该目录下创建一个测试基类

   ```java
   @RunWith(SpringJUnit4ClassRunner.class)
   @ContextConfiguration(location={"calsspath:applicationContext.xml"})
   public class BaseJunit4Test{
   }
   ```

   localtion是加载的上下文配置文件的路径

4. 写测试类继承测试基类，在需要测试的方法上加上@Test注解，就可以在控制台看见相关的打印信息

### 五、spring事务管理

1. spring的两种事务管理

   - 编码式事务管理：就是把事务管理代码嵌入到业务方法中来控制事务的提交和回滚
   - 声明式事务管理：把事务管理的代码与业务代码分离开来，以声明的方式来实现事务管理

2. 常用的是声明式事务管理

   - 使用@Transactional注解在类或方法上，表明该类或方法需要spring的事务支持，当类或方法被调用时spring会开启一个新的事务，当运行过程正常时，spring会提交该事务

   - 在applicationContext.xml文件中添加事务的配置

     ```java
     <tx:annotation-driven transaction-manager="transactionManager" 
         proxy-target-class="true"/>
      <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
          <property name="dataSource" ref="dataSource"/>
      </bean>
     ```

   - 当使用java配置类时，可以使用@EnableTransactionManagement注解在java配置类上开启声明式事务，开启之后spring容器会自动扫描@Transactional注解的方法和类

3. spring传播行为

   当事务方法调用另一个被@Transactional注解的类或方法时，可能继续在现有事务中运行，也可能新开一个事务并在自己的事务中运行等；可以在@Trasactional的属性propagation指定事务的传播行为（下面加粗的是最常用的三种行为）

   - **propagation_requies_new：新建事务，如果当前存在事务，则把当前事务挂起；新事务使用独立的隔离级别和锁**
   - **propagation_required：如果当前没有事务，就新建一个事务，如果已经存在一个事务，则加入到这个事务中；这是Spring默认的传播行为**
   - propagation_supports：支持当前事务，如果当前没有事务则以非事务的方式执行
   - propagation_mandatory：强制的；使用当前事务，如果当前没有事务则抛出异常
   - **propagation_nested：嵌套的；如果当前存在事务，则在当前事务中嵌套执行，没有事务则新建一个事务；新事务沿用之前事务的隔离级别和锁**
   - propagation_not_supports：以非事务方式执行，如果当前存在事务，就把当前事务挂起
   - propagation_never：以非事务方式执行，如果存在事务，则抛出异常

   @Transaction的isolation属性设置事务的隔离级别；五种隔离级别，相比与JDBC的四种多了一种default级别，使用数据库默认的隔离级别；Mysql默认的是read_committed;

   还可以通过timeout属性设置事务的过期时间，readOnly属性指定当前事务是只读事务，rollbackFor（noRollbackFor）指定哪个或那些异常可以引起事务回滚

   #### Spring的API设计很不错，基本上根据英文翻译就能知道作用:Required:必须的。说明必须要有事物，没有就新建事物。supports:支持。说明仅仅是支持事务，没有事务就非事务方式执行。mandatory:强制的。说明一定要有事务，没有事务就抛出异常。required_new:必须新建事物。如果当前存在事物就挂起。not_supported:不支持事物，如果存在事物就挂起。never:绝不有事务。如果存在事物就抛出异常
   
   - spring事务@Transactional失效问题
   
     - 自调用问题：类自身方法之间的调用，并不能够每次都产生新的事务；因为Spring事务的原理是由Spring事务管理器管理的，应用了AOP，AOP的原理是动态代理，类的调用是类自身的调用而不是代理对象的调用，那么就不会产生AOP，那么事务管理器就不会创建新的事务；
   
       - 解决办法：一是可以由另外一个Service去调用另一个Service；二是从IOC容器中获取代理对象去启用AOP
   
         ```
         //从IOC容器中获取代理对象
         UserService userservice=applicationContext.getBean(Userservice.class);
         ```
   
         

### 六、SpringMVC原理

> ![preview](https://pic3.zhimg.com/v2-04d44e4c288806b9641023e4c6131bfe_r.jpg)

（1）上图的SpringMVC工作流程中主要用到的组件

- DispatcherServlet（前端控制器）：接收用户的请求；把处理结果返回给用户；控制整个流程的执行，对各个组件进行统一的调度，起着中央处理器的作用，降低了各个组件之间的耦合性
- HandlerMapping（处理映射器）：请求转发到HandlerMapping之后，如果映射成功，则会返回一个HandlerExecutionChain对象给DispatcherServlet，该对象包含一个Handler对象和多个HandlerInterceptor（拦截器）对象；默认的映射器实现类是BeanNameUrlHandlerMapping，通过name属性来匹配处理器Handler；
- HandlerAdapter（处理适配器）：HandlerAdapter允许多个实例，把处理器包装成适配器，从而可以从支持更多类型的处理器，这是适配器模式的应用；SimpleControllerHandlerAdapter是HandlerAdapter的实现类之一，是所有Controller实现类的适配器，Controller接口只有一个handleRequest()方法，返回值是ModelAndView
- Handler（处理器）：也叫做Controller
- ViewResolver（视图解析器）：是用来把逻辑视图解析为物理视图的；首先是把目标方法的返回类型String,ModelMap,ModelAndView,View都转换成ModelAndView；然后把其中的View对象进行解析，把逻辑视图View解析为物理视图View对象；最后调用物理视图对象的render（）方法进行视图渲染，得到响应结果
- View（视图）：可以是Jsp、Excell、JFreeChart等