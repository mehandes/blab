package org.blab.lambda.demo;

import org.apache.commons.math3.fitting.leastsquares.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.blab.river.Event;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Minimizer implements Lambda<Event, List<Event>> {
  private final Function function;
  private final LeastSquaresOptimizer optimizer;

  private HashMap<String, Double> targets;
  private Configuration configuration;

  public Minimizer() {
    targets = new HashMap<>();
    function = new Function();
    optimizer =
        new LevenbergMarquardtOptimizer()
            .withCostRelativeTolerance(1.0e-12)
            .withParameterRelativeTolerance(1.0e-12);
  }

  @Override
  public List<Event> apply(Event event) {
    if (configuration != null
        && extractTarget(event)
        && targets.size() == configuration.frames().size()) {

      LeastSquaresProblem problem =
          new LeastSquaresBuilder()
              .model(function)
              .target(collectTargets())
              .start(new double[] {configuration.sx(), configuration.sy()})
              .maxIterations(100)
              .maxEvaluations(100)
              .lazyEvaluation(false)
              .build();

      LeastSquaresOptimizer.Optimum solution = optimizer.optimize(problem);

      return List.of(
          new Event(
              "calc.cams.em",
              String.valueOf(solution.getPoint().getEntry(0) * solution.getPoint().getEntry(0)).getBytes(StandardCharsets.US_ASCII)),
          new Event(
              "calc.cams.es",
              String.valueOf(solution.getPoint().getEntry(1) * solution.getPoint().getEntry(1)).getBytes(StandardCharsets.US_ASCII)));
    }

    return Collections.emptyList();
  }

  private double[] collectTargets() {
    double[] t = new double[configuration.frames().size()];

    for (int i = 0; i < configuration.frames().size(); ++i) {
      double j = targets.get(configuration.frames().get(i).lade());
      t[i] = j * j;
    }

    return t;
  }

  private boolean extractTarget(Event event) {
    try {
      Double value =
          Double.parseDouble(
              Arrays.stream(new String(event.message(), StandardCharsets.US_ASCII).split("\\|"))
                  .map(s -> s.split(":"))
                  .collect(Collectors.toMap(s -> s[0], s -> s[1]))
                  .get("val"));

      targets.put(event.lade(), value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  @Override
  public void refresh(Configuration p) {
    targets = new HashMap<>();
    configuration = p;
  }

  class Function implements MultivariateJacobianFunction {
    @Override
    public Pair<RealVector, RealMatrix> value(RealVector realVector) {
      double[] values = new double[configuration.frames().size()];
      double[][] derivatives = new double[configuration.frames().size()][2];

      for (int i = 0; i < configuration.frames().size(); i++) {
        Configuration.Frame f = configuration.frames().get(i);

        values[i] =
            realVector.getEntry(0) * f.beta()
                + (realVector.getEntry(1) * f.etta() * realVector.getEntry(1) * f.etta());
        derivatives[i][0] = f.beta();
        derivatives[i][1] = f.etta() * f.etta() * 2.0 * realVector.getEntry(1);
      }

      return Pair.create(new ArrayRealVector(values), new Array2DRowRealMatrix(derivatives));
    }
  }
}
