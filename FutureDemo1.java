package com.fqy.cave;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureDemo1 {

	public static void main(String[] args) {
		FutureUtil.demonstrate();
	}

}

class FutureUtil {
	public static void demonstrate() {
		ExecutorService executor = Executors.newCachedThreadPool();
		/*
		 * The parameterized value should be of the same type. If we want no
		 * return value, just make the Parameterized value be ?/Void/Void
		 */
		Future<Integer> future = executor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Random random = new Random();
				int duration = random.nextInt(2000);
				if (duration > 1000)
					throw new IOException("Sleeping for too long!");
				System.out.println("Starting");
				Thread.sleep(duration);
				System.out.println("Ending");
				return duration;
			}
		});
		executor.shutdown();
		try {
			System.out.println("Sleeping time in total is: " + future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			IOException ex = (IOException) e.getCause();
			System.out.println(ex.getMessage());
		}

	}
}