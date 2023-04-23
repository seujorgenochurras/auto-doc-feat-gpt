package io.github.seujorgenochurras.reflections;


import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.io.*;
import java.util.*;

public class Reflector {
   private Reflector() {
   }

   public static void reflect() throws IOException {
      Reflections reflections = new Reflections(new ConfigurationBuilder()
              .setScanners(Scanners.SubTypes.filterResultsBy(c -> true))
              .forPackage("io.github.seujorgenochurras"));

      Set<Class<?>> classSet = reflections.getSubTypesOf(Object.class);
      for (Class<?> clazz : classSet) {
         ClassFileReader classFileReader = new ClassFileReader(clazz);
         System.out.println(classFileReader.readClass());
         System.out.println(classFileReader.readMethods());
      }
   }
   private static class ClassFileReader{
      private final Class<?> aClass;
      public ClassFileReader(Class<?> aClass) {
         this.aClass = aClass;
      }
      public String readClass() throws FileNotFoundException {
         String classDirectoryStructure = getClassDirectoryStructure();
         if(classDirectoryStructure.contains("$")) return "";

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


      private String getClassDirectoryStructure(){
         String classPackageAndName = aClass.toString();
         return "src/main/java/".concat(classPackageAndName
                 .replace("class ", "")
                 .replace(".","/"))
                 .concat(".java");

      }

      private static class MethodReader{
         private final ClassFileReader classFileReader;

         public MethodReader(ClassFileReader classFileReader) {
            this.classFileReader = classFileReader;
         }

         public Set<String> readMethods() throws FileNotFoundException {
            String fullClassCode = classFileReader.readClass();

            return Set.of("");
         }
      }
   }
}
