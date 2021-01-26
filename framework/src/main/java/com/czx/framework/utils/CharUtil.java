package com.czx.framework.utils;

/**
 * @author Morgan
 * @date 2020/12/4 17:00
 */
public class CharUtil {

  /**
   * 去除首尾指定字符
   *
   * @param str 字符串
   * @param element 指定字符
   */
  public static String trim(String str, String element) {
    if (str == null || str.equals("")) {
      return str;
    }
    boolean beginIndexFlag = true;
    boolean endIndexFlag = true;
    do {
      int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
      int endIndex =
          str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
      str = str.substring(beginIndex, endIndex);
      beginIndexFlag = (str.indexOf(element) == 0);
      endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
    } while (beginIndexFlag || endIndexFlag);
    return str;
  }


  /**
   * 首字母转小写
   */
  public static String toLowerCaseFirstOne(String s) {
    if (Character.isLowerCase(s.charAt(0))) {
      return s;
    } else {
      return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1))
          .toString();
    }
  }

}
