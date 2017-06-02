package com.fqy.cave;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo1 {

	public static void main(String[] args) {
		ShareResUtil.reLockDemo();
	}

}

class ShareRes {
	private ReentrantLock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();
	private int cnt = 0;

	private void increment() {
		for (int i = 0; i < 10000; i++)
			cnt++;
	}

	public void threadOne() throws InterruptedException {
		lock.lock();
		System.out.println(Thread.currentThread().getName() + " is waiting.");
		cond.await();
		System.out.println(Thread.currentThread().getName() + " resumed.");
		try {
			increment();
		} finally {
			lock.unlock();
		}
	}

	public void threadTwo() {
		lock.lock();
		System.out.println(Thread.currentThread().getName() + " click enter key to signal another thread.");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		scanner.close();
		System.out.println(Thread.currentThread().getName() + " enter key pressed.");
		cond.signal();
		try {
			increment();
		} finally {
			lock.unlock();
		}
		System.out.println(Thread.currentThread().getName() + " will finish first if befeore thread One.");
	}

	public void afterRunning() {
		System.out.println("Final cnt value is: " + cnt);
	}
}

class ShareResUtil {
	public static void reLockDemo() {
		ShareRes sRes = new ShareRes();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					sRes.threadOne();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				sRes.threadTwo();
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
		sRes.afterRunning();
	}
}