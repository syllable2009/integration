# Spring、SpringMVC和SpringBoot
Spring是一个开源容器框架，可以接管web层，业务层，dao层，持久层的组件，并且可以配置各种bean,和维护bean与bean之间的关系。其核心就是控制反转(IOC),和面向切面(AOP),简单的说就是一个分层的轻量级开源框架。
SpringMVC属于SpringFrameWork的后续产品，已经融合在Spring Web Flow里面。SpringMVC是一种web层mvc框架，用于替代servlet（处理|响应请求，获取表单参数，表单校验等。SpringMVC是一个MVC的开源框架，SpringMVC=struts2+spring，springMVC就相当于是Struts2加上Spring的整合。
Springboot是一个微服务框架，延续了spring框架的核心思想IOC和AOP，简化了应用的开发和部署。Spring Boot是为了简化Spring应用的创建、运行、调试、部署等而出现的，使用它可以做到专注于Spring应用的开发，而无需过多关注XML的配置。提供了一堆依赖打包，并已经按照使用习惯解决了依赖问题--->习惯大于约定。
# SpringMVC是如何代替servelt的
些JavaEE的时候，知道了我们自己写Servlet，但是现在我们直接写controller，两种工作方式的差异，让我觉得自己的知识出现了断层；即，Spring框架是如何操作，让Controler来替代Servlet的？
当要实现在Spring框架下的web服务时,那么servlet将无法兼容(因为Spring无法依赖注入到Servlet)
    <servlet>  
        <!-- 配置DispatcherServlet -->  
    　　<servlet-name>springMvc</servlet-name>  
    　　<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>  
    　　<!-- 指定spring mvc配置文件位置 不指定使用默认情况 -->  
    　　<init-param>     
        　　<param-name>contextConfigLocation</param-name>
        　　<param-value>classpath:spring/spring-mvc.xml</param-value>
   　　 </init-param>  
    　　<!-- 设置启动顺序 -->  
    　　<load-on-startup>1</load-on-startup>  
　　</servlet>

　　<!-- ServLet 匹配映射 -->
　　<servlet-mapping>
    　　<servlet-name>springMvc</servlet-name>
   　　 <url-pattern>/</url-pattern>
　　</servlet-mapping>
springmvc配置了DispatcherServlet处理所有的请求，都是请求到了DispatcherServlet类中，该类通过配置项，扫描了我们配置的jar包；然后通过注解，找到相对应的方法，进行代码处理。
# springboot是如何加载DispatcherServlet呢
配置Servlet的方法有许多方式：
1. web.xml静态配置servlet(servletv2.5)
2. ServletContainerInitializer编码动态注册
3. 注解@WebServlet (servletv3.0+)
Servlet3提供了ServletContainerInitializer接口来支持动态的注册Servlet/Filter/Listener
spring-web模块里的SpringServletContainerInitializer实现并抽象出新的接口：WebApplicationInitializer,
所以，它的存在基本替代web.xml配置，在初始化的时候注册并配置容器上下文，官方doc的示例：
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletCxt) {
        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(AppConfig.class);
        ac.refresh();
        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/app/*");
    }
}
为了简化DispatcherServlet的基本千篇一律的配置，也提供了AbstractAnnotationConfigDispatcherServletInitializer直接继承实现对应的配置类列表相关方法即可
SpringBoot没有选择hook到Servlet规范里的的生命周期，而是抽象出自己的接口来配置嵌入的容器，把Servlet/Filter/Listener这些变成Spring Bean一样去配置

ServletContainerInitializer 生命周期是归Servlet容器管理
ServletContextInitializer 他是Spring自己管理
SpringBoot应用一般都是embed server，默认不会去走Servlet那一套，所以想配置Servlet就是通过ServletContextInitializer或者其子类来实现注册.
源码的大概流程
SpringApplication.run();

ServletWebServerApplicationContext#onRefresh() -> createWebServer()

创建嵌入的servlet容器的时候(默认tomcat),new TomcatWebServer()构造函数里执行了初始化initialize()，也就是日志里比较标志性的一行 Tomcat initialized with port(s): 8080 (http)

Tomcat#start() -> LifecycleBase#start()
-> StandardService#startInternal() -> StandardEngine#startInternal() -> ContainerBase#startInternal()
然后提交了异步的FutureTask，就是startStopExecutor.submit(new StartChild(children[i]))，TomcatEmbeddedContext#start()的时候会执行他的ServletContextInitializer列表

ServletWebServerApplicationContext里有一个实现的ServletContextInitializer逻辑：
private void selfInitialize(ServletContext servletContext) throws ServletException {
    prepareWebApplicationContext(servletContext);
    registerApplicationScope(servletContext);
    WebApplicationContextUtils.registerEnvironmentBeans(getBeanFactory(), servletContext);
    // 这里就是完成各种Servlet Filter Listener的注册关键逻辑
    for (ServletContextInitializer beans : getServletContextInitializerBeans()) {
        beans.onStartup(servletContext);
    }
}
getServletContextInitializerBeans()逻辑就是从BeanFactory里获取指定类型的Bean列表，当然这其中就包含了一个DispatcherServletRegistrationBean，这个Bean的配置是在DispatcherServletAutoConfiguration里配置的
https://www.cnblogs.com/chenjunjie12321/p/9357580.html

当我们初次请求的时候，服务器会比较卡，半天才给我们反馈，但是第二次请求的时候，速度就快了很多；为啥呢？懒加载，当我启动服务器的时候，该构造方法并没有被执行，但是当我进行访问的时候，该方法被执行了；

# springboot的tomcat是如何运行的？
Servlet并不一定是随web容器启动而创建，一个web容器中可能有非常多的Servlet，如果有百千个servlet，那服务器启动的时候就要实例化那么多个类，显然对于内存而言负载量相当的大。其实在web.xml的<servlet>标签中可以通过<load-on-startup>来控制Servlet的是否随web容器启动而初始化以及多个Servlet同时创建时的初始化顺序。

#springbmvc的工作流程


#springboot是如何简化springbmvc的
从Servlet技术到Spring和Spring MVC，开发Web应用变得越来越简捷。但是Spring和Spring MVC的众多配置有时却让人望而却步，相信有过Spring MVC开发经验的朋友能深刻体会到这一痛苦。因为即使是开发一个Hello-World的Web应用，都需要我们在pom文件中导入各种依赖，编写web.xml、spring.xml、springmvc.xml配置文件等。
正如Spring Boot的名称一样，一键启动，Spring Boot提供了自动配置功能，为我们提供了开箱即用的功能，使我们将重心放在业务逻辑的开发上。
![image text]()
