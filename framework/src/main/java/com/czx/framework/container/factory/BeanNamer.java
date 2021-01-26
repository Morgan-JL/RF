package com.czx.framework.container.factory;

import com.czx.framework.annotations.Component;
import com.czx.framework.annotations.Configuration;
import com.czx.framework.annotations.Repository;
import com.czx.framework.annotations.Service;
import com.czx.framework.utils.base.Assert;
import com.czx.framework.utils.base.BaseUtils;
import com.czx.framework.utils.reflect.AnnotationUtils;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 默认的起名器
 *
 * @author fk7075
 * @version 1.0.0
 * @date 2020/11/15 下午11:50
 */
public class BeanNamer implements Namer {

  private Class[] COMPONENT_ANNOTATION =
      new Class[]{Component.class, Repository.class, Service.class, Configuration.class};

  @Override
  public String getBeanName(Class<?> beanClass) {
    Annotation annotation = AnnotationUtils.getByArray(beanClass, COMPONENT_ANNOTATION);
    String value = (String) AnnotationUtils.getValue(annotation, "value");
    if (!Assert.isBlankString(value)) {
      return value;
    }
    return getDefBeanName(beanClass);
  }


  @Override
  public String getBeanType(Class<?> beanClass) {
    List<Component> components = AnnotationUtils.strengthenGet(beanClass, Component.class);
    if (Assert.isEmptyCollection(components)) {

    }
    if (components.size() != 1) {

    }
    return components.get(0).type();
  }

  protected String getDefBeanName(Class<?> beanClass) {
    return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
  }

}
