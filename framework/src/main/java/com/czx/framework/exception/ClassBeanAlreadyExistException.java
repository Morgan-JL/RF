package com.czx.framework.exception;

/**
 * bean未找到异常
 *
 * @author Morgan
 * @date 2020/12/8 16:28
 */
public class ClassBeanAlreadyExistException extends RuntimeException {

  public ClassBeanAlreadyExistException(String message) {
    super(message);
  }
}
