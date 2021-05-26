### 一、MySQL问题记录

（1）mysql创建数据库一般使用utf8和utf_general_ci

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
> - ### jdbc.url=jdbc:mysql://xxx.xx.xx.xxx:3306/db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
>
> - ### jdbc.username=root
>
> - ### jdbc.password=admin



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

#### 2.索引的结构

- BTREE:把数据页的索引值按一定的算法存储在二叉树中；表里面的行记录的索引值也存储字二叉树中；每次查询都是从root根节点开始，只有叶子结点才真实存储数据
- HASH:查询单条快，范围查询慢；HASH索引可以一次定位，不需要像树形索引那样逐层查找,因此具有极高的效率。但是，这种高效是有条件的，即只在“=”和“in”条件下高效
- RTREE:RTREE在MySQL很少使用，仅支持geometry数据类型，支持该类型的存储引擎只有MyISAM、BDb、InnoDb、NDb、Archive几种。相对于BTREE，RTREE的优势在于范围查找

#### 3.索引的设计原则

- 表的数据量少的情况下不用设置索引
- 索引设置在where条件设计的表字段上、多表连接的on关键字后的连接表字段上
- 设置联合索引时要遵循最左前缀原则，让最可能会使用的索引放在最左边（where中and条件的先后顺序对如何选择索引是无关的。因为优化器会去分析判断选用哪个索引）
- 使用短索引，就是可在索引列后面加上字符长度，如index(列(10))，这样就表示先只比较该列的前十个字符，如果还没找到，那就把前面十个字符都不匹配的排除掉
- 不可滥用索引，索引也要占磁盘空间，并且会对更新操作的性能有影响
- 外键必须设置索引，InnoDB中设置外键之后会自动创建一个跟列字段相同的索引；因此可以不用手动设置
- 更新操作很平凡的列不适合设置索引
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

   3.两种索引内部都是b+树；b+树分为了聚簇索引和辅助索引；innodb引擎和MyISMA引擎都有聚簇索引和辅助索引，innodb的主键索引是聚簇索引，MyISAM的主键索引是辅助索引；

   4.聚簇索引的优缺点

   　　优点：

   　　　　1.数据访问更快，因为聚簇索引将索引和数据保存在同一个B+树中，因此从聚簇索引中获取数据比非聚簇索引更快

   　　　	2.聚簇索引对于主键的排序、查找和范围查找速度非常快，因为数据是按照索引的大小排序的

   ​			3.聚簇索引适合排序的场景，非聚簇索引不适合

   　　缺点：
   
   　　　　1.插入速度严重依赖于插入顺序，按照主键的顺序插入是最快的方式（当更新的主键小于前一个主键值时，会把新主键插入到前面的主键中间去，导致后面主键会修改值），否则将会出现页分裂，严重影响性能。因此，对于InnoDB表，我们一般都会定义一个自增的ID列为主键；还有就是在大量插入新行之后，使用optimize table语句来优化表，减少行数据碎片；也可以使用独享表空间来弱化碎片
　　　　2.更新主键的代价很高，因为可能会导致被更新的行移动。因此，对于InnoDB表，我们一般定义主键为不可更新	
   　　　　3.二级索引访问需要两次索引查找，第一次找到主键值，第二次根据主键值找到行数据
   
   ​			4.如果主键比较大，那么辅助索引的将会变得更大，因为辅助索引的叶子节点存储有主键值

### 三、数据库事务

#### （1）事务的ACID特性

​	事务的目的是为了让一系列数据库操作要么全部成功，要么全部失败，保持同步性

- Atomicity:原子性；事务的操作是一个原子操作，不可分割，要么全部成功，要么全部失败；原子性是由undo log日志保证的，因为undo log日志保存有旧版本的数据状态，可以实现回滚操作

- Consistency:一致性；事务不但要满足数据库存储数据的规则，还要满足实际业务的规则，不满足外部业务的规则一样要回滚；在事务的开始和结束过程中，数据库的完整性约束并没有被破坏；一致性是由其他三大特性+程序代码保证，如账户字段有100，但需要减去200，这时就需要程序代码来控制正确的业务逻辑

- Isolation:隔离性；数据库提供一定的隔离机制，保证事务在不受外部并发操作影响的隔离级别环境下执行；通过MVCC来保证

- Durability:持久性；事务完成后，它对于数据的修改是永久性的，即使出现系统故障也能够保持；通过内存+redo log日志来保证，mysql修改数据同时在内存和redo log日志中记录这次操作

  | 事务的隔离级别   | 脏读 | 不可重复读 | 幻读 |
  | ---------------- | ---- | ---------- | ---- |
  | Read Uncommitted | Yes  | Yes        | Yes  |
  | Read Committed   | -    | Yes        | Yes  |
  | Repeatable Read  | -    | -          | Yes  |
  | Serializable     | -    | -          | -    |

#### （2）隔离级别

- Read Uncommitted（读未提交）;会读取其他事务未提交的数据（更新语句），如果其他事务回滚，那么该事务读取到的数据就是脏数据，这就是脏读
- Read Committed（读已提交）;通过只读取其他事务已经提交了的数据来避免脏读、脏写；但是不能避免重复读取数据，因为当重复读的时候读取的的都是最新的行数据，当其他事务在期间有更新行数据并提交时就会发生重复读的行数据不一致
- Repeatable Read（可重复读）;在事务第一次读数据的时候选定所要读取的数据行的版本，在该事务的整个期间都读取该版本的数据行，这样每次读取的数据都是一致的；幻读是指，在一个事务中，第一次查询某条记录，发现没有，但是，当试图更新这条不存在的记录时，竟然能成功，并且，再次读取同一条记录，它就神奇地出现了。
- Serializable（串行读）；所有事务都是串行的依次执行，避免了所有的脏读，不可重复读，幻读的情况；但是性能很低

Read Committed和Reoeatable Read 都是使用的MVCC（多版本并发控制）来实现的；并不是通过传统的读写锁，传统的读写锁只支持读读并发，其他的读写，写写，写读都是不能并发的；MVCC就可以实现读读，读写，写读的并发，写写会被阻塞；MVCC 机制会记录每行数据的历史版本，通过可见性算法、undo 日志以及 read view 事务id数组控制每个读操作所读取的行数据历史版本，



#### （3）MVCC多版本并发控制原理

- binlog、undolog、redolog日志：binlog是Mysql server级别的日志，redolog和undolog是InnoDB级别的日志；对mysql的操作都会在binlog中有记录

- 聚簇索引中有个隐藏式trx_id（表示事务id）字段、roll_pointer（上个版本的引用）字段，readview数组里存放的是活动的事务id值（活动的事务指的是未提交的事务），该数组是事务开始时就创建的；从记录行的版本链中获取事务id最大的id（表示是最新的事务），来跟readview数组比较，如果比最小的还小，那表示获取的事务id为已经提交了的事务版本，可以操作；如果id值在数组内或右边，那么用roll_pointer字段获取上个版本的id，直到小于readview数组
- MVCC保证隔离级别的原理
  - MVCC只能实现读已提交、可重复读两种隔离级别；实现读已提交时，就是每次select操作都会生成一个新的readview数组，这样两次读的话可能就不一样，这样就会有不可重复读现象；实现可重复读是都是用的一个readview数组，两次读的都是readview数组左边一个版本的事务行数据；

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
   - 删除有多个依赖的视图（会把所有有依赖的视图全部删了）：  drop view 视图名 cascade

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

- 间隙锁（NEXT-KEYlocking）：InnoDB在数据记录的第一个索引键之前和最后一个索引键之后的空间上设置锁定标记，是在两个索引键之间行数据被锁起来（这是左开右闭的一个区间），所以叫间隙锁
  
  - InnoDB自动使用间隙锁的条件：在可重复读及以下的隔离级别（读未提交、读已提交、可重复读）时，并且检索条件必须有索引（没有索引的话，会进行全表扫描，这时用的就是表锁，整个表的数据其他事务不能修改、删除、添加操作）
  
  - 间隙锁是用来解决幻读的，阻止其他事务的插入操作；原理：间隙锁锁定一个范围的键值之后，会锁定整个范围内所有的索引，不能再锁定范围内插入数据；例如，当使用范围查询时，该范围内的所有索引都会被锁定
  - 间隙锁的缺点；在一些场景下会对性能造成危害；在使用索引查询时，遇到联合索引会把联合索引内的其他索引也会被锁住；
  
- 记录锁：只锁一条记录；触发记录锁的条件是使用的字段上有唯一索引

- 临键锁：属于行级锁的一种，间隙锁和记录锁的结合，锁的是两个索引的闭区间上的所有行数据

- 意向锁；分为意向共享锁，意向排他锁；表上使用了意向共享锁之后，有其他事务试图在该表上添加共享锁，首先需要获得该表的意向共享锁；意向排他锁同理；当判断是否需要对表加锁时，避免了对索引树的遍历操作，有意向共享锁就不能继续加共享锁了

- 乐观锁与悲观锁：两种并不是具体的锁，而是一种思想；

  - 悲观锁：并发条件下悲观的认为一定会被其他事务干扰，所以在操作时直接锁住数据，操作完之后在释放锁；

    ```sql
    //在select语句后面加上for update，意思是加上写锁
    select nums from tb_goods_stock where goods_id = {$goods_id} for update;
    ```

  - 乐观锁：并发条件下乐观的认为不会被其他事务干扰，并设置有版本检测机制；设置一个版本号，当有数据更新时，把版本号加1；当在更新数据时，要加一条版本号是否更改的where条件；如果已经更改了，那么拒绝更新并返回给应用层，让用户重新操作；乐观锁因为是使用程序实现的，所以不会出现死锁的情况

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

- #### 时间类型

  - 只需要年月日时，使用Date类型
  - 常用的是Datetime类型

  > ![img](https://pic3.zhimg.com/80/v2-63be71052bbe166d1d1bfdf63a0dc83a_720w.jpg)

- #### 字符串类型

  MySQL中一个汉字占一个字符，具体占多少字节需要看使用的是什么编码方式，utf-8一个中文是三个字节，英文是一个字节；GBK是中文两个字节，英文一个字节，utf-16的中英文都是两个字节

  > #### ![preview](https://pic2.zhimg.com/v2-898fd037978be6953cc38b91b588592d_r.jpg)

char的长度默认是1个字节，varchar没有默认长度，因此必须声明长度，声明的长度为最大长度，如果超出则会报错，每超出就是存储数据的实际大小；blob可以存储图片和其他类型的文件；还有enum(需要设置可选数),JSON类型；blob要区分大小写，text不区分大小写；

CHAR 和 VARCHAR 类型类似，但它们保存和检索的方式不同。它们的最大长度和是否尾部空格被保留等方面也不同。在存储或检索过程中不进行大小写转换；char使用的空间大，但是效率比varchar高

选择作为varchar类型的数据尽量是不怎么改变长度的，varchar改变长度会导致“行迁移”现象，会造成多余的I/O操作

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

    - not exists：

    - range checked for each record：没有找到合适的索引

    - using filesort：按关键字排序，并按排序顺序检索行

    - using index：表示是否使用了索引

    - using temporary：查询时有group by、order by子句，有using temporary说明SQL需要优化，对驱动表可以直接排序，对非驱动表的字段排序需要对循环查询的合并结果（临时表）进行排序

      驱动表：当进行多表连接查询时，又指定了联接条件，满足查询条件的记录行数少的表为驱动表

    - using where：where子句用于限制哪个行匹配下一个表，如果extra值不是using where并且type为ALL、Index，查询可能有错误

    - using sort_union()、using union()、using intersect()：这些说明如何为index_merge类型合并索引扫描

    - using index for group-by：表示MySql给找到了一个高效点的索引，可以用来查询group by或distinct的所有列，避免额外搜索硬盘访问实际的表

- 查看查询计划来sql优化的经验

  - 当看到subquery、dependent subquery、using temporary、using join buffer等出现时，就告诉SQL需要优化，通过加索引、修改SQL语句来避免这些词语的出现



### 十二、查询优化

- 慢查询日志