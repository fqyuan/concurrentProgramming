package com.fqy.cave;

import java.util.ArrayList;
import java.util.Random;

public class MultiLockDemo {
	public static void main(String[] args) {
		new MultiLockTest().testLock();
	}
}

class MultiLockTest {
	Random random = new Random();
	// 多线程访问共享变量时可能出现线程安全，ArrayList不是线程安全的
	private ArrayList<Integer> l1 = new ArrayList<>();
	private ArrayList<Integer> l2 = new ArrayList<>();
	private Object lock1 = new Object();
	private Object lock2 = new Object();

	private void stage1() {
		synchronized (lock1) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			l1.add(random.nextInt(100));
		}
	}

	private void stage2() {
		synchronized (lock2) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			l2.add(random.nextInt(100));
		}
	}

	private void process() {
		for (int i = 0; i < 100; i++) {
			stage1();
			stage2();
		}
	}

	public void testLock() {
		long startTime = System.currentTimeMillis();
		System.out.println("Starting...");

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				process();
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				process();
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
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken: " + (endTime - startTime));
	}

}