package io.github.seujorgenochurras.utils;

public class StringUtils {
   private StringUtils() {
   }

   public static String subtractStrings(String firstString, String secondString) {
      return firstString.replace(secondString, "");
   }
}
