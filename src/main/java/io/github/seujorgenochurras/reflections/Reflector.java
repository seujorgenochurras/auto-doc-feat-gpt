package io.github.seujorgenochurras.reflections;


import io.github.seujorgenochurras.utils.MethodUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class Reflector {
   private Reflector() {
   }

   public static void reflect() throws FileNotFoundException {
      Reflections reflections = new Reflections(new ConfigurationBuilder()
              .setScanners(Scanners.SubTypes.filterResultsBy(c -> true))
              .forPackage("io.github.seujorgenochurras"));

      Set<Class<?>> classSet = reflections.getSubTypesOf(Object.class);
      for (Class<?> clazz : classSet) {
         ClassFileReader classFileReader = new ClassFileReader(clazz);
         System.out.println(clazz.getSimpleName());
         for (String methodCode : classFileReader.readMethods()) {
            System.out.println(methodCode + "\n");
         }
         System.out.println("\n\n\n\n");
      }
   }

   private static class ClassFileReader {
      private final Class<?> rootClass;

      public ClassFileReader(Class<?> rootClass) {
         this.rootClass = rootClass;
      }

      public String readClass() throws FileNotFoundException {
         String classDirectoryStructure = getClassDirectoryStructure();

         return getClassCodeByPath(classDirectoryStructure);
      }

      private String getClassCodeByPath(String classPath) throws FileNotFoundException {

         FileReader fileReader = new FileReader(classPath);
         Scanner scanner = new Scanner(fileReader);

         String classCode = "";
         while (scanner.hasNext()) {
            classCode = classCode.concat(scanner.next() + " ");
         }
         scanner.close();
         return classCode;
      }

      public Set<String> readMethods() throws FileNotFoundException {
         return new MethodReader(this).readMethods();
      }

      public String getClassDirectoryStructure() {
         String classPackageAndName = rootClass.toString();
         classPackageAndName = removeClassNameFromPackageIfIsInnerClass(classPackageAndName);
         return "src/main/java/".concat(classPackageAndName
                         .replace("class ", "")
                         .replace(".", "/"))
                 .concat(".java");

      }

      private String removeClassNameFromPackageIfIsInnerClass(String className) {
         String innerClassSeparator = "$";
         if (!className.contains(innerClassSeparator)) return className;
         String anyThingAfterClassSeparatorRegex = "\\$(.*)";
         return className.replaceAll(anyThingAfterClassSeparatorRegex, "");
      }

      private static class MethodReader {
         private final ClassFileReader classFileReader;
         private final String fullClassCode;

         public MethodReader(ClassFileReader classFileReader) throws FileNotFoundException {
            this.classFileReader = classFileReader;
            fullClassCode = classFileReader.readClass();
         }

         public Set<String> readMethods() {
            Set<String> methodCodes = new HashSet<>();
            HashMap<Integer, Integer> startAndEndOfMethods = getStartAndEndOfMethods();
            startAndEndOfMethods.forEach((startIndex, endIndex) -> {
               String methodCode = fullClassCode.substring(startIndex, endIndex);
               methodCodes.add(methodCode);
            });

            return methodCodes;
         }

         private HashMap<Integer, Integer> getStartAndEndOfMethods() {
            Set<String> methodDeclarations = getMethodsDeclarations();
            HashMap<Integer, Integer> startAndEndOfMethods = new HashMap<>();

            for (String declaration : methodDeclarations) {
               int methodStartsAt = fullClassCode.indexOf(declaration);
               if (methodStartsAt == -1) continue;
               int methodEndsAt = getEndOfMethodBody(methodStartsAt) + methodStartsAt;
               startAndEndOfMethods.put(methodStartsAt, methodEndsAt);
            }
            return startAndEndOfMethods;
         }

         private int getEndOfMethodBody(int methodStartsAt) {
            String classCodeWhereMethodStarts = fullClassCode.substring(methodStartsAt);
            String[] methodStartCodeLetters = classCodeWhereMethodStarts.split("");

            int bracketsAmount = 0;
            int endOfMethod = 0;
            boolean hasFinishedReadingParams = false; //the method comes without params
            for (String letter : methodStartCodeLetters) {
               endOfMethod++;
               if (letter.equals("{") || letter.equals("}")) {
                  if (letter.equals("{")) {
                     if (!hasFinishedReadingParams) hasFinishedReadingParams = true;
                     bracketsAmount++;
                  } else bracketsAmount--;

                  continue;
               }
               if (bracketsAmount == 0 && hasFinishedReadingParams) break;
            }
            return endOfMethod;
         }

         private Set<String> getMethodsDeclarations() {
            Set<String> declarations = new LinkedHashSet<>();
            Method[] rawClassMethods = classFileReader.rootClass.getMethods();

            for (Method method : rawClassMethods) {
               String methodDeclaration = MethodUtils.getMethodDeclaration(method);
               declarations.add(methodDeclaration);
            }
            return declarations;
         }
      }
   }
}

