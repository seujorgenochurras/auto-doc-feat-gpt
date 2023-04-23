package io.github.seujorgenochurras;

import io.github.seujorgenochurras.reflections.Reflector;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;

public class Main {
   public static void main(String[] args) throws IOException {
      Reflector.reflect();
   }
}