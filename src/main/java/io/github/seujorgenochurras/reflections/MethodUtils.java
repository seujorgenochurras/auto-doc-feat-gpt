package io.github.seujorgenochurras.reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class MethodUtils {
   private MethodUtils() {
   }

   public static Method ofString(String fullMethod){

      return Method.class.getMethods()[0];
   }
   public static String getMethodCode(Method method){
      return "";
   }

   public static String getMethodDeclaration(Method method) {
      String rawDeclaration = method.toGenericString();
      String declarationWithoutAccessModifiers = rawDeclaration.replaceAll("(public|protected|private)(?:\\s+static)?", "");
      String declarationAccessModifiers = "";
      String methodDeclaration = "";
      if (!rawDeclaration.equals(declarationWithoutAccessModifiers)) {
         declarationAccessModifiers = rawDeclaration.replace(declarationWithoutAccessModifiers, "");
      }
      methodDeclaration = methodDeclaration
              .concat(declarationAccessModifiers + " ")
              .concat(method.getReturnType().getSimpleName() + " ")
              .concat(method.getName() + " ");
      if (method.getParameterTypes().length != 0) {
         AtomicReference<String> atomicReference = new AtomicReference<>();
         Arrays.stream(method.getTypeParameters()).forEach(param -> {
         });
      }
      return methodDeclaration;
   }
}

