package org.noneorone.thread;

public class WorkThread implements Runnable {

	private String command;
	
	public WorkThread(String command) {
		this.command = command;
	}
	
	@Override
	public void run() {
		Thread currentThread = Thread.currentThread();
		System.out.println(command + ": " + currentThread.getName() + "---" + currentThread.getState());
		processWork();
		System.out.println(command + " end." );
	}

	public void processWork() {
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
