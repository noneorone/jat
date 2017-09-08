package org.noneorone.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
//		testThreadPool();
		testExecutor();
		
	}

	protected static void testThreadPool() throws InterruptedException {
		int corePoolSize = 2;
		int maximumPoolSize = 4;
		long keepAliveTime = 10;
		TimeUnit unit = TimeUnit.SECONDS;
		int capacity = 2;
		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(capacity);
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		RejectedHandler handler = new RejectedHandler();
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		MonitorThread monitorThread = new MonitorThread(poolExecutor, 2);
		Thread thread = new Thread(monitorThread);
		thread.start();

		for (int i = 0; i < 10; i++) {
			poolExecutor.execute(new WorkThread("command_" + i));
		}
		
		Thread.sleep(10000L);
		poolExecutor.shutdown();
		Thread.sleep(3000L);
		monitorThread.shutdown();
	}
	
	protected static void testExecutor() {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 10; i++) {
			executor.execute(new WorkThread("command_" + i));
		}
		if (!executor.isShutdown()) {
			executor.shutdown();
		}
	}
	
	
}
