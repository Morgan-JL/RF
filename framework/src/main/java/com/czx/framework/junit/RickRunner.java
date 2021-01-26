package com.czx.framework.junit;

import com.czx.framework.annotations.ScanPackage;
import com.czx.framework.container.ClassContainer;
import com.czx.framework.container.Injection;
import com.czx.framework.container.RegisterMachine;
import com.czx.framework.scan.PackageScan;
import java.net.URISyntaxException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author Morgan
 * @date 2020/12/7 16:46
 */
public class RickRunner extends BlockJUnit4ClassRunner {

  public RickRunner(Class<?> klass) throws InitializationError {
    super(klass);
    ClassContainer.create();
    if (klass.getAnnotation(ScanPackage.class) != null) {
      Class<?> clz = klass.getAnnotation(ScanPackage.class).value();
      String path = klass.getAnnotation(ScanPackage.class).path();
      if (!path.isEmpty()) {
        ClassContainer.load(path);
      }
      //包扫描
      try {
        PackageScan packageScan = new PackageScan(clz);
        packageScan.autoScan();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }

      //bean注册到容器
      RegisterMachine registerMachine = new RegisterMachine();
      registerMachine.init();
    }
  }

  protected Object createTest() throws Exception {
    Object test = super.createTest();
    Injection.inject(test);
    return test;
  }
}
