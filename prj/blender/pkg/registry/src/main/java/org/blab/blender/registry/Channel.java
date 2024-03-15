package org.blab.blender.registry;

public class Channel {
  private String topic;
  private Scheme scheme;

  private TopicValidator validator;

  /**
   * @throws NullPointerException if one of given arguments is null.
   * @throws ValidationException  if topic is invalid.
   */
  public Channel(String topic, Scheme scheme) {
    this.validator = TopicValidatorFactory.create();

    if (scheme == null)
      throw new NullPointerException();

    validator.validate(topic);
    this.topic = topic;
    this.scheme = scheme;
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
}
