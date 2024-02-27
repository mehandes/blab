package org.blab.utils.net;

/**
 * The {@link BlockingSocketChannel} client that writes messages to the provided channel.
 *
 * <p>Can be run in a separate thread, in which case all write operations will be performed in
 * background.
 */
public class BlockingSocketWriter implements Runnable {
  private final BlockingSocketChannel channel;

  /**
   * @param channel channel to which the client will write.
   * @throws NullPointerException if provided channel is null.
   */
  public BlockingSocketWriter(BlockingSocketChannel channel) {
    if (channel == null) throw new NullPointerException();
    this.channel = channel;
  }

  @Override
  public void run() {}
}
