package com.czx.framework.enums;

/**
 * @author Morgan
 * @date 2020/12/8 16:35
 */
public enum ExceptionEnum {

  BEANNOTFIND(501, "bean未在容器中找到！");

  private Integer code;
  private String message;

  ExceptionEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
