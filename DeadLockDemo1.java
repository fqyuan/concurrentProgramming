package com.fqy.cave;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/*
 * A deadlock may happen if  4 conditions fulfilled
 * 1). mutual exclusion
 * 2). hold and wait
 * 3). non-preemptive
 * 4). circular wait
 */
public class DeadLockDemo1 {
	public static void main(String[] args) {
		DeadDemoUtil.demonstrate();
	}
}

class AccountInfo {
	private int balance = 10000;

	private void deposit(int val) {
		balance += val;
	}

	private void withdraw(int val) {
		balance -= val;
	}

	public int getBalance() {
		return balance;
	}

	public static void transfer(AccountInfo acc1, AccountInfo acc2, int amount) {
		acc1.withdraw(amount);
		acc2.deposit(amount);
	}
}

class DeadDemo {
	private AccountInfo acc1 = new AccountInfo();
	private AccountInfo acc2 = new AccountInfo();
	private ReentrantLock lock1 = new ReentrantLock();
	private ReentrantLock lock2 = new ReentrantLock();
	private Random random = new Random();

	private void acquireLocks(ReentrantLock lock1, ReentrantLock lock2) throws InterruptedException {
		boolean lock1Status = false;
		boolean lock2Status = false;
		while (true) {
			try {
				lock1Status = lock1.tryLock();
				lock2Status = lock2.tryLock();

			} finally {
				if (lock1Status && lock2Status)
					return;
				if (lock1Status)
					lock1.unlock();
				if (lock2Status)
					lock2.unlock();
			}
			// Acquire neither
			Thread.sleep(1);
		}

	}

	public void threadOne() throws InterruptedException {
		for (int i = 0; i < 1E4; i++) {
			acquireLocks(lock1, lock2);
			try {
				AccountInfo.transfer(acc1, acc2, random.nextInt(50));
			} finally {
				lock1.unlock();
				lock2.unlock();
			}

		}
	}

	public void threadTwo() throws InterruptedException {
		for (int i = 0; i < 1E4; i++) {
			// If both threads acquire the shared resources in the same order,
			// DeadLock may happen then.
			acquireLocks(lock2, lock1);
			try {
				AccountInfo.transfer(acc2, acc1, random.nextInt(50));
			} finally {
				lock1.unlock();
				lock2.unlock();
			}
		}
	}

	public void afterTransfer() {
		System.out.println("Account1 balance is: " + acc1.getBalance());
		System.out.println("Account2 balance is: " + acc2.getBalance());
		System.out.println("Total balance is: " + (acc1.getBalance() + acc2.getBalance()));

	}
}

class DeadDemoUtil {
	public static void demonstrate() {
		DeadDemo demo = new DeadDemo();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					demo.threadOne();
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
					demo.threadTwo();
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
		demo.afterTransfer();
	}
}