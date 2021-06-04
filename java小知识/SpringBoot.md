### 一、创建SpringBoot项目

- 在Spring initializr中创建，之后选择集成的模块，可以只选择Java web

  > ![image-20210330171600816](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210330171600816.png)

- 目录结构

> ![image-20210330171859710](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210330171859710.png)

application.yml配置文件也可以是application.properties格式，当两中文件都存在时propertier配置文件的优先级高于.yml；templates目录用于存放模板文件，在控制器中return时可以直接访问该目录下的视图文件；

- @SpringBootApplication注解是一个组合注解，包含@EnableAutoConfiguration、@ComponentScan、@SpringBootConfiguration三个注解

- SpringBoot有一个入口类；类是自动创建的，类名是项目名+Application；SpringApplication.run()是应用程序开始运行的方法

- @SpringBootTest注解引入了入口类的配置；@RunWith注解需要导入Junit包，普通的Junit单元测试可以不用这个注解

  > ![image-20210330172838738](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210330172838738.png)

- application.yml的配置

  ```yml
  spring:
    datasource:
      name: learnjdbc
      url: jdbc:mysql://localhost:3306/learnjdbc?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
      username: root
      password: db2587468048.
      driver-class-name: com.mysql.cj.jdbc.Driver
  mybatis:
    mapper-locations: classpath:Mapper/*.xml
    type-aliases-package: com.example.demo.bean
  ```

- 在前面选好添加的模块之后，会在pom.xml文件中自动创建依赖代码

  - spring-boot-starter-parent：使用之后常用包的依赖可以省去
  - spring-boot-starter-web：该依赖中包含常用的web方面的依赖包；比如spring-web、spring-webmvc等
  - spring-boot-starter-test：引入该依赖之后就会把所有与测试相关的包全部导入
  - spring-boot-maven-plugin：只是个Maven插件，使用之后可将Spring Boot应用打包为可执行的war或jar包

### 二、集成Sprig Data JPA

1. 引入依赖

   ```xml
   <dependency>
   	<groupId>org.springframword.boot</groupId>
   	<artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
   ```

2. 开发一个UserRepository类来继承JpaRepository<UserBean,String>类，当需要使用JpaRepository类没有封装的方法，可以在实现类里面写个方法体，然后在Service里也写同样的接口，然后在ServiceImpl里面实现

   ```java
   public interface UserRepository extends JpaRepository<UserBean,String> {
       UserBean findAllById(int id);
       List<UserBean> findAllByNameLike(String name);
       List<UserBean> findAllByIdIn(Collection<String> ids);
   }
   public interface UserService {
       UserBean loginIn(String name,String password);
       UserBean findById(int id);
       List<UserBean> findAll();
       UserBean save(UserBean userBean);
       void delete(String id);
       Page<UserBean> findAll(Pageable pageable);
       List<UserBean> findAllByNameLike(String name);
       List<UserBean> findAllByIdIn(Collection<String> ids);
   }
   @Service
   public class UserServiceImpl implements UserService {
      @Resource
       private UserRepository repository;
       @Override
       public UserBean findById(int id) {
           return repository.findAllById(id);
       }
       @Override
       public List<UserBean> findAll() {
           return repository.findAll();
       }
       @Override
       public UserBean save(UserBean userBean) {
           return repository.save(userBean);
       }
       @Override
       public void delete(String id) {
           repository.deleteById(id);
       }
       @Override
       public List<UserBean> findAllByNameLike(String name) {
           return repository.findAllByNameLike(name);
       }
       @Override
       public List<UserBean> findAllByIdIn(Collection<String> ids) {
           return repository.findAllByIdIn(ids);
       }
   
   ```

3. 在Bean上还要加上@Entity、@Table("数据库表名")、在主键上要加上@Id注解；用的是

   ```java
   import javax.persistence.Entity;
   import javax.persistence.Id;
   import javax.persistence.Table;
   ```

4. 使用Spring Data JPA的好处是不用配置集成外部的MyByties，也不用写SQL语句，通过JpaRepository的实现类里的方法名来自动翻译成相关的SQL语句

5. 在使用PageRequest进行查询的分页时，创建PageRequest的对象时用page,size两个参数的构造方法，这是原生的分页，也可以自己重写；

6. Assert断言是种开发中的调试方式，当Assert的方法里有异常时，程序会打印相应的错误；例如isNull()，isTrue()

### 三、集成Themeleaf

1. 在pom.xml文件中导入依赖

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
   	<artifactId>spring-boot-starter-thymeleaf</artifactId>
   </dependency>
   ```

2. 在application.yml中配置Themeleaf

   ```yaml
    thymeleaf:
    #模板的配置，支持HTML、XML、TEXT、JAVASCRIPT等
       mode: HTML
       #开发配置为false，避免修改模板还要重启服务器
       cache: false
       #配置模板路径，默认是templates，可以不用配置
       prefix: classpath:/templates/
   ```

3. 在HTML页面里引入Thymeleaf的命名空间，引入命名空间之后就可以使用Thymeleaf标签、表达式、函数；关键字是“th:”；在控制层把数据添加进Model之后，就可在HTML页面显示数据了

   ```html
   <!DOCTYPE html>
   <html xmlns:th="http://www.thymeleaf.org">
   <head>
       <meta charset="UTF-8">
       <title>success</title>
   </head>
   <body>
   <h1>登录成功！</h1>
   <table>
       <tr>
           <td>用户名</td>
           <td>密码</td>
       </tr>
       <tr th:each="user:${userList}">
           <td th:text="${user.getName()}"></td>
           <td th:text="${user.getPassword()}"></td>
       </tr>
   </table>
   </body>
   </html>
   ```

### 四、Rest Client测试工具

1. 该工具是IDEA里面的工具（在tools/Test RESTful Web Service），可以在里面输入请求方法，参数；然后运行服务器，之后可以在工具的response里面看见响应结果；比在浏览器调试要快得多

   > ![image-20210405210720640](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210405210720640.png)![image-20210405210742854](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210405210742854.png)

### 五、Spring Boot事务

1. Spring Boot开启事务只用一个@Transactional注解，因为Sprig Boot已经默认对JPA、JDBC、MyBatis开启了事务
2. 分为类级别事务和方法级别事务
   - 类级别事务：@Transaction注解作用在类上，类的所有方法都是开启事务的
   - 方法级别事务：@Transaction注解作用在方法上，该方法的事务开启；当类和方法上都有@Transaction注解时，方法级别注解覆盖类级别注解

### 六、使用过滤器和监听器

- 过滤器：新建个过滤器类实现Filter接口（javax.servlet包下的），使用Servlet3.0里面的@WebFilter注解配置，就相当于jsp项目中的web.xml文件中的配置；需要在@SpringBootApplication所在类加上@ServletComponentScan注解扫描监听器和过滤器

  ```java
  @WebFilter(filterName = "MyFilter",urlPatterns = "/*")
  ```

- 监听器：当所监听的范围状态发生变化时，服务器自动调用监听器对象的方法，常用于统计在线人数、在线用户、系统加载时进行信息初始化、统计网站的访问量；使用时就新建一个类来实现需要监听的类的接口；重写contextInitialized()、contextDestroyed()方法；

  - contextDestroyed()：在Servlet容器终止Web应用时调用该方法，在调用该方法之前，容器会先销毁所有的Servlet和Filter过滤器

- 监听器先执行contextInitialized()，在执行过滤器

### 七、集成Redis缓存

1. 在pom.xml中引入依赖

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

2. 在application.yml添加redis的配置

   ```yaml
     redis:
       database: 0
       host: localhost
       port: 6379
       password:
   ```

3. 测试，在操作时需要启动Redis的服务器，不然会报Redis连接错误

   ```java
    	@Resource
       private RedisTemplate redisTemplate;
       @Resource
       private StringRedisTemplate stringRedisTemplate;
       @Test
       void contextLoads() {
           redisTemplate.opsForValue().set("name","db");
           String name= (String) redisTemplate.opsForValue().get("name");
           System.out.println(name);
           redisTemplate.opsForValue().set("name","xl");
           System.out.println(redisTemplate.opsForValue().get("name"));
       }
   ```

   StringRedisTemplate只针对键值是字符串的数据进行访问；两个模板提供了五种数据访问方法对应Redis的五种数据类型

   - opsForValue()：操作简单属性数据
   - opsForList()：操作含有list的数据
   - opsForSet()：操作Set数据
   - opsForZSet()：操作ZSet（SortedSet）数据
   - opsForHsh()：操作hash数据
   
4. ServletContextListener监听器与Redis的结合使用

   - 编写自定义的监听器

     ```java
     @WebListener
     public class MyListener implements ServletContextListener{
         @Resource
         RedisTemplate redisTemplate;
         @Resource
         UserService userService;
         private final static String userList="userList";
         @Override
         public void contextInitialized(ServletContextEvent sce) {
             //因为运行监听器时ServletContext还没有加载，所以依赖还没有注入，所以要手动实现web上下文和Spring的融合，
             WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
             //查询现在的用户
             List<UserBean> userBeanList=userService.findAll();
             //清除缓存中的数据，为后面的把当前用户添加进缓存做准备，不清除有可能数据不一致
             redisTemplate.delete(userList);
             if(userBeanList!=null&&userBeanList.size()>0){
                 redisTemplate.opsForList().leftPushAll(userList,userBeanList);
                 List<UserBean> list=redisTemplate.opsForList().range(userList,0,-1);
                 System.out.println(list.size()+"人");
             }
             System.out.println("ServletContext上下文初始化");
         }
     
         @Override
         public void contextDestroyed(ServletContextEvent sce) {
             System.out.println("ServletContext上下文销毁");
         }
     }
     ```

   - 还需要更换RedisTemplate的默认使用的JdkSerializationRedisSerializer序列化，该序列化使用的iso的编码方式，不支持中文，因此要更换序列化，StringRedisTemplate默认使用StringRedisSerializer序列化；Fastjson的序列化和反序列化速度是高于Jackson的，所以这里需要自己实现Fastjson的序列化方式；

     - 导入Fastjson的依赖

       ```xml
        <dependency>
                   <groupId>com.alibaba</groupId>
                   <artifactId>fastjson</artifactId>
                   <version>1.2.47</version>
               </dependency>
       ```

     - 把需要存入缓存的Java类实现Serializable接口；编写FastJson2JsonRedisSerializer类和RedisConfigure类；在静态代码块中添加需要序列化进Redis中的类路径

       ```java
       public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>{
           private ObjectMapper objectMapper = new ObjectMapper();
           public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
           private Class<T> clazz;
           static {
               ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
               ParserConfig.getGlobalInstance().addAccept("com.example.demo.bean.UserBean");
       
           }
           public FastJson2JsonRedisSerializer(Class<T> clazz) {
               super();
               this.clazz = clazz;
           }
           @Override
           public byte[] serialize(T t) throws SerializationException {
               if (t == null) {
                   return new byte[0];
               }
               return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
           }
       
           @Override
           public T deserialize(byte[] bytes) throws SerializationException {
               if (bytes == null || bytes.length <= 0) {
                   return null;
               }
               String str = new String(bytes, DEFAULT_CHARSET);
               return JSON.parseObject(str, clazz);
           }
           public void setObjectMapper(ObjectMapper objectMapper) {
               Assert.notNull(objectMapper, "'objectMapper' must not be null");
               this.objectMapper = objectMapper;
           }
       
           protected JavaType getJavaType(Class<?> clazz) {
               return TypeFactory.defaultInstance().constructType(clazz);
           }
       }
       ```

       ```java
       @Configuration
       public class RedisConfigure extends CachingConfigurerSupport {
           @Bean
           public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
               RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
               redisTemplate.setConnectionFactory(redisConnectionFactory);
               FastJson2JsonRedisSerializer fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);
               ObjectMapper objectMapper = new ObjectMapper();
               objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
               objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
               fastJson2JsonRedisSerializer.setObjectMapper(objectMapper);
       
               StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
               // key采用String的序列化方式
               redisTemplate.setKeySerializer(stringRedisSerializer);
               // string的value采用fastJson序列化方式
               redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
               // hash的key也采用String的序列化方式
               redisTemplate.setHashKeySerializer(stringRedisSerializer);
               // hash的value采用fastJson序列化方式
               redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);
               redisTemplate.afterPropertiesSet();
               return redisTemplate;
           }
       }
       ```

     - 这样存入Redis缓存中的数据是这种格式的

       ```json
       {"@type":"com.example.demo.bean.UserBean","id":0,"name":"熊波","password":"78444"}
       ```


### 八、集成Log4j2

1. 在pom.xml中添加log4j2的依赖，说是要在web模块去除掉springboot得Logback日志框架，但是似乎没效果

   ```xml
    <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
               <exclusions>
                   <!--springBoot中日志默认使用的是Logback日志框架，使用log4j2需要再web中排除logging的依赖-->
                   <exclusion>
                       <groupId>org.springframework.boot</groupId>
                       <artifactId>spring-boot-starter-logging</artifactId>
                   </exclusion>
               </exclusions>
           </dependency>
   <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-log4j2</artifactId>
           </dependency>
   ```

2. 在Resource目录下添加log4j2的配置文件，可以是xml形式，也可以是properties；xml就是新建个普通文件后缀是xml就可以

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <Configuration>
       <Appenders>
           <Console name="Console" target="SYSTEM_OUT">
               <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
           </Console>
       </Appenders>
       <Loggers>
           <Root level="all">
               <AppenderRef ref="Console"/>
           </Root>
       </Loggers>
   </Configuration>
   ```

3. 然后需要在application.yml中导入log4j2配置文件的位置

   ```yaml
   logging:
     file:
       path: classpath:log4j2.xml
   ```

4. 使用时，在类先创建获取了该类class对象的Logger对象，注意Logger用log4j2下的

   ```java
   Logger logger= LogManager.getLogger(this.getClass());
    logger.info("ServletContext上下文初始化");
   ```


### 九、集成Quartz定时器

Quart定时器是纯Java编写的开源任务调度框架，通过触发器设置作业的运行规则、控制作业的运行时间；功能例如，定时发送信息、定时生成报表等

> ![img](https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=393641107,16744086&fm=26&gp=0.jpg)

1. 在pom.xml中导入依赖

   ```xml
   <dependency>
               <groupId>org.quartz-scheduler</groupId>
               <artifactId>quartz</artifactId>
               <version>2.2.3</version>
           </dependency>
   ```

2. 在resources目录下新建Spring配置文件spring-mvc.xml、spring-quartz.xml；在spring-mvc.xml中导入定时器的配置文件

   ```xml
     <context:annotation-config/>
       <import resource="spring-quartz.xml"/>
   ```

   定时器的配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns="http://www.springframework.org/schema/beans"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
       <bean id="taskJob" class="com.example.demo.quartz.TestTask"/>
       <bean id="jobDetail" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
           <property name="targetObject">
               <ref bean="taskJob"/>
           </property>
           <property name="targetMethod">
               <value>run</value>
           </property>
       </bean>
       <bean id="myTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
           <property name="jobDetail">
               <ref bean="jobDetail"/>
           </property>
        <!--定义触发规则，没10秒执行一次 -->   
           <property name="cronExpression">
               <value>0/10 * * * * ?</value>
           </property>
       </bean>
       <bean id="scheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
           <property name="triggers">
               <list>
                   <ref bean="myTrigger"/>
               </list>
           </property>
       </bean>
   </beans>
   ```

3. 创建定时器类TestTask

   ```java
   public class TestTask {
       private static final Logger logger= LogManager.getLogger(TestTask.class);
       public void run(){
           logger.info("定时器运行了!!");
       }
   }
   ```

4. 除了用xml配置，还可以用注解配置，在quartz目录下创建一个类SendMailQuartz，这种方式简洁得多

   ```java
   @Component
   @Configurable
   @EnableScheduling
   public class SendMailQuartz {
       private static final Logger logger= LogManager.getLogger(SendMailQuartz.class);
    //每五秒执行一次   
       @Scheduled(cron = "*/5 * * * * * ")
       public void reportCurrentByCron(){
           logger.info("定时器运行了！！");
       }
   }
   ```

### 十、集成Mail

- 邮件发送与接收得过程如下
  - 发件人通过SMTP协议把邮件发送到邮件服务器A
  - 邮件服务器A根据发送得目的地，把邮件发送到邮件服务器B
  - 收件人通过POP3协议从邮件服务器B接收邮件
- SMTP协议都是应用层得协议，传输层协议使用得TCP协议
- Spring提供了JavaMailSender接口实现邮件发送，springBoot封装在了Starter模块中

1. 引入依赖

   ```yaml
    mail:
       host: smtp.qq.com	#qq的主机服务器
    username: 2587468048@qq.com	#发送方的邮箱
       password: xyjqlpsyvdxmecie  #第三方的授权密码
       default-encoding: UTF-8
   ```
   
2. 创建接口

   ```java
   public interface SendJunkMailService {
       boolean sendJunkMail(List<UserBean> userBeanList) throws MessagingException;
   }
   ```

   实现类

   ```java
   @Service
   public class SendJunkMailServiceImpl implements SendJunkMailService {
       @Autowired
       JavaMailSender mailSender;
       @Resource
       private UserService userService;
       @Value("${spring.mail.username}")
       private String from;
       private final static Logger logger = LogManager.getLogger(SendJunkMailServiceImpl.class);
       @Override
       public boolean sendJunkMail(List<UserBean> userBeanList) {
           try {
               if (userBeanList == null && userBeanList.size() <= 0) return Boolean.FALSE;
               for (UserBean userBean : userBeanList) {
                   MimeMessage mimeMailMessage = this.mailSender.createMimeMessage();
                   MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMailMessage);
                   messageHelper.setFrom(from);
                   messageHelper.setSubject("地瓜今日特卖");
                   messageHelper.setTo("2929844719@qq.com");
                   messageHelper.setText(userBean.getName() + "你知道吗？厦门的地瓜今日特卖，一斤只要9元");
                   this.mailSender.send(mimeMailMessage);
                   logger.info(userBean.getName() + "的邮件发送成功！");
               }
               return true;
           } catch (Exception e) {
               logger.error("sendJunkMail error and user=%s", userBeanList, e);
               return Boolean.FALSE;
           }
       }
   }
   ```

   