package com.czx.framework.container;

import com.czx.framework.exception.BeanAlreadyExistException;
import com.czx.framework.exception.BeanNotFindException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 配置类对象容器
 *
 * @author Morgan
 * @date 2020/12/4 10:53
 */
public class ConfigureContainer  {

  private Map<String, Class> configContainerMap;

  private static ConfigureContainer beanContainer;

  private ConfigureContainer() {
    configContainerMap = new HashMap<>(20);
  }

  public static ConfigureContainer create() {
    if (beanContainer == null) {
      beanContainer = new ConfigureContainer();
    }
    return beanContainer;
  }

  /**
   * 获取bean实例
   *
   * @param beanName bean实例名
   */
  public Object getBean(String beanName) {
    return configContainerMap.get(beanName);
  }

/*  public Object getBean(Class clz) {
    Set<Entry<String, Object>> entries = configContainerMap.entrySet();
    for (Entry<String, Object> entry : entries) {
      if (entry.getValue().getClass().isAssignableFrom(clz)) {
        return entry.getValue();
      }
    }
    throw new BeanNotFindException("`" + clz + "`未在bean容器中找到！");
  }*/

  public Map<String, Class> getConfigContainerMap() {
    return configContainerMap;
  }

  public void setConfigContainerMap(Map<String, Class> configContainerMap) {
    this.configContainerMap = configContainerMap;
  }

  public boolean exist(String clzName) {
    return configContainerMap.containsKey(clzName);
  }

  public void append(String clzName, Class bean) {
    if (exist(clzName)) {
      throw new BeanAlreadyExistException("`" + clzName + "`已经存在！");
    }
    configContainerMap.put(clzName, bean);
  }

  public void delete(String clzName) {
    if (!exist(clzName)) {
      throw new BeanNotFindException("`" + clzName + "`不存在！");
    }
    configContainerMap.remove(clzName);
  }

  public void destroy() {
    configContainerMap.clear();
  }

}
