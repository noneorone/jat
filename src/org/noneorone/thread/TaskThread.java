package org.noneorone.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskThread {

	public static void main(String[] args) {

		ExecutorService service = Executors.newSingleThreadExecutor();

		Callable<Object> task = new Callable<Object>() {
			@Override
			public Object call() {
				try {
					for (int i = 0; i < 100000; i++) {
						System.out.println(i);
					}
				} catch (Exception e) {
					System.err.println("exception_0: " + e.getStackTrace());
					System.exit(0);
				}
				return "task";
			}
		};
		Future<Object> future = service.submit(task);
		try {
			Object object = future.get(500L, TimeUnit.MILLISECONDS);
			System.out.println("object: " + object);
			System.exit(0);
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("exception_1: " + e.getStackTrace());
			System.exit(0);
		} catch (TimeoutException e) {
			e.getStackTrace();
			boolean cancel = future.cancel(true);
			System.err.println("cancel: " + cancel);
			System.err.println("isDone: " + future.isDone());
			System.err.println("isCancel: " + future.isCancelled());
//			System.exit(0);
		}

	}

}
