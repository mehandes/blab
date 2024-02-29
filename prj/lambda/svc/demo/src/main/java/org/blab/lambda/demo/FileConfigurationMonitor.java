package org.blab.lambda.demo;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class FileConfigurationMonitor implements ConfigurationMonitor {
  private final File file;
  private final Set<Consumer<Configuration>> consumers;
  private final Monitor monitor;

  public FileConfigurationMonitor(File file) {
    this.file = file;
    this.consumers = new HashSet<>();
    this.monitor = new Monitor();

    this.monitor.start();
  }

  @Override
  public void subscribe(Consumer<Configuration> c) {
    consumers.add(c);
  }

  @Override
  public void close() {}

  class Monitor extends Thread {
    @Override
    public void run() {}
  }
}
