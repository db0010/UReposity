# 服务器端

- ## 一、安装 RabbitMQ

  由于 RabbitMQ 需要 erlang 语言的支持，在安装 RabbitMQ 之前需要安装erlang

  首先配置源

  ```
  echo "deb https://dl.bintray.com/rabbitmq/debian trusty main" | sudo tee /etc/apt/sources.list.d/bintray.rabbitmq.list
  echo "deb http://packages.erlang-solutions.com/ubuntu trusty contrib" | sudo tee -a /etc/apt/sources.list.d/erlang_solutions.list
  ```

  导入对应的 key

  ```
  wget -c -O- http://packages.erlang-solutions.com/ubuntu/erlang_solutions.asc | sudo apt-key add -
  wget -O- https://dl.bintray.com/rabbitmq/Keys/rabbitmq-release-signing-key.asc |sudo apt-key add -
  ```

  开始安装 erlang 和 RabbitMQ

  ```
  sudo apt-get update
  sudo apt-get install erlang-nox
  sudo apt-get install rabbitmq-server
  ```

  安装完之后 RabbitMQ 便已经自动启动了，可以使用如下的命令对 RabbitMQ 进行操作：

  ```
  sudo service rabbitmq-server start # 启动
  sudo service rabbitmq-server stop # 停止
  sudo service rabbitmq-server restart # 重启
  sudo service rabbitmq-server status # 查看当前状态
  ```

  ## 配置 RabbitMQ

  添加admin用户，密码设置为admin。

  ```
  sudo rabbitmqctl add_user  admin  admin  
  ```

  赋予权限

  ```
  sudo rabbitmqctl set_user_tags admin administrator 
  ```

  赋予virtual host中所有资源的配置、写、读权限以便管理其中的资源

  ```
  sudo rabbitmqctl  set_permissions -p / admin '.*' '.*' '.*'
  ```

  ## 管理 RabbitMQ

  RabbitMQ 提供了一个 web 管理工具（rabbitmq_management），方便在浏览器端管理 RabbitMQ

  ```
  sudo rabbitmq-plugins enable rabbitmq_management
  ```

  之后在浏览器访问 [http://server-ip:15672/]，账号与密码都是刚才设置的 admin

# 客户端

- 架构图

  ![](D:\zuo_mian\java小知识\image\1552936-20201024103921637-693350551.png)

- Rabbit MQ里面的各个组件使用时都需要先声明，避免使用时不存在的情况；在connection的channel里面队列有默认配置

  - 队列的默认配置；非持久化、独占、自动删除，并且会自动生成一个队列名返回给queueName；

    ```
    //从channel中获取默认声明的队列
    String queueName = channel.queueDeclare().getQueue();
    //声明自定义配置的队列;参数分别为：队列名称、持久化、独占、自动删除、其他队列参数
    channel.queueDeclare(queueName, true, false, false, null);
    ```

    - 参数解释：queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
          Map<String, Object> arguments)

      - queue：队列名称，消费者和生产者交流的中间人
      - durable：队列是否持久化；就是把队列从内存持久化到磁盘中，通常需要消息也一起持久化
      - exckusive：是否排它（独占），即是否允许其他的客户端共同消费该队列，一对多的关系，如微信公众号
      - autoDelete：是否自动删除，当队列里面没有消息之后，队列就会自动销毁
      - arguments：队列的其他参数；例如：

    - 队列和交换机绑定；一个绑定就是基于路由键将交换机和消息队列连接起来的路由规则，所以可以将交换器理解成一个由绑定构成的路由表

         ```
         channel.exchangeDeclare(exchangeName, "direct", true);
         String queueName = channel.queueDeclare().getQueue();
         channel.queueBind(queueName, exchangeName, routingKey);
         ```

         