---
typora-root-url: image
---

### 一、创建SpringBoot项目

- 从Maven中创建SpringBoot

  - 首先创建一个普通的Maven项目（我没有使用Maven模板），然后在pom文件继承springBoot启动器的parent；导入启动器的web模块；导入springBoot的Maven插件；还需要手动创建yaml配置文件；templates静态资源目录；启动类（使用的@SpringBootApplication）里面的main方法调用SpringApplication的run方法，参数为启动类的class对象，args参数；相比与用spring initializer创建SpringBoot项目，还没有自动创建单元测试（注意：测试要导带starter的test包，不然用不了）

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <groupId>com.db</groupId>
        <artifactId>yamlTe</artifactId>
        <version>1.0-SNAPSHOT</version>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.5.0</version>
        </parent>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
        </dependencies>
        <!-- Package as an executable jar -->
        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </project>
    ```

  - 正常的SpringBoot项目结构；使用的是内嵌的Tomacat服务器，默认的端口是8080（可在yaml的server.port中修改端口）；默认载入的配置文件路径：classpath:application.yaml；resources路径和classpath似乎相等的，因此classpath:可以直接访问resource目录下的资源

    ![image-20210615193713106](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615193713106.png)

  - 启动信息分析

    ![image-20210615194734604](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615194734604.png)

    - 第一行显示了使用的JDK版本，运行的主机名，开启的进程id

      ![image-20210615195204200](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615195204200.png)

    - 第二行表示没有激活配置文件，因此使用的是SpringBoot默认的配置文件，在resource目录下的application.yaml文件

    - 第三行表示Tomacat使用的端口号为8081，这是我在yaml文件中修改了的，默认是8080

      ![image-20210615195708288](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615195708288.png)

    - 第五行表示使用的Servlet引擎为Tomacat9版本；之后初始化web容器的上下文WebApplicationContext

  - yaml语法

    - 说明：语法要求严格！

      1、空格不能省略

      2、以缩进来控制层级关系，只要是左边对齐的一列数据都是同一个层级的。

      3、属性和值的大小写都是十分敏感的。

      **字面量：普通的值  [ 数字，布尔值，字符串  ]**

      字面量直接写在后面就可以 ， 字符串默认不用加上双引号或者单引号；

      ```
      k: v
      ```

      注意：

      - “ ” 双引号，不会转义字符串里面的特殊字符 ， 特殊字符会作为本身想表示的意思；

        比如 ：name: "kuang \n shen"  输出 ：kuang  换行  shen

      - '' 单引号，会转义特殊字符 ， 特殊字符最终会变成和普通字符一样输出

        比如 ：name: ‘kuang \n shen’  输出 ：kuang  \n  shen

    - yaml还可以配置对象

      - **对象、Map（键值对）**

        ```
        #对象、Map格式
        k:    
        	v1:    
        	v2:
        ```

        在下一行来写对象的属性和值得关系，注意缩进；比如：

        ```
        student:    
        	name: qinjiang    
        	age: 3
        ```

        行内写法

        ```
        student: {name: qinjiang,age: 3}
        ```

        **数组（ List、set ）**

        用 - 值表示数组中的一个元素,比如：

        ```
        pets: 
        	- cat 
        	- dog 
        	- pig
        ```

        行内写法

        ```
        pets: [cat,dog,pig]
        ```

  - 注入配置文件

    - Spring中是通过使用@Component注解标注类，然后在指定的字段或者set方法上使用@value("值")来注入bean

      ```
      @Component //注册bean
      public class Dog {   
          @Value("阿黄")    
          private String name;    
          @Value("18")    
          private Integer age;
      }
      ```

    - 使用processor处理器来导入初始化文件（需要导入处理器的依赖），类需要一个构造函数，并且每个构造函数参数创建一个属性。否则，就会通过标准getter和setter的存在来发现属性（意思是被注入类需要构造函数或者getset方法）；根据初始化文件与类中属性的一一映射来注入值；

      - 被注入类的注意事项；需要把该类加入到Spring容器中（用@Component注解标注类）；prefix属性的值是字符串类型，并且只能是小写（就算yaml配置文件中使用的是Person大写P，这里也用小写才行）

      ![image-20210615211102090](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615211102090.png)

      ![image-20210615211308524](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615211308524.png)

      - yaml中的注意事项；boby是list类型，每一个成员占一行就可以了；map类型用{键1：值1，键2：值2}包含；Date类型用/斜杠分隔（不能用2000-04-05，会出现类型转换异常）

        ![image-20210615210804094](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615210804094.png)

      - 如果使用的是Properties文件来配置，会有中文问题，需要在IDEA的properties设置中解决；【注意】properties配置文件在写中文的时候，会有乱码 ， 我们需要去IDEA中设置编码格式为UTF-8；

        settings-->FileEncodings 中配置；

      - 编写配置properties文件

        ![image-20210615212047244](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615212047244.png)

  - 两种配置文件依赖注入的比较

    1、@ConfigurationProperties只需要写一次即可 ， @Value则需要每个字段都添加

    2、松散绑定：这个什么意思呢? 比如我的yml中写的last-name，这个和lastName是一样的， - 后面跟着的字母默认是大写的。这就是松散绑定。可以测试一下

    3、JSR303数据校验， 这个就是我们可以在字段是增加一层过滤器验证 ， 可以保证数据的合法性

    - 导入validate的依赖，就可使用@Email等注解，用在字段上，如果该字段不是Email格式，那么会报错可以更改@Email的message属性值可以更改（这是报错之后的错误信息）

    4、复杂类型封装，yml中可以封装对象 ， 使用value就不支持

    - 两者的应用使用场景
      - 如果我们在某个业务中，只需要获取配置文件中的某个值，可以使用一下 @value
      - 如果说，我们专门编写了一个JavaBean来和配置文件进行一一映射，就直接@configurationProperties

  - 多环境配置文件

    - 可以在以下四个位置创建配置文件，加载的优先级从上往下递减；一般用于多个项目环境（生产、测试环境、开发环境）

      ![image-20210615213604134](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615213604134.png)![image-20210615223024849](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615223024849.png)

      

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

   - 狂神导入的是这个pom依赖，不知道有什么区别

     ![image-20210616160604575](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616160604575.png)

     

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

- 在控制层想直接返回一个HTML页面的话，就需要导入themeleaf的包，可以是不在yaml文件中配置，但是要在resourese目录下创建一个templates目录，里面存放静态资源；要控制器的左边有导航标志了才代表成功的引用了页面

  ![image-20210615190910700](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210615190910700.png)

- 链接使用${}括起来；类似国际化的配置使用#{}括起来

  ![image-20210616210353256](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616210353256.png)

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

### 七、集成Redis

1. 创建springBoot项目时需要勾选上Redis组件

   ![image-20210606215652027](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210606215652027.png)
   
2. 在pom.xml中引入依赖

   ```xml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

3. 在application.yml添加redis的配置

   ```yaml
     redis:
       database: 0
       host: localhost
       port: 6379
       password:
   ```

   - redisProperties配置类中的配置选项；springboot2.x后redis底层使用不再是Jedis，而是Lettuce；配置线程池和集群时，建议配置Lettuce上面的配置选项，因为再Jedis的连接工厂类里面像集群、连接池、shiro这些都是没有自动装配的，这些的配置选项用Jedis下的话就会失效；而Lettuce的连接工厂类都是自动装配成功了的

     ![image-20210608150540019](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210608150540019.png)
     
     ![image-20210606225833750](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210606225833750.png)

4. 测试，在操作时需要启动Redis的服务器，不然会报Redis连接错误

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

5. 自定义redisTemplet和编写RedisUtils工具类

   - redisTemplet
- RedisUtils
  
6. ServletContextListener监听器与Redis的结合使用

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
       <bean id="jobDetail" 				    	      class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
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


### 十一、自定义工具类

- Json工具类

  - 对于Date时间对象；Jackson对于date对象会转为一串表示时间的格式，不明了，需要转换为yyyy-MM-dd HH:mm:ss的格式

    ```java
    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.SerializationFeature;
    import java.text.SimpleDateFormat;
    public class JsonUtils {
       
       public static String getJson(Object object) {
           return getJson(object,"yyyy-MM-dd HH:mm:ss");
      }
    
       public static String getJson(Object object,String dateFormat) 	{
           ObjectMapper mapper = new ObjectMapper();
           //不使用时间差的方式
           mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
           //自定义日期格式对象
           SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
           //指定日期格式
           mapper.setDateFormat(sdf);
           try {
               return mapper.writeValueAsString(object);
          } catch (JsonProcessingException e) {
               e.printStackTrace();
          }
           return null;
      }
    }
    ```

- 自定义RedisTemplet；因为SpringBoot中RdisTemplate的序列化使用的是JDK序列化，使用的是ISO的编码，不支持中文，需要更换序列化；实际工作中一般都需要更换序列化方式；一般JavaBean都需要实现Serializable接口，这样就可以直接把JavaBean对象序列化了，但是一般都是先把JavaBean对象转为Json字符串

  ```
  
  ```

  

- RedisUtils工具类；因为RedisTemplet每次都需要调用redisTemplate.opsForValue之类的，并且每个语句实际工作中都需要捕获异常；写在工具类中要方便很多

  ```
  
  ```

  

### 十二、Json

- fastJson

  - fastjson 三个主要的类：

    **JSONObject  代表 json 对象** 

    - JSONObject实现了Map接口, 猜想 JSONObject底层操作是由Map实现的。
    - JSONObject对应json对象，通过各种形式的get()方法可以获取json对象中的数据，也可利用诸如size()，isEmpty()等方法获取"键：值"对的个数和判断是否为空。其本质是通过实现Map接口并调用接口中的方法完成的。

    **JSONArray  代表 json 对象数组**

    - 内部是有List接口中的方法来完成操作的。

    **JSON代表 JSONObject和JSONArray的转化**

    - JSON类源码分析与使用
    - 仔细观察这些方法，主要是实现json对象，json对象数组，javabean对象，json字符串之间的相互转化。

    

    **代码测试，我们新建一个FastJsonDemo 类**

    ```java
    package com.kuang.controller;
    
    import com.alibaba.fastjson.JSON;
    import com.alibaba.fastjson.JSONObject;
    import com.kuang.pojo.User;
    
    import java.util.ArrayList;
    import java.util.List;
    
    public class FastJsonDemo {
       public static void main(String[] args) {
           //创建一个对象
           User user1 = new User("秦疆1号", 3, "男");
           User user2 = new User("秦疆2号", 3, "男");
           User user3 = new User("秦疆3号", 3, "男");
           User user4 = new User("秦疆4号", 3, "男");
           List<User> list = new ArrayList<User>();
           list.add(user1);
           list.add(user2);
           list.add(user3);
           list.add(user4);
    
           System.out.println("*******Java对象 转 JSON字符串*******");
           String str1 = JSON.toJSONString(list);
           System.out.println("JSON.toJSONString(list)==>"+str1);
           String str2 = JSON.toJSONString(user1);
           System.out.println("JSON.toJSONString(user1)==>"+str2);
    
           System.out.println("\n****** JSON字符串 转 Java对象*******");
           User jp_user1=JSON.parseObject(str2,User.class);
           System.out.println("JSON.parseObject(str2,User.class)==>"+jp_user1);
    
           System.out.println("\n****** Java对象 转 JSON对象 ******");
           JSONObject jsonObject1 = (JSONObject) JSON.toJSON(user2);
           System.out.println("(JSONObject) JSON.toJSON(user2)==>"+jsonObject1.getString("name"));
    
           System.out.println("\n****** JSON对象 转 Java对象 ******");
           User to_java_user = JSON.toJavaObject(jsonObject1, User.class);
           System.out.println("JSON.toJavaObject(jsonObject1, User.class)==>"+to_java_user);
      }
    }
    ```



### 十三、自动装配原理

- 在springBoot的autoConfigure包中有个Spring.factories，里面就有很多的配置类（都是用@Configuration注解标注的），这些配置类里有xxxproperties配置类与yaml里面的配置选项一一对应，在yaml中配置时可以看看这里面的默认配置信息

  ![image-20210616145958183](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616145958183.png)

- 原理

  - 1、SpringBoot启动会加载大量的自动配置类

    2、我们看我们需要的功能有没有在SpringBoot默认写好的自动配置类当中；

    3、我们再来看这个自动配置类中到底配置了哪些组件；（只要我们要用的组件存在在其中，我们就不需要再手动配置了）

    4、给容器中自动配置类添加组件的时候，会从properties类中获取某些属性。我们只需要在配置文件中指定这些属性的值即可；

    **xxxxAutoConfigurartion：自动配置类；**给容器中添加组件

    **xxxxProperties:封装配置文件中相关属性；**

    

    ## 了解：@Conditional

    了解完自动装配的原理后，我们来关注一个细节问题，**自动配置类必须在一定的条件下才能生效；**

    **@Conditional派生注解（Spring注解版原生的@Conditional作用）**

    作用：必须是@Conditional指定的条件成立，才给容器中添加组件，配置配里面的所有内容才生效；

    ![图片](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7IPEXZtUAUBhnSZvUmrPzbDGcJRvdK3PtqHPAWYBBmpe1XBVjQJeiatU4vasEaxckHlOga1BV9RPaw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

    **那么多的自动配置类，必须在一定的条件下才能生效；也就是说，我们加载了这么多的配置类，但不是所有的都生效了。**

    我们怎么知道哪些自动配置类生效？

    **我们可以通过启用 debug=true属性；来让控制台打印自动配置报告，这样我们就可以很方便的知道哪些自动配置类生效；**

    ```
    #开启springboot的调试类
    debug=true
    ```

    **Positive matches:（自动配置类启用的：正匹配）**

    **Negative matches:（没有启动，没有匹配成功的自动配置类：负匹配）**

    **Unconditional classes: （没有条件的类）**

### 十四、Web开发

- SpringBoot封装了web模块，没有了SpringMVC的webapp；因此要SpringBoot自动装配WebMvcAutoConfiguration类，里面有个addResourceHandler方法，里面设置了默认的资源映射路径

  - 下面的getStaticLocations()方法添加的就是默认资源路径数组；路径的使用优先级resources最高，public最低，使用spring inister创建的项目默认也会有static目录（一般都使用的static目录）；webjars一般没有使用，需要额外导webjars的包

    ```java
      private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[]{"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"};
    ```

  - 把静态资源放在上面的三个路径下可以直接通过浏览器访问的，不需要通过控制器

    ![image-20210616151033304](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616151033304.png)

```java
 public void addResourceHandlers(ResourceHandlerRegistry registry) {
            if (!this.resourceProperties.isAddMappings()) {
                logger.debug("Default resource handling disabled");
            } else {
                this.addResourceHandler(registry, "/webjars/**", "classpath:/META-INF/resources/webjars/");
                this.addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
                    registration.addResourceLocations(this.resourceProperties.getStaticLocations());
                    if (this.servletContext != null) {
                        ServletContextResource resource = new ServletContextResource(this.servletContext, "/");
                        registration.addResourceLocations(new Resource[]{resource});
                    }

                });
            }
        }
```

- 首页和icon图标

  - 只要把名字叫index.html的文件放在三个资源目录中任意一个下面就可以自动匹配首页；

    ![image-20210616155334171](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616155334171.png)

  ![image-20210616155207120](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616155207120.png)

  - 图标；springBoot2.1.7设置图标可以在static资源目录下放一个名叫favicon.ioc的图像文件，在把yaml配置中把spring.mvc.favicon.enabled=false；但是之后的版本都没有这个配置选项了

- 扩展SpringMVC

  - 根据SpringBoot的官方文档，可以通过编写自定义的Configurer类继承WebMvcConfigurer接口（里面的方法都是default，不是必须全部继承），并用@Configuration标注为配置类；配置类一般放在com.db下的config目录下

  - 下面的配置类就扩展了SpringMVC的视图解析器；在Dispatcher类中的doDispatcher方法（所有请求都会从这里过）中打断点后；发现视图解析器中就有了自定义的视图解析器；实现了ViewResolver接口的就是一个视图解析器

    ![image-20210616183546636](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616183546636.png)

    ![image-20210616183240211](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616183240211.png)

- 还可以扩展SpringMVC中的其他组件

  ```
  Spring MVC Auto-configuration
  // Spring Boot为Spring MVC提供了自动配置，它可以很好地与大多数应用程序一起工作。
  Spring Boot provides auto-configuration for Spring MVC that works well with most applications.
  // 自动配置在Spring默认设置的基础上添加了以下功能：
  The auto-configuration adds the following features on top of Spring’s defaults:
  // 包含视图解析器
  Inclusion of ContentNegotiatingViewResolver and BeanNameViewResolver beans.
  // 支持静态资源文件夹的路径，以及webjars
  Support for serving static resources, including support for WebJars 
  // 自动注册了Converter：
  // 转换器，这就是我们网页提交数据到后台自动封装成为对象的东西，比如把"1"字符串自动转换为int类型
  // Formatter：【格式化器，比如页面给我们了一个2019-8-10，它会给我们自动格式化为Date对象】
  Automatic registration of Converter, GenericConverter, and Formatter beans.
  // HttpMessageConverters
  // SpringMVC用来转换Http请求和响应的的，比如我们要把一个User对象转换为JSON字符串，可以去看官网文档解释；
  Support for HttpMessageConverters (covered later in this document).
  // 定义错误代码生成规则的
  Automatic registration of MessageCodesResolver (covered later in this document).
  // 首页定制
  Static index.html support.
  // 图标定制
  Custom Favicon support (covered later in this document).
  // 初始化数据绑定器：帮我们把请求数据绑定到JavaBean中！
  Automatic use of a ConfigurableWebBindingInitializer bean (covered later in this document).
  ```

  - 可以更改格式化器Formatter，比如时间的默认格式是2020/04/05；可以改成yyyy-MM-dd HH:mm:ss等格式

    ![å¾ç](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7Idia351qHgmH2vbzurk1Pp6zdySrKJPkADN9jtsIpz6QPOyC2nI04l0EiaAOMuU47pwiaPq2RrhUdaA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

  - 设置URL路径与页面的映射关系；通过在WebMvcConfigurer配置类中重写addViewControllers方法，图中的setViewName参数为资源目录下（如static）存在的Html页面，urlPath相当与给该路径取个别名，并且一个真实路径可以对应多个别名urlPath；真实业务中都是取别名的，不然例如登录中的URL地址栏会显示登录的信息

    #### ![image-20210617133621763](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617133621763.png)

    #### ![image-20210617133444230](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617133444230.png)

    #### ![image-20210617132838161](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617132838161.png)

  - 注意：在配置类中不要使用@EnableWebMvc注解，这是全面接管SpringMVC的配置，默认的SpringMVC配置全部会失效导致错误

  - REST风格的注解

    ![image-20210617220049681](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617220049681.png)

- i18n国际化配置

  - 需要配置HTML页面中显示的每个标签中的文字，都用键值对存储起来；需要创建下面的login_en_US、login_zh_CN、login等properties配置文件；

    ![image-20210616210730517](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210616210730517.png)

  - 为了实现各种语言的切换，需要在页面中设置两个a标签（链接请求带参数）；后端SprongBoot在自定义的配置类中扩展LocacleResolver（重写类时模仿原类的写法），返回一个Locale对象

### 十五、集成MyBatis

- SpringBoot使用Druid数据源；在yaml的spring.datasource下配置好基本的配置信息以及Durid的配置；接着自定义一个DruidConfiger配置类，用@ConfiguerProperties("spring.datasource")来跟yaml中的配置信息关联起来；

  ![image-20210617211129550](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617211129550.png)

  - 因为没有了web.xml，因此这些原本在SSM中是在web.xml中配置Druid连接池的账号密码等信息需要用代码配置

  ![image-20210617210955371](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617210955371.png)

  - 配置过滤器

    ![image-20210617211915645](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617211915645.png)

- 集成MyBatis

  - 导入MyBatis关于SpringBoot项目的starter启动器；

    ![image-20210617215821068](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617215821068.png)

  - 在yaml中配置xxxmapper.xml文件的路径和别名（配置了别名包路径，那么就会在Mapper.xml中的类就不用都加上全路径，会从配置路径下扫描JavaBean）；注意：classpath:就等于resources/；因此后面就不用加/，直接接目录，加了classpath:/那么就到项目的springboot-05-mybatis下了

    ![image-20210617213721982](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617213721982.png)

  - 在MyBatis里面Mapper就想到于之前的Dao，写Mapper层时需要多加个@Mapper，这个注解是跟xxxMapper.xml关联的

    ![image-20210617215204562](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210617215204562.png)

  - Mybatis中的事务问题

### 十六、权限框架

- SpringBoot集成Security；导入starter的security包依赖，重写以下两个config的重载方法；使用的是链式编程

  ![image-20210618132808769](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618132808769.png)
  
  - 注销功能其实就是删除Session，这样拦截器就可以拦截没有session的请求；只有在登录成功之后才重新创建一个Sesson；在配置类中设置注销后跳转的URL路径
  
    ![image-20210618161812757](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618161812757.png)
    
    登录中的方法的作用：
    
    ![](/v2-16bba95244953988fe86569f0bd5ade6_720w.jpg)
  
- Themleaf与Spring Security结合来实现页面的显示权限控制，这个需要导入两者的结合包（在Maven仓库搜Themleaf），之后需要在Html页面导入Themleaf的sec链接；可实现菜单的动态显示，会员与非会员的动态显示页面

  - 下图是通过isAuthenticated()方法来判断用户是否有权限

  ![image-20210618155953941](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618155953941.png)

  - 还可根据hasRole()方法来判断用户是否拥有权限，来显示对应的动态页面；

    ![image-20210618160337495](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618160337495.png)
    
  - 限制使用HTTPS

    - HTTPS采用证书对敏感信息进行加密，对于哪些需要加密的页面，Spring可以强制的限制只接收HTTPS请求

      ```
      htpp
      	.requiresChannel().antMatchers("/admin/**").requiresSecure()
      	.and().requiresChannel().antMatchers("/user/**").requiresInsecure();
      ```

      requiresChannel方法说明使用通道，并且访问/admin目录下请求必须是HTTPS请求；第二行使用requiresInsecure方法表示取消安全请求机制；（不知道这两种方法是不是成对出现的）

  - 防止CSRF（跨站点请求伪造）攻击

    - 攻击原理：其他恶意网站通过获取本网站保存在用户浏览器上的Cookie，之后恶意网站就可以通过使用该Cookie去访问本网站，造成损失

    - 解决方法：通过对页面添加隐藏域，隐藏域的id和name属性是后端的csrf参数，value属性是token；JSP中这样设置隐藏域

      ```
      <input type="hidden" id=${_csrf.patameterName} name="${_csrf.parameterName} value="${_csrf.token}"/>
      ```

      

- Shiro框架

  - 快速使用Shiro的步骤，以及常用的方法；可以适用于JavaSE和JavaEE，使用的是自定义的Session，Spring security使用的是HttpSession

    ![image-20210618164126688](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618164126688.png)

  - SpringBoot集成Shiro

    - 首先导入包依赖

      ![image-20210618165929258](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618165929258.png)

    - 编写Shiro的两个配置类

      ![image-20210618170034041](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618170034041.png)

      ![image-20210618170052693](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618170052693.png)

      ![image-20210618170117292](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618170117292.png)

  - 实现拦截器功能；在getShiroFilterFactory的Bean中添加如下代码

    ![image-20210618171000019](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210618171000019.png)

  - 实现用户认证；在控制器中把用户的登录信息封装在token中，在调用Shiro的subject实例的login方法自动验证信息

    ![image-20210618172926404](/image-20210618172926404.png)

    在UserRealm类中认证，用户名认证自己做，密码认证调用Shiro的封装类做

    ![image-20210618173250634](/image-20210618173250634.png)

- 真实业务中需要在数据库表中创建一个perms字段，用来存储用户拥有的权限，在利用Shiro动态的获取该权限，这样请求就可以通过拦截器

  ![image-20210618180007268](/image-20210618180007268.png)