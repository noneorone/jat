package org.noneorone.thread;

import java.util.concurrent.ThreadPoolExecutor;

class MonitorThread implements Runnable {

	private ThreadPoolExecutor executor;
	private int seconds;
	private boolean runnable = true;
	
	public MonitorThread(ThreadPoolExecutor executor, int seconds) {
		this.executor = executor;
		this.seconds = seconds;
	}
	
	public void shutdown() {
		this.runnable = false;
	}
	
	@Override
	public void run() {
		while (runnable) {
			System.out.println(
					String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
							this.executor.getPoolSize(),
							this.executor.getCorePoolSize(),
							this.executor.getActiveCount(),
							this.executor.getCompletedTaskCount(),
							this.executor.getTaskCount(),
							this.executor.isShutdown(),
							this.executor.isTerminated()
					)
            );
			try {
				Thread.sleep(seconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
