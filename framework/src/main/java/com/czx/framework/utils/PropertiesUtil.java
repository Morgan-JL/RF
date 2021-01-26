package com.czx.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Morgan
 * @date 2020/12/11 15:25
 */
public class PropertiesUtil {

  private static final String contextPrefix = "application";

  private static final String contextSuffix = "properties";

  private static Properties parse() {
    String contextPath = contextPrefix + "." + contextSuffix;
    Properties properties = new Properties();
    InputStream inputStream = PropertiesUtil.class.getResourceAsStream("/" + contextPath);
    try {
      properties.load(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return properties;
  }

  public static String getProperty(String key) {
    return parse().getProperty(key);
  }

  public static Map<String, String> getConfigMap() {
    Map<String, String> configMap = new HashMap<>(10);
    Enumeration<?> enumeration = parse().propertyNames();
    while (enumeration.hasMoreElements()) {
      String element = (String) enumeration.nextElement();
      configMap.put(element, getProperty(element));
    }

    return configMap;
  }



}
