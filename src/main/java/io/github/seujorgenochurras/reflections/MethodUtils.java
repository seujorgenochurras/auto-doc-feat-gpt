package io.github.seujorgenochurras.reflections;

import java.lang.reflect.Method;

public class MethodUtils {
   private MethodUtils() {
   }

   public static Method ofString(String fullMethod){

      return Method.class.getMethods()[0];
   }
   public static String getMethodCode(Method method){
      return "";
   }
}
