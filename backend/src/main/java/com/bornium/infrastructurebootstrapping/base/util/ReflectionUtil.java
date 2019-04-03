package com.bornium.infrastructurebootstrapping.base.util;

import java.lang.reflect.ParameterizedType;

public class ReflectionUtil {
    public static Class getGenericTypeName(Class clazz) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();

        for (int i = 0; i < stack.length; i++) {
            try {
                if (Class.forName(stack[i].getClassName()) == clazz) {
                    for(int j = i+1; j < stack.length;j++){
                        Class c = Class.forName(stack[j].getClassName());
                        String genericTypeName = ((ParameterizedType)c.getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
                        try{
                            return Class.forName(genericTypeName);
                        }catch (ClassNotFoundException e){
                            continue;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Wrong usage of this method. Can only be used for subclasses that specify T as a concrete type.");
    }
}
