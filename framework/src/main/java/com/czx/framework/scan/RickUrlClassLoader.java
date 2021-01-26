package com.czx.framework.scan;

import com.czx.framework.container.ClassContainer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author morgan
 * @date 2020/11/23 8:55
 */
public class RickUrlClassLoader extends URLClassLoader {

  //保存本类加载器加载的class字节码
  private Map<String, byte[]> classBytesMap = new HashMap<>(20);

  private Map<String, Class> classMap;

  public RickUrlClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
    classMap = new HashMap<>(20);
    for (URL url : urls) {
      String path = url.getPath();
      try {
        JarFile jarFile = new JarFile(path);
        //初始化类加载器执行类加载
        init(jarFile);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    if (findLoadedClass(name) == null) {
      return super.loadClass(name);
    } else {
      byte[] bytes = classBytesMap.get(name);
      return defineClass(name, bytes, 0, bytes.length);
    }
  }

  /**
   * 方法描述 初始化类加载器，保存字节码
   */
  private void init(JarFile jarFile) {
    //解析jar包每一项
    Enumeration<JarEntry> en = jarFile.entries();
    InputStream input = null;
    String className = null;
    try {
      while (en.hasMoreElements()) {
        JarEntry je = en.nextElement();
        String name = je.getName();

        //路径扫描限制
        if (!name.endsWith(".class")) {
          continue;
        }

        className = name.substring(0, name.length() - 6).replaceAll("/", ".");

        input = jarFile.getInputStream(je);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int bytesNumRead = 0;
        while ((bytesNumRead = input.read(buffer)) != -1) {
          baos.write(buffer, 0, bytesNumRead);
        }
        byte[] classBytes = baos.toByteArray();
        classBytesMap.put(className, classBytes);
      }
    } catch (Throwable e) {
      System.err.printf("类载入失败：%s\n", className);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    //将jar中的每一个class字节码进行Class载入
    for (Map.Entry<String, byte[]> entry : classBytesMap.entrySet()) {
      String key = entry.getKey();
      Class<?> aClass = null;
      try {
        aClass = loadClass(key);
        classMap.put(key, aClass);
      } catch (Throwable e) {
        e.getStackTrace();
      }
    }
  }

  public Map<String, Class> getClassMap() {
    return classMap;
  }

}
