package org.blab.lambda.demo;

import java.util.function.Consumer;

public interface ConfigurationMonitor {
  void subscribe(Consumer<Configuration> c);

  void close();
}
