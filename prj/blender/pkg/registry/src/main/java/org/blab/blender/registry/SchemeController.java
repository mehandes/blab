package org.blab.blender.registry;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface SchemeController {
  /**
   * @throws DataAccessException if there are DAL errors occurs.
   */
  Set<Scheme> findAll();

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if name is null.
   */
  Set<Scheme> findByName(String name);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if namespace is null.
   */
  Set<Scheme> findByNamespace(String namespace);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if id is null.
   */
  Optional<Scheme> findById(UUID id);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if name of namespace is null.
   */
  Optional<Scheme> findByFullName(String name, String namespace);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if id is null.
   */
  boolean existsById(UUID id);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if name of namespace is null.
   */
  boolean existsByFullName(String name, String namespace);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   */
  int count();

  /**
   * Persist new scheme in storage.
   *
   * @param scheme newly created scheme to persist.
   * @throws SchemeDuplicateException if such scheme already exists.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if scheme is null.
   */
  void create(Scheme scheme);

  /**
   * Update specified scheme in storage.
   *
   * @param scheme scheme to update.
   * @throws SchemeNotFoundException if such scheme does not exists.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if scheme is null.
   */
  void update(Scheme scheme);

  /**
   * Delete scheme with specified id from storage.
   *
   * @param id scheme's id.
   * @throws SchemeNotFoundException if scheme with specified id does not exists.
   * @throws SchemeInUseException if scheme with specified id used by channels.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if id is null.
   */
  void removeById(UUID id);
}
