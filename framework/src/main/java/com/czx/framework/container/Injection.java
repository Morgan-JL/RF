package com.czx.framework.container;

import com.czx.framework.annotations.Inject;
import com.czx.framework.exception.BeanNotFindException;
import com.czx.framework.exception.ClassBeanNotFindException;
import com.czx.framework.utils.reflect.AnnotationUtils;
import com.czx.framework.utils.reflect.ClassUtils;
import com.czx.framework.utils.reflect.FieldUtils;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 依赖注入
 *
 * @author Morgan
 * @date 2020/12/18 15:48
 */
public class Injection {

  private static BeanContainer beanContainer = BeanContainer.create();

  //依赖注入
  public static void inject(Object obj) {
    Class<?> aClass = obj.getClass();
    Field[] allFields = ClassUtils.getAllFields(aClass);
    for (Field field : allFields) {
      Class<?> fieldType = field.getType();
      if (field.isAnnotationPresent(Inject.class)) {
        Inject inject = AnnotationUtils.get(field, Inject.class);
        String confId = inject.value();
        if ("".equals(confId)) {
          boolean flag = false;
          //类型匹配
          Collection<Module> modules = beanContainer.getBeanContainerMap().values();
          for (Module module : modules) {
            Class<?> beanClass = module.getBean().getClass();
            if (fieldType.isAssignableFrom(beanClass)) {
              FieldUtils.setValue(obj, field, module.getBean());
              flag = true;
              break;
            }
          }
          if (!flag) {
            throw new ClassBeanNotFindException("`" + fieldType + "`依赖注入失败！");
          }
        } else {
          //ID匹配
          if (beanContainer.getBean(confId) == null) {
            throw new BeanNotFindException("`" + confId + "`依赖注入失败！");
          }
          FieldUtils.setValue(obj, field, beanContainer.getBean(confId));
        }
      }
    }
  }

}
