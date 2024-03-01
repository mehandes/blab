package org.blab.lambda.demo;

import java.io.FileNotFoundException;

public class Demo {
  public static void main(String[] args) throws FileNotFoundException, InterruptedException {
    String config = System.getProperty("user.home") + "/.config/blab/lambda/demo/demo.json";
    ConfigurationMonitor monitor = new FileConfigurationMonitor(config);
    Worker worker = new Worker(new Minimizer());
    monitor.subscribe(worker);
    worker.start();
  }
}
