package org.blab.blender.registry.repository;

import org.blab.blender.registry.Service;

import java.sql.SQLException;
import java.util.UUID;
import java.util.Set;
import java.util.Optional;

public interface ServiceRepository {
  Set<Service> findAll() throws SQLException;

  Set<Service> findByConfigurationChannel(String channelTopic) throws SQLException;

  Optional<Service> findById(UUID id) throws SQLException;

  Optional<Service> findByName(String name) throws SQLException;

  Optional<Service> findByOutputChannel(String channelTopic) throws SQLException;

  boolean existsById(UUID id) throws SQLException;

  boolean existsByName(String name) throws SQLException;

  boolean existsByOutputChannel(String channelTopic) throws SQLException;

  int count() throws SQLException;

  void create(Service service) throws SQLException;

  void update(Service service) throws SQLException;

  void removeById(UUID id) throws SQLException;
}
