package org.blab.commons;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

class Consumer extends Thread {
  private final Buffer<Integer> buffer;
  private final int toConsume;
  private List<Integer> consumed;

  Consumer(Buffer<Integer> buffer, int toConsume) {
    this.toConsume = toConsume;
    this.buffer = buffer;
    this.consumed = new ArrayList<>();
  }

  public List<Integer> getConsumed() {
    return consumed;
  }

  @Override
  public void run() {
    buffer.drainTo(consumed, toConsume);
  }
}


class Producer extends Thread {
  private final Buffer<Integer> buffer;
  private final List<Integer> toProduce;
  private final List<Integer> produced;

  Producer(Buffer<Integer> buffer, List<Integer> toProduce) {
    this.buffer = buffer;
    this.toProduce = toProduce;
    this.produced = new ArrayList<>();
  }

  public List<Integer> getProduced() {
    return produced;
  }

  @Override
  public void run() {
    for (int i : toProduce) {
      buffer.add(i);
      produced.add(1);
    }
  }
}


public class BufferTest {
  @Test
  public void drainToSingleProducerSingleConsumerFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var consumer = new Consumer(buffer, 5);
    var producer = new Producer(buffer, List.of(1, 2, 3, 4, 5));

    consumer.start();
    producer.start();

    consumer.join(1000);
    producer.join(1000);

    Assert.assertEquals(consumer.getConsumed().size(), producer.getProduced().size());
  }

  @Test
  public void drainToSingleProducerSingleConsumerNotFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var consumer = new Consumer(buffer, 5);
    var producer = new Producer(buffer, List.of(1, 2, 3, 4));

    consumer.start();
    producer.start();

    producer.join(1000);
    consumer.join(1000);

    Assert.assertTrue(consumer.isAlive());
  }

  @Test
  public void drainToSingleProducerMultipleConsumerFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var producer = new Producer(buffer, List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    var consumer1 = new Consumer(buffer, 5);
    var consumer2 = new Consumer(buffer, 5);

    consumer1.start();
    consumer2.start();
    producer.start();

    producer.join();
    consumer1.join();
    consumer2.join();

    Assert.assertTrue(consumer1.getConsumed().containsAll(List.of(1, 2, 3, 4, 5)));
    Assert.assertTrue(consumer2.getConsumed().containsAll(List.of(6, 7, 8, 9, 10)));
  }

  @Test
  public void drainToSingleProducerMultipleConsumerNotFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var producer = new Producer(buffer, List.of(1, 2, 3, 4, 5, 6));
    var consumer1 = new Consumer(buffer, 5);
    var consumer2 = new Consumer(buffer, 5);

    consumer1.start();
    consumer2.start();
    producer.start();

    producer.join(1000);
    consumer1.join(1000);
    consumer2.join(1000);

    Assert.assertTrue(consumer1.getConsumed().containsAll(List.of(1, 2, 3, 4, 5)));
    Assert.assertTrue(consumer2.isAlive());
  }

  @Test
  public void drainToMultipleProducersSingleConsumerFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var producer1 = new Producer(buffer, List.of(1, 2, 3));
    var producer2 = new Producer(buffer, List.of(4, 5, 6));
    var consumer = new Consumer(buffer, 6);

    consumer.start();
    producer1.start();
    producer2.start();

    producer1.join(1000);
    producer2.join(1000);
    consumer.join(1000);

    Assert.assertTrue(consumer.getConsumed().containsAll(List.of(1, 2, 3, 4, 5, 6)));
  }

  @Test
  public void drainToMultipleProducersSingleConsumerNotFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var producer1 = new Producer(buffer, List.of(1, 2, 3));
    var producer2 = new Producer(buffer, List.of(4, 5, 6));
    var consumer = new Consumer(buffer, 7);

    consumer.start();
    producer1.start();
    producer2.start();

    producer1.join(1000);
    producer2.join(1000);
    consumer.join(1000);

    Assert.assertEquals(producer1.getProduced().size(), 3);
    Assert.assertEquals(producer2.getProduced().size(), 3);
    Assert.assertTrue(consumer.isAlive());
  }

  @Test
  public void drainToMultipleProducersMultipleConsumersFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var producer1 = new Producer(buffer, List.of(1, 2, 3));
    var producer2 = new Producer(buffer, List.of(4, 5, 6));
    var consumer1 = new Consumer(buffer, 3);
    var consumer2 = new Consumer(buffer, 3);

    consumer1.start();
    consumer2.start();
    producer1.start();
    producer2.start();

    producer1.join(1000);
    producer2.join(1000);
    consumer1.join(1000);
    consumer2.join(1000);

    Assert.assertEquals(producer1.getProduced().size(), 3);
    Assert.assertEquals(producer2.getProduced().size(), 3);
    Assert.assertEquals(consumer1.getConsumed().size(), 3);
    Assert.assertEquals(consumer2.getConsumed().size(), 3);
  }

  @Test
  public void drainToMultipleProducersMultipleConsumersNotFullTest() throws InterruptedException {
    var buffer = new Buffer<Integer>();
    var producer1 = new Producer(buffer, List.of(1, 2, 3));
    var producer2 = new Producer(buffer, List.of(4, 5, 6));
    var consumer1 = new Consumer(buffer, 3);
    var consumer2 = new Consumer(buffer, 4);

    consumer1.start();
    consumer2.start();
    producer1.start();
    producer2.start();

    producer1.join(1000);
    producer2.join(1000);
    consumer1.join(1000);
    consumer2.join(1000);

    Assert.assertEquals(producer1.getProduced().size(), 3);
    Assert.assertEquals(producer2.getProduced().size(), 3);
    Assert.assertEquals(consumer1.getConsumed().size(), 3);
    Assert.assertTrue(consumer2.isAlive());
  }
}
