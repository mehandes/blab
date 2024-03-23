package org.blab.blender.registry;

import org.blab.blender.registry.controller.DefaultChannelController;
import org.blab.blender.registry.controller.DefaultSchemeController;
import org.blab.blender.registry.controller.DefaultServiceController;
import org.blab.blender.registry.repository.RepositoryFactory;

public class ControllerFactory {
  private static SchemeController schemeController;
  private static ChannelController channelController;
  private static ServiceController serviceController;

  public static SchemeController getSchemeController() {
    if (schemeController == null) init();
    return schemeController;
  }

  public static ChannelController getChannelController() {
    if (channelController == null) init();
    return channelController;
  }

  public static ServiceController getServiceController() {
    if (serviceController == null) init();
    return serviceController;
  }

  private static void init() {
    DefaultServiceController sr = new DefaultServiceController()
            .setServiceRepository(RepositoryFactory.getServiceRepository());

    DefaultSchemeController sc = new DefaultSchemeController()
            .setSchemeRepository(RepositoryFactory.getSchemeRepository());

    channelController = new DefaultChannelController()
            .setChannelRepository(RepositoryFactory.getChannelRepository())
            .setSchemeController(sc)
            .setServiceController(sr);

    sr.setChannelController(channelController);
    sc.setChannelController(channelController);

    serviceController = sr;
    schemeController = sc;
  }
}
