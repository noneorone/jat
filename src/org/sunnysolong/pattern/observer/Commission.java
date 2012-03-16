package org.sunnysolong.pattern.observer;

public class Commission {
	
	public static void main(String[] args){
		
		Subject subject = new Subject();
		
		Observer firstObserver = new Observer();
		Observer secondObserver = new Observer();
		Observer thirdObserver = new Observer();
		
		/** Register observer. */
		subject.register(firstObserver);
		subject.register(secondObserver);
		subject.register(thirdObserver);
		
		/** Notify a message. */
		subject.notify("Going to the music party,shall we?");
		
		/**Unregister one observer. */
		subject.unRegister(thirdObserver);

		/** Notify a message again. */
		subject.notify("At tweenty thirty,okay?");
	}
}
