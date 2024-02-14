package org.blab.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A {@link BlockingQueue} that override {@link #drainTo(Collection, int)} operation meaning.
 * 
 * @param <T> the type of elements held in this buffer
 */
public class Buffer<T> implements BlockingQueue<T> {
  private final Queue<T> data;

  private final ReentrantLock lock;
  private final Condition notEmpty;

  public Buffer() {
    this.data = new LinkedList<>();
    this.lock = new ReentrantLock();
    this.notEmpty = this.lock.newCondition();
  }

  @Override
  public int size() {
    lock.lock();

    try {
      return data.size();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int remainingCapacity() {
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean isEmpty() {
    lock.lock();

    try {
      return data.isEmpty();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Object[] toArray() {
    lock.lock();

    try {
      return data.toArray();
    } finally {
      lock.unlock();
    }
  }

  @SuppressWarnings("hiding")
  @Override
  public <T> T[] toArray(T[] a) {
    lock.lock();

    try {
      return data.toArray(a);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public Iterator<T> iterator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean contains(Object o) {
    lock.lock();

    try {
      return data.contains(o);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    lock.lock();

    try {
      return data.containsAll(c);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T element() {
    lock.lock();

    try {
      return data.element();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T peek() {
    lock.lock();

    try {
      return data.element();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T poll() {
    lock.lock();

    try {
      return data.poll();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T poll(long timeout, TimeUnit unit) throws InterruptedException {
    lock.lock();

    try {
      boolean r = true;

      if (data.size() == 0)
        r = notEmpty.await(timeout, unit);

      return r ? data.poll() : null;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T take() throws InterruptedException {
    lock.lock();

    try {
      if (data.size() == 0)
        notEmpty.await();

      return data.poll();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int drainTo(Collection<? super T> c) {
    lock.lock();

    try {
      return drain(c, data.size());
    } finally {
      lock.unlock();
    }
  }

  /**
   * Waits until given number of elements become available and then moves them into given
   * collection.
   */
  @Override
  public int drainTo(Collection<? super T> c, int maxElements) {
    lock.lock();

    try {
      while (data.size() < maxElements)
        notEmpty.awaitUninterruptibly();

      return drain(c, data.size());
    } finally {
      lock.unlock();
    }
  }

  private int drain(Collection<? super T> c, int maxElements) {
    List<T> l = new ArrayList<>(maxElements);

    for (int i = 0; i < maxElements; ++i)
      l.add(data.poll());

    for (int i = maxElements - 1; i >= 0; --i)
      c.add(l.get(i));

    return maxElements;
  }

  @Override
  public T remove() {
    lock.lock();

    try {
      return data.remove();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean remove(Object o) {
    lock.lock();

    try {
      return data.remove(o);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    lock.lock();

    try {
      return data.removeAll(c);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    lock.lock();

    try {
      return data.retainAll(c);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void clear() {
    lock.lock();

    try {
      data.clear();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean add(T e) {
    lock.lock();

    try {
      boolean r = data.add(e);
      notEmpty.signalAll();
      return r;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    lock.lock();

    try {
      boolean r = data.addAll(c);
      notEmpty.signalAll();
      return r;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean offer(T e) {
    lock.lock();

    try {
      boolean r = data.offer(e);

      if (r)
        notEmpty.signalAll();

      return r;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
    return offer(e);
  }

  @Override
  public void put(T e) throws InterruptedException {
    offer(e);
  }
}
