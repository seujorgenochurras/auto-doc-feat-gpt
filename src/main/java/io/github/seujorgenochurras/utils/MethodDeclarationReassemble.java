package io.github.seujorgenochurras.utils;

import static io.github.seujorgenochurras.utils.StringUtils.subtractStrings;


public class MethodDeclarationReassemble {
   private final String initialMethodDeclaration;

   private String methodReassembled = "";

   private MethodDeclarationReassemble(String initialMethodDeclaration) {
      this.initialMethodDeclaration = initialMethodDeclaration;
   }

   /**
    * A factory class that disassembles a method and let your build your own method based on the method "pieces"
    */
   public static MethodDeclarationReassemble of(String dirtyMethodDeclaration) {
      return new MethodDeclarationReassemble(dirtyMethodDeclaration);
   }

   public MethodDeclarationReassemble addScopeOfMethod() {
      String methodScopeRegex = "(public|protected|private)";
      String methodDeclarationWithoutScope = initialMethodDeclaration.replaceAll(methodScopeRegex, "");
      this.methodReassembled = this.methodReassembled.concat(subtractStrings(initialMethodDeclaration, methodDeclarationWithoutScope));
      return this;
   }

   public MethodDeclarationReassemble addStaticIdentifiers() {
      if (initialMethodDeclaration.contains("static")) {
         methodReassembled = methodReassembled.concat(" static");
      }
      return this;
   }

   public String build() {
      return this.toString();
   }

   @Override
   public String toString() {
      return methodReassembled;
   }
}
