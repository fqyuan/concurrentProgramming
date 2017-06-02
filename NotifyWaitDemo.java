package com.fqy.cave;

import java.util.Scanner;

public class NotifyWaitDemo {

	public static void main(String[] args) {
		Foo1Util.notifyAndWaitDemo();
	}

}

class Foo1 {
	public void produce() throws InterruptedException {
		synchronized (this) {
			System.out.println("Start producing...");
			/*
			 * 当前线程放弃monitor，并等待，直到另外的拥有该monitor的线程notify()
			 */
			wait();
			System.out.println("Resumed!");
		}
	}

	public void consume() throws InterruptedException {
		synchronized (this) {
			System.out.println("Click enter key to continue.");
			Thread.sleep(2000);
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			scanner.close();
			this.notify();
			System.out.println("Enter key clicked!");
		}
	}
}

class Foo1Util {
	public static void notifyAndWaitDemo() {
		Foo1 foo1 = new Foo1();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					foo1.produce();
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
					foo1.consume();
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