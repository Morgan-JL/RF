package com.czx.web.beanFactory;

import com.czx.framework.container.AwaitContainer;
import com.czx.framework.container.factory.IocBeanFactory;
import com.czx.framework.proxy.CglibProxyFactory;
import com.czx.framework.utils.BaseUtils;
import com.czx.framework.utils.PropertiesUtil;
import com.czx.framework.utils.reflect.AnnotationUtils;
import com.czx.web.annotations.Controller;
import com.czx.web.annotations.Http;
import com.czx.web.exception.HttpUrlAndKeyIsNoneException;
import com.czx.web.interceptor.HttpRequestInterceptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Morgan
 * @date 2020/12/16 18:34
 */
public class RickWebBeanFactory extends IocBeanFactory {

  private static final Class[] ANNOTATION =
      new Class[]{Controller.class};

  public List<Object> createBean() {
    List<Object> beans = new ArrayList<>();
    AwaitContainer awaitContainer = AwaitContainer.create();
    Set<Entry<String, Class>> entries = awaitContainer.getAwaitContainerMap().entrySet();
    for (Entry<String, Class> entry : entries) {
      Class aClass = entry.getValue();
      if (AnnotationUtils.isExist(aClass, Http.class)) {
        String host = null;
        String value = AnnotationUtils.get(aClass, Http.class).url();
        String key = AnnotationUtils.get(aClass, Http.class).key();
        if (!key.isEmpty()) {
          host = PropertiesUtil.getProperty(key);
        } else if (!value.isEmpty()) {
          host = value;
        } else {
          throw new HttpUrlAndKeyIsNoneException(
              "[" + aClass + "] - {@HTTP}的url和key值都为空，请选择填写其中一个！");
        }
        HttpRequestInterceptor interceptor = new HttpRequestInterceptor();
        interceptor.setHost(host);
        beans.add(CglibProxyFactory.getProxy(aClass, interceptor));
      }

      if (AnnotationUtils.isExist(aClass, Controller.class)) {
        try {
          beans.add(aClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }

    }

    return beans;
  }

  public String getBeanName(Class<?> beanClass) {
    if (!AnnotationUtils.isExistOrByArray(beanClass, ANNOTATION)) {
      return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
    }
    Annotation controllerAnnotation = AnnotationUtils.getByArray(beanClass, ANNOTATION);
    String value = (String) AnnotationUtils.getValue(controllerAnnotation, "value");
    return value.isEmpty() ? BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName()) : value;
  }

}
