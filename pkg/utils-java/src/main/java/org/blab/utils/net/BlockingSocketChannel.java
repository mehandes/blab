package org.blab.utils.net;

import org.blab.utils.RingBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * BlockingSocketChannel is a wrapper for SocketChannel with message partitioning.
 *
 * <p>BlockingSocketChannel only works on messages separated by a newline character, otherwise the
 * behaviour is undefined.
 *
 * <p>For reading purposes, implements a kind of ring buffer, which stores bytes until a terminator
 * is encountered.
 *
 * <p>Can be passed as parameter to {@link BlockingSocketReader}s or {@link BlockingSocketWriter}s
 * to delegate read or write operations respectively. But if the channel is shared by many readers,
 * the messages will spread uncontrollably.
 */
public class BlockingSocketChannel {
  /** Socket which actually performs I/O operations. In blocking mode by default. */
  private SocketChannel socket;

  /** A buffer in which message remnants are cached. */
  private final RingBuffer<Byte> cache;

  /** Locker for read operations. */
  private final ReentrantLock readLock;

  /** Locker for write operations. */
  private final ReentrantLock writeLock;

  /**
   * Create BlockingSocketChannel with specified cache size.
   *
   * <p>The buffer size should be larger than any message potentially received from this channel. If
   * the received message exceeds the specified size, only last {@code bs} bytes will be received.
   *
   * @param cacheSize remnants cache size.
   * @throws IllegalArgumentException if cache size is zero or negative.
   */
  public BlockingSocketChannel(int cacheSize) {
    if (cacheSize <= 0) throw new IllegalArgumentException("Buffer size must be positive.");
    cache = new RingBuffer<>(cacheSize);
    readLock = new ReentrantLock();
    writeLock = new ReentrantLock();
  }

  /**
   * Open connection.
   *
   * <p>If the channel can resolve specified route, the thread is blocked until the connection is
   * established or the timeout occurs. If the route cannot be resolved, an exception is thrown.
   *
   * <p>Closes the previous connection if there is already one.
   *
   * @param address server socket address.
   * @throws NullPointerException if the provided address is null.
   * @throws BlockingSocketException if any socket access errors occur.
   */
  public void open(InetSocketAddress address) {
    if (address == null) throw new NullPointerException();

    readLock.lock();
    writeLock.lock();

    try {
      if (socket != null) socket.connect(address);
      else socket = SocketChannel.open(address);
    } catch (IOException e) {
      throw new BlockingSocketException(e);
    } finally {
      readLock.unlock();
      writeLock.unlock();
    }
  }

  /**
   * Check whether the client is connected or not.
   *
   * @return {@code true} if client connected.
   */
  public boolean isConnected() {
    return socket != null && socket.isConnected();
  }

  /**
   * Write message to channel.
   *
   * <p>Adds a newline character to the end if there is none.
   *
   * @param message byte sequence to write.
   * @throws IllegalStateException if the channel has not yet opened.
   * @throws NotYetConnectedException if the channel has not yet connected.
   * @throws NullPointerException if the provided message is null.
   * @throws BlockingSocketException if any socket access errors occur.
   */
  public void write(byte[] message) {
    if (message == null) throw new NullPointerException("The message cannot be null.");
    if (socket == null) throw new IllegalStateException("The channel is not open yet.");

    writeLock.lock();

    try {
      if (message[message.length - 1] != (byte) '\n') {
        message = Arrays.copyOf(message, message.length + 1);
        message[message.length - 1] = (byte) '\n';
      }

      try {
        socket.write(ByteBuffer.wrap(message));
      } catch (IOException e) {
        throw new BlockingSocketException(e);
      }
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Read all messages received since the last read.
   *
   * <p>Blocks until at least one byte read.
   *
   * @return list of read messages.
   * @throws IllegalStateException if the channel has not yet opened.
   * @throws NotYetConnectedException if the channel has not yet connected.
   * @throws BlockingSocketException if any socket access errors occur.
   */
  public List<byte[]> read() {
    if (socket == null) throw new IllegalStateException("The channel is not opened yet.");

    readLock.lock();

    try {
      List<byte[]> r = new ArrayList<>();
      ByteBuffer b = ByteBuffer.allocate(1024);

      int n = socket.read(b.clear());

      for (int i = 0; i < n; i++)
        if (b.get(i) == (byte) '\n') r.add(cast(cache.pollAvailable()));
        else cache.put(b.get(i));

      return r;
    } catch (IOException e) {
      throw new BlockingSocketException(e);
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Close the connection.
   *
   * @throws IllegalStateException if the channel has not yet opened.
   * @throws BlockingSocketException if any socket access errors occur.
   */
  public void close() {
    if (socket == null) throw new IllegalStateException("The channel is not opened yet.");

    readLock.lock();
    writeLock.lock();

    try {
      socket.close();
    } catch (IOException e) {
      throw new BlockingSocketException(e);
    } finally {
      socket = null;
      readLock.unlock();
      writeLock.unlock();
    }
  }

  private byte[] cast(List<Byte> a) {
    byte[] bytes = new byte[a.size()];
    for (int i = 0; i < a.size(); i++) bytes[i] = a.get(i);
    return bytes;
  }
}
