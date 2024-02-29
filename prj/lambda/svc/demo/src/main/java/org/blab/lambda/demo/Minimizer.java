package org.blab.lambda.demo;

import org.apache.commons.math3.fitting.leastsquares.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.blab.river.Event;
import java.util.List;

public class Minimizer implements Lambda<Event, List<Event>> {
  private Configuration configuration;
  private Function function;
  private LeastSquaresOptimizer optimizer;

  @Override
  public List<Event> apply(Event event) {
    LeastSquaresProblem problem =
        new LeastSquaresBuilder()
            .start(new double[] {1, 2}) // Configured
            .model(function) // Calculated
            .target(new double[] {}) // Estimated
            .weight(null) // Configured
            .maxEvaluations(1000) // Configured
            .maxIterations(1000) // Configured
            .build();

    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
    LeastSquaresOptimizer.Optimum solution = optimizer.optimize(problem);

    return null;
  }

  @Override
  public void refresh(Configuration p) {
    configuration = p;
  }

  class Function implements MultivariateJacobianFunction {
    @Override
    public Pair<RealVector, RealMatrix> value(RealVector realVector) {
      return null;
    }
  }
}
