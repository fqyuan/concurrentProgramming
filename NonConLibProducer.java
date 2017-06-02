package com.fqy.cave;

import java.util.LinkedList;
import java.util.Random;

public class NonConLibProducer {

	public static void main(String[] args) {
		Foo2Util.nonConDemo();
	}

}

class Foo2 {
	private LinkedList<Integer> list = new LinkedList<>();
	private final int LIMIT = 10;
	private Random random = new Random();
	private Object lock = new Object();
	private int value = 0;

	public void produce() throws InterruptedException {
		while (true) {
			synchronized (lock) {
				if (list.size() == LIMIT)
					lock.wait();
				list.add(value++);
				lock.notify();
			}
			Thread.sleep(random.nextInt(800));
		}
	}

	public void consume() throws InterruptedException {
		while (true) {
			synchronized (lock) {
				if (list.size() == 0)
					lock.wait();
				System.out.print("List size is: " + list.size());
				int data = list.removeFirst();
				lock.notify();
				System.out.println("; removed data is: " + data);
			}
			Thread.sleep(random.nextInt(1000));
		}
	}
}

class Foo2Util {
	public static void nonConDemo() {
		Foo2 foo2 = new Foo2();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					foo2.produce();
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
					foo2.consume();
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