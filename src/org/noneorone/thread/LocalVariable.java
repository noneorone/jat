package org.noneorone.thread;

public class LocalVariable {

	public static ThreadLocal<String> text = new ThreadLocal<String>();

	public static void main(String[] args) {
		LocalVariable test = new LocalVariable();
		Thread1 thread1 = test.new Thread1();
		thread1.setName("thread111111111111");
		thread1.start();
		Thread2 thread2 = test.new Thread2();
		thread2.setName("thread222222222222");
		thread2.start();

	}

	class Thread1 extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				if (i % 2 == 0) {
					text.set("hello---" + i);
				} else {
					text.set("hello===" + i);
				}
				System.out.println(Thread.currentThread() + "---text: " + text.get());
			}
		}
	}

	class Thread2 extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				if (i % 2 == 0) {
					text.set("world---" + i);
				} else {
					text.set("world===" + i);
				}
				System.out.println(Thread.currentThread() + "---text: " + text.get());
			}
		}
	}

}
