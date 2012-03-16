package org.sunnysolong.thread;

/** 
 * Title: base<br> 
 * Description: Thread Handler<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jun 14, 2011 3:06:51 PM <br> 
 * @author wangmeng
 */
public class ThreadHandle {

	/**global variable*/
	private int count;
	
	public static void main(String[] args){
		
		ThreadHandle th = new ThreadHandle();
		
		/**the initials of the two inner classes*/
		ThreadOne to = th.new ThreadOne();
		ThreadTwo tt = th.new ThreadTwo();
		
		Thread t = new Thread(to);
		t.start();
		t.setName("p");
		t = new Thread(tt);
		t.start();
		t.setName("m");
	}
	
	/**
	 * synchronized method to superimposite 
	 */
	private synchronized void plus(){
		count++;
		System.out.println(Thread.currentThread().getName()+" -- "+count);
	}
	
	/**
	 * synchronized method to descend
	 */
	private synchronized void minus(){
		count--;
		System.out.println(Thread.currentThread().getName()+" -- "+count);
	}
	
	/**
	 * Title: base<br> 
	 * Description: A inner class which deal with the plus handler<br> 
	 * Copyright: Copyright (c) 2011 <br> 
	 * Create DateTime: Jun 14, 2011 3:09:40 PM <br> 
	 * @author wangmeng
	 */ 
	class ThreadOne implements Runnable{
		public void run(){
			for(int i=0;i<100;i++){
				plus();
			}
		}
	}
	
	/**
	 * Title: base<br> 
	 * Description: A inner class which deal with the minus handler<br> 
	 * Copyright: Copyright (c) 2011 <br> 
	 * Create DateTime: Jun 14, 2011 3:09:40 PM <br> 
	 * @author wangmeng
	 */ 
	class ThreadTwo implements Runnable{
		public void run(){
			for(int i=0;i<100;i++){
				minus();
			}
		}
	}
}
