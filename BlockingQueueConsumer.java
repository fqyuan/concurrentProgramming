package com.fqy.cave;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueConsumer {

	public static void main(String[] args) {
		BlockingUtil.demostrate();
	}

}

class BlockingUtil {
	public static void demostrate() {
		ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
		ProducerBlocking proCon = new ProducerBlocking(queue);
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					proCon.produce();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					proCon.consume();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class ProducerBlocking {

	private BlockingQueue<Integer> queue;
	private Random random = new Random();

	// queue 可以直接在构造函数中初始化，不必外部传入，至于为何这样传入。可能给了调用该方法对象灵活性。
	public ProducerBlocking(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	public void produce() throws InterruptedException {

		while (true) {
			Thread.sleep(50);
			if (random.nextDouble() < 0.1)
				queue.put(random.nextInt(100));
		}
	}

	public void consume() throws InterruptedException {
		while (true) {
			Thread.sleep(100);
			if (random.nextInt(10) == 1) {
				int val = queue.take();
				System.out.println("Taken value is: " + val + "; size is: " + queue.size());
			}
		}
	}
}
