package org.blab.blender.registry.controller;

import org.blab.blender.registry.*;
import org.blab.blender.registry.repository.ChannelRepository;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class DefaultChannelController implements ChannelController {
  private ChannelRepository channelRepository;
  private ServiceController serviceController;
  private SchemeController schemeController;

  public DefaultChannelController setChannelRepository(ChannelRepository channelRepository) {
    this.channelRepository = channelRepository;
    return this;
  }

  public DefaultChannelController setServiceController(ServiceController serviceController) {
    this.serviceController = serviceController;
    return this;
  }

  public DefaultChannelController setSchemeController(SchemeController schemeController) {
    this.schemeController = schemeController;
    return this;
  }

  @Override
  public Set<Channel> findAll() {
    try {
      return channelRepository.findAll();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Set<Channel> findBySchemeId(UUID schemeId) {
    if (Objects.isNull(schemeId)) throw new NullPointerException();

    try {
      return channelRepository.findBySchemeId(schemeId);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public Optional<Channel> findByTopic(String topic) {
    if (Objects.isNull(topic)) throw new NullPointerException();

    try {
      return channelRepository.findByTopic(topic);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsBySchemeId(UUID schemeId) {
    if (Objects.isNull(schemeId)) throw new NullPointerException();

    try {
      return channelRepository.existsBySchemeId(schemeId);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public boolean existsByTopic(String topic) {
    if (Objects.isNull(topic)) throw new NullPointerException();

    try {
      return channelRepository.existsByTopic(topic);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public int count() {
    try {
      return channelRepository.count();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void create(Channel channel) {
    if (Objects.isNull(channel)) throw new NullPointerException();
    if (existsByTopic(channel.getTopic())) throw new ChannelDuplicateException();
    if (!schemeController.existsById(channel.getScheme().getId()))
      throw new SchemeNotFoundException();

    try {
      channelRepository.create(channel);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void update(Channel channel) {
    if (Objects.isNull(channel)) throw new NullPointerException();
    if (!existsByTopic(channel.getTopic())) throw new ChannelNotFoundException();
    if (!schemeController.existsById(channel.getScheme().getId()))
      throw new SchemeNotFoundException();

    try {
      channelRepository.update(channel);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  @Override
  public void removeByTopic(String topic) {
    if (Objects.isNull(topic)) throw new NullPointerException();
    if (!existsByTopic(topic)) throw new ChannelNotFoundException();

    Set<Service> users = serviceController.findByConfigurationChannel(topic);
    serviceController.findByOutputChannel(topic).ifPresent(users::add);
    if (!users.isEmpty()) throw new ChannelInUseException(users);

    try {
      channelRepository.removeByTopic(topic);
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }
}
