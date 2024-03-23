package org.blab.blender.registry;

import org.blab.blender.registry.validation.ServiceNameValidator;
import org.blab.blender.registry.validation.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.Optional;
import java.util.Objects;

public class Service {
  private final UUID id;
  private final String name;
  private final String identificationKey;
  private final Set<Channel> outputChannels;
  private Channel configurationChannel;

  /**
   * @throws NullPointerException if required arguments is null.
   * @throws ValidationException if name is invalid.
   */
  public Service(String name, Set<Channel> outputChannels, Channel configurationChannel) {
    this(
        UUID.randomUUID(),
        name,
        UUID.randomUUID().toString(),
        outputChannels,
        configurationChannel);
  }

  public static Service map(ResultSet resultSet, Set<Channel> outputChannels) throws SQLException {
    return new Service(
        UUID.fromString(resultSet.getString("service_id_")),
        resultSet.getString("service_name_"),
        resultSet.getString("service_key_"),
        outputChannels,
        resultSet.getString("scheme_id_") == null ? null : Channel.map(resultSet));
  }

  private Service(
      UUID id,
      String name,
      String identificationKey,
      Set<Channel> outputChannels,
      Channel configurationChannel) {
    if (Objects.isNull(outputChannels)) throw new NullPointerException();

    this.id = id;
    this.name = ServiceNameValidator.validate(name);
    this.identificationKey = identificationKey;
    this.outputChannels = outputChannels;
    this.configurationChannel = configurationChannel;
  }

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getIdentificationKey() {
    return this.identificationKey;
  }

  public Channel[] getOutputChannels() {
    return this.outputChannels.stream().map(Channel::clone).toArray(Channel[]::new);
  }

  public Optional<Channel> getConfigurationChannel() {
    return Optional.ofNullable(this.configurationChannel);
  }

  public void linkOutputChannel(Channel channel) {
    this.outputChannels.add(channel);
  }

  public void unlinkOutputChannel(String topic) {
    this.outputChannels.removeIf(channel -> channel.getTopic().equals(topic));
  }

  public void setConfigurationChannel(Channel channel) {
    this.configurationChannel = channel;
  }

  @Override
  public String toString() {
    return "Service{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", identificationKey='"
        + identificationKey
        + '\''
        + ", outputChannels="
        + outputChannels
        + ", configurationChannel="
        + configurationChannel
        + '}';
  }
}
