package org.noneorone.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class LocalThread {

	private static final AtomicInteger nextId = new AtomicInteger(0);
	
	private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return nextId.get();
		}
	};

	public static void main(String[] args) {

		Thread t1 = new Thread("thread-1") {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					nextId.incrementAndGet();
					System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
				}
			}
		};
		t1.start();

		Thread t2 = new Thread("thread-2") {
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					nextId.incrementAndGet();
					System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
				}
			}
		};
		t2.start();

	}

}
