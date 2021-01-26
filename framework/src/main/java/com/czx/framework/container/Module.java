package com.czx.framework.container;


/**
 * bean容器实例
 *
 * @author Morgan
 * @date 2020/12/18 15:15
 */
public class Module {

  private String id;

  private Object bean;

  private String type = "component";

  public Module(String id, Object bean) {
    this.id = id;
    this.bean = bean;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Object getBean() {
    return bean;
  }

  public void setBean(Object bean) {
    this.bean = bean;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
