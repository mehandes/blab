package org.blab.blender.registry;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Channel is a topic with fixed data structure. */
public class Channel implements Cloneable {
  /** Hard channel identifier. */
  private final String topic;

  private Scheme scheme;

  /**
   * @throws NullPointerException if one of given arguments is null.
   * @throws ValidationException  if topic is invalid.
   */
  public Channel(String topic, Scheme scheme) {
    if (scheme == null)
      throw new NullPointerException();

    this.topic = TopicValidator.validate(topic);
    this.scheme = scheme;
  }

  public static Channel map(ResultSet resultSet) throws SQLException {
    return new Channel(
            resultSet.getString("topic_"),
            Scheme.map(resultSet)
    );
  }

  public String getTopic() {
    return this.topic;
  }

  /**
   * Get {@link Scheme} linked to this channel.
   * 
   * @return Immutable (copy) of channel's scheme.
   */
  public Scheme getScheme() {
    return this.scheme.clone();
  }

  public void setScheme(Scheme scheme) {
    if (scheme == null)
      throw new NullPointerException();

    this.scheme = scheme;
  }

  @Override
  public Channel clone() {
    return new Channel(topic, scheme.clone());
  }

  @Override
  public String toString() {
    return "Channel{" +
            "topic='" + topic + '\'' +
            ", scheme=" + scheme +
            '}';
  }
}
