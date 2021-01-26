package com.czx.framework;

import com.czx.framework.container.BeanContainer;
import com.czx.framework.container.Module;
import com.czx.framework.container.RegisterMachine;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Morgan
 * @date 2020/12/18 15:40
 */
public class AutoScanApplicationContext implements ApplicationContext {

  private static BeanContainer beanContainer = BeanContainer.create();

  private static RegisterMachine registerMachine = RegisterMachine.create();

  public AutoScanApplicationContext(){
    registerMachine.init();
  }

  public Object getBean(String id) {
    Object bean = null;
    Collection<Module> modules = beanContainer.getBeanContainerMap().values();
    for (Module module : modules) {
      if (module.getId().equals(id)) {
        bean = module.getBean();
      }
    }
    return bean;
  }


  public Module getModule(String id) {
    Collection<Module> modules = beanContainer.getBeanContainerMap().values();
    for (Module module : modules) {
      if (module.getId().equals(id)) {
        return module;
      }
    }
    return null;
  }

  public <T> List<T> getBean(Class<T>... aClass) {
    return null;
  }

  public List<Module> getModule(Class<?>... aClass) {
    return null;
  }

  public List<Object> getBeanByAnnotation(Class<? extends Annotation>... annotationClasses) {
    return null;
  }

  public List<Module> getModuleByAnnotation(Class<? extends Annotation>... annotationClasses) {
    return null;
  }

  public List<Object> getBeans() {
    List<Object> beans = new ArrayList<>(10);
    Collection<Module> modules = beanContainer.getBeanContainerMap().values();
    for (Module module : modules) {
      beans.add(module.getBean());
    }
    return beans;
  }

  public boolean isIOCType(String type) {
    return false;
  }

  public boolean isIOCId(String id) {
    return false;
  }

  public boolean isIOCClass(Class<?> componentClass) {
    return false;
  }

}
