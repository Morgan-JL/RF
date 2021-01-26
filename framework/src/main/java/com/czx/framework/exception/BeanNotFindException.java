package com.czx.framework.exception;

/**
 * bean未找到异常
 *
 * @author Morgan
 * @date 2020/12/8 16:28
 */
public class BeanNotFindException extends RuntimeException {

  public BeanNotFindException(String message) {
    super(message);
  }
}
