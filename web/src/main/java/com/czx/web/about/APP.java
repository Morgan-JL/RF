package com.czx.web.about;


import com.alibaba.fastjson.JSON;
import com.czx.framework.utils.JsonReaderUtil;
import java.net.URL;
import java.util.Map;

/**
 * @author Morgan
 * @date 2020/12/18 09:51
 */
public class APP {

  private static APP app;

  private static String name;

  private static String author;

  private static String version;

  private static String ctime;

  public void init() {
    URL resource = this.getClass().getClassLoader().getResource("app-info.json");
    String path = resource.getPath();
    String jsonFile = JsonReaderUtil.readJsonFile(path);
    Map map = (Map) JSON.parse(jsonFile);
    String name = map.get("name").toString();
    String author = map.get("author").toString();
    String version = map.get("version").toString();
    String ctime = map.get("ctime").toString();
    new APP(name, author, version, ctime);
  }

  public static void main(String[] args) {
    APP.create().init();
    System.out.println(APP.getCtime());
  }

  public static APP create() {
    if (app == null) {
      return new APP();
    }
    return app;
  }

  public APP() {
  }

  public APP(String name, String author, String version, String ctime) {
    APP.name = name;
    APP.author = author;
    APP.version = version;
    APP.ctime = ctime;
  }


  public static String getName() {
    return name;
  }

  public static void setName(String name) {
    APP.name = name;
  }

  public static String getAuthor() {
    return author;
  }

  public static void setAuthor(String author) {
    APP.author = author;
  }

  public static String getVersion() {
    return version;
  }

  public static void setVersion(String version) {
    APP.version = version;
  }

  public static String getCtime() {
    return ctime;
  }

  public static void setCtime(String ctime) {
    APP.ctime = ctime;
  }
}
