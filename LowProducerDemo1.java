package com.fqy.cave;

import java.util.Scanner;

public class LowProducerDemo1 {

	public static void main(String[] args) {
		ProducerFactory producerFactory = new ProducerFactory();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				producerFactory.produce();
			}
		});
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				producerFactory.consume();
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

class ProducerFactory {
	public void produce() {
		synchronized (this) {
			System.out.println("Producer thread running...");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Resumed!");
		}
	}

	public void consume() {
		synchronized (this) {
			System.out.println("Waiing for clicking on the enter key.");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
			scanner.close();
			this.notify();
			System.out.println("Enter key entered!");
		}
	}
}