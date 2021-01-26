package com.czx.web;

import com.czx.web.boot.RickApplication;
import java.io.IOException;

/**
 * @author Morgan
 * @date 2020/12/10 15:09
 */
public class WebApplication {

  public static void main(String[] args) throws IOException {
    RickApplication.run(WebApplication.class,args);
  }

}
