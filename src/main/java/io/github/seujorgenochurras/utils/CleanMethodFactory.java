package io.github.seujorgenochurras.utils;

import static io.github.seujorgenochurras.utils.StringUtils.subtractStrings;

public class CleanMethodFactory {
   private final String dirtyMethodDeclaration;

   private String cleanMethod = "";

   private CleanMethodFactory(String dirtyMethodDeclaration) {
      this.dirtyMethodDeclaration = dirtyMethodDeclaration;
   }

   public static CleanMethodFactory of(String dirtyMethodDeclaration) {
      return new CleanMethodFactory(dirtyMethodDeclaration);
   }

   public CleanMethodFactory addScopeOfMethod() {
      String methodScopeRegex = "(public|protected|private)";
      String methodDeclarationWithoutScope = dirtyMethodDeclaration.replaceAll(methodScopeRegex, "");
      this.cleanMethod = this.cleanMethod.concat(subtractStrings(dirtyMethodDeclaration, methodDeclarationWithoutScope));
      return this;
   }

   public CleanMethodFactory addStaticIdentifiers() {
      if (dirtyMethodDeclaration.contains("static")) {
         cleanMethod = cleanMethod.concat(" static");
      }
      return this;
   }

   public String build() {
      return this.toString();
   }

   @Override
   public String toString() {
      return cleanMethod;
   }
}
