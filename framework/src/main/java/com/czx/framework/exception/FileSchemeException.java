package com.czx.framework.exception;

/**
 * bean未找到异常
 *
 * @author Morgan
 * @date 2020/12/8 16:28
 */
public class FileSchemeException extends RuntimeException {

  public FileSchemeException(String message) {
    super(message);
  }
}
