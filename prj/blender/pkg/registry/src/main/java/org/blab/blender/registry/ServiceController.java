package org.blab.blender.registry;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ServiceController {
  /**
   * @throws DataAccessException if there are DAL errors occurs.
   */
  Set<Service> findAll();

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if channelTopic is null.
   */
  Set<Service> findByConfigurationChannel(String channelTopic);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if id is null.
   */
  Optional<Service> findById(UUID id);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if name is null.
   */
  Optional<Service> findByName(String name);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if channelTopic is null.
   */
  Optional<Service> findByOutputChannel(String channelTopic);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if id is null.
   */
  boolean existsById(UUID id);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if name is null.
   */
  boolean existsByName(String name);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if channelTopic is null.
   */
  boolean existsByOutputChannel(String channelTopic);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if channelTopic is null.
   */
  boolean existsByConfigurationChannel(String channelTopic);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   */
  int count();

  /**
   * @throws ServiceDuplicateException if service with the same name already exists.
   * @throws ChannelNotFoundException if included channel does not exists.
   * @throws ChannelInUseException if included channel used by another service.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if service is null.
   */
  void create(Service service);

  /**
   * @throws ServiceDuplicateException if service with the same name already exists.
   * @throws ServiceNotFoundException if service does not exists.
   * @throws ChannelNotFoundException if included channel does not exists.
   * @throws ChannelInUseException if included channel used by another service.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if service is null.
   */
  void update(Service service);

  /**
   * @throws ServiceNotFoundException if service does not exists.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if id is null.
   */
  void removeById(UUID id);
}
