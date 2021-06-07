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
>
> 返回值：
>
> - 1如果key被设置了
> - 0如果key没有被设置

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

  ![image-20210607123253159](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607123253159.png)

## 性能比较

![image-20210607123922921](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607123922921.png)

![image-20210607123954150](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607123954150.png)

## 持久化配置



# 五、复制

- 主从复制设置

  - Redis配置文件默认是主服务器；设置Redis为从服务器有两种方式；两者的区别就是如果用命令行的slaveof命令配置的主从关系，那么当从服务器断了之后重新连接，那么从服务器就会变成默认的主机，不能再接收之前主机的写命令；而从配置文件里配置的主从关系，那么断了之后还是从服务器，还能接收命令

    - 在redis.conf配置文件中配置主服务器的配置信息，这样在从配置文件中启动Redis时自动就是以从服务器的形式启动的

    - 不修改配置文件，启动Redis之后，用slaveof命令设置主服务器

      > 语法：SLAVEOF host port
      >
      > 注意：如果当前服务器已经是某个主服务器(master server)的从属服务器，那么执行 SLAVEOF host port 将使当前服务器停止对旧主服务器的同步，丢弃旧数据集，转而开始对新主服务器进行同步。
      >
      > 另外，对一个从属服务器执行命令 `SLAVEOF NO ONE` 将使得这个从属服务器关闭复制功能，并从从属服务器转变回主服务器，原来同步所得的数据集不会被丢弃。
      >
      > 利用『 SLAVEOF NO ONE 不会丢弃同步所得数据集』这个特性，可以在主服务器失败的时候，将从属服务器用作新的主服务器，从而实现无间断运行

- 复制原理

  ![image-20210607144706402](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607144706402.png)

- 主从链

- 主从节点

- 主从复制的作用

  - 注意事项，Redis集群最小配置是一主二从，单个Redis最大内存不超过20G，多了就要考虑加其他的Redis了

  ![image-20210607141235805](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607141235805.png)

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

> 设置或者清空key的value(字符串)在offset处的bit值
>
> 语法：SETBIT key offset value
>
> 返回值：在offset处原来的bit值
>
> 注意：当key不存在的时候，就创建一个新的字符串value。要确保这个字符串大到在offset处有bit值。参数offset需要大于等于0，并且小于232(限制bitmap大小为512)。当key对应的字符串增大的时候，新增的部分bit值都是设置为0

![image-20210606193320350](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210606193320350.png)

> 统计字符串被设计为1的数量；一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行
>
> 语法：BITCOUNT key [start end]
>
> 返回值：被设置为 1 的位的数量

![image-20210603120221451](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603120221451.png)	![image-20210603120247245](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210603120247245.png)



- #### 应用场景：bitset和bitcount结合使用可以实现用户上线次数的统计（用户名作为key，时间作为offset）、登陆人数统计、打卡人数，这样占用的空间非常小，并且计算性能高；只要场景只有两种状态就可以使用



# 八、Jedis

- 导入Redis客户端的Jar包以及fasJson包

  ```xml
  	<dependencies>
          <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>fastjson</artifactId>
              <version>1.2.49</version>
          </dependency>
          <dependency>
              <groupId>redis.clients</groupId>
              <artifactId>jedis</artifactId>
              <version>2.9.0</version>
          </dependency>
      </dependencies>
  ```

  

- 注意事项

  - 调用jedis.multi()方法开启Redis事务时会返回一个Transaction对象，事务里面的入队命令都只能用该事务对象来调用；可以在catch中捕获到异常之后multi来调用discard()来终止事务；在finally中应该关闭Jedis连接；

    ![image-20210606204611244](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210606204611244.png)

    上图中在事务中间获取name和get是没有意义的，在命令行中也只是把该获取命令加入到事务队列中，并不会马上执行；所以应该放到finally中去用jedis来调用执行

  - 在使用multi事务的代码中间不能使用Jedis的其他命令，只能调用事务对象的命令；不然会出现 JedisDataException异常

    ![image-20210606202856553](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210606202856553.png)



# 九、发布与订阅

- 应用场景

  - 实时消息系统
  - 聊天室
  - 订阅，关注系统

- 概念

  - 发布与订阅：发布的消息分到不同的频道，不需要知道什么样的订阅者订阅。订阅者对一个或多个频道感兴趣，只需接收感兴趣的消息，不需要知道什么样的发布者发布的。这种发布者和订阅者的解耦合可以带来更大的扩展性和更加动态的网络拓扑

  - 消息的结构；第一个元素是消息的类型，比如message；第二个是订阅的频道；第三个是消息的内容

    ![image-20210607140219313](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607140219313.png)

  - 作用域：发布/订阅与key所在空间没有关系，它不会受任何级别的干扰，包括不同数据库编码。 发布在db 10,订阅可以在db 1。 如果你需要区分某些频道，可以通过在频道名称前面加上所在环境的名称（例如：测试环境，演示环境，线上环境等）

    

- 订阅频道

  > 语法：SUBSCRIBE channel [channel ...]
  >
  > 作用：订阅给指定频道的信息
  >
  > 注意：一旦客户端进入订阅状态，客户端就只可接受订阅相关的命令subscribe、psubscribe、unsubscribe和punsubscribe除了这些命令，其他命令一律失效

  ![image-20210607133944357](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607133944357.png)

- 退订频道

  > 语法：UNSUBSCRIBE [channel [channel ...]]
  >
  > 作用：指示客户端退订给定的频道，若没有指定频道，则退订所有频道

- 推送消息到指定的频道

  > 语法：PUBLISH channel message
  >
  > 返回值：收到消息的客户端数量

- 模式匹配

  > 语法：PSUBSCRIBE pattern [pattern ...]
  >
  > 注意：支持的模式(patterns)有:
  >
  > - `h?llo` subscribes to `hello`, `hallo` and `hxllo`
  > - `h*llo` subscribes to `hllo` and `heeeello`
  > - `h[ae]llo` subscribes to `hello` and `hallo,` but not `hillo`
  >
  > 如果想输入普通的字符，可以在前面添加`\`

  ![image-20210607135020622](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607135020622.png)

  - 退订模式

    > 语法：PUNSUBSCRIBE [pattern [pattern ...]]
    >
    > 作用：指示客户端退订指定模式，若果没有提供模式则退出所有模式

- pubsub命令

  > 语法：PUBSUB NUMSUB [channel-1 ... channel-N]
  >
  > 返回值：列出指定信道的订阅者个数(不包括订阅模式的客户端订阅者)；就是执行了subscribe命令的客户端个数

  ![image-20210607132855731](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607132855731.png)

> 语法：PUBSUB CHANNELS [pattern]
>
> 返回值：活跃的信道列表，或者符合指定模式的信道

![image-20210607133318531](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607133318531.png)

> 语法：PUBSUB NUMPAT
>
> 返回值：客户端订阅的所有模式的数量总和



# 十、info命令

- 通过给定可选的参数 section ，可以让命令只返回某一部分的信息:

  - `server`: Redis服务器的一般信息
  - `clients`: 客户端的连接部分
  - `memory`: 内存消耗相关信息
  - `persistence`: RDB和AOF相关信息
  - `stats`: 一般统计
  - `replication`: 主/从复制信息
  - `cpu`: 统计CPU的消耗
  - `commandstats`: Redis命令统计
  - `cluster`: Redis集群信息
  - `keyspace`: 数据库的相关统计

- 下面是所有 **server** 相关的信息:

  - `redis_version`: Redis 服务器版本
  - `redis_git_sha1`: Git SHA1
  - `redis_git_dirty`: Git dirty flag
  - `redis_build_id`: 构建ID
  - `redis_mode`: 服务器模式（standalone，sentinel或者cluster）
  - `os`: Redis 服务器的宿主操作系统
  - `arch_bits`: 架构（32 或 64 位）
  - `multiplexing_api`: Redis 所使用的事件处理机制
  - `atomicvar_api`: Redis使用的Atomicvar API
  - `gcc_version`: 编译 Redis 时所使用的 GCC 版本
  - `process_id`: 服务器进程的 PID
  - `run_id`: Redis 服务器的随机标识符（用于 Sentinel 和集群）
  - `tcp_port`: TCP/IP 监听端口
  - `uptime_in_seconds`: 自 Redis 服务器启动以来，经过的秒数
  - `uptime_in_days`: 自 Redis 服务器启动以来，经过的天数
  - `hz`: 服务器的频率设置
  - `lru_clock`: 以分钟为单位进行自增的时钟，用于 LRU 管理
  - `executable`: 服务器的可执行文件路径
  - `config_file`: 配置文件路径

  下面是所有 **clients** 相关的信息:

  - `connected_clients`: 已连接客户端的数量（不包括通过从属服务器连接的客户端）
  - `client_longest_output_list`: 当前连接的客户端当中，最长的输出列表
  - `client_biggest_input_buf`: 当前连接的客户端当中，最大输入缓存
  - `blocked_clients`: 正在等待阻塞命令（BLPOP、BRPOP、BRPOPLPUSH）的客户端的数量

  下面是所有 **memory** 相关的信息:

  - `used_memory`: 由 Redis 分配器分配的内存总量，以字节（byte）为单位
  - `used_memory_human`: 以人类可读的格式返回 Redis 分配的内存总量
  - `used_memory_rss`: 从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）。这个值和 top 、 ps 等命令的输出一致。
  - `used_memory_peak`: Redis 的内存消耗峰值（以字节为单位）
  - `used_memory_peak_human`: 以人类可读的格式返回 Redis 的内存消耗峰值
  - `used_memory_peak_perc`: 使用内存占峰值内存的百分比
  - `used_memory_overhead`: 服务器为管理其内部数据结构而分配的所有开销的总和（以字节为单位）
  - `used_memory_startup`: Redis在启动时消耗的初始内存大小（以字节为单位）
  - `used_memory_dataset`: 以字节为单位的数据集大小（used_memory减去used_memory_overhead）
  - `used_memory_dataset_perc`: used_memory_dataset占净内存使用量的百分比（used_memory减去used_memory_startup）
  - `total_system_memory`: Redis主机具有的内存总量
  - `total_system_memory_human`: 以人类可读的格式返回 Redis主机具有的内存总量
  - `used_memory_lua`: Lua 引擎所使用的内存大小（以字节为单位）
  - `used_memory_lua_human`: 以人类可读的格式返回 Lua 引擎所使用的内存大小
  - `maxmemory`: maxmemory配置指令的值
  - `maxmemory_human`: 以人类可读的格式返回 maxmemory配置指令的值
  - `maxmemory_policy`: maxmemory-policy配置指令的值
  - `mem_fragmentation_ratio`: `used_memory_rss` 和 `used_memory` 之间的比率
  - `mem_allocator`: 在编译时指定的， Redis 所使用的内存分配器。可以是 libc 、 jemalloc 或者 tcmalloc 。
  - `active_defrag_running`: 指示活动碎片整理是否处于活动状态的标志
  - `lazyfree_pending_objects`: 等待释放的对象数（由于使用ASYNC选项调用UNLINK或FLUSHDB和FLUSHALL）

  在理想情况下， used_memory_rss 的值应该只比 used_memory 稍微高一点儿。

  当 rss > used ，且两者的值相差较大时，表示存在（内部或外部的）内存碎片。

  内存碎片的比率可以通过 mem_fragmentation_ratio 的值看出。

  当 used > rss 时，表示 Redis 的部分内存被操作系统换出到交换空间了，在这种情况下，操作可能会产生明显的延迟。

  由于Redis无法控制其分配的内存如何映射到内存页，因此常住内存（used_memory_rss）很高通常是内存使用量激增的结果。

  当 Redis 释放内存时，内存将返回给分配器，分配器可能会，也可能不会，将内存返还给操作系统。

  如果 Redis 释放了内存，却没有将内存返还给操作系统，那么 used_memory 的值可能和操作系统显示的 Redis 内存占用并不一致。

  查看 used_memory_peak 的值可以验证这种情况是否发生。

  要获得有关服务器内存的其他内省信息，可以参考[`MEMORY STATS`](http://www.redis.cn/commands/memory-stats)和[`MEMORY DOCTOR`](http://www.redis.cn/commands/memory-doctor)。

  下面是所有 **persistence** 相关的信息:

  - `loading`: 指示转储文件（dump）的加载是否正在进行的标志
  - `rdb_changes_since_last_save`: 自上次转储以来的更改次数
  - `rdb_bgsave_in_progress`: 指示RDB文件是否正在保存的标志
  - `rdb_last_save_time`: 上次成功保存RDB的基于纪年的时间戳
  - `rdb_last_bgsave_status`: 上次RDB保存操作的状态
  - `rdb_last_bgsave_time_sec`: 上次RDB保存操作的持续时间（以秒为单位）
  - `rdb_current_bgsave_time_sec`: 正在进行的RDB保存操作的持续时间（如果有）
  - `rdb_last_cow_size`: 上次RDB保存操作期间copy-on-write分配的字节大小
  - `aof_enabled`: 表示AOF记录已激活的标志
  - `aof_rewrite_in_progress`: 表示AOF重写操作正在进行的标志
  - `aof_rewrite_scheduled`: 表示一旦进行中的RDB保存操作完成，就会安排进行AOF重写操作的标志
  - `aof_last_rewrite_time_sec`: 上次AOF重写操作的持续时间，以秒为单位
  - `aof_current_rewrite_time_sec`: 正在进行的AOF重写操作的持续时间（如果有）
  - `aof_last_bgrewrite_status`: 上次AOF重写操作的状态
  - `aof_last_write_status`: 上一次AOF写入操作的状态
  - `aof_last_cow_size`: 上次AOF重写操作期间copy-on-write分配的字节大小

  `changes_since_last_save`指的是从上次调用`SAVE`或者`BGSAVE`以来，在数据集中产生某种变化的操作的数量。

  如果启用了AOF，则会添加以下这些额外的字段：

  - `aof_current_size`: 当前的AOF文件大小
  - `aof_base_size`: 上次启动或重写时的AOF文件大小
  - `aof_pending_rewrite`: 指示AOF重写操作是否会在当前RDB保存操作完成后立即执行的标志。
  - `aof_buffer_length`: AOF缓冲区大小
  - `aof_rewrite_buffer_length`: AOF重写缓冲区大小
  - `aof_pending_bio_fsync`: 在后台IO队列中等待fsync处理的任务数
  - `aof_delayed_fsync`: 延迟fsync计数器

  如果正在执行加载操作，将会添加这些额外的字段：

  - `loading_start_time`: 加载操作的开始时间（基于纪元的时间戳）
  - `loading_total_bytes`: 文件总大小
  - `loading_loaded_bytes`: 已经加载的字节数
  - `loading_loaded_perc`: 已经加载的百分比
  - `loading_eta_seconds`: 预计加载完成所需的剩余秒数

  下面是所有 **stats** 相关的信息:

  - `total_connections_received`: 服务器接受的连接总数
  - `total_commands_processed`: 服务器处理的命令总数
  - `instantaneous_ops_per_sec`: 每秒处理的命令数
  - `rejected_connections`: 由于`maxclients`限制而拒绝的连接数
  - `expired_keys`: key到期事件的总数
  - `evicted_keys`: 由于`maxmemory`限制而导致被驱逐的key的数量
  - `keyspace_hits`: 在主字典中成功查找到key的次数
  - `keyspace_misses`: 在主字典中查找key失败的次数
  - `pubsub_channels`: 拥有客户端订阅的全局pub/sub通道数
  - `pubsub_patterns`: 拥有客户端订阅的全局pub/sub模式数
  - `latest_fork_usec`: 最新fork操作的持续时间，以微秒为单位

  下面是所有 **replication** 相关的信息:

  - `role`: 如果实例不是任何节点的从节点，则值是”master”，如果实例从某个节点同步数据，则是”slave”。 请注意，一个从节点可以是另一个从节点的主节点（菊花链）。

  如果实例是从节点，则会提供以下这些额外字段：

  - `master_host`: 主节点的Host名称或IP地址
  - `master_port`: 主节点监听的TCP端口
  - `master_link_status`: 连接状态（up或者down）
  - `master_last_io_seconds_ago`: 自上次与主节点交互以来，经过的秒数
  - `master_sync_in_progress`: 指示主节点正在与从节点同步

  如果SYNC操作正在进行，则会提供以下这些字段：

  - `master_sync_left_bytes`: 同步完成前剩余的字节数
  - `master_sync_last_io_seconds_ago`: 在SYNC操作期间自上次传输IO以来的秒数

  如果主从节点之间的连接断开了，则会提供一个额外的字段：

  - `master_link_down_since_seconds`: 自连接断开以来，经过的秒数

  以下字段将始终提供：

  - `connected_slaves`: 已连接的从节点数

  对每个从节点，将会添加以下行：

  - `slaveXXX`: id，地址，端口号，状态

  下面是所有 **cpu** 相关的信息:

  - `used_cpu_sys`: 由Redis服务器消耗的系统CPU
  - `used_cpu_user`: 由Redis服务器消耗的用户CPU
  - `used_cpu_sys_children`: 由后台进程消耗的系统CPU
  - `used_cpu_user_children`: 由后台进程消耗的用户CPU

  **commandstats**部分提供基于命令类型的统计，包含调用次数，这些命令消耗的总CPU时间，以及每个命令执行所消耗的平均CPU。

  对于每一个命令类型，添加以下行：

  - `cmdstat_XXX`: `calls=XXX,usec=XXX,usec_per_call=XXX`

  **cluster**部分当前只包含一个唯一的字段：

  - `cluster_enabled`: 表示已启用Redis集群

  **keyspace**部分提供有关每个数据库的主字典的统计，统计信息是key的总数和过期的key的总数。

  对于每个数据库，提供以下行：

  - `dbXXX`: `keys=XXX,expires=XXX`

# 十一、哨兵模式

- 配置哨兵模式

  ![image-20210607150839453](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607150839453.png)

- 优缺点

  ![image-20210607150629669](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210607150629669.png)

- 配置文件

  ```bash
  # Example sentinel.conf  
    
  # 哨兵sentinel实例运行的端口 默认26379  
  port 26379  
    
  # 哨兵sentinel的工作目录  
  dir /tmp  
    
  # 哨兵sentinel监控的redis主节点的 ip port   
  # master-name  可以自己命名的主节点名字 只能由字母A-z、数字0-9 、这三个字符".-_"组成。  
  # quorum 当这些quorum个数sentinel哨兵认为master主节点失联 那么这时 客观上认为主节点失联了  
  # sentinel monitor <master-name> <ip> <redis-port> <quorum>  
    sentinel monitor mymaster 127.0.0.1 6379 2  
    
  # 当在Redis实例中开启了requirepass foobared 授权密码 这样所有连接Redis实例的客户端都要提供密码  
  # 设置哨兵sentinel 连接主从的密码 注意必须为主从设置一样的验证密码  
  # sentinel auth-pass <master-name> <password>  
  sentinel auth-pass mymaster MySUPER--secret-0123passw0rd  
    
    
  # 指定多少毫秒之后 主节点没有应答哨兵sentinel 此时 哨兵主观上认为主节点下线 默认30秒  
  # sentinel down-after-milliseconds <master-name> <milliseconds>  
  sentinel down-after-milliseconds mymaster 30000  
    
  # 这个配置项指定了在发生failover主备切换时最多可以有多少个slave同时对新的master进行 同步，  
  这个数字越小，完成failover所需的时间就越长，  
  但是如果这个数字越大，就意味着越 多的slave因为replication而不可用。  
  可以通过将这个值设为 1 来保证每次只有一个slave 处于不能处理命令请求的状态。  
  # sentinel parallel-syncs <master-name> <numslaves>  
  sentinel parallel-syncs mymaster 1  
    
    
    
  # 故障转移的超时时间 failover-timeout 可以用在以下这些方面：   
  #1. 同一个sentinel对同一个master两次failover之间的间隔时间。  
  #2. 当一个slave从一个错误的master那里同步数据开始计算时间。直到slave被纠正为向正确的master那里同步数据时。  
  #3.当想要取消一个正在进行的failover所需要的时间。    
  #4.当进行failover时，配置所有slaves指向新的master所需的最大时间。不过，即使过了这个超时，slaves依然会被正确配置为指向master，但是就不按parallel-syncs所配置的规则来了  
  # 默认三分钟  
  # sentinel failover-timeout <master-name> <milliseconds>  
  sentinel failover-timeout mymaster 180000  
    
  # SCRIPTS EXECUTION  
    
  #配置当某一事件发生时所需要执行的脚本，可以通过脚本来通知管理员，例如当系统运行不正常时发邮件通知相关人员。  
  #对于脚本的运行结果有以下规则：  
  #若脚本执行后返回1，那么该脚本稍后将会被再次执行，重复次数目前默认为10  
  #若脚本执行后返回2，或者比2更高的一个返回值，脚本将不会重复执行。  
  #如果脚本在执行过程中由于收到系统中断信号被终止了，则同返回值为1时的行为相同。  
  #一个脚本的最大执行时间为60s，如果超过这个时间，脚本将会被一个SIGKILL信号终止，之后重新执行。  
    
  #通知型脚本:当sentinel有任何警告级别的事件发生时（比如说redis实例的主观失效和客观失效等等），将会去调用这个脚本，  
  #这时这个脚本应该通过邮件，SMS等方式去通知系统管理员关于系统不正常运行的信息。调用该脚本时，将传给脚本两个参数，  
  #一个是事件的类型，  
  #一个是事件的描述。  
  #如果sentinel.conf配置文件中配置了这个脚本路径，那么必须保证这个脚本存在于这个路径，并且是可执行的，否则sentinel无法正常启动成功。  
  #通知脚本  
  # sentinel notification-script <master-name> <script-path>  
    sentinel notification-script mymaster /var/redis/notify.sh  
    
  # 客户端重新配置主节点参数脚本  
  # 当一个master由于failover而发生改变时，这个脚本将会被调用，通知相关的客户端关于master地址已经发生改变的信息。  
  # 以下参数将会在调用脚本时传给脚本:  
  # <master-name> <role> <state> <from-ip> <from-port> <to-ip> <to-port>  
  # 目前<state>总是“failover”,  
  # <role>是“leader”或者“observer”中的一个。   
  # 参数 from-ip, from-port, to-ip, to-port是用来和旧的master和新的master(即旧的slave)通信的  
  # 这个脚本应该是通用的，能被多次调用，不是针对性的。  
  # sentinel client-reconfig-script <master-name> <script-path>  
   sentinel client-reconfig-script mymaster /var/redis/reconfig.sh 
  ```

  

# 十二、缓存穿透

- 什么是缓存穿透：指有大量的请求没有命中缓存时，走了数据库查询，给DAO层造成了很大的压力

- 解决方案

  - 在Redis中实现布隆过滤器；

    - 布隆过滤器的结构；类似Redis的bitmap

    ![img](https://pic3.zhimg.com/80/v2-530c9d4478398718c15632b9aa025c36_720w.jpg)

    - 实现的原理：当一个请求存一个key在Redis中，需要多个hash函数来散列该key；散列的对应位置设为1；之后请求查询key时，还是通过之前的多个hash函数来散列，查询对应位置状态是否为1，如果都为1，那么可能存在缓存之中，去Redis里面找；如果不全为1，那么肯定不存在，直接返回信息给客户端

    - hash函数可以选用MurmurHash算法，高性能低碰撞

      - FNV – 443.668038 mb/sec
      - SuperFastHash – 985.335173 mb/sec    
      - lookup3 – 988.080652 mb/sec    
      - MurmurHash 1.0 – 1363.293480 mb/sec    
      - MurmurHash 2.0 – 2056.885653 mb/sec 
      - OneAtATime – 354.163715 mb/sec

    - /大Value拆分

      Redis 因其支持 setbit 和 getbit 操作，且纯内存性能高等特点，因此天然就可以作为布隆过滤器来使用。但是布隆过滤器的不当使用极易产生大 Value，增加 Redis 阻塞风险，因此生成环境中建议对体积庞大的布隆过滤器进行拆分。

      拆分的形式方法多种多样，但是本质是不要将 Hash(Key) 之后的请求分散在多个节点的多个小 bitmap 上，而是应该拆分成多个小 bitmap 之后，对一个 Key 的所有哈希函数都落在这一个小 bitmap 上

    - 选择适当的hash函数个数和布隆过滤器长度；

      - 很显然，过小的布隆过滤器很快所有的 bit 位均为 1，那么查询任何值都会返回“可能存在”，起不到过滤的目的了。布隆过滤器的长度会直接影响误报率，布隆过滤器越长其误报率越小

      - 另外，哈希函数的个数也需要权衡，个数越多则布隆过滤器 bit 位置为 1 的速度越快，且布隆过滤器的效率越低；但是如果太少的话，那我们的误报率会变高

        ![img](https://pic4.zhimg.com/80/v2-05d4a17ec47911d9ff0e72dc788d5573_720w.jpg)

        k 为哈希函数个数，m 为布隆过滤器长度，n 为插入的元素个数，p 为误报率

        - 下面为实际业务中m和k的选择依据

![img](https://pic1.zhimg.com/80/v2-1ed5b79aa7ac2e9cd66c83690fdbfcf0_720w.jpg)

- 缓存空对象

  - 当存储层不命中后，即使返回的空对象也将其缓存起来，同时会设置一个过期时间，之后再访问这个数据将会从缓存中获取，保护了后端数据源；

  - 但是这种方法会存在两个问题：

    - 如果空值能够被缓存起来，这就意味着缓存需要更多的空间存储更多的键，因为这当中可能会有很多的空值的键；

    - 即使对空值设置了过期时间，还是会存在缓存层和存储层的数据会有一段时间窗口的不一致，这对于需要保持一致性的业务会有影响

# 十三、缓存击穿

- 什么是缓存击穿：key对应的数据存在，但在redis中过期，此时若有大量并发请求过来，这些请求发现缓存过期一般都会从后端DB加载数据并回设到缓存，这个时候大并发的请求可能会瞬间把后端DB压垮

  - 缓存击穿的解决方案

    - 使用互斥锁(mutex key)

      ```java
      public String get(key) {
            String value = redis.get(key);
            if (value == null) { //代表缓存值过期
                //设置3min的超时，防止del操作失败的时候，下次缓存过期一直不能load db
            if (redis.setnx(key_mutex, 1, 3 * 60) == 1) {  //代表设置成功
                     value = db.get(key);
                            redis.set(key, value, expire_secs);
                            redis.del(key_mutex);
                    } else {  //这个时候代表同时候的其他线程已经load db并回设到缓存了，这时候重试获取缓存值即可
                            sleep(50);
                            get(key);  //重试
                    }
                } else {
                    return value;      
                }
       }
      ```

      

- 什么是缓存雪崩：缓存雪崩是指，由于缓存层承载着大量请求，有效的保护了存储层，但是如果缓存层由于某些原因整体不能提供服务，于是所有的请求都会达到存储层，存储层的调用量会暴增，造成存储层也会挂掉的情况

  - 缓存雪崩的解决方案；

    - 保证缓存层服务高可用性
      	即使个别节点、个别机器、甚至是机房宕掉，依然可以提供服务，比如 Redis Sentinel 和 Redis Cluster 都实现了高可用。

      ​	依赖隔离组件为后端限流并降级
      ​	在缓存失效后，通过加锁或者队列来控制读数据库写缓存的线程数量。比如对某个key只允许一个线程查询数据和写缓存，其他线程等待

      ​	还有一个简单方案就时讲缓存失效时间分散开，比如我们可以在原有的失效时间基础上增加一个随机值，比如1-5分钟随机，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件

    - 数据预热

      可以通过缓存reload机制，预先去更新缓存，再即将发生大并发访问前手动触发加载缓存不同的key，设置不同的过期时间，让缓存失效的时间点尽量均匀。

  - 缓存并发
    缓存并发是指，高并发场景下同时大量查询过期的key值、最后查询数据库将缓存结果回写到缓存、造成数据库压力过大

  - 分布式锁
    在缓存更新或者过期的情况下，先获取锁，在进行更新或者从数据库中获取数据后，再释放锁，需要一定的时间等待，就可以从缓存中继续获取数据