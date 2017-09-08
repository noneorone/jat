package org.noneorone.lang.object;

public class Commission {

	public static void main(String[] args) {
		
		Beauty one = new Beauty();
		one.setAge(25);
		one.setFacial("angle");
		one.setMarried(false);
		Beauty two = one.clone();
		System.out.println(two.getAge());

		
		LittleMrs first = new LittleMrs();
		first.setAge(30);
		first.setHasBaby(false);
		LittleMrs second = (LittleMrs) ObjectSerializableClone.cloneObject(first);
		System.out.println(second.isHasBaby());
		
	}
	
}
