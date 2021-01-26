package com.czx.web.boot;

import com.czx.framework.container.ClassContainer;
import com.czx.framework.scan.JarScan;
import com.czx.framework.scan.PackageScan;
import com.czx.web.utils.FileUtil;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Morgan
 * @date 2020/12/10 18:21
 */
public class RickApplication {

  private static final Logger logger;

  private static final RuntimeMXBean mxb = ManagementFactory.getRuntimeMXBean();

  static {
    // 彩色日志
    System.setProperty("log4j.skipJansi", "false");
    String pid = mxb.getName().split("@")[0];
    ThreadContext.put("pid", pid);
    logger = LoggerFactory.getLogger(RickApplication.class);
  }

  public static void run(Class<?> application, String[] args) {
    // 参数获取
    RunParams.create().add(args);
    // 项目初始化信息
    init();
    // 创建bean
    createBean(application);
    // 启动tomcat容器
    RickTomcat.run();
  }

  private static void init() {
    InputStream is = RickApplication.class.getResourceAsStream("/banner.txt");
    String content = FileUtil.readFileContent(is);
    logger.info("\n" + content);
  }

  private static void createBean(Class<?> application) {
    try {
      if (application.getClassLoader().getResource("") == null) {
        //jar扫描
        JarScan jarScan = new JarScan(application);
        jarScan.autoScan();
        logger.info("[jar scan] - 已加载的类：{}", ClassContainer.create().getClassContainerMap());
      } else {
        //包扫描
        PackageScan packageScan = new PackageScan(application);
        packageScan.autoScan();
        logger.info("[package scan] - 已加载的类：{}",ClassContainer.create().getClassContainerMap());
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

}
