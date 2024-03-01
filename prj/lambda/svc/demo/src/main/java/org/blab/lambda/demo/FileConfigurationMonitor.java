package org.blab.lambda.demo;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class FileConfigurationMonitor implements ConfigurationMonitor {
  private final String path;
  private final Set<Consumer<Configuration>> consumers;
  private final Monitor monitor;

  private Configuration last = null;

  public FileConfigurationMonitor(String path) {
    this.path = path;
    this.consumers = new HashSet<>();
    this.monitor = new Monitor();

    this.monitor.start();
  }

  @Override
  public void subscribe(Consumer<Configuration> c) {
    consumers.add(c);
  }

  @Override
  public void close() {
    monitor.interrupt();
  }

  class Monitor extends Thread {
    @Override
    public void run() {
      while (true) {
        try {
          Configuration configuration =
              new Gson().fromJson(new FileReader(path), Configuration.class);

          if (!configuration.equals(last)) consumers.forEach(c -> c.accept((last = configuration)));

          Thread.sleep(2000);
        } catch (Exception ignored) {
        }
      }
    }
  }
}
