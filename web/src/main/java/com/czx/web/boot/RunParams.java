package com.czx.web.boot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Morgan
 * @date 2020/12/14 16:03
 */
public class RunParams {

  private static final String port = "server.port";

  private static RunParams RunParams;

  private static Map<String, String> runArgs;

  public static RunParams create() {
    if (RunParams == null) {
      return new RunParams();
    }
    return RunParams;
  }


  public void add(String[] args) {
    runArgs = new HashMap<>(20);
    List<String> list = Arrays.asList(args);
    for (String arg : list) {
      String[] param = arg.split("=");
      runArgs.put(param[0], param[1]);
    }
  }

  public static String get(String key) {
    return runArgs.get(key);
  }

  public static String getPort() {
    return port;
  }
}