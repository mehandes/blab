package org.blab.commons;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A {@link BlockingQueue} that override {@link #drainTo(Collection)} and
 * {@link #drainTo(Collection, int)} operations, so that they waitinig buffer to become full.
 * 
 * <p>
 * If there isn't capacity restriction, capacity doubled if {@link #size} > 0.75 *
 * {@link #capacity}.
 * </p>
 * 
 * @author Ivan Paraskun
 * @param <T> the type of elements held in this buffer
 */
public class Buffer<T> implements BlockingQueue<T> {
  /** The array into which the elements are stored. */
  private Object[] data;

  /**
   * Buffer's maximum size. If there isn't capacity restrictions, grows dynamically with initial
   * value 10.
   */
  private int capacity;

  /** Current number of elements in buffer. */
  private int size;

  /** Indicating wether buffer static or not. */
  private boolean isStatic;

  private ReentrantLock readLock;

  private ReentrantLock writeLock;

  private Condition isFull;

  private Buffer(int capacity, boolean isStatic) {
    if (capacity <= 0)
      throw new IllegalArgumentException("Capacity must be positive integer.");

    this.isStatic = isStatic;
    this.capacity = capacity;
    this.data = new Object[this.capacity];
    this.size = 0;
    this.readLock = new ReentrantLock();
    this.writeLock = new ReentrantLock();
    this.isFull = writeLock.newCondition();
  }

  /** Construct dynamic buffer with growing capacity. */
  public Buffer() {
    this(10, false);
  }

  /**
   * Construct static buffer with fixed capacity.
   * 
   * @param capacity - buffer's maximum size
   * @throws IllegalArgumentException if capacity not positive
   */
  public Buffer(int capacity) {
    this(capacity, true);
  }

  private void push(T element) {
    if (size == capacity)
      if (isStatic)
        throw new IllegalStateException("Buffer is full.");
      else
        grow();

    data[size++] = element;
  }

  private void grow() {
    var doubled = new Object[capacity *= 2];

    for (int i = 0; i < size; i++)
      doubled[i] = data[i];

    data = doubled;
  }

  @Override
  public T remove() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'remove'");
  }

  @Override
  public T poll() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'poll'");
  }

  @Override
  public T element() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'element'");
  }

  @Override
  public T peek() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'peek'");
  }

  @Override
  public int size() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'size'");
  }

  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isEmpty'");
  }

  @Override
  public Iterator<T> iterator() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'iterator'");
  }

  @Override
  public Object[] toArray() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'toArray'");
  }

  @Override
  public <T> T[] toArray(T[] a) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'toArray'");
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'containsAll'");
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addAll'");
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removeAll'");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'retainAll'");
  }

  @Override
  public void clear() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'clear'");
  }

  @Override
  public boolean add(T e) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'add'");
  }

  @Override
  public boolean offer(T e) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'offer'");
  }

  @Override
  public void put(T e) throws InterruptedException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'put'");
  }

  @Override
  public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'offer'");
  }

  @Override
  public T take() throws InterruptedException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'take'");
  }

  @Override
  public T poll(long timeout, TimeUnit unit) throws InterruptedException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'poll'");
  }

  @Override
  public int remainingCapacity() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'remainingCapacity'");
  }

  @Override
  public boolean remove(Object o) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'remove'");
  }

  @Override
  public boolean contains(Object o) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'contains'");
  }

  @Override
  public int drainTo(Collection<? super T> c) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'drainTo'");
  }

  @Override
  public int drainTo(Collection<? super T> c, int maxElements) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'drainTo'");
  }
}
