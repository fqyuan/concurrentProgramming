package com.fqy.cave;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InterruptionDemo1 {

	public static void main(String[] args) {
		InterruptionUtil.demonstrate();
	}

}

class InterruptionUtil {
	public static void demonstrate() {
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.submit(new Runnable() {
			@Override
			public void run() {
				Random random = new Random();
				for (int i = 0; i < 1E8; i++) {
					/*
					 * 为什么这里的break是必要的？
					 * 1）线程的状态并没有被isInterrupted改变，所以如果注释了break就会一直执行
					 * 2）interrupted是个静态方法，改变了线程状态。
					 */
					if (Thread.currentThread().isInterrupted()) {
						System.out.println(Thread.currentThread().getName() + " is Interrupted!");
						break;
					}
					Math.sin(random.nextDouble());
				}
			}
		});
		System.out.println(Thread.currentThread().getName() + " is going to sleep for 1000ms.");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.shutdownNow();
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished!");
	}
}