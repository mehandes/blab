package org.blab.lambda.demo;

import java.util.List;
import java.util.Objects;

public record Configuration(
    String hostname,
    int port,
    List<Frame> frames,
    double sx,
    double sy,
    double maxIterations,
    double maxEvaluations) {
  public record Frame(String lade, double beta, double etta, double weight) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Frame frame = (Frame) o;
      return Double.compare(beta, frame.beta) == 0
          && Double.compare(etta, frame.etta) == 0
          && Double.compare(weight, frame.weight) == 0
          && Objects.equals(lade, frame.lade);
    }

    @Override
    public int hashCode() {
      return Objects.hash(lade, beta, etta, weight);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Configuration that = (Configuration) o;
    return port == that.port
        && Double.compare(sx, that.sx) == 0
        && Double.compare(sy, that.sy) == 0
        && Double.compare(maxIterations, that.maxIterations) == 0
        && Double.compare(maxEvaluations, that.maxEvaluations) == 0
        && Objects.equals(hostname, that.hostname)
        && listEquals(frames, that.frames);
  }

  private static <T> boolean listEquals(List<T> l1, List<T> l2) {
    if (l1.size() != l2.size()) return false;
    for (int i = 0; i < l1.size(); i++) if (!l1.get(i).equals(l2.get(i))) return false;
    return true;
  }
}
