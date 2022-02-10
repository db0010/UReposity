### 一、MySQL问题记录

（1）mysql创建数据库一般使用utf8和utf_general_ci；_ci表示不区分大小写，_cs表示区分大小写

（2）MySql 数据库更新为8.0及以上后，对应的应用程序数据库链接驱动包也要更新为8.0版本。否则会报驱动异常。

> 以前版本
>
> - ### jdbc.driver=com.mysql.jdbc.Driver
>
> - ### jdbc.url=jdbc:mysql://xxx.xx.xx.xxx:3306/db?characterEncoding=utf-8
>
> - ### jdbc.username=root
>
> - ### jdbc.password=admin
>
> 现在版本
>
> - ### jdbc.driver=com.mysql.cj.jdbc.Driver
>
> - ### jdbc.url=jdbc:mysql://localhost:3306/db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
>
> - ### jdbc.username=root
>
> - ### jdbc.password=admin

- #### application.yml中的datasource中的配置

  ```yaml
  spring:
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/learnjdbc?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
      username: root
      password: root
  ```

  

### 二、MySQL数据库的索引

- 索引的基本原理：把无序的数据通过hash算法建立有序哈希表，这是哈希索引结构；还有B-tree结构

- 使用索引是为了避免使用全表扫描；一个索引就是一个键值数组，值可以是子索引，也可以是数据

- InnoDB的数据和索引都存储在“.Ibd”文件中，其中的ibdata1文件是InnoDB数据文件（不是表数据）；MyISAM的数据放在“.MYD”文件中，索引放在“.MYI”文件中

- 唯一索引是为了让该列的值没有重复的，普通索引是为了提高访问速度

- 主键索引：是非空的、唯一的（unique）

- 创建索引和添加索引：

  ```sql
  //在students表的name字段的前两个字符创建索引
  CREATE INDEX index_students_name ON students(NAME(2))
  //在students表上添加name、score字段上的联合索引
  ALTER TABLE students ADD INDEX index_students_name(NAME,score)
  //删除students表的index_students_name索引
  ALTER TABLE students DROP INDEX index_students_name
  ```

- 在建表前和建表后创建、删除唯一索引

  ```sql
  //在建表时创建name字段的唯一索引，该表的id自增是从10开始的
  //建表时就可以指定主键索引
  `test`CREATE TABLE test(
  id MEDIUMINT(20) NOT NULL AUTO_INCREMENT,
  NAME VARCHAR(100),
  PRIMARY KEY(id),
  UNIQUE KEY index_test_name(NAME)
  )ENGINE=MYISAM CHARSET=utf8 AUTO_INCREMENT=10
  ```

  ```sql
  //在建表后创建test表的字段age的唯一索引uniIndex_test_age
  CREATE UNIQUE INDEX uniIndex_test_age ON test(age)
  ```

  ```sql
  //删除唯一索引跟删除普通索引一样的语句
  ALTER TABLE test DROP INDEX index_test_name
  ```

  ```sql
  //创建个没有主键的表
  CREATE TABLE test1(
  id MEDIUMINT(20) NOT NULL,
  score TINYINT(20),
  address VARCHAR(100)
  )ENGINE=INNODB CHARSET=utf8
  
  ```

  

#### 1.索引的类型

- 普通索引：相比于唯一索引，加了普通索引的列值可以重复
- 唯一索引：unique index关键字实现，加了唯一索引的列值不可重复
- 联合索引：索引覆盖多个列，比如index(列1，列2)，这里有个最左前缀原则来提高效率
- 主键索引：表的主键会默认的加上主键索引，也是唯一的
- 全文索引: FULLTEXT即为全文索引，目前只有MyISAM引擎支持。其可以在CREATE TABLE ，ALTER TABLE ，CREATE INDEX 使用，不过目前只有 CHAR、VARCHAR ，TEXT 列上可以创建全文索引。它的出现是为了解决WHERE name LIKE “%word%"这类针对文本的模糊查询效率较低的问题。单纯的模糊查询是不使用索引的
  - 创建全文索引；可以在create table语句中添加FULLTEXT(列)
  - 使用全文索引；Match(列)：指定被搜索的列；Against(表达式)：指定要使用的搜索表达式
  - 注意
    - 搜索是不区分大小写的，除非使用BINARY方式
    - 搜索结果是按照搜索等级的降序排列的；关键字越靠前，占有的关键字越多，那么等级越高

#### 2.索引的结构

- BTREE:把数据页的索引值按一定的算法存储在二叉树中；表里面的行记录的索引值也存储字二叉树中；每次查询都是从root根节点开始，只有叶子结点才真实存储数据
- HASH:查询单条快，范围查询慢；HASH索引可以一次定位，不需要像树形索引那样逐层查找,因此具有极高的效率。但是，这种高效是有条件的，即只在“=”和“in”条件下高效
- RTREE:RTREE在MySQL很少使用，仅支持geometry数据类型，支持该类型的存储引擎只有MyISAM、BDb、InnoDb、NDb、Archive几种。相对于BTREE，RTREE的优势在于范围查找

#### 3.索引的设计原则

- 索引失效的情况：https://blog.csdn.net/weixin_39557797/article/details/116538566

- 表的数据量少的情况下不用设置索引
- 索引设置在where条件设计的表字段上、多表连接的on关键字后的连接表字段上、group by分组的字段上
- 设置联合索引时要遵循最左前缀原则，让最可能会使用的索引放在最左边（where中and条件的先后顺序对如何选择索引是无关的。因为优化器会去分析判断选用哪个索引）
- 使用短索引，就是可在索引列后面加上字符长度，如index(列(10))，这样就表示先只比较该列的前十个字符，如果还没找到，那就把前面十个字符都不匹配的排除掉
- 不可滥用索引，索引也要占磁盘空间，并且会对更新操作的性能有影响
- 外键必须设置索引，InnoDB中设置外键之后会自动创建一个跟列字段相同的索引；因此可以不用手动设置
- 更新操作很频繁的列不适合设置索引
- 不能很好的区分列数据也不适合设置索引，如性别列，只有男女
- 要添加索引时，考虑能不能在已有索引的基础上添加联合索引
- 在列类型为text、image、bit时不适合设置索引
- 对查询操作少，并且重复数据多的列不适合设置索引

#### 4.聚集索引（聚簇）与辅助索引（非聚簇）

> ![img](https://img-blog.csdn.net/20180226122802566?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamg5OTM2Mjc0NzE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

1. 聚集索引：

   - 聚集索引表示索引和数据聚集在一起的，InnoDB引擎就是索引和数据都在“.idb”文件中

- innodb存储引擎的主键索引使用的就是聚集索引，把表的主键作为b+树的结点，叶子结点存储的就是该主键的行记录；InnoDB的辅助索引的叶子节点存储的是指向主键索引的地址，存有主键，因此主键的大小应该设置得尽量得小

  - b+树索引的叶子结点之间是一个双向链表；
  - 该索引两个优点：
    - 1.InnoDB的新记录是紧接着上一条记录插入的，获取了主键的索引就是获取了对于的行记录；对b+树上的主键（表）的排序查找和范围查找比较快；
    - 2.对数据页里面的行记录的范围查找也快

- 每张表只能建一个聚簇索引，可有多个辅助索引，并且建聚簇索引需要至少相当该表120%的附加空间，以存放该表的副本和索引中间页

  - InnoDB的聚集索引行上有个隐藏的trx_id字段表示事务的id值，这个id值跟MVCC的版本链和readView数组（存放的是事务id值从小到大的排列）有关

  2.辅助索引：

  - MyISAM引擎使用的就是非聚簇索引；
  - 在InnoDB引擎中辅助索引就是一个为了寻找主键索引的二级索引，先找到主键索引再通过主键索引找数据；

- 在MyISAM引擎中，主键索引和普通索引都是辅助索引，不过两者都有一个单独的B+树，两个B+树的结构一样，只是存储的值不一样，这两颗树的叶子节点都有指向表数据的地址，辅助树可以通过该地址在数据文件中找到对应得表数据；主键树的结点存储的是主键，辅助树的结点存储的是辅助键；

  - InnoDB的辅助索引B+树的叶子节点除了包含键值外，还包含了相应行数据的书签（bookmark）来寻找聚簇索引键。辅助索引的存在不影响数据在聚簇索引中的组织，所以一张表可以有多个辅助索引

  4.聚簇索引的优缺点

  　　优点：

  　　　　1.数据访问更快，因为聚簇索引将索引和数据保存在同一个B+树中，因此从聚簇索引中获取数据比非聚簇索引更快

  　　　	2.聚簇索引对于主键的排序、查找和范围查找速度非常快，因为数据是按照主键的大小排序的

  　　缺点：

  　　　　1.插入速度严重依赖于插入顺序，按照主键的顺序插入是最快的方式（当更新的主键小于前一个主键值时，会把新主键插入到前面的主键中间去，导致后面主键会修改值），否则将会出现页分裂，严重影响性能。因此，对于InnoDB表，我们一般都会定义一个自增的ID列为主键；还有就是在大量插入新行之后，使用optimize table语句来优化表，减少行数据碎片；也可以使用独享表空间来弱化碎片
　　　　2.更新主键的代价很高，因为可能会导致被更新的行移动。因此，对于InnoDB表，我们一般定义主键为不可更新	
  　　　　3.二级索引访问需要两次索引查找，第一次找到主键值，第二次根据主键值找到行数据

  ​			4.如果主键比较大，那么辅助索引的将会变得更大，因为辅助索引的叶子节点存储有主键值

### 三、数据库事务

#### （1）事务的ACID特性

​	事务的目的是为了让一系列数据库操作要么全部成功，要么全部失败，保持同步性

- Atomicity:原子性；事务的操作是一个原子操作，不可分割，要么全部成功，要么全部失败；原子性是由undo log日志保证的，因为undo log日志保存有旧版本的数据状态，可以实现回滚操作

  - 回滚操作是通过执行一条更新的SQL语句，那么会在undo log日志中保存相反的SQL操作；例如业务执行了insert操作，那么在undo log中就保存一条delete语句；业务执行delete语句，那么undo log就保存insert语句；业务执行update语句，undo log就会保存修改

- Consistency:一致性；事务不但要满足数据库存储数据的规则，还要满足实际业务的规则，不满足外部业务的规则一样要回滚；在事务的开始和结束过程中，数据库的完整性约束并没有被破坏；一致性是由其他三大特性+程序代码保证，如账户字段有100，但需要减去200，这时就需要程序代码来控制正确的业务逻辑

- Isolation:隔离性；数据库提供一定的隔离机制，保证事务在不受外部并发操作影响的隔离级别环境下执行；通过MVCC来保证

  - 写-写：通过行级锁、表级锁等实现的；例如一个事务更新完id为1的记录，还没有提交事务，这时该事务就占有这行的锁，那么另一个事务查询id为1的记录的话只会查到之前已经提交事务的记录，这样就实现了事务间的隔离性；当它想更新id为1的记录时会阻塞

    ![image-20211017205904330](D:\zuo_mian\java小知识\image\image-20211017205904330.png)

  - 写-读、写-读：通过MVCC机制实现的；

- Durability:持久性；事务完成后，它对于数据的修改是永久性的，即使出现系统故障也能够保持；通过MySQL的缓冲区+redo log日志来保证，mysql修改数据同时在缓冲区和redo log日志中记录这次操作

  - redo log在内存中有个缓冲区，默认是在事务提交时追加进磁盘的undo log日志中（顺序IO，追加文件，Kafaka也是），能保证一定会写进去
  
  - redo log缓存刷新进日志有下面三种方式，默认配置为1
  
    ![image-20211017214401408](D:\zuo_mian\java小知识\image\image-20211017214401408.png)
  
    ![image-20211017214311675](D:\zuo_mian\java小知识\image\image-20211017214311675.png)
  
  | 事务的隔离级别   | 脏读 | 不可重复读 | 幻读 |
  | ---------------- | ---- | ---------- | ---- |
  | Read Uncommitted | Yes  | Yes        | Yes  |
  | Read Committed   | -    | Yes        | Yes  |
  | Repeatable Read  | -    | -          | Yes  |
  | Serializable     | -    | -          | -    |

#### （2）隔离级别

- Read Uncommitted（读未提交）;会读取其他事务未提交的数据（更新语句），如果其他事务回滚，那么该事务读取到的数据就是脏数据，这就是脏读
- Read Committed（读已提交）;通过只读取其他事务已经提交了的数据来避免脏读、脏写；但是不能避免重复读取数据，因为当重复读的时候读取的的都是最新的行数据，当其他事务在期间有更新行数据并提交时就会发生重复读的行数据不一致
- Repeatable Read（可重复读）;为MySQL的默认隔离级别；在事务第一次读数据的时候选定所要读取的数据行的版本，在该事务的整个期间都读取该版本的数据行，这样每次读取的数据都是一致的；幻读是指，在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，竟然能成功，并且，再次读取同一条记录，它就神奇地出现了。
- Serializable（串行读）；所有事务都是串行的依次执行，避免了所有的脏读，不可重复读，幻读的情况；但是性能很低

Read Committed和Reoeatable Read 都是使用的MVCC（多版本并发控制）来实现的；并不是通过传统的读写锁，传统的读写锁只支持读读并发，其他的读写，写写，写读都是不能并发的；MVCC就可以实现读读，读写，写读的并发，写写会被阻塞；MVCC 机制会记录每行数据的历史版本，通过可见性算法、undo 日志以及 read view 事务id数组控制每个读操作所读取的行数据历史版本，



#### （3）MVCC多版本并发控制原理

- binlog、undolog、redolog日志：binlog是Mysql server级别的日志，redolog和undolog是InnoDB级别的日志；对mysql的操作都会在binlog中有记录
- InnoDB引擎的记录中有个隐藏式trx_id（表示事务id）字段、roll_pointer（上个版本的引用）字段，readview数组里存放的是活动的事务id值（活动的事务指的是未提交的事务），该数组是事务开始时就创建的；从记录行的版本链中获取事务id最大的id（表示是最新的事务），来跟readview数组比较，如果比最小的还小，那表示获取的事务id为已经提交了的事务版本，可以操作；如果id值在数组内或右边，那么用roll_pointer字段获取上个版本的id，直到小于readview数组
- 行记录隐藏的列包含：行的版本号、删除行的版本号（开始为空）；可重复读隔离级别下MVCC的增删改查操作流程（使用的类似乐观并发控制）
  - 有一个类似全局变量，存储事务的系统版本号；（事务开始时刻的系统版本号会作为事务的版本号）
  - select；只读取行的系统版本号小于或者等于事务的系统版本号的行；小于表示读取的行在该事务开启之前就已经存在，等于表示该行是该事务修改或者插入的；其次，还要保证行的删除标识版本号要么没有定义，要么大于当前事务的系统版本号（表示还没有被删除）；因此保证了不会读取到脏数据；
  - insert；把事务的系统版本号来作为插入行的行系统版本号
  - delete；把事务的系统版本号作为行删除标识的系统版本号
  - update；插入一行新纪录，把当前事务的系统版本号作为行的系统版本号，把当前事务的系统版本号作为原来行的删除表示版本号
  - 效果：使用了这两个系统版本号，是大多数读操作不用加锁
- MVCC保证隔离级别的原理
  - MVCC只能实现读已提交、可重复读两种隔离级别；实现读已提交时，就是每次select操作都会生成一个新的readview数组，这样两次读的话可能就不一样，这样就会有不可重复读现象；实现可重复读是都是用的一个readview数组，两次读的都是readview数组左边一个版本的事务行数据；
  - 读未提交不支持MVCC是因为，它每次都读取最新的数据行，不符合当前事务版本的数据行；串行读会对多有读取的行都加锁

### 四、数据库视图

1. 定义：视图是从一个或几个基本表（或视图）导出的表；数据库中只存放视图的定义，而不存放数据

2. 作用：

   - 在查看时和在视图上进行SQL操作时，视图能简化用户的操作
   - 能对机密数据提供安全保护

3. 建立视图的语句

   ```sql
   CREAT 视图名称（列名，列名）
   AS SELECT ...完整的查询语句
   [with check option]//最后加上该语句之后，表示之后对视图进行更新时会自动加上前面的条件语句（where子句）
   ```

4. 一下三种情况必须指定组成视图的所有列名

   - 列中有聚集函数或列表达式
   - 多表连接时选出了几个同名列作为视图的字段
   - 要为列设置列名时

5. 删除视图

   -  删除普通视图：	drop view 视图名
   -  删除有多个依赖的视图（会把所有有依赖的视图全部删了）：  drop view 视图名 cascade

6. 查询视图

   - 方法和查询基本表的操作是一样的；只是在视图用了with check option 的情况下数据库会自动加上条件子句

   - 查询的流程：有一个视图消解的过程，是视图查询转换程基本表查询

   - where子句中不能有聚集函数：解决办法是用having子句替代

     例如：

     ```sql
     where AVG(Grade)>=90
     group by Sno
     ```

     用下面的Having替代

     ```sql
     group by Sno
     having AVG(Grade)>=90
     ```

7. 更新视图

   - 对视图的更新都要转化为对基本表的更新
   - 并非所有视图都是可更新的，不能正确的确定被更新的数据时，就不能对视图更新，例如
     - 分组（使用group by和having）
     - 联结
     - 子查询
     - 并
     - 聚集函数
     - distinct
     - 导出（计算）列

### 五、三大范式

- 规范化：一个第一级的关系模式通过模式分解可转化为若干个高一级的关系模式的集合
- 第一范式（1NF）

  - 定义：确保每列保持原子性（即不可再分）
  - 缺点：有数据冗余、更新异常、插入异常、删除异常
- 第二范式（2NF）

  - 定义：属性完全依赖于主键（属性都是该对象拥有的）；确保数据库表中每一列都和主键相关，而不只是和某个主键相关（针对多个主键的情况）
  - 可解决数据冗余、插入异常、删除异常、更新异常
- 第三范式（3NF）

  - 定义：主键与非主键间不存在依赖传递
- 第三范式的扩展（BCNF）

  - 是指在第三范式的基础上进一步消除主属性对于码的部分函数依赖和传递依赖
  - 消除了删除异常、插入异常和更新异常

### 六、锁

- 间隙锁(GapLock)：主要自动使用在范围查询时；InnoDB在数据记录的第一个索引键之前和最后一个索引键之后的空间上设置锁定标记，是在两个索引键之间行数据被锁起来（这是左开右闭的一个区间），所以叫间隙锁

  - InnoDB自动使用间隙锁的条件：在可重复读及以下的隔离级别（读未提交、读已提交、可重复读）时，并且检索条件必须有索引（没有索引的话，会进行全表扫描，这时用的就是表锁，整个表的数据其他事务不能修改、删除、添加操作，并发要注意索引失效导致的行锁升级为表锁，这个是很影响性能的）

  - 间隙锁是用来解决幻读的，阻止其他事务的插入操作；原理：间隙锁锁定一个范围的键值之后，会锁定整个范围内所有的索引，不能再锁定范围内插入数据；例如，当使用范围查询时，该范围内的所有索引都会被锁定
  - 间隙锁的缺点；在一些场景下会对性能造成危害；在使用索引查询时，遇到联合索引会把联合索引内的其他索引也会被锁住；

- 记录锁(RecordLock)：只锁一条记录；触发记录锁的条件是使用的字段上有唯一索引

- 临键锁（NEXT-KEYlocking）：属于行级锁的一种，间隙锁和记录锁的结合，锁的是两个索引的闭区间上的所有行数据

- 意向锁；是InnoDB引擎内部使用的；分为意向共享锁，意向排他锁；表上使用了意向共享锁之后，有其他事务试图在该表上添加共享锁，首先需要获得该表的意向共享锁；意向排他锁同理；当判断是否需要对表加锁时，避免了对索引树的遍历操作，有意向共享锁就不能继续加共享锁了

- 乐观锁与悲观锁：两种并不是具体的锁，而是一种思想；

  - 悲观锁：并发条件下悲观的认为一定会被其他事务干扰，所以在操作时直接锁住数据，操作完之后在释放锁；for update子句用在where子句之后

    - 在数据库里面锁住，类似for update查询

    ```sql
    //在select语句后面加上for update，意思是加上写锁
    select nums from tb_goods_stock where goods_id = {$goods_id} for update;
    ```

  - 乐观锁：并发条件下乐观的认为不会被其他事务干扰，并设置有版本检测机制；设置一个版本号，当有数据更新时，把版本号加1；当在更新数据时，要加一条版本号是否更改的where条件；如果已经更改了，那么拒绝更新并返回给应用层，让用户重新操作；乐观锁因为是使用程序实现的，所以不会出现死锁的情况；

    - 不是在数据库端锁住的，而是程序端控制的。此时可以在mybatis中实现乐观锁机制；以下代码写在MyBatis的DAO接口中，当版本不一致，数据被修改，怎么获取update受影响的行数是个问题

    ```sql
    update tb_goods_stock set nums = nums - {$num}, version = version + 1 
    where goods_id = {$goods_id} 
    and version = {$version} and nums >= {$num};
    ```

  - 两者的使用场景：乐观锁用于读操作多的场景，不然经常检测到版本更新让用户重新操作更影响性能；写操作多的情况下应使用悲观锁

- 表级锁：锁住整个表，可以同时读，不可同时写；不会出现死锁；表级锁也分为了读锁和写锁

- 页级锁：给一组相邻的一组记录加锁；BDB引擎支持页锁，可能会出现死锁

- 行级锁：行级锁又分为共享锁和排他锁；行级锁是锁住行记录的主键的索引，可锁住多行记录；InnoDB引擎默认使用的就是行级锁；行级锁的优点：访问不同的行时只存在少量的锁冲突，回滚时只有少量的更改，可以长时间锁定一行；缺点：占用的内存多，当频繁使用多数数据时，效率慢，可能会出现死锁

  - 共享锁：也叫读锁（S锁）；意思是多个事务对于同一数据共享一把锁；能读不能写

  - 排他锁：也叫写锁（X锁）；当一个事务获取了一个记录行的排他锁后，当前事务可以读取和修改该记录行，其他事务都不能获取该行的共享锁和排他锁；

  - 行锁的设置：在select语句后面加上for update语句（注意查询的条件要成功且是用的主键作为查询条件，不然都会自动变成表锁），其他的更新sql语句InnoDB引擎会自动加上行级锁的，不用手动设置

    例如：

    ```sql
    //当products表中有id=3的记录时，把id=3的行设置行级锁
    select * from products where id='3' for update;
    ```

    

- 三种锁（行锁、表锁、页锁）的应用场景

  - 当需要频繁的读取表的大部分数据时，包括在这大部分数据上频繁进行Group BY分组操作，需要扫描整个表；这时表级锁效率高，因为行级锁需要频繁的获取锁
  - 当需要按索引条件更改表中的少量数据时，以及需要并发查询时，可以使用行锁，效率高；
  - 页锁介于行锁和表锁之间

### 七、引擎

- InnoDB引擎与MyISAM引擎的区别

  - InnoDB引擎支持事务、外部键，MyISAM不支持事务

  - InnoDB引擎支持行级锁和表级锁，MyISAM引擎只支持表级锁

  - InnoDB有MVCC（多版本并发控制），MyISAM没有；高并发下InnoDB性格更好，MyISAM在高并发下可以采用多实例、分库、分表的架构来应对高并发

  - InnoDB中不保存表的具体行数，当执行

    ```sql
    //当没有wheret条件时，InnoDB需要扫描整个表来计算有多少行；MyISAM保存有行数，可以直接读出行数
    select count(*) from table
    //加了where条件之后，InnoDB和MyISAM的操作就一样了
    select count(*) from table where id=2
    ```

  - MyISAM的读的性能比InnoDB好；写的话要看有没有索引的Update，有索引的话InnoDB性能更好

  - MyISAM有崩溃恢复问题，InnoDB崩溃之后更好恢复 

  - MyISAM在全表扫描、count（）时效率比InnoDB高

### 八、MySQL线程池

- 死锁的概念：在多线程下，两个线程都想获取对方的锁（临界资源），造成循环等待

### 九、数据库类型

- MySQL的数据类型

  > #### ![img](https://pic1.zhimg.com/80/v2-915c5176b0e6619c6ba12e14caa281c4_720w.jpg)

- 数值类型

  - 整型有1、2、3、4、8字节；常用的用户年龄用TinyInt类型（最大值是127），自增的ID用MediumInt（3字节），还可以加上unsigned无符号属性，存储容量增大一倍

  > #### ![img](https://pic4.zhimg.com/80/v2-f4b43ee3bff4c373d56517020a2c247f_720w.jpg)

  - decimal：钱都用decimal存储，因为double和float都会出现精度缺失；默认包含zerofull（填充0）和unsigned属性
  - innoDB引擎最好使用自增主键，数据量大、长时间运行的可以使用unsigned bigint 来作为主键（每次插入都会分配id）；考虑到id会分配完，可以使用redis来分配id（具体怎么弄不知道）
  - 自增的主键也不是连续的；当出现主键冲突、事务回滚时就会出现自增值先增加，然后再回滚

- #### 时间类型

  - 只需要年月日时，使用Date类型
  - 常用的是Datetime类型；每个表都要有创建时间、最后修改时间，都设置为datetime类型

  > ![img](https://pic3.zhimg.com/80/v2-63be71052bbe166d1d1bfdf63a0dc83a_720w.jpg)

- #### 字符串类型

  MySQL中一个汉字占一个字符，具体占多少字节需要看使用的是什么编码方式，utf-8一个中文是三个字节，英文是一个字节；GBK是中文两个字节，英文一个字节，utf-16的中英文都是两个字节

  > #### ![preview](https://pic2.zhimg.com/v2-898fd037978be6953cc38b91b588592d_r.jpg)

- CHAR和VARCHAR类型类似，但它们保存和检索的方式不同。它们的最大长度和是否尾部空格被保留等方面也不同。在存储或检索过程中不进行大小写转换。

  CHAR和VARCHAR类型声明的长度表示你想要保存的最大字符数。例如，CHAR(30)可以占用30个字符。

  CHAR列的长度固定为创建表时声明的长度。长度可以为从0到255的任何值。当保存CHAR值时，在它们的右边填充空格以达到指定的长度。当检索到CHAR值时，尾部的空格被删除掉。在存储或检索过程中不进行大小写转换。

  VARCHAR列中的值为可变长字符串。长度可以指定为0到65,535之间的值。(VARCHAR的最大有效长度由最大行大小和使用的字符集确定。整体最大长度是65,532字节）。

  同CHAR对比，VARCHAR值保存时只保存需要的字符数，另加一个字节来记录长度(如果列声明的长度超过255，则使用两个字节)。

  VARCHAR值保存时不进行填充。当值保存和检索时尾部的空格仍保留，符合标准SQL。

  char的长度默认是1个字节，varchar没有默认长度，因此必须声明长度

  char使用的空间大，但是效率比varchar高

  选择作为varchar类型的数据尽量是不怎么改变长度的，varchar改变长度会导致“行迁移”现象，会造成多余的I/O操作

- 根据分配给列的字符集校对规则对CHAR和VARCHAR列中的值进行排序和比较。

  请注意所有MySQL校对规则属于PADSPACE类。这说明在MySQL中的所有CHAR和VARCHAR值比较时不需要考虑任何尾部空格

- | **值**     | CHAR(4) | **存储需求** | VARCHAR(4) | **存储需求** |
  | ---------- | ------- | ------------ | ---------- | ------------ |
  | ''         | '  '    | 4个字节      | ''         | 1个字节      |
  | 'ab'       | 'ab '   | 4个字节      | 'ab '      | 3个字节      |
  | 'abcd'     | 'abcd'  | 4个字节      | 'abcd'     | 5个字节      |
  | 'abcdefgh' | 'abcd'  | 4个字节      | 'abcd'     | 5个字节      |

  请注意上表中最后一行的值只适用*不使用严格模式*时；如果MySQL运行在严格模式，超过列长度的值不保存，并且会出现错误。

  从CHAR(4)和VARCHAR(4)列检索的值并不总是相同，因为检索时从CHAR列删除了尾部的空格

- #### 数据类型的选择

- #### 设计数据类型要考量：

  - #### 使用最精确的类型，占用最少的空间。（现在考虑的少了因为磁盘便宜了）

  - #### 还应该考虑到相关语言处理方便性。像我们用得是 Java 语言，要考虑使用在Java中有对应类的类型

  - #### 考虑移值的兼容性。我们现在用 MySQL 来存储数据，万一要把数据移到 oracle 数据库呢！

- 选择数据类型：

1. #### 整型：根据要显示的最大值决定；

2. #### 浮点型：要显示小数。如果要精确到小数点后10位，就选择DOUBLE，而不应该选择FLOAT。DECIMAL精度较高，浮点数会出现误差，如果精度较高，则应选择定点数DECIMAL；

3. #### 字符串型：定长与变长的区别，CHAR类型占用空间比较大，但是处理速度比VARCHAR快，如果长度变化不大，如身份证号码那种，最好选择CHAR类型。而对于评论字符串，最好选择VARCHAR；

4. #### 时间：根据需要显示的类型咯，特别是TIMESTAMP,如果需要显示的时间与时区对应，就应该选择TIMESTAMP；

5. #### ENUM类型和SET类型：长度不同，ENUM类型最多可以由65535个成员，性别这种固定的值就可以用Enum类型；而SET类型最多只能包含64个成员。且ENUM只能单选，而SET类型可以多选；

6. #### TEXT类型和BLOB类型：TEXT只能存储字符数据，而BLOB可以存储二进制数据。如果是纯文本，适合TEXT。如果是图片等适合存二进制；网站中的“评论”、“新闻”、“留言”用MediumText类型合适；

- default、zeroFill、unsigned、signed、not null、Auto_increment这些都是列属性类型
- Auto_increment使用的注意事项：
  - 对于是整型的自增id，可以使用MediumInt unsigned，增加一倍的容量
  - 建表时可用“Auto_increment=n”来指定一个自增的初始值
  - 删除表数据，但是还想保留列的序号，可用"delete table 表名 where 1"
  - 可用last_insert_Id()函数来获取刚刚自增过的值
- Binary类型，保存二进制字符串，不区分大小写，多余的空间用“\0”填充，如：“6\0\0\0\0”

### 十、外键

- 外键的作用是为了保证多张表中数据的一致性，通过在参照表中设置外键，该外键依赖被参照表中字段，通过该字段让两张表一起删除和更改；

- 创建外键的时候，InnoDB会自动为其创建与字段名一样的索引

- 在创建表之后添加外键；创建外键的时候，两张表的引擎都需要支持外键，比如都是InnoDB引擎；其中有表是MyISAM引擎（不支持外键）时，执行创建外键的语句会出错；还有就是两张表连接的字段类型是否是同一类的，int和char就不是同一类型的

  ```sql
  //在temp表中添加outTable_id外键，该外键依赖于被参照表outTable中的id字段，同时删除和修改，保持数据一致性
  alter table temp add constraint  foreign key(outTable_id) references outTable(id) on delete cascade on update cascade; 
  //在创建表时创建外键
  create table temp(
  id int,
  name char(20),
  PRIMARY KEY(id),
  foreign key(id) references outTable(id)
  on delete cascade on update cascade); 
  //删除外键，注意是按照外键的约束名称删除外键，而按照外键的索引名称来删除；要知道外键的约束名称可以看表信息下面的DDL表的结构信息
  alter table 表名 drop foreign key 外键约束名称；
  ```

- 在修改或删除被参照表（outTable）的依赖字段(id)时，参照表(temp)中的外键(outTable_id)会跟着同时修改或删除；当相反时，如想要修改参照表中的外键时，而被参照表的依赖字段没变，这时会出错，修改不了，但是可以把整条记录删除了，这样两张表数据还是一致的

- 在删除外键的时候，如果表中已经有不满足外键约束的行数据时，删除的时候会报错；解决办法就是删除对应的行数据之后就可以删除外键了

### 十一、执行计划

- 查看执行计划

  ```sql
  EXPLAIN  SELECT s.id,s.name FROM students s WHERE NAME LIKE '谢%' 
  ```

  > ![image-20210426205601520](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210426205601520.png)

- 三大重要参数

  - type：连接类型

    - 从最好到最差：system、const、eq_ref、ref、ref_or_null、index_merge、unique_subquery、rang、index、ALL

    - const：一般const就是最优化的了，类型是const的话，那么肯定用到了主键primary key或者唯一索引unique，只匹配一行数据，这时的rows也为1

    - eq_ref：多表连接时最好的类型，就是where条件中有主键索引或者唯一索引

      > ![image-20210426211505250](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210426211505250.png)

    - ref：where子句或者连接条件不能用主键或者唯一索引的话，用普通索引，就是ref类型

      > ![image-20210426211950702](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210426211950702.png)

    - ref_or_null：相比于ref，增添了可以专门搜索包含NULL值得行；上面的几种类型都是无需优化sql的，后面的几种就需要

    - index_merge：使用了索引合并优化方法

    - unique_subquery：基于唯一索引的子查询

    - index_subquery：普通索引的子查询

    - range：给定范围内检索，比如where子句里面用in关键字（还要字段是主键才是rang类型，是其他普通字段就是ALL），列名 between  1 and 5 ，

    - index：扫描整个索引文件

    - ALL：扫描所有行，从硬盘中读取数据

  - key：MySql使用的索引

  - rows：执行查询的行数，越大越不好

- 其他参数

  - id：select查询序列号，序列号越大越先执行

  - select_type：select查询的类型，有以下几种类型

    - simple：表示简单的select，没有union（查询的结果集的并集）查询和子查询
    - primary：在有子查询的外面个语句
    - union：第二个或之后的
    - union result：union结果集的并集

  - table：输出行所用的表

  - possible_keys：提示使用哪个索引会在该表中找到行

  - key_len：使用的索引长度

  - ref：表示使用哪个列或者常数与key一起从表中选择行

  - extra：MySql解决查询的详细信息，感觉还是比较重要，有以下内容

    - distinct：发现第一个匹配行之后，停止搜索

    - not exists：表示使用了not exists（）、not in（）范围查询，这会导致索引失效的，exits()、in()不会

    - range checked for each record：没有找到合适的索引

    - using filesort：按关键字排序，并按排序顺序检索行；注意：排序时最好使用索引排序，出现filesort表示不太好

    - using index：表示是否使用了索引；索引覆盖优化

    - using temporary：查询时有group by、order by子句，有using temporary说明SQL需要优化，对驱动表可以直接排序，对非驱动表的字段排序需要对循环查询的合并结果（临时表）进行排序

      驱动表：当进行多表连接查询时，又指定了联接条件，满足查询条件的记录行数少的表为驱动表（优化器会自动选择，并不是根据sql写的连接表顺序连接的）

    - using where：where子句用于限制哪个行匹配下一个表，如果extra值不是using where并且type为ALL、Index，查询可能有错误

    - using sort_union()、using union()、using intersect()：这些说明如何为index_merge类型合并索引扫描

    - using index for group-by：表示MySql给找到了一个高效点的索引，可以用来查询group by或distinct的所有列，避免额外搜索硬盘访问实际的表

- 查看查询计划来sql优化的经验

  - 当看到subquery、dependent subquery、using temporary、using join buffer等出现时，就告诉SQL需要优化，通过加索引、修改SQL语句来避免这些词语的出现



### 十二、查询优化

- 慢查询日志：文件名默认为“主机+slow”.log；可以通过设置mysql的一个全局变量的值，sql超过该值的查询就会记录再慢查询日志中

### 十三、SQL语句

#### 一、更新

（1）mysqlyog里面可以把表导出为excel xml格式，然后把改文件的后缀名为.xlsx,就转换成了标准的excel表
（2）创建表的关键字是create（注意末尾有e）,
create table filename(
	id int(10) not null auto_increment comment '学生id',
	name varchar(100),
	primary key(id)
	)

- auto_increment列每个只允许有一个，而且该列还必须被索引；如果手动设置了自动增加值的列，那么后面新添加行时该值在上面值的基础上增加
- last_insert_id()：此方法返回最后一个auto_increment值；使用时有些问题
- 创建表时默认列都是not null

以上的代码赋值到sqlyog中去关键字就会自动转化为大写的，comment是备注，备注的这些字符串才要加单引号括起来，其他的表明，字段名都不用
（3）插入记录：当id设置为自增的时候id的插入值就不是value参数控制的了，而是从1，2开始逐次增加；注意插入记录的参数数量必须与表的列一样，除了自增主键可以不用写，数据库会自动给自增主键赋值；插入记录的时候如果列不插入完，没有设置默认值的列会报错

insert 

[LOW_PRIORITY]

into tablename(id,name)
value(1,'代彬')

注意：insert后面的可选项LOW_PRIORITY表示会降低insert语句的优先级，其他的update和delete语句一样的；这样的操作是为了提升select语句的优先级，因为数据检索通常是最重要的

- 插入多条记录；注意使用的是values关键字，每条记录用小括号和逗号分隔

  ```sql
  insert 
  into actor(actor_id,first_name,last_name,last_update)
  values(1,'PENELOPE','GUINESS','2006-02-15 12:34:33'),(2,'NICK','WAHLBERG','2006-02-15 12:34:33')
  ```


- 插入多条记录时还可以使用insert ……select语句；把select的结果表作为插入表

  ```sql
  insert 
  into actor(name ,age)
  select name,age
  from actor1;
  ```

  注意：插入不是按照列的名称来的，而是按照select的搜索顺序插入的，所以名称可以不用一一对应

（4）更新记录:name和id是自己定义的字段；注意写更新语句的时候一定要加where语句，不然就是把指定表的所有该属性设置为了这个值
update tablename
set name='熊波'
where id=1

- 注意：update没有from字句，直接接上表名，有set字句；如果没有where字句，那么会更新表中的所有行，使用时需要小心
- update操作具有原子性，中间遇见错误，那么所有的操作都会回退到更新之前；可用update ignore 表名……来打破update的原子性；这样中间遇见错误也可以继续操作下去

（5）删除记录：where语句的条件可以是其他任意的字段，用delete语句产出记录并不能让表的id恢复自动排序,
delete from tablename
where id=2

- 注意：delete字句没有table字句，有from字句，删除的单位是行

（6）删除表：drop语句是删除表的整个结构也全部删除了，不能重新插入数据了，得重新在创建表
drop table tablename
truncate语句也是删除表的意思，不过是删除整个表中的数据，并没有删除表的结构，还可以继续用插入语句插入新的记录
truncate table tablename 
（7）插入表的字段：添加表字段，括号中的column(列的意思)关键字可以省略
alter table tablename
add (column) name varchar(100)
（8）修改表名：
alter table tablename rename to newname
（9）删除表的字段：
alter table tablename 
drop 字段名
（10）修改字段的类型，注释等信息（不能修改字段名）：
ALTER  TABLE 表明 MODIFY   字段名  VARCHAR(120) COMMENT '注'
（11）修改字段名：注意要加上新类型，不然要语法出错
ALTER  TABLE 表明  CHANGE 旧字段名 新字段名 VARCHAR(100)

（12）修改数据库表的引擎

```sql
alter table 表名 ENGINE = InnoDB;
```

#### 二、查询

- 查看系统变量的值

  ```sql
  SHOW VARIABLES LIKE 'innodb_flush%'
  ```
  
- in()的使用和exists()的区别

  ```
  select name,id
  from user u
  where u.name in('123','name')
  ```

  - exists()只能用于子查询，并且exists的左边没有参数，in的左边有列名

  ```
  SELECT PASSWORD,u.username
  FROM USER u
  WHERE  EXISTS(SELECT t.id FROM students t WHERE t.id=u.id)
  ```

  

- select语句中子句的顺序

  select、from、where、group by、having、order by、limit

- select语句中只能有一个order by子句，但是可以一个order by有多个字段排序

（1）查询的where条件中常用的几种运算符关键字：not、and、or、like（字符类型相等用的不是=，而是like关键字）、大于，小于，大于等于，大于小于，等于，between	60 and 90（左右包含的60到90）；where查询条件中的字符串需要用''单引号括起来

（2）not、and、or的优先级：not最高、and、or最低；可以添加小括号来改变优先级

（3）字符匹配中的like模糊匹配；_表示匹配任意一位字符，%表示匹配任意多个字符；但是在实际的sql语句中使用时还需要配合escape关键字来对 _ 和%进行转义；sql中字符串都是用的单引号来包含的而不是双引号

（4）escape关键字的使用：例子：SELECT * FROM t_user t WHERE  t.name LIKE 'yao/_ming' ESCAPE '/'

1. 在字符串的后面加上escape '/'来标注/符号
2. 在字符串中的 _和%前面都需要加上/符号，不然还是不能转义

（5）投影查询与起别名；投影查询就是只返回表中的某些列，该表中返回列的名字作为别名，直接在select加字段的后加价格空格加上要使用的别名就是了；

例如：SELECT id, score points, name FROM students WHERE gender = 'M';

（6）查询结果按序输出；关键字 oder by，desc（descending），asc（ascent上升），oder by后面加上想要按照这个排序的字段，当想要在遇到数值一样大时还可以在后面继续加一个排序的字段；排序默认是升序；设计数据库一般把id作为主键

例子：

```sql
SELECT id, name, gender, score
FROM students
WHERE class_id = 1
ORDER BY score DESC;
```

（7）分页查询；好处是分开操作记录，避免了操作大量数据影响性能延迟；关键字：limit 3 offset 6；3是指一页三条记录，6表示从是第2页的最后一个个记录的索引开始（不包括），意思是返回的页面的索引为7，8，9；分页查询的关键在于，首先要确定每页需要显示的结果数量pageSize（这里是3），然后根据当前页的索引pageIndex（从1开始），确定LIMIT和OFFSET应该设定的值：

```
语法1：select * from user limit startIndex,pageSize//例如(0,2]表示第0个记录开始，所有记录两条为1页，返回从0开始的页
语法2：select * from user limit n	//表示返回从(0,n]的记录，返回前n条记录
```



- LIMIT总是设定为pageSize；

- OFFSET计算公式为pageSize * (pageIndex - 1)。

  ```sql
  select emp_no,salary
  from salaries s
  order by s.salary desc limit 1,1
  
  ```

  limit语句可以用在order by 的后面；当要查询类似工资第二多的员工时，就可以使用order by和limit分页实现

OFFSET超过了查询的最大数量并不会报错，而是得到一个空的结果集

（9）group by分组：分组的意思是：显示的时候行按分组的条件显示；例如：按班级字段分组，那每行就显示对应的班级；分组时一般还需设置别名，这样才清晰

（10）多表查询：示例：SELECT * FROM students, classes;返回结果中列是两个表的列之和，行是两个表的行之积；又称为笛卡尔查询，因此返回行数很多，使用需小心；可以给表也设置别名，还是在原名后面加空格加别名

（11）连接查询：关键字：INNER JOIN,ON,FULL OUTER JOIN,LEFT OUTER JOIN,RIGHT OUTER JOIN;连接分为内连接和外连接，外联接分为左、右、全连接；内外连接都需要用on关键字，后面是两个表的需要连接在一起的条件

内连接例子：

```sql
SELECT s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
FROM students s
INNER JOIN classes c
ON s.class_id = c.id;
```

外联接中的全连接：

```sql
SELECT s.id, s.name, s.class_id, c.name class_name, s.gender, s.score
FROM students s
FULL OUTER JOIN classes c
ON s.class_id = c.id;
```

几种连接使用的判断方法图：

```sql
SELECT ... FROM tableA ??? JOIN tableB ON tableA.column1 = tableB.column2;
```

> 我们把tableA看作左表，把tableB看成右表，那么INNER JOIN是选出两张表都存在的记录：
>
> ![inner-join](https://www.liaoxuefeng.com/files/attachments/1246892164662976/l)
>
> LEFT OUTER JOIN是选出左表存在的记录，左表中存在的记录右表没有，那么记录还是会显示，右表没有的字段会用NULL填充：
>
> ![left-outer-join](https://www.liaoxuefeng.com/files/attachments/1246893588481376/l)
>
> RIGHT OUTER JOIN是选出右表存在的记录，右表存在的记录左表没有，那么记录还是会显示，左表没有的字段会用NULL填充：
>
> ![right-outer-join](https://www.liaoxuefeng.com/files/attachments/1246893609222688/l)
>
> FULL OUTER JOIN则是选出左右表都存在的记录，会把两张表存在的字段都显示出来，没有的用NULL填充，这个跟内连接不同，内连接时显示两张表都同时存在的字段：
>
> ![full-outer-join](https://www.liaoxuefeng.com/files/attachments/1246893632359424/l)

- 当要多表（三张表以上）连接时，需要先把两个表连接起来组成一个新表，在把新表和另外张表连接；

  注意：select里面的字段相当于嵌套查询里的返回值，签到表直接用()括起来就是了

  ```sql
  select last_name,first_name,b.dept_name
  from employees em 
  left outer join (
      select emp_no,dept_name
      from dept_emp dm
      inner join departments de
      on dm.dept_no=de.dept_no
  ) b
  on b.emp_no=em.emp_no 
  ```

- 左连接的原理：把左表中的所有行添加到联接表中，再去扫描右表的所有记录，用on连接条件来作为筛选条件，满足条件就加入到联接表中，左边表没有与右边匹配时，select中的右边表中的字段列位置下就会填充null，然后继续尝试匹配右表的下一条记录；下图中password是右表的字段列

  ![image-20210610213409304](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610213409304.png)

- 窗口查询函数（针对排序的）

  ```sql
  select emp_no,salary,dense_rank() over (order by salary desc)
  from salaries sa
  where to_date='9999-01-01'
  order by salary desc
  ```

  | 窗口函数     | 返回类型 | 描述                                                         |
  | :----------- | :------- | :----------------------------------------------------------- |
  | rank()       | int      | 从1开始依次排序。若值相等则得到同样的序号；且下一个序号将会出现空位，例如，有3条排在第1位时，排序为：1，1，1，4······ |
  | dense_rank() | int      | 从1开始依次排序。若值相等则得到同样的序号；但下一个序号**不会**出现空位，有3条排在第1位时，排序为：1，1，1，2······ |
  | row_number() | int      | 从1开始依次排序，生成不会重复的序号，有3条排在第1位时，排序为：1，2，3，4······ |
  | ntile(n)     | int      | 将分组数据按照顺序平均分成n箱，返回当前值所在位置，n-th；如6条数据时，ntile(3)，那么为1，1，2，2，3，3 |

(12)集合查询

#### 三、关键字

（1）distinct：唯一的；关键字用在select要查找的字段前面，表示查找的该字段只显示唯一的值，如果有重复的则不会显示；

例子：

```sql
select distinct salary
from salaries
where to_date='9999-01-01'
order by salary desc;
```

#### 四、索引的设置

1. 索引：索引是关系数据库中对某一列和多列进行预排序的数据结构

2. 使用索引的好处：使用了索引之后，可以让数据库不用扫描整个表，而是直接定位到符合条件的记录，这样大大加快了查询的查询的速度；使用索引后删除、插入、更新表时同时也会修改索引，因此索引越多，更新、插入、删除记录的速度就越慢

3. 当要经常使用某些列查询时，可以设置一个该列的索引，例如对teacher表的user_name列添加索引，因为登录使用的是名称查找

   ```sql
   ALTER TABLE teacher
   ADD INDEX idx_user_name(user_name,password)//多列就在参数里用逗号隔开，多列用一个索引叫做联合索引
   ```

4. 对于主键；在关系型数据库中会自动为其创建索引，主键的索引是最高效的，因为主键会保证绝对唯一

5. 唯一索引；在业务中一些字段在同一个表中只能是唯一的（例如身份证号、邮箱地址），因为具有业务含义因此不适合设置为主键；

   - 方法一是为该列添加唯一索引：

   ```java
   ALTER TABLE teacher
   ADD UNIQUE INDEX idx_user_name(user_name)
   ```

   - 方法二是添加唯一约束而不创建索引：

     ```sql
     ALTER TABLE teacher
     ADD CONSTRAINT uni_user_name UNIQUE(user_name)
     ```


#### 五、锁的设置

- 设置读锁

  ```sql
  lock TABLE students READ
  unlock TABLES //释放锁
  ```

  当对students表上读锁之后，当前线程和其他线程都可读取表里的数据，但是都不能修改；

  当在修改时当前线程和其他线程的反应会不同

  - 当前线程的MySQL会直接报错
  - 其他线程会是一直等待执行的状态，这时不能做其他的事；只能等待当前线程释放锁，当其他线程检测到锁释放之后会继续执行sql语句

- 设置写锁

  ```sql
  LOCK TABLE students WRITE
  unlock tables//释放锁
  ```

  当对students表上写锁之后，当前线程可以读数据、可以更新数据，其他线程就不可以读数据、也不可以更新数据；其他线程在读或写数据的时候会一直处于执行态，直到当前线程释放锁之后才会继续执行sql语句
  
- 乐观锁的实现

  - 在数据库表中多设置一个version字段，表示表的版本；每次更新操作都要自增version字段


#### 六、MySQL正则表达式

- 模糊查询

  - %通配符：表示匹配任意数量的任意字符
  - _通配符：表示匹配一个任意字符

- 什么是正则表达式：用来匹配文本的特殊的串；MySQL中正则表达式用于where子句中；关键字是REGEXP

  > 语法：where 字段 REGEXP 模式
  >
  > 模式：由字面值、通配符或两者结合构成的搜索条件
  >
  > 通配符：用来匹配值的一部分的特殊字符，比如%，_等

- 基本字符匹配；使用特殊字符点（.）；点表示匹配任意一个字符或者包含该字符串的，似乎不能匹配字符串

  ![image-20210609205344639](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210609205344639.png)

- 匹配几个字符；或特殊字符（|）

  ![image-20210609205717862](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210609205717862.png)

  - 特殊字符'[ ]'是另外中形式的or语句

    ![image-20210609205957951](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210609205957951.png)

- 匹配范围；'[a-z]'表示a到z的任意字符、'[6-9]'表示6-9的数字；满足该范围的记录都会被查询出来

- 匹配特殊字符；使用双反斜杠\\\\，表示转义，Java里单反斜杠就是转义

- 匹配字符类；常用的有：匹配任意数字、任意大写字母、任意小写字母、任意字母

  ![2d0bddaae10e11640e65bdf324caf737.png](https://img-blog.csdnimg.cn/img_convert/2d0bddaae10e11640e65bdf324caf737.png)

- 匹配重复元字符

  | *     | 匹配前面的子表达式零次或多次。例如，zo* 能匹配 "z" 以及 "zoo"。* 等价于{0,}。 |
  | ----- | ------------------------------------------------------------ |
  | +     | 匹配前面的子表达式一次或多次。例如，'zo+' 能匹配 "zo" 以及 "zoo"，但不能匹配 "z"。+ 等价于 {1,}。 |
  | ?     | 匹配前面的子表达式零次或一次。例如，"do(es)?" 可以匹配 "do" 或 "does" 。? 等价于 {0,1}。 |
  | {n}   | n 是一个非负整数。匹配确定的 n 次。例如，'o{2}' 不能匹配 "Bob" 中的 'o'，但是能匹配 "food" 中的两个 o。 |
  | {n,}  | n 是一个非负整数。至少匹配n 次。例如，'o{2,}' 不能匹配 "Bob" 中的 'o'，但能匹配 "foooood" 中的所有 o。'o{1,}' 等价于 'o+'。'o{0,}' 则等价于 'o*'。 |
  | {n,m} | m 和 n 均为非负整数，其中n <= m。最少匹配 n 次且最多匹配 m 次。例如，"o{1,3}" 将匹配 "fooooood" 中的前三个 o。'o{0,1}' 等价于 'o?'。请注意在逗号和两个数之间不能有空格 |

- 定位符

  - ^：有两种用法；一个是文本的开始；另一个是当使用于集合中时，用来否定该集合，例如：^[123]表示集合中没有1或者2或者3是才匹配成功
  - $：文本的结尾
  - [[:<:]]：词的开始
  - [[:>:]]：词的结尾



#### 七、计算字段

- 字段就是列数据库中的列名；在行数据库中字段就是行名；通常对字段的转换、计算、格式化都是在MySQL Server（数据库服务器）上完成的；在数据库服务器上操作要比在应用服务器上操作要快得多

- 拼接字段；使用Concat(多个字段逗号分隔)函数；函数使用在select子句中

  ![image-20210609213615948](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210609213615948.png)

- 删除空格；Trim()删除字段两边的空格、Rtirm()删除字段右边的空格、Ltrim()删除字段左边的空格

- 别名；MySQL使用AS关键字对字段取别名（alias）；别名的好处：给字段一个易懂的名字、避免原字段中包含不符合规定的字符（如空格）；别名也叫导出列

- 算术计算；在select子句中有+、-、*、/四种算术运算符

#### 八、文本处理函数

- 处理文本串；主要作用分为获取字串、获取长度、填充字符、大小写转换、替换字符、去除空格、发音匹配、返回字符索引、字符串拼接

  Concat(多个字段逗号分隔)函数：连接多个字段

  Left（str,len）：返回串左边的len参数长度的字符

  #### ![image-20210610134521848](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610134521848.png)

  Right（）：返回串右边的值

  Length（）：返回串的字节长度；一个英文字母为1，一个中文为3

  #### ![image-20210610134752231](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610134752231.png)

  Locate（）：找出串的一个子串

  Upper（）：将串转换为大写

  Lower（）：将串转换为小写

  Trim（）：去除串两边的空格

  LTrim（）：去除串左边的空格

  RTrim（）：去除串右边的空格

  SubString（str,index）：返回从index开始的后面子串的字符；index是从1开始的，并且包括index位置的字符；关键字substr()一样的

  #### ![image-20210610134210328](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610134210328.png)

  lpad(str1,len,str2) ; rpad(str1,len,str2)：功能：用指定的字符实现左/右填充指定长度，将字符串str2填充到str1的开始处，使字符串的长度达到len

  instr()：功能：返回子串第一次出现的索引，如果找不到返回0；select instr('chinese','in')

  Replace（）：替换字符串；字符串中包含'小'字符串才替换为'大'，没有就不变

  #### ![image-20210610133435204](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610133435204.png)

  11 Soundex（）：返回串的SOUNDEX值（注意：SOUNDEX是一个将任何文本串转换为描述其语音表示的字母数字模式的算法）对于英文字母是保留首字母后按照发音来匹配的；对于中文是保留首个汉字，根据该汉字匹配，不管后面的发音对不对都会匹配成功；是使用在where子句中

  #### ![image-20210610131613609](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610131613609.png)

- 处理数值

- 处理日期与时间

  - 日期和时间的格式

    | **说明符** | **说明**                                                     |
    | ---------- | ------------------------------------------------------------ |
    | %a         | 工作日的缩写名称 (Sun..Sat)                                  |
    | %b         | 月份的缩写名称 (Jan..Dec)                                    |
    | %c         | 月份，数字形式(0..12)                                        |
    | %D         | 带有英语后缀的该月日期 (0th, 1st, 2nd, 3rd, ...)             |
    | %d         | 该月日期, 数字形式 (00..31)                                  |
    | %e         | 该月日期, 数字形式(0..31)                                    |
    | %f         | 微秒 (000000..999999)                                        |
    | %H         | 小时(00..23)                                                 |
    | %h         | 小时(01..12)                                                 |
    | %I         | 小时 (01..12)                                                |
    | %i         | 分钟,数字形式 (00..59)                                       |
    | %j         | 一年中的天数 (001..366)                                      |
    | %k         | 小时 (0..23)                                                 |
    | %l         | 小时 (1..12)                                                 |
    | %M         | 月份名称 (January..December)                                 |
    | %m         | 月份, 数字形式 (00..12)                                      |
    | %p         | 上午（AM）或下午（ PM）                                      |
    | %r         | 时间 , 12小时制 (小时hh:分钟mm:秒数ss 后加 AM或PM)           |
    | %S         | 秒 (00..59)                                                  |
    | %s         | 秒 (00..59)                                                  |
    | %T         | 时间 , 24小时制 (小时hh:分钟mm:秒数ss)                       |
    | %U         | 周 (00..53), 其中周日为每周的第一天                          |
    | %u         | 周 (00..53), 其中周一为每周的第一天                          |
    | %V         | 周 (01..53), 其中周日为每周的第一天 ; 和 %X同时使用          |
    | %v         | 周 (01..53), 其中周一为每周的第一天 ; 和 %x同时使用          |
    | %W         | 工作日名称 (周日..周六)                                      |
    | %w         | 一周中的每日 (0=周日..6=周六)                                |
    | %X         | 该周的年份，其中周日为每周的第一天, 数字形式,4位数;和%V同时使用 |
    | %x         | 该周的年份，其中周一为每周的第一天, 数字形式,4位数;和%v同时使用 |
    | %Y         | 年份, 数字形式,4位数                                         |
    | %y         | 年份, 数字形式 (2位数)                                       |
    | %%         | ‘%’文字字符                                                  |

  - DATE_FORMAT()：设置时间的格式

    ![image-20210610152010292](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610152010292.png)

  - now()返回的日期和时间：2021-06-10 14:34:32

  - AddDate(日期，interval表达式)：增加一个日期；interval后面的日期单位可以换成day日、week周、month月、quarter季（三个月）、year年

    ![image-20210610145550865](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610145550865.png)

  - AddTime(指定时期或者时间，增加的日期或者时间)：增加一个时间

    ![image-20210610150306046](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610150306046.png)

  - CurDate()：将当前日期按照'YYYY-MM-DD' 格式返回，如果有对数字的算术运算，那么以YYYYMMDD的格式返回

  - CurTime()：将当前时间以'HH:MM:SS'格式返回时间，如果有对数字的算术运算，那么以HHMMSS的格式返回

  - DATEDIFF() ：差值运算，返回起始时间 *expr*和结束时间*expr2*之间的天数。*Expr*和*expr2* 为日期或 date-and-time 表达式。计算中只用到这些值的日期部分；跟算术运算一样，还是可以返回负数，返回的整数的单位是day

    ![image-20210610151409703](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210610151409703.png)

  ![image-20210609220608240](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210609220608240.png)

#### 九、处理结果集

- union并集：union必须由两条以上的select子句组成，三个select需要两个union
  - union的使用要求：每个select必须包含相同的列（顺序可以不一样）、表达式或者聚集函数；列数据类型必须兼容，要可以隐式自动转换的类型
  - union与where行条件筛选的区别：简单查询用where，复杂的用union；union all可以查找时包含重复行，where子句是默认取消重复行的
- intersect交集
- excpet差集

#### 十、分组和排序

- 在select语句中使用group by字句实现分组
- 分组的注意事项
  - 如果在group by字句中嵌套了分组，数据将在最后规定的分组上进行汇总（外层还是内层？）
  - 如果分组中有null，那么null也会单独为一组
  - group by在where之后，order by之前
  - group by和having一起使用，having是以组为单位的，where是以行为单位的
  - group by默认会排序的，默认是升序
- 用order by字句排序

#### 十一、聚合函数

- COUNT()：计算参数的所有的记录数；如果参数为null，那么返回0；如果参数为常数，那么返回的就是记录数（有记录中有null也算）；如果参数为字段，那么返回的就是表中该列非null的记录数

  - 注意，使用count时，如果有where子句且where子句不满足时，那么count会返回0；

  - count(distinct col) 计算该列除 NULL 之外的不重复行数，注意 count(distinct col1, col2) 如果其中一列全为 NULL，那么即使另一列有不同的值，也返回为 0。

    例如：表中没有id=4的记录

    ![image-20210614233152933](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210614233152933.png)

- AVG()：计算参数字段的平均数；参数字段需为数值类型；如果字段列中有null，那么不加在分母里

- MAX()：计算参数字段的最大值；参数字段可为非数值型，这时返回按排序最前面的值；不算null

- MIN()：计算参数字段的最小值；参数字段可为非数值型，这时返回按排序最后面的值；不算null

- SUM()：计算该字段的累计和；参数字段需为数值类型

  - 当某一列的值全是 NULL 时，count(col)的返回结果为 0，但 sum(col)的返回结果为

    NULL，因此使用 sum()时需注意 NPE 问题。

    正例：可以使用如下方式来避免 sum 的 NPE 问题：SELECT IF(ISNULL(SUM(g)),0,SUM(g))

    FROM table;

- FLOOR()：地板；返回小数数值参数的向下舍入值；例如：FLOOR(1.56)=1

- CEILING()：天花板；返回小数数值的向上舍入值；例如：CEILING(1.44)=2

- ROUND():返回参数值的四舍五入后的值；例如：ROUND(1.56)=2

- isNULL()：使用 ISNULL()来判断是否为 NULL 值。

  说明：NULL 与任何值的直接比较都为 NULL。 

  1） NULL<>NULL 的返回结果是 NULL，而不是 false。 

  2） NULL=NULL 的返回结果是 NULL，而不是 true。 

  3） NULL<>1 的返回结果是 NULL，而不是 true。

### 十四、触发器

- 只有表才支持触发器，视图和临时表都不支持；每个表最多支持6个触发器（insert、update、delete语句的之前和之后）；之后insert、update、delete语句才支持触发器

- 创建触发器；

  > 语法：
  >
  > CREATE TRIGGER 触发器名字 
  >
  > AFTER INSERT ON 表名	//该触发器在每次插入成功之后执行
  >
  > FOR EACH ROW	//表示代码对每个插入行执行
  >
  > SELECT 'Product added';	//该语句为触发器触发之后执行的语句

- 删除触发器；

  > 语法：DROP TRIGGER 触发器名字

- 修改触发器；触发器不能更新或覆盖，因此触发器不能直接修改，只能删除旧触发器然后重新创建

- 三种触发器、NEW和OLD关键字

  - insert；可配合NEW关键字来对新插入行的值进行范围控制等操作
  - delete；可配合OLD关键字获取删除之前的行数据
  - update；在before update可使用new，在after update可使用OLD
  - NEW；表示新行的引用，如果没有新行产生则不能NEW；这两个关键字不区分大小写；对于before触发器的NEW才具有更改权限，其他的只有读权限
  - OLD；表示旧行的引用，如果没有旧行则不能用OLD；对于OLD引用的行是只读的，没有更改权限

- 例子，定义了1个UPDATE触发程序，用于检查更新每一行时将使用的新值，并更改值，使之位于0～100的范围内。它必须是BEFORE触发程序，这是因为，需要在将值用于更新行之前对其进行检查

  ```sql
  mysql> delimiter //
  mysql> CREATE TRIGGER upd_check BEFORE UPDATE ON account
      -> FOR EACH ROW
      -> BEGIN
      ->     IF NEW.amount < 0 THEN
      ->         SET NEW.amount = 0;
      ->     ELSEIF NEW.amount > 100 THEN
      ->         SET NEW.amount = 100;
      ->     END IF;
      -> END;//
  mysql> delimiter ;
  ```

  注意：也可以把上面的逻辑处理部分写在存储过程中，然后在触发器中调用（CALL命令）存储过程，这样对于需要多次使用的逻辑、从数个触发程序内部调用相同的子程序时比较方便

- 触发器的注意事项

  - 如果BEFORE触发程序失败，不执行相应行上的操作。
  - 仅当BEFORE触发程序（如果有的话）和行操作均已成功执行，才执行AFTER触发程序。
  - 如果在BEFORE或AFTER触发程序的执行过程中出现错误，将导致调用触发程序的整个语句的失败。
  - 对于事务性表，如果触发程序失败（以及由此导致的整个语句的失败），该语句所执行的所有更改将回滚。对于非事务性表，不能执行这类回滚，因而，即使语句失败，失败之前所作的任何更改依然有效
  - 触发程序不能调用将数据返回客户端的存储程序，也不能使用采用CALL语句的动态SQL（允许存储程序通过参数将数据返回触发程序）。
  - 触发程序不能使用以显式或隐式方式开始或结束事务的语句，如START TRANSACTION、COMMIT或ROLLBACK

### 十五、存储过程

- 使用存储过程的好处

  - 当用不同语言编写多客户应用程序，或多客户应用程序在不同平台上运行且需要执行相同的数据库操作之时
  - 安全极为重要之时。比如，银行对所有普通操作使用存储程序。这提供一个坚固而安全的环境，程序可以确保每一个操作都被妥善记入日志。在这样一个设置中，应用程序和用户不可能直接访问数据库表，但是仅可以执行指定的存储程序
  - 可以提高性能；使用存储过程比使用单条的SQL语句快
  - 存储程序也允许你在数据库服务器上有函数库

- 使用存储过程的缺点

  - 存储过程的编写要比SQL语句复杂
  - 创建、删除、执行存储过程需要相应的权限
    - 创建存储子程序需要CREATE ROUTINE权限。
    - 提醒或移除存储子程序需要ALTER ROUTINE权限。这个权限自动授予子程序的创建者。
    - 执行子程序需要EXECUTE权限。然而，这个权限自动授予 子程序的创建者。

- 执行存储过程；

  - 参数有三种类型；
    - IN；传递给存储函数
    - OUT；从存储函数传出
    - INOUT；表示该参数既要传出存储过程，经过计算后，又要传出存储过程

  > 语法：CALL 存储过程名称(@参数)，多个参数用逗号分隔

- 编写存储过程

  - 在命令行编写存储过程需要先用，delimiter//，然后在begin end中间编写程序，end后面加//，最后用delimiter结束；使用deimiter是为了区分结束符

  - 创建存储过程；CREATE PROCEDURE doiterate(in p1 INT)[comment'说明信息'] begin end;多个参数用逗号分隔，一个参数语法由三部分组成：参数作用+参数名+数据类型；例如（IN id INT，IN name CHAR，OUT acount DECIMAL（8，2））

  - 查看存储过程信息；SHOW PROCEDURE STATUS；这会显示所有的存储过程信息，要显示具体的某个存储过程，在后面加上like ''匹配

  - 声明变量；对一个CONTINUE处理程序，当前子程序的执行在执行 处理程序语句之后继续。对于EXIT处理程序，当前BEGIN...END复合语句的执行被终止；

    注意：声明变量是可用defalut字句设置默认值，不设置默认值的话任何数据类型的默认值都为null；
    
  - #### SELECT ... INTO语句

    ```
    SELECT col_name[,...] INTO var_name[,...] table_expr
    ```

    这个SELECT语法把选定的列直接存储到变量。因此，只有单一的行可以被取回。

    ```
    SELECT id,data INTO x,y FROM test.t1 LIMIT 1;
    ```

    注意，用户变量名在MySQL 5.1中是对大小写不敏感的。请参阅[9.3节，“](https://www.mysqlzh.com/doc/225/507.html#variables)[用户变量”](https://www.mysqlzh.com/doc/225/507.html#variables)。

    **重要**: SQL变量名不能和列名一样。如果SELECT ... INTO这样的SQL语句包含一个对列的参考，并包含一个与列相同名字的 局部变量，MySQL当前把参考解释为一个变量的名字。例如，在下面的语句中，xname 被解释为到xname *variable* 的参考而不是到xname *column*的：

    ```
    CREATE PROCEDURE sp1 (x VARCHAR(5))
      BEGIN
        DECLARE xname VARCHAR(5) DEFAULT 'bob';
        DECLARE newname VARCHAR(5);
        DECLARE xid INT;
        
        SELECT xname,id INTO newname,xid 
          FROM table1 WHERE xname = xname;
        SELECT newname;
      END;
    ```

    当这个程序被调用的时候，无论table.xname列的值是什么，变量newname将返回值‘bob’

- 游标（光标）

  - 什么是游标：游标是一个存储在MySQL服务器上的数据库查询，它是由Select语句查询出来的结果集；可以结合FETCH语句来遍历游标；游标是使用在存储过程之中的，存储过程使用完毕之后游标就删除了

  - #### 声明光标

    ```
    DECLARE cursor_name CURSOR FOR select_statement
    ```

    这个语句声明一个光标。也可以在子程序中定义多个光标，但是一个块中的每一个光标必须有唯一的名字。

    SELECT语句不能有INTO子句。

### 十六、流程控制

- 语句

  - ####  IF语句

    ```
    IF search_condition THEN statement_list
        [ELSEIF search_condition THEN statement_list] ...
        [ELSE statement_list]
    END IF
    ```

    IF实现了一个基本的条件构造。如果*search_condition*求值为真，相应的SQL语句列表被执行。如果没有*search_condition*匹配，在ELSE子句里的语句列表被执行。*statement_list*可以包括一个或多个语句。

    请注意，也有一个IF() 函数，它不同于这里描述的IF语句

  - #### CASE语句

    ```
    CASE case_value
        WHEN when_value THEN statement_list
        [WHEN when_value THEN statement_list] ...
        [ELSE statement_list]
    END CASE
    ```

    Or:

    ```
    CASE
        WHEN search_condition THEN statement_list
        [WHEN search_condition THEN statement_list] ...
        [ELSE statement_list]
    END CASE
    ```

    ```sql
    //select语句中的CASE语句，这时结束符不是end case；而是end
    mysql> SELECT CASE 1 WHEN 1 THEN 'one'
        ->     WHEN 2 THEN 'two' ELSE 'more' END;
            -> 'one'
    ```

    - case when的应用；常用在select语句中，作为一个子句；

      ```sql
    //统计各个分数段的学生人数情况，这里可以count(*)表示计算全表的记录,也可以count(score)表示score不等于null的记录
      SELECT COUNT(*),(
    	CASE 
      	WHEN s.score<60 THEN '不及格'
      	WHEN s.score>=60 AND s.score<80 THEN '良好'
      	WHEN s.score>=80 AND s.score<=100 THEN '优秀'
      	ELSE '不在范围内'
      	END 
    ) AS score2	//注意这里结果的别名不能取students表中的字段，不然会混淆导致结果出错
      FROM students s
    GROUP BY score2
      ```

      运行结果

      ![image-20210918212255128](D:\zuo_mian\java小知识\image\image-20210918212255128.png)
  
    存储程序的CASE语句实现一个复杂的条件构造。如果*search_condition* 求值为真，相应的SQL被执行。如果没有搜索条件匹配，在ELSE子句里的语句被执行。

    **注意：**这里介绍的用在 存储程序里的CASE语句与12.2节“控制流程函数”里描述的SQL CASE表达式的CASE语句有轻微不同。这里的CASE语句不能有ELSE NULL子句，并且用END CASE替代END来终止

  - ####  LOOP语句

    ```
    [begin_label:] LOOP
        statement_list
  END LOOP [end_label]
    ```

    LOOP允许某特定语句或语句群的重复执行，实现一个简单的循环构造。在循环内的语句一直重复直循环被退出，退出通常伴随着一个LEAVE 语句。

    LOOP语句可以被标注。除非*begin_label*存在，否则*end_label*不能被给出，并且如果两者都出现，它们必须是同样
  
  - #### LEAVE语句
  
    ```
    LEAVE label
    ```
  
    这个语句被用来退出任何被标注的流程控制构造。它和BEGIN ... END或循环一起被使用
  
  - #### ITERATE语句

    ```
  ITERATE label
    ```
  
    ITERATE只可以出现在LOOP, REPEAT, 和WHILE语句内。ITERATE意思为：“再次循环。”
  
    例如：
  
  ```
    CREATE PROCEDURE doiterate(p1 INT)
  BEGIN
      label1: LOOP
      SET p1 = p1 + 1;
        IF p1 < 10 THEN ITERATE label1; END IF;
      LEAVE label1;
      END LOOP label1;
      SET @x = p1;
    END
  ```
  
  - #### REPEAT语句
  
    ```
    [begin_label:] REPEAT
        statement_list
    UNTIL search_condition
    END REPEAT [end_label]
    ```
  
    REPEAT语句内的语句或语句群被重复，直至*search_condition* 为真。
  
    REPEAT 语句可以被标注。 除非*begin_label*也存在，*end_label*才能被用，如果两者都存在，它们必须是一样的

    例如：

    ```
  mysql> delimiter //
     
    mysql> CREATE PROCEDURE dorepeat(p1 INT)
        -> BEGIN
        ->   SET @x = 0;
        ->   REPEAT SET @x = @x + 1;
      		UNTIL @x > p1 END REPEAT;	//当@x>p1为真是退出循环；
        -> END
        -> //
    ```
```
  
注意：控制循序退出，还可用类似下面的语句配合FTECH语句遍历游标，当遍历完之后就会出现未找到的MySQL错误'02000'，就被continue handler捕获到，把done设置为1；这时的until done end repeat;语句就退出循环达到了遍历所有行的目的
  
  declare continue handler for SQLstate '02000' set done=1;
```

  

  - #### WHILE语句

    ```
    [begin_label:] WHILE search_condition DO
        statement_list
    END WHILE [end_label]
    ```
  
    WHILE语句内的语句或语句群被重复，当search_condition为真时才执行循环体；gin_label也存在，end_label才能被用，如果两者都存在，它们必须是一样的
    
  - 流程控制函数
  
    - IF(*expr1*,*expr2*,*expr3*)
    
    

  如果 *expr1* 是TRUE (*expr1* <> 0 and *expr1* <> NULL)，则 IF()的返回值为*expr2*; 否则返回值则为 *expr3*。IF() 的返回值为数字值或字符串值，具体情况视其所在语境而定
    
  - IFNULL(*expr1*,*expr2*)
    
  
    假如*expr1* 不为 NULL，则 IFNULL() 的返回值为 *expr1*; 否则其返回值为 *expr2*。IFNULL()的返回值是数字或是字符串，具体情况取决于其所使用的语境
  
    - NULLIF(*expr1*,*expr2*)
      
    
    如果*expr1* = *expr2* 成立，那么返回值为NULL，否则返回值为 *expr1*



### 十七、SQL优化

- 索引失效

  - 防止因字段类型不同造成的隐式转换，导致索引失效。

- select查询

  - 避免使用select *；要尽量使用索引覆盖来查询

- join关联

  - 两表关联时，对于关联字段（就是on的列），主表不要在该列上建立索引（不然会导致冗余索引，没使用到），因为只会使用第二个表该列上的索引；

- where条件

  - 建组合索引的时候，区分度最高的在最左边。

    正例：如果 where a=? and b=? ，如果 a 列的几乎接近于唯一值，那么只需要单建 idx_a

    索引即可。说明：存在非等号和等号混合时，在建索引时，请把等号条件的列前置。如：where c>? and

    d=? 那么即使 c 的区分度更高，也必须把 d 放在索引的最前列，即索引 idx_d_c。
    
  - in()，exists(),not in(),not exists()的区别

    - exists()因为参数只能是子查询，因此外表使用不到索引，内表可以

    ```sql
    SELECT u.id,u.username
    FROM USER u
    WHERE  EXISTS(SELECT t.id FROM students t WHERE t.id=u.id)
    ```

    ![image-20211011220200213](D:\zuo_mian\java小知识\image\image-20211011220200213.png)

    - 上面的user表没有使用到索引，走的是全表扫描；而子查询的students表使用了索引

      ```sql
      SELECT PASSWORD,id
      FROM USER u
      WHERE u.password IN(SELECT NAME FROM students t WHERE t.id=u.id)
      ```

      ![image-20211011220242252](D:\zuo_mian\java小知识\image\image-20211011220242252.png)

    - 相比于exists(),in()的参数可以是常量，例如'123',2等；当是常量时就能使用到索引；常量时not in也是索引为rang级别的

      ```sql
      SELECT PASSWORD,id
      FROM USER u
      WHERE u.password IN('123','root')
      ```

      ![image-20211011221734131](D:\zuo_mian\java小知识\image\image-20211011221734131.png)

    - 两者的应用场景

      - 参数是少量的常数时，就使用in；因为可以使用索引
      - 当参数是子查询时，两者的外表都无法使用到索引，这里跟网上的内容不一致，需要多验证！！！！！
      - 优化的话试试看使用union all会不会更好

- group up分组

  - 分为可使用索引列来分组、临时表、文件排序来分组；最好就是使用索引来分组（在分组列上加上索引）

  - 当用临时表和文件排序时，可以优化分组；

    - 在有join连接的分组中，分组列设置为连接列（就是on的那列）；

    - 分组的select中的列最好只有分组列，或者其他查询列跟分组列直接相关（比如id和姓名，姓名更有区分度，age区分度就低）；这个不是根据索引结构这些的要求，而是业务上的要求，当有其他更改时可能会导致结果改变

      ```sql
      select actor.first_name,actor.last_name,c.cnt
      from actor
      join(
      	select actor_id,count(*) as cnt
          from film f
          group by actor_id
      ) as c on c.actor_id=f.actor_id
      ```

    - group by默认会使用排序；如果不需要排序可以通过使用group by null来禁止文件排序；如果需要排序可以使用group by ASC或者DESC

- order by排序

  - order by和group by中的列可以是多列，比如order by name，age；保证这多列都要是有索引，并且还要是同一个表中的列，这样才能使用到索引来优化这个过程

- 聚合函数

  - count（）优化：count（星）效率最高的，相比与count（某一列）；count(星)中的星并不是扩展为所有列，而是忽略所有列而是直接统计所有行数；因此直接用count(星的效率要更高)

    ```sql
    select count(name)	//如果想统计null列name的行数，这个语句会被优化器优化为count(星)
    from user
    ```

- union连结

  - MySQL是通过创建并填充临时表的方式来执行union

  - 业务上不需要必须排除重复的行，那么都应该使用union all；因为使用union会对临时表做distinct数据唯一性检查；union all就不用检查，直接返回所有行（包括重复的）

- limit分页

  - 当limit 偏移量，每页的行数；当偏移量太大时，可以使用子查询和join主键来实现延迟关联；

  - 原理：先通过主键索引找到对应的行，然后在提取返回的行，这样通过索引来扫描就比扫描数据行快得多

    ```sql
    select name,age
    from user u
    join(
        select id
        from user
        where name='代彬'
        limit 1000000,50
    ) s on s.id=u.id
    ```

    

- 子查询

  - 尽量用关联查询来替换子查询（5.6版本之前）是这样；目前不知道

- 利用主键索引

  - InnoDB使用的是B+树索引结构，二级索引包含了两部分：一是索引本身，二是保存了主键索引的引用；因此可以利用这个默认的联合索引，来实现一些索引覆盖

    ```sql
    select name,id
    from user u
    where name='代彬'
    ```

  - 主键设置为自增，这样让相关联的数据在B+树中都聚集在一起，加快了插入时间（因为插入的顺序和扫描B树的顺序一致），避免了页分裂导致的空间碎片

    - 如果使用UUID来作为主键；因为数是随机的，那么可能会经常出现新插入的数在当前位置的前面，那么就会导致B树的其他数据的移动，因此插入的时间要比使用主键慢；还会导致页的数据稀疏，页分裂，空间碎片化；优点就是不会出现主键自增时id有可能出现id用完的情况，因为它是固定的32位16进制数