package com.czx.framework.exception;

/**
 * bean未找到异常
 *
 * @author Morgan
 * @date 2020/12/8 16:28
 */
public class ClassBeanNotFindException extends RuntimeException {

  public ClassBeanNotFindException(String message) {
    super(message);
  }
}
