package org.blab.blender.registry;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;

public interface SchemeRepository {
  Set<Scheme> findAll() throws SQLException;
  Set<Scheme> findByName(String name) throws SQLException;
  Set<Scheme> findByNamespace(String namespace) throws SQLException;

  Optional<Scheme> findById(UUID id) throws SQLException;
  Optional<Scheme> findByFullName(String name, String namespace) throws SQLException;

  boolean existsById(UUID id) throws SQLException;
  boolean existsByFullName(String name, String namespace) throws SQLException;
  int count() throws SQLException;
  
  void create(Scheme scheme) throws SQLException;
  void update(Scheme scheme) throws SQLException;
  void removeById(UUID id) throws SQLException;
}
