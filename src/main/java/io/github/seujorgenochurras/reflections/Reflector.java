package io.github.seujorgenochurras.reflections;


import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Reflector {
   private Reflector() {
   }

   public static void reflect() throws IOException {
      Reflections reflections = new Reflections(new ConfigurationBuilder()
              .setScanners(Scanners.SubTypes.filterResultsBy(c -> true))
              .forPackage("io.github.seujorgenochurras"));

      Set<Class<?>> classSet = reflections.getSubTypesOf(Object.class);
      for (Class<?> clazz : classSet) {
         String methodDeclaration = MethodUtils.getMethodDeclaration(clazz.getMethods()[0]);
         System.out.println(methodDeclaration);
      }
   }

   private static class MethodDeclarationStringCleaner {
      private final Method dirtyMethodDeclaration;

      private final Set<Supplier<String>> stringsToReplace = new HashSet<>();

      private String dirtyMethodDeclarationToBeCleaned;

      private MethodDeclarationStringCleaner(Method dirtyMethodDeclaration) {
         this.dirtyMethodDeclaration = dirtyMethodDeclaration;
         this.dirtyMethodDeclarationToBeCleaned = dirtyMethodDeclaration.toGenericString();
      }

      public static MethodDeclarationStringCleaner ofMethod(Method dirtyMethod) {
         return new MethodDeclarationStringCleaner(dirtyMethod);
      }

      public MethodDeclarationStringCleaner removePackageNameFromReturnType() {
         Class<?> returnType = dirtyMethodDeclaration.getReturnType();
         String returnTypePackageName = returnType.getPackageName();
         String diamondInsideTypePackageName = dirtyMethodDeclaration.getGenericReturnType().getTypeName();
         stringsToReplace.add(() -> returnTypePackageName);
         return this;
      }
   }

   private static class ClassFileReader {
      private final Class<?> rootClass;

      public ClassFileReader(Class<?> rootClass) {
         this.rootClass = rootClass;
      }

      public String readClass() throws FileNotFoundException {
         String classDirectoryStructure = getClassDirectoryStructure();
         if (classDirectoryStructure.contains("$")) return "";
         FileReader fileReader = new FileReader(classDirectoryStructure);
         Scanner scanner = new Scanner(fileReader);

         String classAsString = "";
         while (scanner.hasNext()) {
            classAsString = classAsString.concat(scanner.next() + " ");
         }
         scanner.close();
         return classAsString;
      }

      public Set<String> readMethods() throws FileNotFoundException {
         return new MethodReader(this).readMethods();
      }

      public String getClassDirectoryStructure() {
         String classPackageAndName = rootClass.toString();
         return "src/main/java/".concat(classPackageAndName
                         .replace("class ", "")
                         .replace(".", "/"))
                 .concat(".java");

      }

      private static class MethodReader {
         private final ClassFileReader classFileReader;
         private final String classFullCode;

         public MethodReader(ClassFileReader classFileReader) throws FileNotFoundException {
            this.classFileReader = classFileReader;
            classFullCode = classFileReader.readClass();
         }

         public Set<String> readMethods() throws FileNotFoundException {
            String fullClassCode = classFileReader.readClass();
            fullClassCode.indexOf("a");
            return Set.of("");
         }

         private int getIndexOfMethodInClasCode(String classCode, String methodInitiator) {
            return classCode.indexOf(methodInitiator);
         }

         private Set<String> getAllMethodInitCodes() {
            return Arrays.stream(classFileReader.rootClass.getMethods())
                    .map(this::getMethodInitiatorCode).collect(Collectors.toSet());
         }

         private String getMethodInitiatorCode(Method method) {
            return method.toGenericString();
         }
      }
   }
}

