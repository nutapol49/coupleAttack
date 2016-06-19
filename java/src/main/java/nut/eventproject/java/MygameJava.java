package nut.eventproject.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import nut.eventproject.core.Mygame;

public class MygameJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new Mygame());
  }
}
