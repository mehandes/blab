package org.blab.utils.net;

import org.blab.utils.RingBuffer;

import java.util.concurrent.TimeoutException;

/**
 * The {@link BlockingSocketChannel} client that writes messages to the provided channel.
 *
 * <p>Can be run in a separate thread, in which case all write operations will be performed in
 * background.
 */
public class BlockingSocketWriter implements Runnable {

  /** Channel to write */
  private final BlockingSocketChannel channel;

  /** Buffer for messages. */
  private final RingBuffer<byte[]> buffer;

  /** Runtime exception if there are {@link BlockingSocketChannel} errors occurs. */
  private BlockingSocketException error;

  /** Indicated whether client running or not. */
  private boolean isRunning;

  /**
   * @param channel channel to which the client will write.
   */
  BlockingSocketWriter(BlockingSocketChannel channel) {
    this.channel = channel;
    this.buffer = new RingBuffer<>(256);
  }

  /**
   * Add a message to the recording queue.
   *
   * @param message message to write.
   * @throws IllegalStateException if the client does not running.
   * @throws BlockingSocketException if the client was aborted due to fatal exception.
   */
  public void write(byte[] message) {
    if (!isRunning) throw new IllegalStateException("Client need to be started first.");
    if (error != null) throw error;
    buffer.put(message);
  }

  /**
   * Start writing loop.
   *
   * @throws IllegalStateException if the connection is not established.
   */
  @Override
  public void run() {
    if (!channel.isConnected())
      throw new IllegalStateException("The channel must be connected before running the client.");

    isRunning = true;

    while (error == null)
      try {
        if (channel.isConnected())
          try {
            channel.write(buffer.pollOldest(-1));
          } catch (InterruptedException | TimeoutException e) {
            break;
          }
        else channel.reconnect();
      } catch (BlockingSocketException e) {
        error = e;
      }

    isRunning = false;
  }
}
