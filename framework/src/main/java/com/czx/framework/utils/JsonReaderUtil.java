package com.czx.framework.utils;

import com.alibaba.fastjson.JSON;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Scanner;

/**
 * json文件读取工具类
 *
 * @author Morgan
 * @date 2020/11/11 18:42
 */
public class JsonReaderUtil {

  public static String readJsonFile(String fileName) {
    String jsonStr = "";
    try {
      File jsonFile = new File(fileName);
      FileReader fileReader = new FileReader(jsonFile);
      Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
      int ch = 0;
      StringBuffer sb = new StringBuffer();
      while ((ch = reader.read()) != -1) {
        sb.append((char) ch);
      }
      fileReader.close();
      reader.close();
      jsonStr = sb.toString();
      return jsonStr;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }



  public static void writeJsonFile(String fileName, String content) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
      out.write(content);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
  /*  String jsonStr = JsonReaderUtil.readJsonFile("jarPath.json");
    JSONObject object = JSON.parseObject(jsonStr);
    Map jsonMap = (Map) JSON.parse(jsonStr);

    System.out.println(object.put("2", "12"));
    writeJsonFile("jarPath.json", object.toString());*/
    String jsonStr = JsonReaderUtil
        .readJsonFile("F:\\rick\\web\\src\\main\\resources\\jarPath.json");
    System.out.println((Map) JSON.parse(jsonStr));
  }

}
