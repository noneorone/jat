package org.noneorone.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskCommission {

	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Callable<Object> callable = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				return "hui diao";
			}
		};
		
		Future<Object> future = executor.submit(callable);
		
		try {
			Object object = future.get();
			System.out.println(object);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
