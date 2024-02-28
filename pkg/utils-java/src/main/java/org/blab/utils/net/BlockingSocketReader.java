package org.blab.utils.net;

import org.blab.utils.RingBuffer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * The {@link BlockingSocketChannel} client that reads messages from the provided channel.
 *
 * <p>Can be run in a separate thread, in which case all read operations will be performed in
 * background.
 *
 * <p>Implements a polling system in which the client reads messages in a special buffer from which
 * you can poll them at your discretion.
 */
public class BlockingSocketReader implements Runnable {

  /** Channel to read. */
  private final BlockingSocketChannel channel;

  /** Buffer for messages. */
  private final RingBuffer<byte[]> buffer;

  /** Runtime exception if there are {@link BlockingSocketChannel} errors occurs. */
  private BlockingSocketException error;

  /** Indicated whether client running or not. */
  private boolean isRunning;

  /**
   * @param channel channel that the client will read.
   */
  BlockingSocketReader(BlockingSocketChannel channel) {
    this.channel = channel;
    this.buffer = new RingBuffer<>(256);
  }

  /**
   * Retrieve all received messages since last fetch.
   *
   * <p>If there are no messages, blocks until the provided timeout expires or until some message
   * becomes available.
   *
   * @param timeout maximum message waiting time. Negative values are treated as infinity.
   * @return List of messages retrieved since the last poll. May be empty.
   * @throws IllegalStateException if the client does not running.
   * @throws BlockingSocketException if the client was aborted due to fatal exception.
   * @throws InterruptedException if the thread interrupted during timeout.
   */
  public List<byte[]> poll(long timeout) throws InterruptedException {
    if (!isRunning) throw new IllegalStateException("Client need to be started first.");
    if (error != null) throw error;

    try {
      return buffer.pollAtLeast(1, timeout);
    } catch (TimeoutException e) {
      return Collections.emptyList();
    }
  }

  /**
   * Start reading loop.
   *
   * @throws IllegalStateException if the connection is not established.
   */
  @Override
  public void run() {
    if (!channel.isConnected())
      throw new IllegalStateException("The channel must be connected before running the client.");

    isRunning = true;

    while (error == null)
      if (channel.isConnected()) buffer.put(channel.read());
      else channel.reconnect();

    isRunning = false;
  }
}
