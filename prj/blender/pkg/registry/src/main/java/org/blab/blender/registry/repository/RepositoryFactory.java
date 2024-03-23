package org.blab.blender.registry.repository;

import org.blab.blender.registry.repository.sql.DataSourceFactory;
import org.blab.blender.registry.repository.sql.SQLChannelRepository;
import org.blab.blender.registry.repository.sql.SQLSchemeRepository;
import org.blab.blender.registry.repository.sql.SQLServiceRepository;

public class RepositoryFactory {
  private static SchemeRepository schemeRepository;
  private static ChannelRepository channelRepository;
  private static ServiceRepository serviceRepository;

  public static SchemeRepository getSchemeRepository() {
    if (schemeRepository == null) init();
    return schemeRepository;
  }

  public static ChannelRepository getChannelRepository() {
    if (channelRepository == null) init();
    return channelRepository;
  }

  public static ServiceRepository getServiceRepository() {
    if (serviceRepository == null) init();
    return serviceRepository;
  }

  private static void init() {
    schemeRepository = new SQLSchemeRepository(DataSourceFactory.getDataSource());
    channelRepository = new SQLChannelRepository(DataSourceFactory.getDataSource());
    serviceRepository = new SQLServiceRepository(DataSourceFactory.getDataSource());
  }
}
