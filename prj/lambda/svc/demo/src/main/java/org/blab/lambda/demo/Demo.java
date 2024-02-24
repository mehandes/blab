package org.blab.lambda.demo;

import java.io.File;

public class Demo {
  public static void main(String[] args) {
    Worker worker = new Worker(new Minimizer());
    ConfigurationMonitor monitor = new FileConfigurationMonitor(new File("demo.yaml"));
    monitor.subscribe(worker);
    worker.start();
  }
}
