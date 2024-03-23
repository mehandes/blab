package org.blab.blender.registry;

import org.blab.blender.registry.validation.TopicValidator;
import org.blab.blender.registry.validation.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Channel implements Cloneable {
  private final String topic;
  private Scheme scheme;

  /**
   * @throws NullPointerException if one of given arguments is null.
   * @throws ValidationException if topic is invalid.
   */
  public Channel(String topic, Scheme scheme) {
    if (scheme == null) throw new NullPointerException();

    this.topic = TopicValidator.validate(topic);
    this.scheme = scheme;
  }

  public static Channel map(ResultSet resultSet) throws SQLException {
    return new Channel(resultSet.getString("channel_topic_"), Scheme.map(resultSet));
  }

  public String getTopic() {
    return this.topic;
  }

  public Scheme getScheme() {
    return this.scheme.clone();
  }

  public void setScheme(Scheme scheme) {
    if (scheme == null) throw new NullPointerException();
    this.scheme = scheme;
  }

  @Override
  public Channel clone() {
    return new Channel(topic, scheme.clone());
  }

  @Override
  public String toString() {
    return "Channel{" + "topic='" + topic + '\'' + ", scheme=" + scheme + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Channel channel = (Channel) o;
    return Objects.equals(topic, channel.topic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topic);
  }
}
