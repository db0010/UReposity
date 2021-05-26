- 安装完Maven后，配置Maven的环境变量，跟JDK一样

  ![image-20210518221720270](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210518221720270.png)

![image-20210518221747809](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210518221747809.png)

- 需要配置conf目录下的settings.xml文件，把默认的本地仓库路径修改了，注意中间的值为路径，分隔符需要用斜杠/而不是反斜杠\

  ```xml
  <localRepository>D:/program/Apache Software Foundation/maven/LocalJWarHouse</localRepository>
  ```

  还把monitors下载路径添加阿里的镜像

  ```xml
  <!-- 阿里云仓库 -->
  <mirror>
    <id>alimaven</id>
    <mirrorOf>central</mirrorOf>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
  </mirror>
  ```

- 手动创建Maven项目

  ![image-20210518222339156](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210518222339156.png)

![image-20210518222407280](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210518222407280.png)

![image-20210518222430467](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210518222430467.png)



- 使用mvn -version表示查看Maven信息；mvn compile 编译当前目录，这时处于D:\zuo_mian\java_project\MavenTest\Maven01路径；mvn exec:java -Dexec.mainClass="Hello"用于执行Hello类

- Maven常用命令

  mvn archetype:generate 创建Maven项目

  mvn compile 编译源代码

  mvn deploy 发布项目

  mvn test-compile 编译测试源代码

  mvn test 运行应用程序中的单元测试

  mvn site 生成项目相关信息的网站

  mvn clean 清除项目目录中的生成结果

  mvn package 根据项目生成的jar

  mvn install 在本地Repository中安装jar

  mvn eclipse:eclipse 生成eclipse项目文件

  mvnjetty:run 启动jetty服务

  mvntomcat:run 启动tomcat服务

  mvn clean package -Dmaven.test.skip=true:清除以前的包后重新打包，跳过测试

- Maven中packge、install、deploy命令的区别

  mvn clean package依次执行了
  clean（清除工程目前下的target目录）
  resources（配置文件拷贝到指定的目录）
  compile（源码编译成字节码生成class文件，并把编译好的class文件输出到target\classes目录下）
  testResources（把src\test\java下的代码编译成字节码输出到target\test-classes）
  testCompile（把src\test\resources下的配置文件拷贝到target\test-classes）
  test（运行测试用例）
  jar(打包)等７个阶段。

  - mvn clean packge依次执行了clean、resources、compile、testResources、testCompile、test、jar七个阶段

  - mvn clean install依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install（布署到本地maven仓库）等8个阶段


  - mvn clean deploy依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install、deploy（布署到远程maven私服仓库）等９个阶段


  由上面的分析可知主要区别如下，
  package命令完成了项目编译、单元测试、打包功能，但没有把打好的可执行jar包（war包或其它形式的包）布署到本地maven仓库和远程maven私服仓库

  install命令完成了项目编译、单元测试、打包功能，同时把打好的可执行jar包（war包或其它形式的包）布署到本地maven仓库，但没有布署到远程maven私服仓库

  deploy命令完成了项目编译、单元测试、打包功能，同时把打好的可执行jar包（war包或其它形式的包）布署到本地maven仓库和远程maven私服仓库

- IDEA中修改新建项目的Maven配置，在others settings中修改Maven配置后，每次新建都用的该配置

  ![image-20210519144257770](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210519144257770.png)

- group id一般为公司名称，artifact为项目名称

- 创建java项目和web项目时，使用的Maven模板不同，一个是quickStart，一个是webapp

  ![image-20210519150923498](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210519150923498.png)



- 在多模块中引入其他模块依赖时，可以在爆红的类上按alt+enter键，他会提示导入模块的依赖；
- 创建父子模块时，父Maven不适用模板，子模块按情况使用对应的模板，比如dao模块使用普通java的模板quickstart模板，Controller模块使用webapp模板；在父模块上右键就创建子模块了