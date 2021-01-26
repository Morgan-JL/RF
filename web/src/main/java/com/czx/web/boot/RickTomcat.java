package com.czx.web.boot;

import com.czx.framework.container.BeanContainer;
import com.czx.framework.container.Module;
import com.czx.framework.container.RegisterMachine;
import com.czx.framework.utils.PropertiesUtil;
import com.czx.framework.utils.reflect.AnnotationUtils;
import com.czx.web.config.FilterMapping;
import com.czx.web.config.ListenerMapping;
import com.czx.web.config.ServletMapping;
import com.czx.web.config.WebConfig;
import com.czx.web.route.RouteContainer;
import com.czx.web.servlet.EntryServlet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;
import java.util.ServiceLoader;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

/**
 * @author Morgan
 * @date 2020/12/10 15:11
 */
public class RickTomcat {

  // tomcat端口号
  private static String port = "8080";
  // tomcat的字符编码集
  private static String code = "UTF-8";
  // 拦截请求路径
  private static String hinderURL = "/";
  // 请求转发路径
  private static String shiftURL = "/";
  // tomcat对象
  private static Tomcat tomcat;

  public RickTomcat() {
    init();
  }

  // 启动内嵌tomcat
  public static void run() {
    RegisterMachine registerMachine = new RegisterMachine();
    registerMachine.init();
    //路由容器初始化
    RouteContainer.create().rigOut();
    new RickTomcat();
    try {
      tomcat.start();
      tomcat.getServer().await();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
  }

  // 初始化tomcat容器
  private void init() {
    // 端口的优先权设置
    portPriority();
    tomcat = new Tomcat();

    // 创建连接器
    Connector conn = tomcat.getConnector();
    conn.setPort(Integer.valueOf(port));
    conn.setURIEncoding(code);

    // 设置Host
    Host host = tomcat.getHost();
    host.setAppBase("webapps");

    // 获取目录绝对路径
    String classPath = System.getProperty("user.dir");

    // 配置tomcat上下文
    Context context = tomcat.addContext(host, hinderURL, classPath);
    configureServlet(context);
  }

  private void configureServlet(Context context) {
    WebConfig webConfig = WebConfig.create();
    for (ServletContainerInitializer initializer : ServiceLoader
        .load(ServletContainerInitializer.class)) {
      Collection<Module> modules = BeanContainer.create().getBeanContainerMap().values();
      for (Module module : modules) {
        Object bean = module.getBean();
        if (AnnotationUtils.isExist(bean.getClass(), WebServlet.class)) {
          String name = AnnotationUtils.get(bean.getClass(), WebServlet.class).name();
          if(name.isEmpty()){
            name = bean.getClass().getSimpleName();
          }
          ServletMapping servletMapping = new ServletMapping();
          servletMapping.setName(name);
          webConfig.addServlet(servletMapping);
        }
        if (AnnotationUtils.isExist(bean.getClass(), WebFilter.class)) {
          String name = AnnotationUtils.get(bean.getClass(), WebFilter.class).filterName();
          if(name.isEmpty()){
            name = bean.getClass().getSimpleName();
          }
          FilterMapping filterMapping = new FilterMapping();
          filterMapping.setName(name);
          webConfig.addFilter(filterMapping);
        }
        if (AnnotationUtils.isExist(bean.getClass(), WebListener.class)) {
          String name = AnnotationUtils.get(bean.getClass(), WebListener.class).value();
          if(name.isEmpty()){
            name = bean.getClass().getSimpleName();
          }
          ListenerMapping listenerMapping = new ListenerMapping();
          listenerMapping.setName(name);
          webConfig.addListener(listenerMapping);
        }
      }
      context.addServletContainerInitializer(initializer, null);
    }
  }

  private void portPriority() {
    String port = RunParams.getPort();
    if (PropertiesUtil.getProperty(port) != null) {
      setPort(PropertiesUtil.getProperty(port));
    }
    if (RunParams.get(port) != null) {
      setPort(RunParams.get(port));
    }
  }

  public static String getPort() {
    return port;
  }

  public static void setPort(String port) {
    RickTomcat.port = port;
  }

  public static String getCode() {
    return code;
  }

  public static void setCode(String code) {
    RickTomcat.code = code;
  }

  public static String getHinderURL() {
    return hinderURL;
  }

  public static void setHinderURL(String hinderURL) {
    RickTomcat.hinderURL = hinderURL;
  }

  public static String getShiftURL() {
    return shiftURL;
  }

  public static void setShiftURL(String shiftURL) {
    RickTomcat.shiftURL = shiftURL;
  }

  public static Tomcat getTomcat() {
    return tomcat;
  }

  public static void setTomcat(Tomcat tomcat) {
    RickTomcat.tomcat = tomcat;
  }

}
