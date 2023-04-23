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
   private static String getMethodAccessModifiers(Method dirtyMethod) {
      String dirtyMethodDeclaration = dirtyMethod.toGenericString();
      return CleanMethodFactory.of(dirtyMethodDeclaration)
              .addScopeOfMethod()
              .addStaticIdentifiers()
              .build();
   }

}

