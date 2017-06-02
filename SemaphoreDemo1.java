package com.fqy.cave;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
 * 信号量机制限制同时访问共享资源的线程数量
 */
public class SemaphoreDemo1 {

	public static void main(String[] args) {
		ConnectionUtils.demostrate();
	}
}

class Connection1 {
	private static final Connection1 INSTANCE = new Connection1();
	private int connection = 0;
	private Semaphore semaphore = new Semaphore(10, true);

	private Connection1() {

	}

	public static Connection1 getInstance() {
		return INSTANCE;
	}

	public void connect() {
		synchronized (this) {
			connection++;
			System.out.println("Current connections is: " + connection);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (this) {
			connection--;
		}
	}

	public void doConnect() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connect();
		} finally {
			semaphore.release();
		}
	}
}

class ConnectionUtils {
	public static void demostrate() {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 30; i++) {
			executor.submit(new Runnable() {
				@Override
				public void run() {
					// If changed to connect(), the output will be continuing
					// increment of the connections
					Connection1.getInstance().doConnect();
					// Connection1.getInstance().connect();
				}
			});
		}
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}