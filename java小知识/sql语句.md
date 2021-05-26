### 一、更新

（1）mysqlyog里面可以把表导出为excel xml格式，然后把改文件的后缀名为.xlsx,就转换成了标准的excel表
（2）创建表的关键字是create（注意末尾有e）,
create table filename(
	id int(10) not null auto_increment comment '学生id',
	name varchar(100),
	primary key(id)
	)
以上的代码赋值到sqlyog中去关键字就会自动转化为大写的，comment是备注，备注的这些字符串才要加单引号括起来，其他的表明，字段名都不用
（3）插入记录：当id设置为自增的时候id的插入值就不是value参数控制的了，而是从1，2开始逐次增加；注意插入记录的参数数量必须与表的列一样，除了自增主键可以不用写，数据库会自动给自增主键赋值；插入记录的时候如果列不插入完，没有设置默认值的列会报错
insert into 
tablename(id,name)
value(1,'代彬')
（4）更新记录:name和id是自己定义的字段；注意写更新语句的时候一定要加where语句，不然就是把指定表的所有该属性设置为了这个值
update tablename
set name='熊波'
where id=1
（5）删除记录：where语句的条件可以是其他任意的字段，用delete语句产出记录并不能让表的id恢复自动排序,
delete from tablename
where id=2
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

### 二、查询

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

（7）查询结果按照页面返回；关键字：limit 3 offset 6；3是指一页三条记录，6表示从是第2页的最后一个个记录的索引开始（不包括），意思是返回的页面的索引为7，8，9；分页查询的关键在于，首先要确定每页需要显示的结果数量pageSize（这里是3），然后根据当前页的索引pageIndex（从1开始），确定LIMIT和OFFSET应该设定的值：

- LIMIT总是设定为pageSize；

- OFFSET计算公式为pageSize * (pageIndex - 1)。

  ```sql
  select emp_no,salary
  from salaries s
  order by s.salary desc limit 1,1
  
  ```

  limit语句可以用在order by 的后面；当要查询类似工资第二多的员工时，就可以使用order by和limit分页实现

OFFSET超过了查询的最大数量并不会报错，而是得到一个空的结果集

（8）聚合查询；常用的几种聚合函数：

- COUNT()：计算参数的所有的记录数
- AVG()：计算参数字段的平均数；参数字段需为数值类型
- MAX()：计算参数字段的最大值；参数字段可为非数值型，这时返回按排序最前面的值
- MIN()：计算参数字段的最小值；参数字段可为非数值型，这时返回按排序最后面的值
- SUM()：计算该字段的累计和；参数字段需为数值类型
- FLOOR()：地板；返回小数数值参数的向下舍入值；例如：FLOOR(1.56)=1
- CEILING()：天花板；返回小数数值的向上舍入值；例如：CEILING(1.44)=2
- ROUND():返回参数值的四舍五入后的值；例如：ROUND(1.56)=2

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

- 左连接的原理：从左表选出一条数据，选出所有与on匹配的右表记录（n条）进行连接，形成n条记录；如果右边表没有与on条件匹配的列，那连接的字段都填充NULL；然后继续读取左表的下一条记录

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

### 三、关键字

（1）distinct：唯一的；关键字用在select要查找的字段前面，表示查找的该字段只显示唯一的值，如果有重复的则不会显示；

例子：

```sql
select distinct salary
from salaries
where to_date='9999-01-01'
order by salary desc;
```

### 四、索引的设置

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


### 五、锁的设置

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

  