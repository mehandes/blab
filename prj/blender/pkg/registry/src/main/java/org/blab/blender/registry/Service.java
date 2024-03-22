package org.blab.blender.registry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.Optional;
import java.util.Objects;

public class Service {
  /** Hard service identifier. */
  private final UUID id;

  /** Soft service identifier. */
  private String name;

  /** Access key for owned channels. */
  private String key;

  /** List of output definitions. */
  private Set<Channel> outs;

  /** Configuration definition. */
  private Optional<Channel> cfg;

  /**
   * @throws NullPointerException if required arguments is null.
   * @throws ValidationException  if name is invalid.
   */
  public Service(
          String name,
          String key,
          Set<Channel> outs,
          Channel cfg
  ) {
    this(UUID.randomUUID(), name, key, outs, cfg);
  }

  public static Service map(ResultSet resultSet, Set<Channel> outs) throws SQLException {
    return new Service(
            UUID.fromString(resultSet.getString("service_id_")),
            resultSet.getString("service_name_"),
            resultSet.getString("key_"),
            outs,
            Channel.map(resultSet)
    );
  }

  /**
   * @throws NullPointerException if required arguments is null.
   * @throws ValidationException  if name is invalid.
   */
  private Service(
      UUID id,
      String name,
      String key,
      Set<Channel> outs,
      Channel cfg) {
    if (Objects.isNull(id) || Objects.isNull(outs))
      throw new NullPointerException();

    this.id = id;
    this.name = validateName(name);
    this.key = key;
    this.outs = outs;
    this.cfg = Optional.ofNullable(cfg);
  }

  private String validateName(String name) {
    if (Objects.isNull(name))
      throw new NullPointerException();

    if (name.isBlank())
      throw new ValidationException("Name must be non-empty.");

    return name;
  }

  /**
   * @throws NullPointerException if required arguments is null.
   * @throws ValidationException  if name is invalid.
   */
  public Service(
      String name,
      Set<Channel> outs,
      Channel cfg) {
    this(UUID.randomUUID(), name, UUID.randomUUID().toString(), outs, cfg);
  }

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getKey() {
    return this.key;
  }

  public Channel[] getOutsArray() {
    return this.outs.stream().map(Channel::clone).toArray(Channel[]::new);
  }

  public Optional<Channel> getCfg() {
    return this.cfg.isPresent() ? Optional.of(cfg.get().clone()) : Optional.empty();
  }

  public void addOut(Channel channel) {
    this.outs.add(channel);
  }

  public void removeOut(Channel channel) {
    this.outs.remove(channel);
  }

  public void setCfgChannel(Channel channel) {
    this.cfg = Optional.ofNullable(channel);
  }
}

