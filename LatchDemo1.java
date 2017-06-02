package com.fqy.cave;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LatchDemo1 {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		// If changed to 2, time take will double.
		ExecutorService executor = Executors.newFixedThreadPool(2);
		// If changed to 4, the program never exits.
		CountDownLatch latch = new CountDownLatch(3);
		for (int i = 0; i < 3; i++) {
			executor.submit(new LatchTest(latch));
		}
		try {
			// Cause the current thread to wait until the latch has counted down
			// to zero.
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time taken: " + (endTime - startTime));
	}

}

class LatchTest extends Thread {
	private CountDownLatch latch;

	public LatchTest(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		System.out.println("Beginning...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		latch.countDown();
	}
}