package com.czx.web.boot;

import com.czx.framework.utils.PropertiesUtil;
import com.czx.web.config.FilterMapping;
import com.czx.web.config.ListenerMapping;
import com.czx.web.config.ServletMapping;
import com.czx.web.config.WebConfig;
import com.czx.web.servlet.DefaultServlet;
import java.util.EventListener;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.apache.catalina.Wrapper;

/**
 * @author Morgan
 * @date 2020/12/15 18:31
 */
public class RickServletContainerInitializer implements ServletContainerInitializer {

  public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
    WebConfig webConfig = WebConfig.create();

<<<<<<< HEAD
    for (ServletMapping servlet : webConfig.getServlets()) {
      servletContext.addServlet(servlet.getName(), servlet.getServlet());
    }

    for (FilterMapping filter : webConfig.getFilters()) {
      servletContext.addFilter(filter.getName(), filter.getFilter());
    }

    for (ListenerMapping listener : webConfig.getListeners()) {
      servletContext.addListener(listener.getListener());
=======
    for (Servlet servlet : webConfig.getServlets()) {
      servletContext.addServlet("", servlet);
    }

    for (Filter filter : webConfig.getFilters()) {
      servletContext.addFilter("", filter);
    }

    for (EventListener listener : webConfig.getListeners()) {
      servletContext.addListener(listener);
>>>>>>> b84e2bc... 内嵌tomcat容器servlet三大组件的初步实现
    }
  }


}
