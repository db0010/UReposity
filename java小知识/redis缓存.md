# 一、Redis五种数据类型

一：字符串类re型string

字符串类型是Redis的最基本类型，它可以存储任何形式的字符串，例如字符串类型、整型、浮点。其它的四种类型都是字符串类型的不同形式。

- 字符串单个数据类型能存储512M的数据

| 取值设值         | get、set    | 语法：GET key，SET key value  value如果有空格需要双引号以示区分 |
| ---------------- | ----------- | ------------------------------------------------------------ |
| 整数递增         | incr        | 语法：INCR key   默认值为0，所以首先执行命令得到 1 ，不是整型提示错误 |
| 增加指定的整数   | incrby      | 语法：INCRBY key increment                                   |
| 整数递减         | decr        | 语法：DECR key  默认值为0，所以首先执行命令得到 -1，不是整型提示错误 |
| 减少指定的整数   | decrby      | 语法：DECRBY key increment                                   |
| 增加指定浮点数   | incrbyfloat | 语法：INCRBYFLOAT key increment  与INCR命令类似，只不过可以递增一个双精度浮点数 |
| 向尾部追加值     | append      | 语法：APPEND key value  redis客户端并不是输出追加后的字符串，而是输出字符串总长度 |
| 获取字符串长度   | strlen      | 语法：STRLEN key  如果键不存在返回0，注意如果有中文时，一个中文长度是3，redis是使用UTF-8编码中文的 |
| 获取多个键值     | mget        | 语法：MGET key [key ...]  例如：MGET key1 key2               |
| 设置多个键值     | mset        | 语法：MSET key value [key value ...]  例如：MSET key1 1 key2 "hello redis" |
| 二进制指定位置值 | getbit      | 语法：GETBIT key offset  例如：GETBIT key1 2，key1为hello 返回 1，返回的值只有0或1，当key不存在或超出实际长度时为0 |
| 设置二进制位置值 | setbit      | 语法：SETBIT key offset value ，返回该位置的旧值             |
| 二进制是1的个数  | bitcount    | 语法：BITCOUNT key [start end] ，start 、end为开始和结束字节 |
| 位运算           | bitop       | 语法：BITOP operation destkey key [key ...]  ，operation支持AND、OR、XOR、NOT |
| 偏移             | bitpos      | 语法：BITPOS key bit [start] [end]                           |

 

​      

二：散列类型hash

| 设置单个         | hset    | 语法：HSET key field value，不存在时返回1，存在时返回0，没有更新和插入之分 |
| ---------------- | ------- | ------------------------------------------------------------ |
| 设置多个         | hmset   | 语法：HMSET key field value [field value ...]                |
| 读取单个：       | hget    | 语法：HGET key field，不存在是返回null                       |
| 读取多个         | hmget   | 语法：HMGET key field [field ...]                            |
| 读取全部         | hgetAll | 语法：HGETALL key，返回时字段和字段值的列表                  |
| 判断字段是否存在 | hexists | 语法：HEXISTS key field，存在返回1 ，不存在返回0             |
| 字段不存在时赋值 | hsetnx  | 语法：HSETNX key field value，与hset命令不同，hsetnx是键不存在时设置值 |
| 增加数字         | hincrby | 语法：HINCRBY key field increment ，返回增加后的数，不是整数时会提示错误 |
| 删除字段         | hdel    | 语法：HDEL key field [field ...] ，返回被删除字段的个数      |
| 只获取字段名     | hkeys   | 语法：HKEYS key ，返回键的所有字段名                         |
| 只获取字段值     | hvals   | 语法：HVALS key  ，返回键的所有字段值                        |
| 字段数量         | hlen    | 语法：HLEN key ，返回字段总数                                |

三：列表类型（list）

内部使用双向链表实现，所以获取越接近两端的元素速度越快，但通过索引访问时会比较慢；列表类型的元素是有序且可以重复的

| 添加左边元素           | lpush     | 语法：LPUSH key value [value ...]  ，返回添加后的列表元素的总个数 |
| ---------------------- | --------- | ------------------------------------------------------------ |
| 添加右边元素           | rpush     | 语法：RPUSH key value [value ...]  ，返回添加后的列表元素的总个数 |
| 移除左边第一个元素     | lpop      | 语法：LPOP key  ，返回被移除的元素值                         |
| 移除右边第一个元素     | rpop      | 语法：RPOP key ，返回被移除的元素值                          |
| 列表元素个数           | llen      | 语法：LLEN key， 不存在时返回0，redis是直接读取现成的值，并不是统计个数 |
| 获取列表片段           | lrange    | 语法：LRANGE key start stop，如果start比stop靠后时返回空列表，0 -1 返回整个列表。 正数时：start 开始索引值，stop结束索引值（索引从0开始）。负数时：例如 lrange num -2 -1，-2表示最右边第二个，-1表示最右边第一个 |
| 删除指定值             | lrem      | 语法：LREM key count value，返回被删除的个数。 count>0，从左边开始删除前count个值为value的元素。 count<0，从右边开始删除前\|count\|个值为value的元素。 count=0，删除所有值为value的元素 |
| 索引元素值             | lindex    | 语法：LINDEX key index ，返回索引的元素值，-1表示从最右边的第一位 |
| 设置元素值             | lset      | 语法：LSET key index value                                   |
| 保留列表片段           | ltrim     | 语法：LTRIM key start stop，start、top 参考lrange命令        |
| 一个列表转移另一个列表 | rpoplpush | 语法：RPOPLPUSH source desctination ，从source列表转移到desctination列表，命令分两步看，首先source列表RPOP右移除，再desctination列表LPUSH |

四：集合类型（set）

集合类型值具有唯一性，常用操作是向集合添加、删除、判断某个值是否存在，集合内部是使用值为空的散列表实现的。

| 添加元素           | sadd        | 语法：SADD key member [member ...] ，向一个集合添加一个或多个元素，因为集合的唯一性，所以添加相同值时会被忽略。返回成功添加元素的数量。 |
| ------------------ | ----------- | ------------------------------------------------------------ |
| 删除元素           | srem        | 语法：SREM key member [member ...] 删除集合中一个或多个元素，返回成功删除的个数。 |
| 获取全部元素       | smembers    | 语法：SMEMBERS key ，返回集合全部元素                        |
| 值是否存在         | sismember   | 语法：SISMEMBER key member ，如果存在返回1，不存在返回0      |
| 差运算             | sdiff       | 语法：SDIFF key [key ...] ，例如：集合A和集合B，差集表示A-B，在A里有的元素B里没有，返回差集合；多个集合(A-B)-C |
| 交运算             | siniter     | 语法：SINTER key [key ...]，返回交集集合，每个集合都有的元素 |
| 并运算             | sunion      | 语法：SUNION key [key ...]，返回并集集合，所有集合的元素     |
| 集合元素个数       | scard       | 语法：SCARD key ，返回集合元素个数                           |
| 集合运算后存储结果 | sdiffstroe  | 语法：SDIFFSTROE destination key [key ...] ，差运算并存储到destination新集合中。 SINTERSTROE destination key [key ...]，交运算并存储到destination新集合中。 SUNIONSTROE destination key [key ...]，并运算并存储到destination新集合中 |
| 随机获取元素       | srandmember | 语法SRANDMEMBER key [count]，根据count不同有不同结果，count大于元素总数时返回全部元素。count>0 ，返回集合中count不重复的元素。count<0，返回集合中count的绝对值个元素，但元素可能会重复 |
| 弹出元素           | spop        | 语法：SPOP key [count] ，因为集合是无序的，所以spop会随机弹出一个元素 |

五：有序集合类型（zset）

- 有序集合的键成为key，每个key都是不重复的；值被叫做score，score必须为浮点数，zset集合就是根据socore的大小来排序
- 有序集合是散列表和跳跃表实现的，即使读取中间的元素也比较快。

| 添加集合元素     | zadd           | 语法：ZADD key [NX\|XX] [CH] [INCR] score member [score member ...]，不存在添加，存在更新。 |
| ---------------- | -------------- | ------------------------------------------------------------ |
| 删除元素         | zrem           | 语法：zrem key  member，根据成员来删除键值对                 |
| 获取元素分数     | zscore         | 语法：ZSCORE key member ，返回元素成员的score分数            |
| 元素小到大       | zrange         | 语法：ZRANGE key start top [WITHSCORES] ，参考LRANGE ，加上withscores 返回带元素，即元素，分数。当分数一样时，按元素排序 |
| 元素大到小       | zrevrange      | 语法：ZREVRANGE key start [WITHSCORES] ，与zrange区别在于zrevrange是从大到小排序 |
| 指定分数范围元素 | zrangebyscore  | 语法：ZRANGEBYSCORE key min max [WITHSCORE] [LIMIT offest count]。返回从小到大的在min和max之间的元素，( 符号表示不包含，例如：80-100，(返回带分数 limit offest count 向左偏移offest个元素，并获取前count个元素 |
| 指定分数范围元素 | zrevrangescore | 语法：ZREVRANGEBYSCORE key max  min [WITHSCORE] [LIMIT offest count]与zrangebyscore类似，只不过该命令是从大到小排序的 |
| 增加分数         | zincrby        | 语法：ZINCRBY key increment member ，注意是增加分数，返回增加后的分数；如果成员不存在，则添加一个为0的成员。 |



# 二、redis通用命令

## 常用管理命令

#### 1、启动Redis

```
> redis-server [--port 6379]
```

如果命令参数过多，建议通过配置文件来启动Redis。

```
> redis-server [xx/xx/redis.conf]
```

6379是Redis默认端口号。

#### 2、连接Redis

```
> ./redis-cli [-h 127.0.0.1 -p 6379]
```

#### 3、停止Redis

```
> redis-cli shutdown

> kill redis-pid
```

以上两条停止Redis命令效果一样。

#### 4、发送命令

给Redis发送命令有两种方式：

1、redis-cli带参数运行，如：

```
> redis-cli shutdown
not connected> 
```

这样默认是发送到本地的6379端口。

2、redis-cli不带参数运行，如：

```
> ./redis-cli

127.0.0.1:6379> shutdown
not connected> 
```

#### 5、测试连通性

```
127.0.0.1:6379> ping
PONG
```

## key操作命令

#### 获取所有键

> 语法：keys pattern

```
127.0.0.1:6379> keys *
1) "javastack"
```

- *表示通配符，表示任意字符，会遍历所有键显示所有的键列表，时间复杂度O(n)，在生产环境不建议使用。

#### 获取键总数

> 语法：dbsize

```
127.0.0.1:6379> dbsize
(integer) 6
```

获取键总数时不会遍历所有的键，直接获取内部变量，时间复杂度O(1)。

#### 查询键是否存在

> 语法：exists key [key …]

```
127.0.0.1:6379> exists javastack java
(integer) 2
```

查询查询多个，返回存在的个数。

#### 删除键

> 语法：del key [key …]

```
127.0.0.1:6379> del java javastack
(integer) 1
```

可以删除多个，返回删除成功的个数。

#### 查询键类型

> 语法： type key

```
127.0.0.1:6379> type javastack
string
```

#### 移动键

> 语法：move key db

如把javastack移到2号数据库。

```
127.0.0.1:6379> move javastack 2
(integer) 1
127.0.0.1:6379> select 2
OK
127.0.0.1:6379[2]> keys *
1) "javastack"
```

#### 查询key的生命周期（秒）

> 秒语法：ttl key
>
> 毫秒语法：pttl key

```
127.0.0.1:6379[2]> ttl javastack
(integer) -1
```

-1：永远不过期。

#### 设置过期时间

> 秒语法：expire key seconds
>
> 毫秒语法：pexpire key milliseconds

```
127.0.0.1:6379[2]> expire javastack 60
(integer) 1
127.0.0.1:6379[2]> ttl javastack
(integer) 55
```

#### 设置永不过期

> 语法：persist key

```
127.0.0.1:6379[2]> persist javastack
(integer) 1
```

#### 更改键名称

> 语法：rename key newkey

```
127.0.0.1:6379[2]> rename javastack javastack123
OK
```

## 字符串操作命令

字符串是Redis中最基本的数据类型，单个数据能存储的最大空间是512M。

#### 存放键值

> 语法：set key value [EX seconds] [PX milliseconds] [NX|XX]

nx：如果key不存在则建立，xx：如果key存在则修改其值，也可以直接使用setnx/setex命令。

```
127.0.0.1:6379> set javastack 666
OK
```

> 设置键值并且设置过期时间（比分开操作快），时间单位是秒
>
> 语法：SETEX key seconds value

![image-20210531143342393](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210531143342393.png)

> 如果键不存在才设置，存在就不设置；在分布式锁和乐观锁中使用；还有hash的hsetnx命令
>
> 语法：SETNX key value

![image-20210602143733474](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602143733474.png)

> 通过set可以把对象名设置为键，json字符串设置为值
>
> 例如：set user:1 {name:zhangsan,age:20}

![image-20210603114401282](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603114401282.png)

#### 获取键值

> 语法：get key

```
127.0.0.1:6379[2]> get javastack
"666"
```

#### 值递增/递减

如果字符串中的值是数字类型的，可以使用incr命令每次递增，不是数字类型则报错。

> 语法：incr key

```
127.0.0.1:6379[2]> incr javastack
(integer) 667
```

一次想递增N用incrby命令，如果是浮点型数据可以用incrbyfloat命令递增。

同样，递减使用decr、decrby命令。

#### 批量存放键值

> 语法：mset key value [key value …]

```
127.0.0.1:6379[2]> mset java1 1 java2 2 java3 3
OK
```

> 批量设置字符串键值对，如果其中有任何一个键已经存在，那么整个msetnx操作都会失败，因为msetnx是一个原子操作
>
> 语法：MSETNX key value [key value ...]

#### 获取获取键值

> 语法：mget key [key …]

```
127.0.0.1:6379[2]> mget java1 java2
1) "1"
2) "2"
```

Redis接收的是UTF-8的编码，如果是中文一个汉字将占3位返回。

#### 获取值长度

> 语法：strlen key
>
> 127.0.0.1:6379[2]> strlen javastack
> (integer) 3

#### 追加内容

> 语法：append key value

```
127.0.0.1:6379[2]> append javastack hi
(integer) 5
```

向键值尾部添加，如上命令执行后由666变成666hi

#### 获取部分字符

> 语法：getrange key start end

```
> 127.0.0.1:6379[2]> getrange javastack 0 4
"javas"
```

#### 更新键值

> 更新键值，并且返回原来键值，如果新设置的键值不是字符串，那么出现错误
>
> 语法：GETSET key value
>
> 应用场景：getset和incr命令结合使用可以实现支持重置的计数器功能；

![image-20210603230016881](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603230016881.png)

## 集合操作命令

集合类型和列表类型相似，只不过是集合是无序且不可重复的。

## 集合

#### 存储值

> 语法：sadd key member [member …]

```
// 这里有8个值（2个java），只存了7个
127.0.0.1:6379> sadd langs java php c++ go ruby python kotlin java
(integer) 7
```

#### 获取元素

> 获取所有元素语法：smembers key

```
127.0.0.1:6379> smembers langs
1) "php"
2) "kotlin"
3) "c++"
4) "go"
5) "ruby"
6) "python"
7) "java"
```

> 随机获取语法：srandmember langs count

```
127.0.0.1:6379> srandmember langs 3
1) "c++"
2) "java"
3) "php"
```

#### 判断集合是否存在元素

> 语法：sismember key member

```
127.0.0.1:6379> sismember langs go
(integer) 1
```

#### 获取集合元素个数

> 语法：scard key

```
127.0.0.1:6379> scard langs
(integer) 7
```

#### 删除集合元素

> 语法：srem key member [member …]

```
127.0.0.1:6379> srem langs ruby kotlin
(integer) 2
```

#### 弹出元素

> 语法：spop key [count]

```
127.0.0.1:6379> spop langs 2
1) "go"
2) "java"
```

## 有序集合

和列表的区别：

1、列表使用链表实现，两头快，中间慢。有序集合是散列表和跳跃表实现的，即使读取中间的元素也比较快。

2、列表不能调整元素位置，有序集合能。

3、有序集合比列表更占内存。

- 当zset的score都是一样的时，就会改为对member进行排序，可以利用这一点来设计成员名实现对成员名的顺序扫描；可用于计数器

#### 存储值

> 语法：zadd key [NX|XX] [CH] [INCR] score member [score member …]

```
127.0.0.1:6379> zadd footCounts 16011 tid 20082 huny 2893 nosy
(integer) 3
```

#### 获取元素分数

> 语法：zscore key member

```
127.0.0.1:6379> zscore footCounts tid
"16011"	
```

> 获取排名范围排名语法：zrange key start stop [WITHSCORES]
>
> 有序集合默认按照score值的升序排列

```
// 获取所有，没有分数
127.0.0.1:6379> zrange footCounts 0 -1
1) "nosy"
2) "tid"
3) "huny"

// 获取所有及分数
127.0.0.1:6379> zrange footCounts 0 -1 Withscores
1) "nosy"
2) "2893"
3) "tid"
4) "16011"
5) "huny"
6) "20082"
```

> 获取指定分数范围排名语法：zrangebyscore key min max [WITHSCORES] [LIMIT offset count]

```
127.0.0.1:6379> zrangebyscore footCounts 3000 30000 withscores limit 0 1
1) "tid"
2) "16011"
```

> 获取成员member的有序集合（按照score的大小来计算排列顺序，降序排列）
>
> 语法：zrevrange key start stop [WITHSCORES]

![image-20210530223204357](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210530223204357.png)

#### 增加指定元素分数

> 语法：zincrby key increment member

```
127.0.0.1:6379> zincrby footCounts 2000 tid
"18011"
```

#### 获取集合元素个数

> 语法：zcard key

```
127.0.0.1:6379> zcard footCounts
(integer) 3
```

#### 获取指定范围分数个数

> 语法：zcount key min max

```
127.0.0.1:6379> zcount footCounts 2000 20000
(integer) 2
```

#### 删除指定元素

> 语法：zrem key member [member …]

```
127.0.0.1:6379> zrem footCounts huny
(integer) 1
```

> 删除指定索引范围内的元素（都是闭区间）
>
> 语法：zremrangebyrank key start stop

![image-20210531134446824](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210531134446824.png)

#### 获取元素排名

> 语法：zrank key member

```
127.0.0.1:6379> zrank footCounts tid
(integer) 1
```

#### 多个集合运算

> 计算指定的numskey个集合的交集
>
> 语法：zinterstore destination numkeys key [key ...] [WEIGHTS weight] 

![image-20210530225406930](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210530225406930.png)

> 计算指定numskey个集合的并集
>
> 语法：zunionstore destination numkeys key [key ...] [WEIGHTS weight] [SUM|MIN|MAX]
>
> ​	计算给定的numkeys个有序集合的并集，并且把结果放到destination中。在给定要计算的key和其它参数之前，必须先给定key个数(numberkeys)。 默认情况下，结果集中某个成员的score值是所有给定集下该成员score值之和。
>
> ​	使用WEIGHTS选项，你可以为每个给定的有序集指定一个乘法因子，意思就是，每个给定有序集的所有成员的score值在传递给聚合函数之前都要先乘以该因子。如果WEIGHTS没有给定，默认就是1。
>
> ​	使用AGGREGATE选项，你可以指定并集的结果集的聚合方式。默认使用的参数SUM，可以将所有集合中某个成员的score值之和作为结果集中该成员的score值。如果使用参数MIN或者MAX，结果集就是所有集合中元素最小或最大的元素。
>
> ​	如果key destination存在，就被覆盖

![image-20210530225518982](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210530225518982.png)

## 列表操作命令

列表类型是一个有序的字段串列表，内部是使用双向链表实现，所有可以向两端操作元素，获取两端的数据速度快，通过索引到具体的行数比较慢。

列表类型的元素是有序且可以重复的。

#### 存储值

> 左端存值语法：lpush key value [value …]

```
127.0.0.1:6379> lpush list lily sandy
(integer) 2
```

> 右端存值语法：rpush key value [value …]

```
127.0.0.1:6379> rpush list tom kitty
(integer) 4
```

> 索引存值语法：lset key index value

```
127.0.0.1:6379> lset list 3 uto
OK
```

#### 弹出元素

> 左端弹出语法：lpop key

```
127.0.0.1:6379> lpop list
"sandy"
```

> 右端弹出语法：rpop key

```
127.0.0.1:6379> rpop list
"kitty"
```

#### 获取元素个数

> 语法：llen key

```
127.0.0.1:6379> llen list
(integer) 2
```

#### 获取列表元素

> 两边获取语法：lrange key start stop

```
127.0.0.1:6379> lpush users tom kitty land pony jack maddy
(integer) 6

127.0.0.1:6379> lrange users 0 3
1) "maddy"
2) "jack"
3) "pony"
4) "land"

// 获取所有
127.0.0.1:6379> lrange users 0 -1
1) "maddy"
2) "jack"
3) "pony"
4) "land"
5) "kitty"
6) "tom"

// 从右端索引
127.0.0.1:6379> lrange users -3 -1
1) "land"
2) "kitty"
3) "tom"
```

> 索引获取语法：lindex key index

```
127.0.0.1:6379> lindex list 2
"ketty"

// 从右端获取
127.0.0.1:6379> lindex list -5
"sady"
```

#### 删除元素

> 根据值删除语法：lrem key count value

```
127.0.0.1:6379> lpush userids 111 222 111 222 222 333 222 222
(integer) 8

// count=0 删除所有
127.0.0.1:6379> lrem userids 0 111
(integer) 2

// count > 0 从左端删除前count个
127.0.0.1:6379> lrem userids 3 222
(integer) 3

// count < 0 从右端删除前count个
127.0.0.1:6379> lrem userids -3 222
(integer) 2
```

> 范围删除语法：ltrim key start stop

```
// 只保留2-4之间的元素
127.0.0.1:6379> ltrim list 2 4
OK
```

## 散列操作命令

redis字符串类型键和值是字典结构形式，这里的散列类型其值也可以是字典结构。

#### 存放键值

> 单个语法：hset key field value

```
127.0.0.1:6379> hset user name javastack
(integer) 1
```

> 多个语法：hmset key field value [field value …]

```
127.0.0.1:6379> hmset user name javastack age 20 address china
OK
```

> 不存在时语法：hsetnx key field value

```
127.0.0.1:6379> hsetnx user tall 180
(integer) 0
```

#### 获取字段值

> 单个语法：hget key field

```
127.0.0.1:6379> hget user age
"20"
```

> 多个语法：hmget key field [field …]

```
127.0.0.1:6379> hmget user name age address
1) "javastack"
2) "20"
3) "china"
```

> 获取所有键与值语法：hgetall key

```
127.0.0.1:6379> hgetall user
1) "name"
2) "javastack"
3) "age"
4) "20"
5) "address"
6) "china"
```

> 获取所有字段语法：hkeys key

```
127.0.0.1:6379> hkeys user
1) "name"
2) "address"
3) "tall"
4) "age"
```

> 获取所有值语法：hvals key

```
127.0.0.1:6379> hvals user
1) "javastack"
2) "china"
3) "170"
4) "20"
```

#### 判断字段是否存在

> 语法：hexists key field

```
127.0.0.1:6379> hexists user address
(integer) 1
```

#### 获取字段数量

> 语法：hlen key

```
127.0.0.1:6379> hlen user
(integer) 4
```

#### 递增/减

> 语法：hincrby key field increment

```
127.0.0.1:6379> hincrby user tall -10
(integer) 170
```

#### 删除字段

> 语法：hdel key field [field …]

```
127.0.0.1:6379> hdel user age
(integer) 1
```





# 三、Redis事务

- 目的：为了解决在多客户端同时处理相同数据时，可能造成数据不一致的情况

- 应用场景：当卖家准备卖货物到市场上时，需要保证在市场的有序集合添加货物、从卖家的包裹中删除货物操作之前，卖家的包裹里的该货物存在，没有被其他的客户端移除掉；这时就需要用watch命令监视卖家的包裹中是否存在该货物

- 与MySQL事务的区别

  - 没有原子性：如果加入事务队列的语句有编译时异常，那么所有语句都无法执行；如果加入事务队列的语句有运行时异常，那么其他正确的语句就还是可以执行，只有错误的语句无法执行

    ![image-20210603230725335](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603230725335.png)

    ![image-20210603231227703](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603231227703.png)

  - redis的事务没有实现隔离性

- 命令

  - watch

    > 语法：WATCH key [key ...]
    >
    > 标记所有指定的key 被监视起来，在事务中有条件的执行（乐观锁）
    >
    > 需要捕获异常，异常情况为监视的对象发生变化时；通过while循环来实现重试机制，条件为时间，这样就可以设置最大重试时间

    - watch命令的乐观锁应用：在修改钱包值之前（开启事务之前），使用watch命令监视事务中所用到的所有键，因为每个键都可能会被其他线程修改；exec，discard，unwatch命令都会清除连接中的所有监视

    - 下面就是没有监视全部key的结果，会导致错误的结果

      ![image-20210603234816065](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603234816065.png)

    - 在使用watch命令之后，后面就不要写修改被监视的键值对，不然本线程修改还是算不合法的（其他线程修改也不行），下面的事务一样会执行失败

      ![image-20210603235943506](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603235943506.png)

  - unwatch

    刷新一个事务中已被监视的所有key；如果执行EXEC 或者DISCARD， 则不需要手动执行UNWATCH

  - multi

    标示事务开始，可以用在watch之后

  - exec

    执行事务中所有在排队等待的指令并将链接状态恢复到正常 当使用watch时，只有当被监视的键没有被修改，且允许检查设定机制时，EXEC会被执行

  - discard

    刷新一个事务中所有在排队等待的指令（取消事务），并且将连接状态恢复到正常；如果已使用watch，discard将释放所有被watch的key

- 特征

  - 延迟执行+流水线：执行multi命令后，客户端会把redis命令加入队列，并没有立马执行，执行exec命令时会把队列中的命令一次性的发送给redis服务器端，这就是流水线
  - 配合watch就可以有乐观锁
  - 使用事务既可以防止数据出错，还可以提升性能

- 非事务型流水线

  pipe=conn.pipeline(false)；false表示不使用multi、exec命令开启事务；这时pipe就是非事务型的流水线，pipe来执行命令，默认使用的流水线；最后用exec命令提交，一次性的发送到redis服务端

  - 优点：使用流水线，减少了redis客户端（web服务器）和redis服务器端的通信次数（通信使用的局域网时），每次通信往返大约耗费1~2毫秒，一般可以减少到原来通信次数的1/2~1/5；

# 四、持久化

## 快照

- 快照：指的是将Redis中某一时刻的所有数据都写入硬盘里面
- 创建快照的五种方法

## AOF

- AOF：只追加文件（append-only file）

## 持久化配置



# 五、复制

- 主服务器
- 辅服务器
- 主从链
- 主从节点

# 六、应用场景

- 计数器与数据统计
  - 例如网站点击量、销量、日志的频繁度
  - 获得某些数据的最小值min、最大值max、值的和sum、数量count、值的平方和sumsq；通过这些可以计算出平均值svg、标准差
- 查找IP所属城市即国家
- 用于缓存
  - 比如缓存频繁访问的页面、cookie

# 七、三种特殊类型

### geo

- geo实际上还是使用的有序集合结构，所以删除geo时还是用的zrem命令；redis3.2版本以上才支持这个类型

  geo和zset的区别和联系

  > geo的member就是zset的member，geo的geohash就是zset的score；zset里面的排序是按照geohash值来排序的，zrank获取排名的命令

  ![image-20210602213324332](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602213324332.png)

  > 执行zset的zincryby命令时，会改变score值，也就是geohash的值，geohash的值变大withdist选项的距离也会变大

- 添加geo的键名以及member成员，需要指定经度、维度

  > 语法：GEOADDkey longitude latitude member [longitude latitude member ...]
  >
  > 有效的经度从-180度到180度。有效的纬度从-85.05112878度到85.05112878度

![image-20210602204513653](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602204513653.png)

- 返回两个给定位置之间的距离

  > 语法：GEODIST key member1 member2 [unit]
  >
  > unit有以下几种单位，默认是m
  >
  > - **m** 表示单位为米。
  > - **km** 表示单位为千米。
  > - **mi** 表示单位为英里。
  > - **ft** 表示单位为英尺

![image-20210602204810790](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602204810790.png)

- 返回一个或多个位置元素的 Geohash 表示

  > 语法：GEOHASH key member [member ...]
  >
  > 使用Geohash位置52点整数编码；返回一个geohash数组，每一个都是一个11个字符的geohash字符串
  >
  > 特点：如果两个geohash字符串越像，那么距离就越近

![image-20210602205204990](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602205204990.png)

- 返回所有给定位置元素的位置（经度和纬度）

  > 语法：GEOPOS key member [member ...]
  >
  > ​	GEOPOS 命令返回一个数组， 数组中的每个项都由两个元素组成： 第一个元素为给定位置元素的经度， 而第二个元素则为给定位置元素的纬度
  >
  > ​	当给定的位置元素不存在时， 对应的数组项为空值

![image-20210602205631005](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602205631005.png)

- 以给定的经纬度为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素

  > 语法：GEORADIUS key longitude latitude radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count]
  >
  > 选项：
  >
  > - withdist: 在返回位置元素的同时， 将位置元素与中心之间的距离也一并返回。 距离的单位和用户给定的范围单位保持一致。
  > - withcoord: 将位置元素的经度和维度也一并返回。
  > - withhash: 以 52 位有符号整数的形式， 返回位置元素经过原始 geohash 编码的有序集合分值。 这个选项主要用于底层应用或者调试， 实际中的作用并不大
  > - asc: 根据中心的位置， 按照从近到远的方式返回位置元素。
  > - desc: 根据中心的位置， 按照从远到近的方式返回位置元素
  > - count：用来设置返回的结果数，但是会导致命令执行慢，但是可以减少带宽

![image-20210602210714939](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602210714939.png)

- 该命令和georadius命令类似，不过是根据member来查找有序集合里面指定距离的地理位置信息

  > 语法：GEORADIUSBYMEMBER key member radius m|km|ft|mi [WITHCOORD] [WITHDIST] [WITHHASH] [COUNT count]
  >
  > 选项和georadius一样
  >
  > 应用场景：比如查找附近的人功能

![image-20210602211647729](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602211647729.png)



### HyperLogLog

- 又叫基数统计，基数是不重复的数；相比于set就是内存小；set存储的是键值对，并且可以遍历查看、修改这些，HyperLogLog就不能，只能获得不重复的个数

- HyperLogLog的应用场景：访问量，在线人数，搜索次数，注册人数，等待

  HyperLogLog命令只有三个：PFadd添加，PFcount获取基数，PFmerge合并

> 添加基数，可以添加一个或者多个
>
> 语法：PFADD key element [element ...]
>
> 如果 HyperLogLog 的内部被修改了（表示添加进的元素有不一样的）,那么返回 1,否则返回 0；添加多个元素时如果有不一样的就返回1（表示添加成功，有重复的会略过），全部都重复了的话就会返回0

> 统计基数数量
>
> 语法：PFCOUNT key [key ...]
>
> 注意: 这个命令的一个副作用是可能会导致HyperLogLog内部被更改，出于缓存的目的,它会用8字节的来记录最近一次计算得到基数,所以pfcount命令在技术上是个写命令

> 合并两个或者多个HyperLogLog，取并集
>
> 语法：PFMERGE destkey sourcekey [sourcekey ...]
>
> 合并得出的 HyperLogLog 会被储存在目标变量（第一个参数）里面， 如果该键并不存在， 那么命令在执行之前， 会先为该键创建一个空的
>
> 返回值：只会返回ok

![image-20210602222753023](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210602222753023.png)

### bitmap

> 设置bitmap
>
> 语法：

w

> 统计字符串被设计为1的数量；一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行
>
> #### 语法：BITCOUNT key [start end]

![image-20210603120221451](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603120221451.png)	![image-20210603120247245](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603120247245.png)



- #### 应用场景：bitset和bitcount结合使用可以实现用户上线次数的统计（用户名作为key，时间作为offset）、登陆人数统计、打卡人数，这样占用的空间非常小，并且计算性能高；只要场景只有两种状态就可以使用