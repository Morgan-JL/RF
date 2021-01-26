package com.czx.framework.scan;

import com.czx.framework.container.factory.BeanFactory;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

/**
 * @author Morgan
 * @date 2020/12/10 11:43
 */
public class PackageScan implements Scan{

  private String rootPath;

  private String projectPath;

  public PackageScan(Class<?> application) throws URISyntaxException {
    rootPath = application.getClassLoader().getResource("").toURI().getPath();
    projectPath = rootPath.replaceAll("test-classes", "classes");
  }

  public void autoScan() {
    try {
      fileScan(projectPath);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public void fileScan(String path) throws ClassNotFoundException {
    File bin = new File(path);
    File[] listFiles = bin.listFiles();
    for (File file : listFiles) {
      if (file.isDirectory()) {
        fileScan(path + "/" + file.getName());
      } else if (file.getAbsolutePath().endsWith(".class")) {
        Scanner.scan(getFileClass(file));
      }
    }
  }

  private Class<?> getFileClass(File file) throws ClassNotFoundException {
    String className = file.getAbsolutePath().replaceAll("\\\\", "/").replaceAll(projectPath, "")
        .replaceAll("/", "\\.");
    className = className.substring(0, className.length() - 6);
    Class<?> fileClass = Class.forName(className.split("classes.")[1]);
    return fileClass;
  }

}
