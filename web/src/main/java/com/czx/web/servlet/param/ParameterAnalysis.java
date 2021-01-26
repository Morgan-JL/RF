package com.czx.web.servlet.param;

import com.czx.framework.utils.reflect.ParameterUtils;
import com.czx.web.servlet.Model;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * @author Morgan
 * @date 2020/12/1 15:58
 */
public interface ParameterAnalysis {

    boolean can(Model model, Method method, Parameter parameter, String asmParam);

    Object analysis(Model model, Method method, Parameter parameter, Type genericType,String asmParam)
        throws IOException;

    default String getParamName(Parameter parameter, String asmParam){
        return ParameterUtils.getParamName(parameter,asmParam);
    }

}
