package com.czx.framework.container;


import com.alibaba.fastjson.JSON;
import com.czx.framework.exception.ClassBeanAlreadyExistException;
import com.czx.framework.exception.ClassBeanNotFindException;
import com.czx.framework.exception.FileSchemeException;
import com.czx.framework.scan.RickUrlClassLoader;
import com.czx.framework.scan.Scanner;
import com.czx.framework.utils.JsonReaderUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
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
public class ClassContainer {

  private static final Logger logger = LoggerFactory.getLogger(ClassContainer.class);

  private static ClassContainer classContainer;

  private Map<String, Class> classContainerMap;

  private ClassContainer() {
    classContainerMap = new HashMap<>(20);
  }

  //创建容器
  public static ClassContainer create() {
    if (classContainer == null) {
      classContainer = new ClassContainer();
    }
    return classContainer;
  }

  public Map<String, Class> getClassContainerMap() {
    return classContainerMap;
  }

  public void setClassContainerMap(Map<String, Class> classContainerMap) {
    this.classContainerMap = classContainerMap;
  }

  /**
   * 获取加载的class
   *
   * @param classId 类id
   */
  public Class getMapping(String classId) {
    if (classContainerMap.containsKey(classId)) {
      return classContainerMap.get(classId);
    }
    throw new ClassBeanNotFindException("没有找到`" + classId + "`对应的class！");
  }

  /**
   * 判断{@code clzName}是否已经在容器中存在
   *
   * @param clzName 类名
   */
  public boolean exist(String clzName) {
    return classContainerMap.containsKey(clzName);
  }

  /**
   * 通过文件路径进行加载
   *
   * @param filepath 服务存放路径
   */
  public static void load(String filepath) {
    if (filepath == null) {
      logger.info("当前没有外部jar需要进行加载！");
      return;
    }

    if (filepath.endsWith(".json")) {
      loadJSON(filepath);
      return;
    }

    if (filepath.endsWith(".jar")) {
      loadJar(filepath);
      logger.info("当前没有外部jar需要进行加载！");
      return;
    }

    throw new FileSchemeException("文件格式错误！目前只支持.json || .jar");
  }

  private static void loadJar(String filepath) {
    try {
      String uri = "file:" + filepath;
      URL[] urls = new URL[]{new URL(uri)};
      loadByUrl(urls);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  private static void loadByUrl(URL[] urls) {
    RickUrlClassLoader moduleClassLoader = new RickUrlClassLoader(urls,
        Thread.currentThread().getContextClassLoader());
    Collection<Class> values = moduleClassLoader.getClassMap().values();
    for (Class clz : values) {
      Scanner.scan(clz);
    }
  }

  private static void loadJSON(String filepath) {
    try {
      String jsonStr = JsonReaderUtil.readJsonFile(filepath);
      Map serveMap = (Map) JSON.parse(jsonStr);
      URL[] urls = new URL[serveMap.size()];
      int index = 0;
      for (Object value : serveMap.values()) {
        String uri = "file:" + value;
        urls[index] = new URL(uri);
        index++;
      }
      logger.info("文件path:{} - json文件加载外部jar - 外部jar:{}", filepath, serveMap);
      loadByUrl(urls);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public void append(String clzName, Class clz) {
    if (exist(clzName)) {
      throw new ClassBeanAlreadyExistException("`" + clzName + "`已经存在！");
    }
    classContainerMap.put(clzName, clz);
  }

  public void delete(String clzName) {
    if (!exist(clzName)) {
      throw new ClassBeanNotFindException("`" + clzName + "`不存在！");
    }
    classContainerMap.remove(clzName);
  }

  public void destroy() {
    classContainerMap.clear();
  }
}

