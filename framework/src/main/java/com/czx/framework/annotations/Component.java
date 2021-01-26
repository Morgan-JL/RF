package com.czx.framework.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Morgan
 * @date 2020/12/8 14:06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Component{


  /**
   * 为该Component组件指定一个唯一ID，默认会使用[首字母小写类名]作为组件的唯一ID
   * @return 组件的ID
   */
  String value() default "";

  /**
   * 定义该组件的类型
   * @return 组件类型
   */
  String type() default "component";
}
