package org.blab.lambda.demo;

public class Demo {
  public static void main(String[] args) {
    String config =
        System.getProperty("user.home") + "/Projects/.config/blab/lambda/demo/demo.json";
    ConfigurationMonitor monitor = new FileConfigurationMonitor(config);
    Worker worker = new Worker(new Minimizer());
    monitor.subscribe(worker);
    worker.start();
  }
}
