package org.blab.utils;

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
 */
public class BlockingSocketChannel {
  /** Socket which actually performs I/O operations. */
  private SocketChannel channel;

  /** A buffer in which bytes will be stored. */
  private final RingBuffer buffer;

  /** Common locker for all operations. */
  private final ReentrantLock lock;

  /**
   * Create BlockingSocketChannel with specified buffer size.
   *
   * <p>The buffer size should be larger than any message potentially received from this channel. If
   * the received message exceeds the specified size, it will be trimmed starting from the end.
   *
   * @param bufferSize size of buffer.
   * @throws IllegalArgumentException if buffer size is zero or negative.
   */
  public BlockingSocketChannel(int bufferSize) {
    if (bufferSize <= 0) throw new IllegalArgumentException("Buffer size must be positive.");
    buffer = new RingBuffer(bufferSize);
    lock = new ReentrantLock();
  }

  /**
   * Open TCP connection.
   *
   * <p>Blocks until the connection is established or a fatal exception occurs.
   *
   * <p>Discards the previous connection if there is already one.
   *
   * @param addr TCP server address.
   * @throws NullPointerException if the provided address is null.
   * @throws IOException if the I/O error occurs.
   */
  public void open(InetSocketAddress addr) throws IOException, NullPointerException {
    if (addr == null) throw new NullPointerException();
    lock.lock();

    try {
      channel = SocketChannel.open(addr);
    } finally {
      lock.unlock();
    }
  }

  /**
   * Check whether the client is connected or not.
   *
   * @return {@code true} if client connect, {@code false} otherwise.
   */
  public boolean isConnected() {
    return channel != null && channel.isConnected();
  }

  /**
   * Write message to channel.
   *
   * <p>Adds a newline character to the end if there is none.
   *
   * @param message message to write.
   * @throws NotYetConnectedException if the client is disconnected.
   * @throws NullPointerException if the provided message is null.
   * @throws IOException if the I/O error occurs.
   */
  public void write(byte[] message)
      throws NotYetConnectedException, IOException, NullPointerException {
    if (message == null) throw new NullPointerException();

    if (message[message.length - 1] != (byte) '\n') {
      message = Arrays.copyOf(message, message.length + 1);
      message[message.length - 1] = (byte) '\n';
    }

    channel.write(ByteBuffer.wrap(message));
  }

  /**
   * Read all messages received since the last read.
   *
   * @return list of read messages.
   * @throws NotYetConnectedException if the client is disconnected.
   */
  public List<byte[]> read() throws NotYetConnectedException, IOException {
    List<byte[]> r = new ArrayList<>();
    ByteBuffer b = ByteBuffer.allocate(1024);
    int n = channel.read(b.clear());

    for (int i = 0; i < n; i++)
      if (buffer.add(b.get(i)))
        r.add(buffer.get());

    return r;
  }

  /**
   * Close connection.
   *
   * @throws NotYetConnectedException if the client is disconnected.
   * @throws IOException if the I/O error occurs.
   */
  public void close() throws NotYetConnectedException, IOException {
    if (!isConnected()) throw new NotYetConnectedException();
    channel.close();
  }

  /** A ring buffer for storing received bytes. */
  private static class RingBuffer {
    private final byte[] buffer;

    /** Pointer associated with buffer. Points to one byte after last read byte. */
    private int pointer;

    /** Size of current sequence of bytes without terminator. Should not be changed directly. */
    private int size;

    /** Get index of element after provided. */
    private int next(int i) {
      return i == buffer.length - 1 ? 0 : i + 1;
    }

    /** Get index of element before provided. */
    private int prev(int i) {
      return i == 0 ? buffer.length - 1 : i - 1;
    }

    public RingBuffer(int size) {
      buffer = new byte[size];
    }

    /**
     * Add byte to buffer.
     *
     * @return {@code true} if buffer contains completed message and ready to perform {@link
     *     #get()}.
     */
    public boolean add(byte b) {
      if (b == (byte) '\n') return true;

      buffer[pointer] = b;
      pointer = next(pointer);
      size += 1;

      return false;
    }

    /** Get last {@link #size} bytes from buffer. */
    public byte[] get() {
      byte[] r = new byte[size];
      for (int i = size - 1, j = prev(pointer); i >= 0; i--, j = prev(j)) r[i] = buffer[j];
      size = 0;
      return r;
    }
  }
}
