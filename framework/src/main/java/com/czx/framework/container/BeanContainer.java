package com.czx.framework.container;

import com.czx.framework.exception.BeanAlreadyExistException;
import com.czx.framework.exception.BeanNotFindException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * bean对象容器
 *
 * @author Morgan
 * @date 2020/12/4 10:53
 */
public class BeanContainer {

  private Map<String, Module> beanContainerMap;

  private static BeanContainer beanContainer;

  private BeanContainer() {
    beanContainerMap = new HashMap<>(20);
  }

  public static BeanContainer create() {
    if (beanContainer == null) {
      beanContainer = new BeanContainer();
    }
    return beanContainer;
  }

  /**
   * 获取bean实例
   *
   * @param beanName bean实例名
   */
  public Object getBean(String beanName) {
    return beanContainerMap.get(beanName);
  }

  /**
   * 通过类获取bean实例
   *
   * @param clz 对应的类
   */
  public Object getBean(Class clz) {
    Collection<Module> modules = beanContainerMap.values();
    for (Module module : modules) {
      if (module.getBean().getClass().isAssignableFrom(clz)) {
        return module.getBean();
      }
    }
    throw new BeanNotFindException("`" + clz + "`未在bean容器中找到！");
  }

  public Map<String, Module> getBeanContainerMap() {
    return beanContainerMap;
  }

  public void setBeanContainerMap(Map<String, Module> beanContainerMap) {
    this.beanContainerMap = beanContainerMap;
  }

  public boolean exist(String clzName) {
    return beanContainerMap.containsKey(clzName);
  }

  public void append(String clzName, Module module) {
    if (exist(clzName)) {
      throw new BeanAlreadyExistException("`" + clzName + "`已经存在！");
    }

    beanContainerMap.put(clzName, module);
  }


  public void delete(String clzName) {
    if (!exist(clzName)) {
      throw new BeanNotFindException("`" + clzName + "`不存在！");
    }
    beanContainerMap.remove(clzName);
  }

  public void destroy() {
    beanContainerMap.clear();
  }

}
