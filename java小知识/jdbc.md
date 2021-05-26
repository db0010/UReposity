(1)java的JDK中定义了JDBC的接口，好处是Java不用因为数据库的不同而更改，只用选择响应的驱动jar包就行了，具体实现是由各大数据库厂商的JDBC驱动来实现的，驱动是以jar包的格式出现的，jar包里的内容就是JDBC接口的实现类；在maven中导入MYsql的jar包：

```java
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
    <scope>runtime</scope>
</dependency>

```

scope标签的runtime意思是运行是才使用改jar包，类似动态链接的意思
（2）Connection：代表一个JDBC连接（通常是通过TCP），连接需要URL，数据库用户名，数据库密码，这三样数据才能把java程序和数据库连接起来
（3）URL：每个数据库厂商的URL都不一样，MYsql的统一格式是：localhost是主机，3306是端口，learnjdbc是数据库名，后面是不适用SSL加密，和使用utf8编码，mysql中是utf8，而不是java中的utf-8，下面例子中问号后面的设置必须要的，不然要报错
jdbc:mysql://localhost:3306/learnjdbc?useSSL=false&characterEncoding=utf8
（4）password一定要是数据库的密码，就是个字符串，一定要注意！！不然会报access denied for user 'root'@'localhost'(using password:YES)错误
String JDBC_PASSWORD = "db2587468048.";字符串不是”password“！
（5）连接数据库步骤：
1.通过DriverManager的静态方法getConnection（jdbc的url，数据库的用户名，数据库的密码）
2.用PreparedStatement的对象来接收Connection连接对象的prepareStatement("sql语句")，在参数的sql语句中可以用?问号来作为占位符，在下面可以使用PreparedStatement的对象的setObject(占位符参数的索引,展位符参数的值)
3.sql的查询结果使用ResultSet的对象来接收，PreparedStatement的对象的静态方法executeQuery()来执行sql语句
4.最后用rs.next()方法作为while循环的条件来循环获取ResultSet对象的返回结果
例子：

```java
try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
    try (PreparedStatement ps = conn.prepareStatement("SELECT id, grade, name, gender FROM students WHERE gender=? AND grade=?")) {
        ps.setObject(1, "M"); // 注意：索引从1开始
        ps.setObject(2, 3);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                long id = rs.getLong("id");
                long grade = rs.getLong("grade");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
            }
        }
    }
}

```

（6）上面用到的带参数的try语句的作用把需要关闭资源的语句写在try的参数里面，在执行完try的结构体之后就会自动释放参数里的资源，官方说是try with resource语法

（7）jdbc的insert，update，delete操作实际上都是update操作，只有query的查询操作不一样；update使用的是PreparedStatement的excuteUpdate()方法来执行sql语句，返回值是int类型，代表受update影响的记录数；query操作使用的是PreparedStatement的excuteQurery()方法，返回值是ResultSet对象；

```java
try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
    try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO students (grade, name, gender) VALUES (?,?,?)",
            Statement.RETURN_GENERATED_KEYS)) {
        ps.setObject(1, 1); // grade
        ps.setObject(2, "Bob"); // name
        ps.setObject(3, "M"); // gender
        int n = ps.executeUpdate(); // 1
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                long id = rs.getLong(1); // 注意：索引从1开始
            }
        }
    }
}
```

（8）当要返回插入数据的自增主键时，相比与普通的sql操作，需要在PreparedStatement的构造函数上添加第二个参数为Statement.RETURN_GENERATED_KEYS；在执行完sql操作之后，用一个Result对象来接受ps的getGeneratedKeys()方法，它是直接就获取了插入数据的id，用个if判断就行了；这个返回id只能用于插入的sql操作，其他的不行；

(9)事务的ACID特性：事务的目的是为了让一系列数据库操作要么全部成功，要么全部失败，保持同步性

- Atomicity:原子性

- Consistency:一致性

- Isolation:隔离性

- Durability:持久性

  | 事务的隔离级别   | 脏读 | 不可重复读 | 幻读 |
  | ---------------- | ---- | ---------- | ---- |
  | Read Uncommitted | Yes  | Yes        | Yes  |
  | Read Committed   | -    | Yes        | Yes  |
  | Repeatable Read  | -    | -          | Yes  |
  | Serializable     | -    | -          | -    |


（10）jdbc中的事务操作：

1. 获取Connection连接对象
2. 把自动提交事务设置为false，然后执行多条sql操作
3. 捕获异常，如果有异常则回滚事务并打印异常提示
4. 在finalily中把自动提交事务恢复为true，sql默认是自动提交事务为true

例子：

```java
Connection conn = openConnection();
try {
    // 关闭自动提交:
    conn.setAutoCommit(false);
    // 执行多条SQL语句:
    insert(); update(); delete();
    // 提交事务:
    conn.commit();
} catch (SQLException e) {
    // 回滚事务:
    conn.rollback();
} finally {
    conn.setAutoCommit(true);
    conn.close();
}
```

（11）Jdbc的Batch（批处理）：把执行同一条sql语句（但是参数不同）的多个sql操作用for循环一起处理了

实质上还是每个sql操作都执行了的，只是提高了开发效率；与普通的jdbc操作多了两个操作：

1. 在for循环里执行完一个sql操作之后把PreparedStatement对象调用addBatch()方法，这个方法的底层是把ps中存储的数据通过一系列的方式暂时存储在ArryList中，这里面存储的是PreparedStatemen的对象
2. for循环之后再执行executeBatch()方法，该方法的返回值是一个整型数组，如果每个sql操作只操作一条记录的话，那每个返回值都是1

（12）jdbc连接池：使用连接池的目的是为了减少Connection的开销，使用连接池后当执行完JDBC操作之后连接并不是直接注销，而是放在连接池中，这样下次有sql操作的时候直接从连接池中获取连接就可以了，跟线程池的效果是一样的；还可以使用连接池的监控程序来监控数据库的情况；常用的四种连接池

- HikariCP
- C3P0
- BoneCP
- Druid

要用那种连接池就直接再maven里面导依赖包就行了，例如HikariCP的包：

```java
<dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>2.7.1</version>
    </dependency>
```

数据库的三个配置url、user、password会在连接池中配置，这样后面使用从连接池中获取的连接都不用配置这三个参数；使用的时候有四步不同：

1. 先创建要使用的连接池的配置类对象
2. 配置类对象调用相关set方法设置三个参数和其他的连接池配置
3. 创建DataSource对象，把配置类对象作为DataSource构造函数的参数
4. 创建Connection对象，不使用DriverManegment来获取连接，使用第三步的DataSource对象来获取连接，后面的操作就跟其他的一样了

（13）jdbc的批处理操作可以和事务操作结合起来使用，例子：

```java
HikariConfig config=new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(JDBC_USER);
        config.setPassword(JDBC_PASSWORD);
        config.setConnectionTimeout(1000);//超时，单位是毫秒
        config.setMaximumPoolSize(1);//最大连接数
        config.setIdleTimeout(60000);//空闲超时时间60秒
        DataSource dataSource=new HikariDataSource(config);
//       DataSource dataSource=config.getDataSource();//这样是错的
        try(Connection connection=dataSource.getConnection()){
            try {
                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(sql2)) {
                    for (Student student : studentList) {
                        ps.setString(1, student.getName());
                        ps.setInt(2, student.getGender());
                        ps.setInt(3, student.getGrade());
                        ps.setInt(4, student.getScore());
                        ps.addBatch();
                    }
                    int[] ints = ps.executeBatch();
                    for (int n : ints) {
                        System.out.println(n + "inserted");
                    }
                }
            }
            catch(SQLException e){
                System.out.println("异常原因:"+e);
                connection.rollback();
            }
            finally {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
```

### 二、JDBC的执行步骤

JDBC应用步骤：
		1，注册加载一个driver驱动
		2，创建数据库连接（Connection）
		3，创建一个Statement（发送sql）
		4，执行sql语句
		5，处理sql结果（select语句）
		6，关闭Statement
		7，关闭连接Connection

### 三、Statement和prestatement的区别

-   PreparedStatement 接口继承 Statement ，  PreparedStatement 实例包含已编译的 SQL 语句， 所以其执行速度要快于 Statement 对象。 Statement为一条Sql语句生成执行计划， 如果要执行两条sql语句 
  **select colume from table where colume=1;select colume from table where colume=2;** 会生成两个执行计划 一千个查询就生成一千个执行计划！ PreparedStatement用于使用绑定变量重用执行计划 **select colume from table where colume=:x;** 通过set不同数据只需要生成一次执行计划，可以重用