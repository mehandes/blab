package org.blab.lambda.demo;

import java.util.List;

public record Configuration(String hostname, int port, List<Frame> frames) {
  public record Frame(String lade, Type type, double beta, double etta) {
    public enum Type {
      HORIZONTAL,
      VERTICAL
    }
  }
}
