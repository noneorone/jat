package org.noneorone.thread.netty;

import io.netty.util.internal.chmv8.ForkJoinPool;
import io.netty.util.internal.chmv8.ForkJoinTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Execu {

	public static void main(String[] args) {
		
		
//		doExec();
	
		
//		ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(0, true);
//		try {
//			blockingQueue.put(1);
//			blockingQueue.take();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		Object obj = new Object();
//		System.out.println();
		
//		countDownLatch();
//		countDownLatch1();
		timer();
		
	}

	private static void doExec() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Callable<Object> task = new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				System.out.println("call ...");
				return "value returnd";
			}
		};
		
		Future<Object> future = executor.submit(task);
		try {
			Object object = future.get(2, TimeUnit.SECONDS);
			System.out.println(object);
			if (object != null) {
				future.cancel(false);
				executor.shutdown();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void countDownLatch() {
		final int COUNT = 10;
		final CountDownLatch cdl = new CountDownLatch(COUNT);
		
		for (int i = 0; i < COUNT; i++) {
			Thread thread = new Thread("thread-" + i) {
				@Override
				public void run() {
					System.out.println(Thread.currentThread());
					cdl.countDown();
				}
			};
			thread.start();
		}
	}
	
	public static void countDownLatch1() {
		final int COUNT = 10;
		final CountDownLatch cdl = new CountDownLatch(COUNT);
		
		for (int i = 0; i < COUNT; i++) {
			Thread thread = new Thread("thread-" + i) {
				@Override
				public void run() {
					try {
						cdl.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
					System.out.println(Thread.currentThread());
				}
			};
			thread.start();
		}
		
		System.out.println("count down ...");
		cdl.countDown();
		for (int i = 0; i < 10000; i++) {
			if (i == 9999) {
				System.out.println(i);
				synchronized (cdl) {
					cdl.notify();
				}
			}
		}
	}
	

	public static void timer() {
		ForkJoinPool fj = new ForkJoinPool();
		ForkJoinTask<Object> task = new ForkJoinTask<Object>() {
			
			@Override
			protected void setRawResult(Object arg0) {
				System.out.println(arg0);
			}
			
			@Override
			public Object getRawResult() {
				return null;
			}
			
			@Override
			protected boolean exec() {
				return false;
			}
		};
		fj.invoke(task);
	}
	
	
	
}




