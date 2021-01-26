package com.czx.framework.container;


import com.czx.framework.annotations.Bean;
import com.czx.framework.annotations.Configuration;
import com.czx.framework.container.factory.BeanFactory;
import com.czx.framework.container.factory.BeanNamer;
import com.czx.framework.container.factory.IocBeanFactory;
import com.czx.framework.container.factory.Namer;
import com.czx.framework.utils.BaseUtils;
import com.czx.framework.utils.reflect.AnnotationUtils;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 * 注册机
 *
 * @author Morgan
 * @date 2020/12/16 16:47
 */
public class RegisterMachine {

  private static RegisterMachine registerMachine;

  private static BeanContainer beanContainer;

  private static ConfigureContainer configureContainer;

  private Namer namer = new BeanNamer();

  public RegisterMachine() {
    beanContainer = BeanContainer.create();
    configureContainer = ConfigureContainer.create();
  }

  public static RegisterMachine create() {
    if (registerMachine == null) {
      registerMachine = new RegisterMachine();
    }
    return registerMachine;
  }

  public void init() {
    register();
    injection();
  }

  public void register() {
    ClassContainer classContainer = ClassContainer.create();
    Collection<Class> values = classContainer.getClassContainerMap().values();

    // 普通的组件[@Component]加载到ioc
    for (Class aClass : values) {
      try {
        // 接口和抽象类无法被实例化
        if (aClass.isInterface() || Modifier
            .isAbstract(aClass.getModifiers())) {
          continue;
        }
        // 配置类组件[@Configuration]加载到ioc
        if (AnnotationUtils.isExist(aClass, Configuration.class)) {
          configureContainer.append(aClass.getSimpleName(), aClass);
          continue;
        }
        String id = namer.getBeanName(aClass);
        beanContainer.append(id, new Module(id, aClass.newInstance()));
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    // 继承自IocBeanFactory加载到ioc
    for (BeanFactory factory : ServiceLoader.load(BeanFactory.class)) {
      IocBeanFactory beanFactory = null;
      try {
        beanFactory = (IocBeanFactory) factory.getClass().newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
      beanFactory.add(factory.getClass());
    }
  }

  //依赖注入
  public void injection() {
    Collection<Module> modules = beanContainer.getBeanContainerMap().values();
    for (Module module : modules) {
      Injection.inject(module.getBean());
    }
  }

  public String getBeanName(Class<?> beanClass) {
    Namer namer = new Namer() {
      public String getBeanName(Class<?> beanClass) {
        return BaseUtils.lowercaseFirstLetter(beanClass.getSimpleName());
      }

      public String getBeanType(Class<?> beanClass) {
        return "component";
      }
    };

    return namer.getBeanName(beanClass);
  }
}
