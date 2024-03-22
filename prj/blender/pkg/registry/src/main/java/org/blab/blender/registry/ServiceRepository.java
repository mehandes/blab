package org.blab.blender.registry;

import java.sql.SQLException;
import java.util.UUID;
import java.util.Set;
import java.util.Optional;

public interface ServiceRepository {
  Set<Service> findAll() throws SQLException;
  Set<Service> findByChannelTopic(String topic) throws SQLException;

  Optional<Service> findById(UUID id) throws SQLException;
  Optional<Service> findByName(String name) throws SQLException;
  Optional<Service> findByOutChannelTopic(String topic) throws SQLException;

  boolean existsById(UUID id) throws SQLException;
  boolean existsByName(String name) throws SQLException;
  boolean existsByOutChannelTopic(String topic) throws SQLException;
  int count() throws SQLException;

  void create(Service service) throws SQLException;
  void update(Service service) throws SQLException;
  void removeById(UUID id);
}
