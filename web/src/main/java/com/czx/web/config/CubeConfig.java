package com.czx.web.config;

import com.czx.framework.utils.PropertiesUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Morgan
 * @date 2020/12/22 10:06
 */
public class CubeConfig {

  /**
   * "server.port"
   */
  private String port;

  /**
   * "serve.path"
   */
  private String servePath;

  private Map<String, String> httpUrl;

  public CubeConfig() {
    httpUrl = new HashMap<>(5);
  }

  public CubeConfig create() {
    return new CubeConfig();
  }

  public void init() {
    this.port = PropertiesUtil.getProperty("server.port");
    this.servePath = PropertiesUtil.getProperty("serve.path");
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getServePath() {
    return servePath;
  }

  public void setServePath(String servePath) {
    this.servePath = servePath;
  }

  public Map<String, String> getHttpUrl() {
    return httpUrl;
  }

  public void setHttpUrl(Map<String, String> httpUrl) {
    this.httpUrl = httpUrl;
  }
}
