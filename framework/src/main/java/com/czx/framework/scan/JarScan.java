package com.czx.framework.scan;

import com.czx.framework.exception.JarScanException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Morgan
 * @date 2020/12/10 18:26
 */
public class JarScan implements Scan {

  protected String jarpath;
  protected String prefix;

  public JarScan(Class<?> clzz) throws URISyntaxException {
    String allname = clzz.getName();
    String simpleName = clzz.getSimpleName();
    prefix = allname.substring(0, allname.length() - simpleName.length()).replaceAll("\\.", "/");
    jarpath = clzz.getResource("").getPath();
    jarpath = jarpath.substring(5);
    if (jarpath.contains(".jar")) {
      if (jarpath.contains(":")) {
        jarpath = jarpath.substring(1, jarpath.indexOf(".jar") + 4);
      } else {
        jarpath = jarpath.substring(0, jarpath.indexOf(".jar") + 4);
      }
    }
  }

  public void autoScan() {
    JarFile jarFile = null;
    try {
      jarFile = new JarFile(jarpath);
    } catch (IOException e) {
      throw new JarScanException("找不到jar文件：[" + jarpath + "]", e);
    }
    Enumeration<JarEntry> entrys = jarFile.entries();
    try {
      while (entrys.hasMoreElements()) {
        JarEntry entry = entrys.nextElement();
        String name = entry.getName();
        if (name.endsWith(".class") && name.startsWith(prefix)) {
          name = name.substring(0, name.length() - 6);
          String clzzName = name.replaceAll("/", "\\.");
          Class<?> fileClass = Class.forName(clzzName);
          Scanner.scan(fileClass);
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }
}
