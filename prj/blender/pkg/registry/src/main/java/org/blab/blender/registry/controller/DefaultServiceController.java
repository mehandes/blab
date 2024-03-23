package org.blab.blender.registry.controller;

import org.blab.blender.registry.*;
import org.blab.blender.registry.repository.ServiceRepository;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DefaultServiceController implements ServiceController {
  private ChannelController channelController;
  private ServiceRepository serviceRepository;

  public DefaultServiceController setChannelController(ChannelController channelController) {
    this.channelController = channelController;
    return this;
  }

  public DefaultServiceController setServiceRepository(ServiceRepository serviceRepository) {
    this.serviceRepository = serviceRepository;
    return this;
  }

  @Override
  public Set<Service> findAll() {
    try {
      return serviceRepository.findAll();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Set<Service> findByConfigurationChannel(String channelTopic) {
    if (Objects.isNull(channelTopic)) throw new NullPointerException();

    try {
      return serviceRepository.findByConfigurationChannel(channelTopic);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Optional<Service> findById(UUID id) {
    if (Objects.isNull(id)) throw new NullPointerException();

    try {
      return serviceRepository.findById(id);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Optional<Service> findByName(String name) {
    if (Objects.isNull(name)) throw new NullPointerException();

    try {
      return serviceRepository.findByName(name);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Optional<Service> findByOutputChannel(String channelTopic) {
    if (Objects.isNull(channelTopic)) throw new NullPointerException();

    try {
      return serviceRepository.findByOutputChannel(channelTopic);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsById(UUID id) {
    if (Objects.isNull(id)) throw new NullPointerException();

    try {
      return serviceRepository.existsById(id);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsByName(String name) {
    if (Objects.isNull(name)) throw new NullPointerException();

    try {
      return serviceRepository.existsByName(name);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsByOutputChannel(String channelTopic) {
    if (Objects.isNull(channelTopic)) throw new NullPointerException();

    try {
      return serviceRepository.existsByOutputChannel(channelTopic);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsByConfigurationChannel(String channelTopic) {
    if (Objects.isNull(channelTopic)) throw new NullPointerException();

    return false;
  }

  @Override
  public int count() {
    try {
      return serviceRepository.count();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void create(Service service) {
    if (Objects.isNull(service)) throw new NullPointerException();

    if (existsById(service.getId()) || existsByName(service.getName()))
      throw new ServiceDuplicateException();

    if (service.getConfigurationChannel().isPresent()
        && !channelController.existsByTopic(service.getConfigurationChannel().get().getTopic()))
      throw new ChannelNotFoundException();

    for (Channel channel : service.getOutputChannels()) {
      if (!channelController.existsByTopic(channel.getTopic()))
        throw new ChannelNotFoundException();

      Optional<Service> optional = findByOutputChannel(channel.getTopic());

      if (optional.isPresent()) throw new ChannelInUseException(Set.of(optional.get()));
    }

    try {
      serviceRepository.create(service);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void update(Service service) {
    if (Objects.isNull(service)) throw new NullPointerException();
    if (!existsById(service.getId())) throw new ServiceNotFoundException();

    Optional<Service> optional = findByName(service.getName());
    if (optional.isPresent() && !optional.get().getId().equals(service.getId()))
      throw new ServiceDuplicateException();

    if (service.getConfigurationChannel().isPresent()
        && !channelController.existsByTopic(service.getConfigurationChannel().get().getTopic()))
      throw new ChannelNotFoundException();

    for (Channel channel : service.getOutputChannels()) {
      if (!channelController.existsByTopic(channel.getTopic()))
        throw new ChannelNotFoundException();

      optional = findByOutputChannel(channel.getTopic());

      if (optional.isPresent() && !optional.get().getId().equals(service.getId()))
        throw new ChannelInUseException(Set.of(optional.get()));
    }

    try {
      serviceRepository.update(service);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void removeById(UUID id) {
    if (Objects.isNull(id)) throw new NullPointerException();
    if (!existsById(id)) throw new ServiceNotFoundException();

    try {
      serviceRepository.removeById(id);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }
}
