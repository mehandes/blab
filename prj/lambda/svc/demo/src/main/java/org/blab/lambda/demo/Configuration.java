package org.blab.lambda.demo;

import java.util.List;

public class Configuration {
  private List<Frame> frames;

  public static class Frame {
    private String lade;
    private Type type;
    private double beta;
    private double etta;

    public enum Type {
      HORIZONTAL,
      VERTICAL
    }
  }
}
