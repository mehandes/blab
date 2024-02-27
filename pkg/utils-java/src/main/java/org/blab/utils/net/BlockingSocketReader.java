package org.blab.utils.net;

/**
 * The {@link BlockingSocketChannel} client that reads messages from the provided channel.
 *
 * <p>Can be run in a separate thread, in which case all read operations will be performed in
 * background.
 *
 * <p>Implements a polling system in which the client reads messages in a special buffer from which you can
 * poll them at your discretion.
 */
public class BlockingSocketReader implements Runnable {
  private final BlockingSocketChannel channel;

  /**
   * @param channel channel that the client will read.
   * @throws NullPointerException if provided channel is null.
   */
  public BlockingSocketReader(BlockingSocketChannel channel) {
    if (channel == null) throw new NullPointerException();
    this.channel = channel;
  }

  @Override
  public void run() {}


}
