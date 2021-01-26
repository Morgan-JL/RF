package com.czx.web.servlet;

import com.czx.framework.container.ClassContainer;
import com.czx.framework.container.RegisterMachine;
import com.czx.web.handler.RequestHandler;
import com.czx.web.route.RouteContainer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Morgan
 * @date 2020/12/2 9:46
 */
public class EntryServlet extends HttpServlet {

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    //请求处理
    RequestHandler.execute(req, resp);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    doGet(req, resp);
  }

  public void init(ServletConfig config) throws ServletException {
    String CONF_PATH = config.getInitParameter("conf");
    //初始化：加载jar包至class容器
    ClassContainer.load(CONF_PATH);
    RegisterMachine registerMachine = new RegisterMachine();
    registerMachine.init();
    //路由容器初始化
    RouteContainer.create().rigOut();
  }
}
