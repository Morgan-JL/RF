package com.czx.framework.exception;

/**
 * bean未找到异常
 *
 * @author Morgan
 * @date 2020/12/8 16:28
 */
public class BeanAlreadyExistException extends RuntimeException {

  public BeanAlreadyExistException(String message) {
    super(message);
  }
}
