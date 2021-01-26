package com.czx.framework.container.factory;

import com.czx.framework.container.BeanContainer;
import com.czx.framework.container.Module;
import com.czx.framework.utils.base.Assert;
import com.czx.framework.utils.base.BaseUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morgan
 * @date 2020/12/16 15:53
 */
public abstract class IocBeanFactory implements BeanFactory, Namer {

  private BeanContainer beanContainer = BeanContainer.create();

  public final void add(Class aClass) {
    Assert.isNull(createBean(), "当前" + aClass + "添加的bean为空！");
    createBean().forEach(bean -> {
      String id = getBeanName(bean.getClass());
      beanContainer.append(id, new Module(id, bean));
    });
  }

  @Override
  public List<Object> createBean() {
    return new ArrayList<>();
  }

  public String getBeanType(Class<?> beanClass) {
    return "component";
  }

  public String getBeanName(Class<?> beanClass) {
    return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
  }
}
