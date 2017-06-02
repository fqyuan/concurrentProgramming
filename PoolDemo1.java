package com.fqy.cave;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolDemo1 {
	public static void main(String[] args) {
		/*
		 * Why we need thread pool? Creation and destruction of thread takes
		 * overhead, we can avoid it using thread pool mechanism.
		 */

		ExecutorService executor = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 5; i++) {
			executor.submit(new PoolDemoTest(i));
		}
		System.out.println("All tasks submited.");
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("All tasks completed.");
	}

}

class PoolDemoTest implements Runnable {
	private int id;

	public PoolDemoTest(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		System.out.println("Starting " + id);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Completing " + id);
	}

}
