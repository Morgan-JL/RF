package com.czx.framework.container;


import com.czx.framework.exception.ClassBeanAlreadyExistException;
import com.czx.framework.exception.ClassBeanNotFindException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单例的类容器 [K：V]->[对应的类包：对应的类]
 *
 * @author morgan
 * @date 2020/11/24 11:55
 */
public class AwaitContainer  {

  private static final Logger logger = LoggerFactory.getLogger(AwaitContainer.class);

  private static AwaitContainer awaitContainer;

  private Map<String, Class> awaitContainerMap;

  private AwaitContainer() {
    awaitContainerMap = new HashMap<>(20);
  }

  //创建容器
  public static AwaitContainer create() {
    if (awaitContainer == null) {
      awaitContainer = new AwaitContainer();
    }
    return awaitContainer;
  }

  public Map<String, Class> getAwaitContainerMap() {
    return awaitContainerMap;
  }

  public void setAwaitContainerMap(Map<String, Class> classContainerMap) {
    this.awaitContainerMap = classContainerMap;
  }

  /**
   * 获取加载的class
   *
   * @param classId 类id
   */
  public Class getMapping(String classId) {
    if (awaitContainerMap.containsKey(classId)) {
      return awaitContainerMap.get(classId);
    }
    throw new ClassBeanNotFindException("没有找到`" + classId + "`对应的class！");
  }

  /**
   * 判断{@code clzName}是否已经在容器中存在
   *
   * @param clzName 类名
   */
  public boolean exist(String clzName) {
    return awaitContainerMap.containsKey(clzName);
  }

  public void append(String clzName, Class clz) {
    if (exist(clzName)) {
      throw new ClassBeanAlreadyExistException("`" + clzName + "`已经存在！");
    }
    awaitContainerMap.put(clzName, clz);
  }



  public void delete(String clzName) {
    if (!exist(clzName)) {
      throw new ClassBeanNotFindException("`" + clzName + "`不存在！");
    }
    awaitContainerMap.remove(clzName);
  }

  public void destroy() {
    awaitContainerMap.clear();
  }
}

