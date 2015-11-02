package org.noneorone.thread;

public class Hook {

	
	class HookCase implements Runnable {

		public HookCase() {
			Runtime.getRuntime().addShutdownHook(new Thread(this));
			System.out.println("Hook registered!!!");
		}
		
		@Override
		public void run() {
			System.out.println("/n>>> About to execute: " + HookCase.class.getName() + ".run() to clean up before JVM exits.");
			try {
				Thread.sleep(5000);
				this.cleanUp();
				Thread.sleep(5000);
				System.out.println("abc");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        System.out.println(">>> Finished execution: " + HookCase.class.getName() + ".run()");		
	    }
		
		private void cleanUp() {
			System.out.println("clean up ...");
			for (int i=0; i<10; i++) {
				System.out.println(i);
			}
		}
		
	}
	
	public static void main(String[] args) {
//		new HookCommission().new HookCase();
	}
	
}
