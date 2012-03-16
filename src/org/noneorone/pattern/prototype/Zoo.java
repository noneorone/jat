package org.noneorone.pattern.prototype;

/** 
 * Title: base<br> 
 * Description: Client<br> 
 * Copyright: Copyright (c) 2011 <br> 
 * Create DateTime: Jul 7, 2011 8:48:31 PM <br> 
 * @author wangmeng
 */
public class Zoo {

	private Animal animal;

	public Zoo(Animal animal){
		this.animal = animal;
	}
	
	public Animal makeAnimal(){
		return (Animal) this.animal.clone();
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		//This clone is not the same as the CLONE that defined by myself.
		return super.clone();
	}
	
	public static void main(String[] args){
		Zoo zoo = null;
		Animal animal = null;
		DonaldDuck duck = new DonaldDuck();
		MickeyMouse mouse = new MickeyMouse();
		
		//Make three ducks.
		//Set the prototype.
		zoo = new Zoo(duck);
		for(int i=0; i<3; i++){
			//#Get many objects by copying prototype.
			//#(Just see the hashCode of object and the answer is front.)
			animal = zoo.makeAnimal();
			System.out.println(animal.hashCode() + ": " + animal.getStatement());
		}
		
		//Make two mice.
		zoo = new Zoo(mouse);
		for(int i=0; i<2; i++){
			animal = zoo.makeAnimal();
			System.out.println(animal.hashCode() + ": " + animal.getStatement());
		}
	}
}
