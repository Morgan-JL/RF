package com.czx.framework.container.factory;

import com.czx.framework.annotations.Bean;
import com.czx.framework.annotations.Configuration;
import com.czx.framework.container.AwaitContainer;
import com.czx.framework.container.ConfigureContainer;
import com.czx.framework.container.Module;
import com.czx.framework.utils.reflect.AnnotationUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Morgan
 * @date 2020/12/22 09:39
 */
public class ConfigurationBeanFactory extends IocBeanFactory {

  public List<Object> createBean() {
    List<Object> beans = new ArrayList<>(20);
    ConfigureContainer configureContainer = ConfigureContainer.create();
    Collection<Class> configClzs = configureContainer.getConfigContainerMap().values();
    for (Class aClass : configClzs) {
      Method[] methods = aClass.getDeclaredMethods();
      for (Method method : methods) {
        if (AnnotationUtils.isExist(method, Bean.class)) {
          Object bean = null;
          try {
            bean = method.invoke(aClass.newInstance(), method.getParameters());
            beans.add(bean);
          } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
          }
        }
      }
    }

    return beans;
  }

  public String getBeanName(Class<?> beanClass) {
    return super.getBeanName(beanClass);
  }
}
