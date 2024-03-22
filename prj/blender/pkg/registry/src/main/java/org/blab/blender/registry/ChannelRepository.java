package org.blab.blender.registry;

import java.sql.SQLException;
import java.util.Set;
import java.util.Optional;

public interface ChannelRepository {
  Set<Channel> findAll() throws SQLException;
  Set<Channel> findBySchemeName(String schemeName) throws SQLException;
  Set<Channel> findBySchemeNamespace(String schemeNamespace) throws SQLException;
  Set<Channel> findBySchemeFullName(String schemeName, String schemeNamespace) throws SQLException;

  Optional<Channel> findByTopic(String topic) throws SQLException;

  boolean existsByTopic(String topic) throws SQLException;
  int count() throws SQLException;

  void create(Channel channel) throws SQLException;
  void update(Channel channel) throws SQLException;
  void removeByTopic(String topic) throws SQLException;
}

