package com.czx.web.route;

import com.czx.framework.container.AwaitContainer;
import com.czx.framework.container.BeanContainer;
import com.czx.framework.utils.reflect.ASMUtil;
import com.czx.web.annotations.Controller;
import com.czx.web.annotations.Mapping;
import com.czx.web.exception.BeanMappingNotFoundException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 路由容器 [路由地址：映射类]
 *
 * @author Morgan
 * @date 2020/12/4 11:22
 */
public class RouteContainer {

  private static final Logger log = LoggerFactory.getLogger(RouteContainer.class);

  private Map<String, BeanMapping> routeContainerMap;

  private static RouteContainer routeContainer;

  public RouteContainer() {
    routeContainerMap = new HashMap<>(20);
  }

  private void createRouteContainer() {
    Map<String, Class> awaitContainerMap = AwaitContainer.create().getAwaitContainerMap();
    for (Entry<String, Class> entry : awaitContainerMap.entrySet()) {
      String controllerName = null;
      Controller controllerAnn = (Controller) entry.getValue().getAnnotation(Controller.class);
      if (controllerAnn != null) {
        controllerName = controllerAnn.url();

        Method[] methods = entry.getValue().getDeclaredMethods();
        for (Method method : methods) {
          // 扫描方法上带有[@Mapping]的注解
          if (method.getAnnotation(Mapping.class) != null) {
            controllerName = controllerName.startsWith("/") ? controllerName : "/" + controllerName;
            controllerName = controllerName.endsWith("/") ? controllerName : controllerName + "/";
            String mappingVal = method.getAnnotation(Mapping.class).value();
            mappingVal = mappingVal.startsWith("/") ? mappingVal.substring(1) : mappingVal;
            String beanId = controllerName + mappingVal;
            routeContainerMap
                .put(beanId,
                    new BeanMapping(BeanContainer.create().getBean(entry.getValue()), method));
            Map<String,String> paramInfo = new HashMap<>(10);
            for (Parameter parameter : method.getParameters()) {
              String type = parameter.getType().getName();
              String name = parameter.getName();
              paramInfo.put(type,name);
            }
            log.info("[{}] - params:{}", beanId, paramInfo);
          }
        }
      }
    }
  }

  //装配路由
  public void rigOut() {
    //创建路由容器
    createRouteContainer();
  }


  public static RouteContainer create() {
    if (routeContainer == null) {
      routeContainer = new RouteContainer();
    }
    return routeContainer;
  }

  public void delete(String containerName) {
    if (!exist(containerName)) {
      throw new RuntimeException("`" + containerName + "`不存在！");
    }
    routeContainerMap.remove(containerName);
  }

  public void destroy() {
    routeContainerMap.clear();
  }

  public boolean exist(String route) {
    return routeContainerMap.containsKey(route);
  }

  public void append(String route, BeanMapping beanMapping) {
    if (exist(route)) {
      throw new RuntimeException("`" + route + "`已经存在！");
    }
    routeContainerMap.put(route, beanMapping);
  }

  public BeanMapping getBeanMapping(String path) {
//    String servletPath = CharUtil.trim(path, "/");
    if (getRouteContainerMap().get(path) == null) {
      throw new BeanMappingNotFoundException(
          "`" + path + "`未在 [RouteContainer] " + getRouteContainerMap() + "中找到");
    }
    return getRouteContainerMap().get(path);
  }

  private Map<String, BeanMapping> getRouteContainerMap() {
    return routeContainerMap;
  }

  public void setRouteContainerMap(Map<String, BeanMapping> routeContainerMap) {
    this.routeContainerMap = routeContainerMap;
  }
}
