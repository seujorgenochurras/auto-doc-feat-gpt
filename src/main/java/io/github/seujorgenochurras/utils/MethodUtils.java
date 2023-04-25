package io.github.seujorgenochurras.utils;

import java.lang.reflect.Method;

public class MethodUtils {
   private MethodUtils() {
   }

   public static String getMethodDeclaration(Method method) {
      String declarationAccessModifiers = getMethodAccessModifiers(method);

      return declarationAccessModifiers.concat(" ")
              .concat(method.getReturnType().getSimpleName() + " ")
              .concat(method.getName());
   }
   private static String getMethodAccessModifiers(Method method) {
      String methodDeclaration = method.toGenericString();

      return MethodDeclarationReassemble.of(methodDeclaration)
              .addScopeOfMethod()
              .addStaticIdentifiers()
              .build();
   }

}

