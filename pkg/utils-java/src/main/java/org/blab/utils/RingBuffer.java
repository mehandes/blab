package org.blab.utils;

import java.util.*;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Blocking buffer with fixed circular storage.
 *
 * @param <T> type of persisted elements.
 */
public class RingBuffer<T> {
  private final Object[] data;

  /** Pointer associated with buffer. Points to one position after last wrote element. */
  private int pointer;

  /** Size of current sequence of elements. */
  private int size;

  /** Common locker for all read/write operations. */
  private final ReentrantLock locker;

  /** Condition for blocking read operations. Should be signalled by all write operations. */
  private final Condition notEmpty;

  private int next(int index, int step) {
    return (index + step) % data.length;
  }

  private int prev(int index, int step) {
    return (data.length + (index - step) % data.length) % data.length;
  }

  private void add(T e) {
    if (e == null) throw new NullPointerException();
    data[pointer] = e;
    pointer = next(pointer, 1);

    if (size < data.length) size++;
  }

  private void addAll(List<T> es) {
    if (es == null) throw new NullPointerException();

    for (T e : es) {
      data[pointer] = e;
      pointer = next(pointer, 1);
    }

    size = size <= (data.length - es.size()) ? size + es.size() : data.length;
  }

  @SuppressWarnings("unchecked")
  private T get() {
    if (size == 0) return null;
    return (T) data[prev(pointer, 1)];
  }

  @SuppressWarnings("unchecked")
  private T pop() {
    if (size == 0) return null;
    size--;
    pointer = prev(pointer, 1);
    return (T) data[pointer];
  }

  @SuppressWarnings("unchecked")
  private T popOldest() {
    if (size == 0) return null;
    int i = prev(pointer, size);
    size--;
    return (T) data[i];
  }

  @SuppressWarnings("unchecked")
  private List<T> pop(int c) {
    if (size < c) return null;
    List<T> r = new ArrayList<>(Collections.nCopies(c, null));

    for (int j = c - 1; j >= 0; --j) {
      pointer = prev(pointer, 1);
      r.set(j, (T) data[pointer]);
    }

    size -= c;
    return r;
  }

  /** Create new buffer with specified capacity. */
  public RingBuffer(int capacity) {
    data = new Object[capacity];
    locker = new ReentrantLock();
    notEmpty = locker.newCondition();
  }

  public int size() {
    locker.lock();

    try {
      return size;
    } finally {
      locker.unlock();
    }
  }

  public int capacity() {
    return data.length;
  }

  /**
   * Append an element to the end of the buffer.
   *
   * <p>If the buffer size is equal to capacity, the oldest element will be overwritten.
   *
   * @param e element to append.
   * @throws NullPointerException if provided element is null.
   */
  public void put(T e) {
    locker.lock();

    try {
      add(e);
      notEmpty.signalAll();
    } finally {
      locker.unlock();
    }
  }

  /**
   * Append elements to the end of the buffer.
   *
   * <p>If the buffer size is equal to capacity, the oldest element will be overwritten.
   *
   * @param es elements to append.
   * @throws NullPointerException if provided list is null.
   */
  public void put(List<T> es) {
    locker.lock();

    try {
      addAll(es);
      notEmpty.signalAll();
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve but does not remove the last element.
   *
   * @return The last element, if exists.
   */
  public Optional<T> peek() {
    locker.lock();

    try {
      return Optional.ofNullable(get());
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove the last element. The {@link Optional} is empty if the buffer is empty.
   *
   * @return The last element, if exists.
   */
  public Optional<T> poll() {
    locker.lock();

    try {
      return Optional.ofNullable(pop());
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove the oldest element. The {@link Optional} is empty if the buffer is empty.
   *
   * @return The oldest element, if exists.
   */
  public Optional<T> pollOldest() {
    locker.lock();

    try {
      return Optional.ofNullable(popOldest());
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove the last element.
   *
   * <p>If the buffer is empty, waits for specified timeout.
   *
   * <p>A negative timeout is treated as infinity.
   *
   * @param timeout timeout in milliseconds.
   * @return The last element if it appears within the specified timeout.
   * @throws InterruptedException if thread interrupted during timeout.
   * @throws TimeoutException if timeout expired but buffer is still empty.
   */
  public T poll(long timeout) throws InterruptedException, TimeoutException {
    locker.lock();

    try {
      boolean r = true;

      if (size == 0)
        if (timeout < 0) notEmpty.await();
        else r = notEmpty.await(timeout, TimeUnit.MILLISECONDS);

      if (!r) throw new TimeoutException();
      else return Objects.requireNonNull(pop());
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove the oldest element.
   *
   * <p>If the buffer is empty, waits for specified timeout.
   *
   * <p>A negative timeout is treated as infinity.
   *
   * @param timeout timeout in milliseconds.
   * @return The oldest element if it appears within the specified timeout.
   * @throws InterruptedException if thread interrupted during timeout.
   * @throws TimeoutException if timeout expired but buffer is still empty.
   */
  public T pollOldest(long timeout) throws InterruptedException, TimeoutException {
    locker.lock();

    try {
      boolean r = true;

      if (size == 0)
        if (timeout < 0) notEmpty.await();
        else r = notEmpty.await(timeout, TimeUnit.MILLISECONDS);

      if (!r) throw new TimeoutException();
      else return Objects.requireNonNull(popOldest());
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove all available elements.
   *
   * @return all elements since last poll.
   */
  public List<T> pollAvailable() {
    locker.lock();

    try {
      return pop(size);
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove last {@code c} elements. The {@link Optional} is empty if there are not
   * enough elements.
   *
   * @return Last {@code c} elements, if exists.
   */
  public Optional<List<T>> pollExactly(int n) {
    if (n < 0 || n > data.length) throw new IllegalArgumentException();

    locker.lock();

    try {
      return Optional.ofNullable(pop(n));
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove last {@code c} elements.
   *
   * <p>If the buffer is empty, waits for specified timeout.
   *
   * <p>Negative timeout treated as infinity.
   *
   * @param timeout timeout in milliseconds.
   * @return The last {@code c} elements if they are appears within the specified timeout.
   * @throws InterruptedException if thread interrupted during timeout.
   * @throws TimeoutException if timeout expired but there are still not enough elements.
   */
  public List<T> pollExactly(int n, long timeout) throws TimeoutException, InterruptedException {
    if (n < 0 || n > data.length) throw new IllegalArgumentException();

    locker.lock();

    try {
      if (!awaitAtLeast(n, timeout)) throw new TimeoutException();
      else return Objects.requireNonNull(pop(n));
    } finally {
      locker.unlock();
    }
  }

  /**
   * Retrieve and remove all elements, but at least {@code c}.
   *
   * <p>If there are not enough elements, waits for specified timeout.
   *
   * <p>Negative timeout treated as infinity.
   *
   * @param timeout timeout in milliseconds.
   * @return The last {@code c} elements if they are appears within the specified timeout.
   * @throws InterruptedException if thread interrupted during timeout.
   * @throws TimeoutException if timeout expired but there are still not enough elements.
   */
  public List<T> pollAtLeast(int n, long timeout) throws TimeoutException, InterruptedException {
    if (n < 0 || n > data.length) throw new IllegalArgumentException();

    locker.lock();

    try {
      if (!awaitAtLeast(n, timeout)) throw new TimeoutException();
      else return Objects.requireNonNull(pop(size));
    } finally {
      locker.unlock();
    }
  }

  private boolean awaitAtLeast(int c, long timeout) throws InterruptedException {
    if (timeout < 0) while (size < c) notEmpty.await();
    else {
      timeout = TimeUnit.MILLISECONDS.toNanos(timeout);

      while (size < c && timeout > 0) timeout = notEmpty.awaitNanos(timeout);
    }

    return c <= size;
  }
}
