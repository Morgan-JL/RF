package com.czx.framework.scan;

import com.czx.framework.annotations.Await;
import com.czx.framework.annotations.Component;
import com.czx.framework.container.AwaitContainer;
import com.czx.framework.container.ClassContainer;
import com.czx.framework.utils.reflect.AnnotationUtils;

/**
 * @author Morgan
 * @date 2020/12/3 14:21
 */
public class Scanner {

  private static ClassContainer classContainer = ClassContainer.create();
  private static AwaitContainer awaitContainer = AwaitContainer.create();

  public static void scan(Class<?> clz) {
    if (clz.isAnnotation()) {
      return;
    }
    if (AnnotationUtils.strengthenIsExist(clz, Component.class)) {
      if (AnnotationUtils.strengthenIsExist(clz, Await.class)) {
        awaitContainer.append(clz.getSimpleName(), clz);
        return;
      }
      classContainer.append(clz.getSimpleName(), clz);
    }
  }

}
