11月8日：（1）向http发出请求时，出现405错误，提示未找到doget()方法或者dopost方法，但是明明已经重写了这两个方法，那就是被父类的这两个方法覆盖了
		这是没有实现请求的方法，就会报405错误	
		解决方法：把super.doget()和super.dopost(）删了
12月14日：（1）Cookie的创建通过Cookie的构造函数，例如：Cookie cookie1=new Cookie（“key1”，“value1”）；
	    (2)删除操作通过设置Cookie 的最大存活时间，就是设置为0，并且响应给客户端，则会立刻删除Cookie
		例如：cookie1.setMaxAge(0);response.addCookie(cookie1);这个方法也可以用来设置Cookie的存活时间
	   （3）设置Cookie 的路径：cookie1.setPath("request.getContextPath()");参数就是路径，可以自己设
	   (4)修改Cookie的值，方法一：重新new一个和想要修改的Cookie的key一样的，值就改变成想要的value，在添加进响应里面
		方法二：先找到和key匹配的Cookie，然后调用setValue（）设置新的value，
		注意：寻找Cookie 的时候需要获得请求里面的Cookie数组，然后遍历数组，用equals（）方法找key相同的Cookie，要自己写一个方法
		例如：cookie1.setValue("value1");
12月15日：（1）写Servlet的时候，可以先写一个BaseServlet，里面放置每个请求都需要的代码，
		例如：处理请求和响应、接受action参数（请求的具体方法）,在把字符串action的内容转换成Servlet程序里面对应的方法
		需要用到反射和Method类的getDeclaredMethod()、method对象的invoke()方法
		示例：  Method method=this.getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
           		 //调用目标业务方法：method.invoke(this,request,response);
	  （2）Fillter过滤器可以用来设置项目的权限管理  
	  （3）开始编写一个项目模块的操作顺序：
		1，梳理service层和dao层的方法，以及方法之间的使用关系
		2，开始在数据库创建表，在pojo下创建每个表的pojo类：get()、set()，有参构造函数和无参构造函数，toString（）
		3，编写dao层的接口，把基本的创建、删除、更新等等一整条数据项的sql语句，这样后面就可以直接继承这个接口就行了
			避免了SQL代码的重复
12月16日： （1）在html页面里面，在导入gson-2.2.4.jar的jar包之后，可以用JSON.stringify(jsonObj)方法把json对象转化为字符串，参数为对象
		用JSON.parse(jsonString)方法把json对象转化为字符串，参数为字符串
12月17日： （1）在服务器端可以用toJson()方法，把JavaBean、list、map等的对象数据转化为Json字符串，参数为需要转化的对象
		用fromJson(String,对象类型)方法把json字符串转化为需要的类型结构，第一个参数是json，第二个参数是需要转换数据结构的类型（type）
		这个需要用该结构继承TypeToken类，new它的对象之后调用对象的getType()方法就获取类型了

3月21日：（1）在控制器把提示信息添加进reques域之后，可以用这种方式显示出来;似乎还可以换成EL表达式，要更简洁，还不会

```jsp
   <%
                    String wrongMsg= (String) request.getAttribute("wrongMsg");
                    if(wrongMsg==null){
                        wrongMsg="";
                    }
                %>
            <span><%=wrongMsg%></span>
```

1. doGet()和doPost()方法的区别

   - get方法是客户端向服务器获得数据，post是客户端向服务器发送数据的
   - get传输的数据量小，收URL长度的限制；post传输的数据量大，可以用来传输文件、
   - get是不安全的，请求的数据会在URL上显示，post请求是放在请求头部的，是安全的

2. Cookie和Session

   - Cookie是保存在浏览器上的文件，以键值对的形式保存，包含用户的相关信息；客户端向服务器发送请求，就会提取浏览器中的用户信息由Http发送给服务器
   - Session是存储在服务器中的，是浏览器和服务器的会话；服务器默认为浏览器的Cookie设置sessionid,浏览器在向服务器请求的过程中传输的Cookie包含sessionid，服务器根据sessionid获取会话中存储的信息，然后确定会话的身份信息
   - Cookie和Session的区别
     - Cookie存放在客户端上，安全性差，Session存放在服务器端上，安全性高些
     - 单个Cookie保存的数据不能超过4k，session无此限制
   - Cookie和Session的作用是用户判断用户的身份，因为Http是无法判断用户身份的

3. 一个完整的HTTP请求经历的步骤

   1. 建立TCP连接
   2. Web浏览器向Web服务器发送请求行，一旦建立了TCP连接，**Web浏览器就会向Web服务器发送请求命令**。例如：GET /sample/hello.jsp HTTP/1.1。
   3. Web浏览器发送请求头，浏览器发送其请求命令之后，还要以头信息的形式向Web服务器发送一些别的信息，**之后浏览器发送了一空白行来通知服务器**，它已经结束了该头信息的发送。
   4. Web服务器应答，客户机向服务器发出请求后，服务器会客户机回送应答， **HTTP/1.1 200 OK ，应答的第一部分是协议的版本号和应答状态码。**
   5. Web服务器发送应答头，正如客户端会随同请求发送关于自身的信息一样，服务器也会随同应答向用户发送关于它自己的数据及被请求的文档。
   6. Web服务器向浏览器发送数据Web服务器向浏览器发送头信息后，它会发送一个空白行来表示头信息的发送到此为结束，接着，**它就以Content-Type应答头信息所描述的格式发送用户所请求的实际数据**。
   7. Web服务器关闭TCP连接

4. TCP的三次握手和四次挥手

   > ![img](https://user-gold-cdn.xitu.io/2020/4/13/1717297c73467e00?imageslim)
   >
   > 

- 三次握手

  - 第一次握手：Client将SYN置1，随机产生一个初始序列号seq发送给Server，进入SYN_SENT状态
  - 第二次握手：Server收到Client的SYN=1之后，知道客户端请求建立连接，将自己的SYN置1，ACK置1，产生一个acknowledge number=sequence number+1，并随机产生一个自己的初始序列号，发送给客户端；进入SYN_RCVD状态；
  - 第三次握手：客户端检查acknowledge number是否为序列号+1，ACK是否为1，检查正确之后将自己的ACK置为1，产生一个acknowledge number=服务器发的序列号+1，发送给服务器；进入ESTABLISHED状态；服务器检查ACK为1和acknowledge number为序列号+1之后，也进入ESTABLISHED状态；完成三次握手，连接建立。

  > ![](https://user-gold-cdn.xitu.io/2020/4/13/1717297c746f6ee2?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

- 四次挥手

  - 第一次挥手：Client将FIN置为1，发送一个序列号seq给Server；进入FIN_WAIT_1状态；
  - 第二次挥手：Server收到FIN之后，发送一个ACK=1，acknowledge number=收到的序列号+1；进入CLOSE_WAIT状态。此时客户端已经没有要发送的数据了，但仍可以接受服务器发来的数据。
  - 第三次挥手：Server将FIN置1，发送一个序列号给Client；进入LAST_ACK状态；
  - 第四次挥手：Client收到服务器的FIN后，进入TIME_WAIT状态；接着将ACK置1，发送一个acknowledge number=序列号+1给服务器；服务器收到后，确认acknowledge number后，变为CLOSED状态，不再向客户端发送数据。客户端等待2*MSL（报文段最长寿命）时间后，也进入CLOSED状态。完成四次挥手。
  
- JSP

  - 定义：是在Html代码中添加了Java代码，Java代码可以是JavaBean、SQL语句、RMI（远程方法调用）对象；JSP也是Servlet的扩展，在Web服务器请求JSP页面时，JSP页面会被编译成Servlet代码
  - JVM为每个请求创建一个线程；JSP只会被编译一次，提高了系统的响应速率；
  - 九大内置对象：request、response、session、pageContext、ApplicationContext、ServletContext、out、ServletConfig、Exception

### 一、Servlet

1. 定义：Servlet是用Java类编写的服务端应用程序，它可以被看成位于客户端和服务器的中间层，负责接收客户端请求和响应给客户端

2. 作用：
   - 拦截器作用；在请求前检查访问权限、设定程序的字符集、检查用户角色等
   - 读取客户端数据；如URL中的参数、页面表单、Ajax参数、请求的来源、Cookie（缓存数据）、客户端类型
   - 计算、生成结果、发送响应的数据
   
3. Servlet生命周期
   1. 初始化；先装载到Java内存中，接着初始化；Web容器启动后，加载web.xml文件，读取Servlet的配置信息，实例化Servlet类；并为每个Servlet配置创建一个ServletConfig对象，在运行Servlet的init()方法时，把ServletConfig对象作为参数传入Servlet；
      - 一个Servlet中init()方法只执行一次，因此使用的单例模式，注意在并发中的问题
   2. 运行；当有请求，对应的Servlet会创建HttpServletRequest、HttpServletResponse对象，在使用service()方法匹配使用的具体方法；重写了Service()方法后，不会再调用doPost()、doGet()方法，只能在Service()方法里面转到具体的方法
   3. 销毁；调用destory()方法，跟init()方法一样只调用一次
   
4. ServletAPI
   - getPart()、getParts()：在Servlet3.0中可用这两个方法实现文件上传，要用@Multipart注解配置存储文件的路径，上传文件的限制大小
   - include()：在Servlet中显示另一个Servlet的内容
   - forward()：把请求转发到其他的Servlet，视图页面的URL不变；例如：request.getRequestDispatcher("/hello.html").forward(request,reponse)
   - setContenType()：设置JSP响应类型，要在获取输出流之前设置
   - getWriter()：获取printWriter对象
   - getOutputStream()：获取ServletOutputStream流对象
   - SetHeader()、addHeader：设置标头
   - sendRedirect()：对页面重定向，URL地址会变化；当想在使用了重定向的多个请求间传递数据，可以把数据添加到HttpSession域对象中
   - sendError()：对浏览器发送错误信息
   - setCharacterEncoding()：设置请求的字符编码，UTF-8；例如request.setCharacterEncoding("UTF-8")
   - getReader()：获取请求正文，用BufferReader对象接收；例如BufferReader read=request.getReader();
   
5. Session会话管理
   - 目的：因为HTTP协议是无状态的协议，每次请求的数据数据都不会持久化，因此需要使用Session和Cookie来实现客户端和服务端的会话
   - 实现会话的三种方法：
     - 使用表单的隐藏域
   
6. Servlet、ServletConfig、GenericServlet的关系和用法

   - #### 关系：GenericServlet类实现了Servlet接口和ServletConfig；一个Servlet实例就有一个ServletConfig接口

   - ServletConfig接口可以用来获取Servet实例的名称、获取ServetContext对象的引用、获取配置参数的值；可以作为日志信息打印；getServletName()、getServletContext()、getInitParameter(String name)、getInitParameterNames()

   - ServletContext存储的是Servlet中的全局信息，所有的Servlet共享一个ServletContext，Web容器为Web应用程序创建唯一的ServletContext对象

     - getRealPath(String path)：获取真实路径
     - getResource(String urlPath)：获取指定资源路径的URL对象
     - getResourceAsStream(String urlPath)：返回指定资源路径的InputStream
     - getRequestDispatcher(String urlPath)：获取RequestDipatcher对象
     - getResourcePaths(String path)：返回资源的Set集合
     - getServerInfo()：获取服务器的名字和版本号

7. 监听器Listener

   1.servlet上下文相关监听接口：

   - ServletContextListener
     - contextInitialized(ServletContextEvent sce)：通知监听器，已经加载Web应用和初始化参数
     - contextDestroyed(ServletContextEvent sce)：通知监听器，Web应用即将关闭
   - ServletContextAttributeListener

   2.http session相关监听接口：

   - HttpSessionListener
     - sessionCreated(HttpSessionEvent se)：通知监听器，产生了新的会话
     - sessionDestroyed(HttpSessionEvent se)：通知监听器，已经消除了一个会话
     - 可以利用这两个方法记录在线人数的功能
   - HttpSessionActivationListener
   - HttpSessionAttributeListener
   - HttpSessionBindingListener

   3.servlet request相关监听接口：

   - ServletRequestListener
     - requestInitialized(ServletRequestEvent arg0)：产生新的request对象
     - requestDestroyed(ServletRequestEvent arg0)：消除一个request对象
   - ServletRequestAttributeListener

8. 过滤器Filter

   - 定义：过滤器是作用在Servlet之前，既可以拦截、过滤浏览器的请求，也可以改变对浏览器的响应；在客户端和服务端起了中间组件的作用，对二者之间的数据信息进行信息过滤
   - 作用：
     - 对用户请求进行身份验证
     - 对用户发送的数据进行过滤或者替换
     - 转换图像的数据格式
     - 数据压缩
     - 数据加密
     - XML数据的转换
     - 修改请求数据的字符集
   - Filter接口
     - 接口的位置：javax.serverlet.Filter
     - 方法：
       - void init(FilterConfig filterConfig)：初始化过滤器，利用FilterConfig对象可以获取配置参数信息
       - void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)：实现对请求和响应的数据处理；处理后可以调用FilterChain对象的doFilter(request，reponse)方法来把请求传给过滤器链中的下一个过滤器，也可以返回响应或者将目标重定向
       - void destroy()：释放过滤器中的资源
   - HttpServletRequestWrapper请求封装器：利用该类对请求的内容进行统一的修改；例如修改请求字符编码、替换字符、权限验证
   - HttpServletResponseWrapper响应封装器：利用该类对响应内容进行统一修改，例如：压缩输出内容、替换输出内容

9. JSP七大内置对象

   - request：获取请求参数、主机客户机信息
   
   - response：设置头信息、设置页面重定向
   
   - session：获取sessionID、用户登录信息保存
   
   - application：获取指定页面路径
   
   - out
   
   - page
   
   - config
   
   - **内置对象**
   
     **request**
   
     request 对象是 javax.servlet.httpServletRequest类型的对象。 该对象代表了客户端的请求信息，主要用于接受通过HTTP协议传送到服务器的数据。（包括头信息、系统信息、请求方式以及请求参数等）。request对象的作用域为一次请求。
   
     **response**
   
     response 代表的是对客户端的响应，主要是将JSP容器处理过的对象传回到客户端。response对象也具有作用域，它只在JSP页面内有效。
   
     **session**
   
     1.什么是session：从一个客户打开浏览器并连接到服务器开始，到客户关闭浏览器离开这个服务器结束，被称为一个会话。当一个客户访问一个服务器时，可能会在这个服务器的几个页面之间反复连接，反复刷新一个页面，服务器应当通过某种办法知道这是同一个客户，这就需要session对象。
   
     2．session对象的ID：当一个客户首次访问服务器上的一个JSP页面时，JSP引擎产生一个session对象，同时分配一个String类型的ID号，JSP引擎同时将这个ID号发送到客户端，存放在Cookie中，这样session对象和客户之间就建立了一一对应的关系。当客户再访问连接该服务器的其他页面时，不再分配给客户新的session对象，直到客户关闭浏览器后，服务器端该客户的session对象才取消，并且和客户的会话对应关系消失。当客户重新打开浏览器再连接到该服务器时，服务器为该客户再创建一个新的session对象。
   
     3.session对象存在一定时间过期问题，所以存在session中的名值对会在一定时间后失去，可以通过更改session有效时间来避免这种情况。同时编程时尽量避免将大量有效信息存储在session中，request是一个不错的替代对象。
   
     **application**
   
     1．什么是application:
   
     服务器启动后就产生了这个application对象，当客户在所访问的网站的各个页面之间浏览时，这个application对象都是同一个，直到服务器关闭。但是与session不同的是，所有客户的application对象都是同一个，即所有客户共享这个内置的application对象。
   
     2．application对象常用方法:
   
     (1)public void setAttribute(String key,Object obj): 将参数Object指定的对象obj添加到application对象中，并为添加的对象指定一个索引关键字。
   
     (2)public Object getAttribute(String key): 获取application对象中含有关键字的对象。
   
     **out**
   
     out 对象用于在Web浏览器内输出信息，并且管理应用服务器上的输出缓冲区。在使用 out 对象输出数据时，可以对数据缓冲区进行操作，及时清除缓冲区中的残余数据，为其他的输出让出缓冲空间。待数据输出完毕后，要及时关闭输出流。
   
     **page**
   
     page 对象代表JSP本身，只有在JSP页面内才是合法的。 page隐含对象本质上包含当前 Servlet接口引用的变量，类似于Java编程中的 this 指针。
   
     **config**
   
     config 对象的主要作用是取得服务器的配置信息。通过 pageContext对象的 getServletConfig() 方法可以获取一个config对象。当一个Servlet 初始化时，容器把某些信息通过 config对象传递给这个 Servlet。 开发者可以在web.xml 文件中为应用程序环境中的Servlet程序和JSP页面提供初始化参数。
   
     **exception**
   
     java.lang.Throwable 的实例，该实例代表其他页面中的异常和错误。只有当页面是错误处理页面，即编译指令page 的isErrorPage 属性为true 时，该对象才可以使用。常用的方法有getMessage()和printStackTrace()等。
   
     **pageContext**
   
     pageContext 对象的作用是取得任何范围的参数，通过它可以获取 JSP页面的out、request、reponse、session、application 等对象。pageContext对象的创建和初始化都是由容器来完成的，在JSP页面中可以直接使用 pageContext对象。

### 二、Tomacat

1. 定义：Tomacat时Web应用服务器、Servlet和Jsp容器；特点是轻量级的，开源，适合中小型公司；Weblogic、WebSphere是大型应用服务器，收费的，大型公司使用

### 三、页面跳转

- 转发：携带数据，服务器端完成，是同一个请求；应用于需要直接转到视图页面的场景

  request.getRequestDispatcher("JSP页面").forward(request,response);

  ![image-20210504130857706](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210504130857706.png)

- 重定向：在客户端完成，是两次请求；应用与需要从一个控制器转到另一个控制器处理数据的应用场景，可以把数据放在HttpSession域中，也似乎可以用RedirectAttributes来自动拼接URL（在URL上自动加上参数）

  ```java
  		redirectAttributes.addAttribute("results",results);
           return "redirect:/gradeReport/handleGrade";
  ```

  ```java
  		httpSession.setAttribute("results",results);
          httpSession.setAttribute("score",score);
          httpSession.setAttribute("answer",answer);
          return "redirect:/gradeReport/handleGrade";
  ```

总结：到方法：forward和redirect+方法路径
到页面，重定向是页面相对路径，转发是jsp页面的名称

### 四、MVC

![img](http://uploadfiles.nowcoder.com/images/20160506/426037_1462465406280_0E22A1FEC50C6044271BA2798F3C9CBF)

- MVC全名是Model View Controller，是模型(model)－视图(view)－控制器(controller)的缩写，一种软件设计典范，用一种业务逻辑、数据、界面

  显示分离的方法组织代码，将业务逻辑聚集到一个部件里面，在改进和个性化定制界面及用户交互的同时，不需要重新编写业务逻辑。MVC被独特的发展起来用于映射传统的输入、处理和输出功能在一个逻辑的图形化用户界面的结构中。 

  MVC只是将分管不同功能的逻辑代码进行了隔离，增强了可维护和可扩展性，增强代码复用性，因此可以减少代码重复。但是不保证减少代码量，多层次的调用模式还有可能增加代码量

### 五、Cookie和Session

- 1.session用来表示用户会话，session对象在服务端维护，一般tomcat设定session生命周期为30分钟，超时将失效，也可以主动设置无效； 2.cookie存放在客户端，可以分为内存cookie和磁盘cookie。内存cookie在浏览器关闭后消失，磁盘cookie超时后消失。当浏览器发送请求时，将自动发送对应cookie信息，前提是请求url满足cookie路径； 3.可以将sessionId存放在cookie中，也可以通过重写url将sessionId拼接在url。因此可以查看浏览器cookie或地址栏url看到sessionId； 4.请求到服务端时，将根据请求中的sessionId查找session，如果可以获取到则返回，否则返回null或者返回新构建的session，老的session依旧存在，请参考API