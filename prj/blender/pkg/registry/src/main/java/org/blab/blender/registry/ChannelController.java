package org.blab.blender.registry;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ChannelController {
  /**
   * @throws DataAccessException if there are DAL errors occurs.
   */
  Set<Channel> findAll();

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if schemeId is null.
   */
  Set<Channel> findBySchemeId(UUID schemeId);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if topic is null.
   */
  Optional<Channel> findByTopic(String topic);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if schemeId is null.
   */
  boolean existsBySchemeId(UUID schemeId);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if topic is null.
   */
  boolean existsByTopic(String topic);

  /**
   * @throws DataAccessException if there are DAL errors occurs.
   */
  int count();

  /**
   * Persist new channel in storage.
   *
   * @throws ChannelDuplicateException if such channel already exists.
   * @throws SchemeNotFoundException if included scheme does not exists.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if channel is null.
   */
  void create(Channel channel);

  /**
   * Update specified channel in storage.
   *
   * @throws ChannelNotFoundException if such channel does not exists.
   * @throws SchemeNotFoundException if included scheme does not exists.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if channel is null.
   */
  void update(Channel channel);

  /**
   * Delete channel with specified topic from storage.
   *
   * @throws ChannelNotFoundException if channel with specified topic does not exists.
   * @throws ChannelInUseException if channel with specified topic used by services.
   * @throws DataAccessException if there are DAL errors occurs.
   * @throws NullPointerException if topic is null.
   */
  void removeByTopic(String topic);
}
