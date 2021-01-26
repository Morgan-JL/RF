package com.czx.framework.utils.reflect;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MethodUtils {


    private static final Method[] objectMethods=Object.class.getDeclaredMethods();

    /**
     * 判断方法是否为Object类的方法
     * @param method 待判断的方法
     * @return
     */
    public static boolean isObjectMethod(Method method){
        for (Method objectMethod : objectMethods) {
            if(method.equals(objectMethod)){
                return true;
            }
        }
        return false;
    }

    /**
     * 使用反射机制执行方法
     * @param targetObject 对象实例
     * @param method 要执行的方法的Method
     * @param params 方法执行所需要的参数
     * @return
     */
    public static Object invoke(Object targetObject,Method method,Object...params){
        try {
            method.setAccessible(true);
            return method.invoke(targetObject,params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法通过反射机制执行方法！ Method: "+method+", Object: "+targetObject+", Param: "+ Arrays.toString(params),e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("无法通过反射机制执行方法！ Method: "+method+", Object: "+targetObject+", Param: "+ Arrays.toString(params),e);
        }
    }

    /**
     * 使用反射机制执行方法
     * @param targetObject 对象实例
     * @param methodName 要执行的方法的方法名
     * @param params 方法执行所需要的参数
     * @return
     */
    public static Object invoke(Object targetObject,String methodName,Object[] params){
        try {
            Method method=targetObject.getClass().getMethod(methodName,ClassUtils.array2Class(params));
            return invoke(targetObject,method,params);
        } catch (NoSuchMethodException e) {
           throw new RuntimeException(e);
        }
    }

    /**
     * 使用反射机制执行方法
     * @param targetObject 对象实例
     * @param declaredMethodName 要执行的方法的方法名
     * @param params 方法执行所需要的参数
     * @return
     */
    public static Object invokeDeclaredMethod(Object targetObject,String declaredMethodName,Object[] params){
        try {
            Method method=targetObject.getClass().getDeclaredMethod(declaredMethodName,ClassUtils.array2Class(params));
            return invoke(targetObject,method,params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * interface Method : public void method(String name,Double price)
     *      ->
     * List[name,price]
     * 只有在编译参数-parameters开启后才能生效
     *
     * 通过ASM得到接口中方法的所有参数名
     * @param method 要操作的方法的Method
     * @return
     * @throws IOException
     */
    public static List<String> getInterfaceParamNames(Method method) throws IOException {
        return ASMUtil.getInterfaceMethodParamNames(method);
    }

    /**
     * class Method : public void method(String name,Double price)
     *      ->
     * String[] =[name,price]
     * 不依赖编译参数-parameters
     *
     * 通过ASM得到类方法的所有参数名
     * @param method 要操作的方法的Method
     * @return
     */
    public static String[] getClassParamNames(Method method){
        return ASMUtil.getMethodParamNames(method);
    }

    /**
     * 通过JDK8的Parameter类得到方法的所有参数名
     * @param method 要操作的方法的Method
     * @return
     */
    public static String[] getParamNamesByParameter(Method method){
        Parameter[] parameters = method.getParameters();
        String[] names=new String[parameters.length];
        for(int i=0;i<parameters.length;i++)
            names[i]=parameters[i].getName();
        return names;
    }


    /**
     * 接口方法
     * 得到由参数列表的参数名和参数值所组成的Map
     * @param method 接口方法
     * @param params 执行参数
     * @return
     * @throws IOException
     */
    public static Map<String,Object> getInterfaceMethodParamsNV(Method method,Object[] params) throws IOException {
        Map<String,Object> paramMap=new HashMap<>();
        List<String> interParams = ASMUtil.getInterfaceMethodParamNames(method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < interParams.size(); i++) {
            paramMap.put(ParameterUtils.getParamName(parameters[i],interParams.get(i)),params[i]);
        }
        return paramMap;
    }

    /**
     * 类方法
     * 得到由参数列表的参数名和参数值所组成的Map
     * @param method 类方法
     * @param params 执行参数
     * @return
     * @throws IOException
     */
    public static Map<String,Object> getClassMethodParamsNV(Method method, Object[] params) throws IOException {
        Map<String,Object> paramMap=new HashMap<>();
        String[] mparams = ASMUtil.getMethodParamNames(method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            paramMap.put(ParameterUtils.getParamName(parameters[i],mparams[i]),params[i]);
        }
        return paramMap;
    }

    /**
     * 兼容接口方法与类方法
     * 得到由参数列表的参数名和参数值所组成的Map
     * @param method 方法
     * @param params 执行参数
     * @return
     * @throws IOException
     */
    public static Map<String,Object> getMethodParamsNV(Method method, Object[] params) {
        try {
            Map<String, Object> interfaceMethodParamsNV = getInterfaceMethodParamsNV(method, params);
            return interfaceMethodParamsNV.isEmpty()?getClassMethodParamsNV(method,params):interfaceMethodParamsNV;
        } catch (IOException e) {
            throw new RuntimeException("获取`"+method+"`的参数列表时出现异常！",e);
        }
    }

    /**
     * 获取方法返回值类型
     * @param method 要操作的Method
     * @return
     */
    public static Class<?> getReturnType(Method method){
        return method.getReturnType();
    }

    /**
     * 获取方法返回值类型的泛型，如果返回值不包含泛型则返回null
     * @param method 要操作的Method
     * @return
     */
    public static Class<?>[] getReturnTypeGeneric(Method method){
        Type type = method.getGenericReturnType();
        return ClassUtils.getGenericType(type);
    }

    public static Method getMethod(Class<?> targetClass,String methodName,Class<?>...paramClasses){
        try {
            return targetClass.getMethod(methodName,paramClasses);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("在"+targetClass+"中找不到方法名为\""+methodName+"\"，参数列表为("+Arrays.toString(paramClasses)+")的方法",e);
        }
    }

    public static Method getDeclaredMethod(Class<?> targetClass,String methodName,Class<?>...paramClasses){
        try {
            return targetClass.getDeclaredMethod(methodName,paramClasses);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("在"+targetClass+"中找不到方法名为\""+methodName+"\"，参数列表为("+Arrays.toString(paramClasses)+")的方法",e);
        }
    }

    public static Method[] getMethods(Class<?> targetClass){
        return targetClass.getMethods();
    }

    public static Method[] getDeclaredMethods(Class<?> targetClass){
        return targetClass.getDeclaredMethods();
    }

    public static Parameter[] getParameter(Method method){
       return method.getParameters();
    }

    public static String getWithParamMethodName(Method method){
        StringBuilder methodName=new StringBuilder(method.getName()).append("(");
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type type : genericParameterTypes) {
            String typeName = type.getTypeName();
            //泛型参数
            if(type instanceof ParameterizedType){
                //java.util.Set<com.lucky.framework.container.Module>
                int divide = typeName.indexOf("<");
                String head=typeName.substring(0,divide);
                head=head.contains(".")?head.substring(head.lastIndexOf(".")+1):head;
                String[] split=typeName.substring(divide).split(",");
                StringBuilder tail=new StringBuilder();
                for (String s : split) {
                    s=s.contains(".")?s.substring(s.lastIndexOf(".")+1):s;
                    tail.append(s).append(" ");
                }
                methodName.append(head).append("<").append(tail.substring(0,tail.length()-1)).append(" ");
            }else{
                typeName=typeName.contains(".")?typeName.substring(typeName.lastIndexOf(".")+1):typeName;
                methodName.append(typeName).append(" ");
            }
        }
        return methodName.substring(0,methodName.length()-1)+")";
    }


}
