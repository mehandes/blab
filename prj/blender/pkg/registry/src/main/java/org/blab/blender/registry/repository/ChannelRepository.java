package org.blab.blender.registry.repository;

import org.blab.blender.registry.Channel;

import java.sql.SQLException;
import java.util.Set;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
  Set<Channel> findAll() throws SQLException;

  Set<Channel> findBySchemeId(UUID schemeId) throws SQLException;

  Optional<Channel> findByTopic(String topic) throws SQLException;

  boolean existsBySchemeId(UUID schemeId) throws SQLException;

  boolean existsByTopic(String topic) throws SQLException;

  int count() throws SQLException;

  void create(Channel channel) throws SQLException;

  void update(Channel channel) throws SQLException;

  void removeByTopic(String topic) throws SQLException;
}
