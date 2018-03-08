package org.noneorone.test.java8;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Java8Test {

	public static void main(String[] args) {

		// List<A> list = new ArrayList<A>();
		// list.add(new A("A1"));
		// list.add(new A("A2"));
		// list.add(new A("A3"));

		// Arrays.asList(new A("A1"), new A("A2"), new A("A3")).forEach(
		// a -> System.out.println(a.getName())
		// );

		// List<Integer> asList = Arrays.asList(5, 3, 7, 9, 8);
		// asList.sort((a1, a2) -> {
		// int result = a1.compareTo(a2);
		// System.out.println(result);
		// return result;
		// });
		// System.out.println(asList.toString());

		// LocalDate ld = LocalDate.now();
		// LocalTime lt = LocalTime.now();
		// LocalDateTime ldt = LocalDateTime.now();
		// ZonedDateTime zdt = ZonedDateTime.now();
		// System.out.println(ld);
		// System.out.println(lt);
		// System.out.println(ldt);
		// System.out.println(zdt);
		//
		// Clock clock = Clock.systemUTC();
		// System.out.println(clock.instant());
		// System.out.println(clock.millis());
		// System.out.println(Clock.tickSeconds(ZoneId.of("Asia/Shanghai")).instant());
		//
		// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");
		// System.out.println(ldt.format(dtf));

		// interface default and static method
		DefA defa = new DefA();
		DefB defb = new DefB();
		System.out.println(defa.getName());
		System.out.println(defb.getName());
		IDefault.declare();

		// class constructor
		final Car car = Car.create(Car::new);
		final List<Car> cars = Arrays.asList(car);
		System.out.println(cars.size());
		cars.forEach(Car::collide);
		cars.forEach(Car::repair);
		cars.forEach(car::follow);

		// optional judge null-able object
		Optional<String> fullName = Optional.ofNullable("Mars");
		System.out.println("full name is set? " + fullName.isPresent());
		System.out.println("full name " + fullName.orElseGet(() -> "[none]"));
		System.out.println("full name " + fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));

		// java script engine
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("JavaScript");
		System.out.println(engine.getClass().getName());
		try {
			System.out.println(engine.eval("function f(){return 1}; f();"));
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		// base64
		String text = "hello world";
		String encoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
		System.out.println("base64 encoded: " + encoded);
		String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
		System.out.println("base64 decoded: " + decoded);

	}

}

class A {
	private String name;

	public A(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

interface IDefault {
	default String getName() {
		return "IDefault name";
	}

	static void declare() {
		System.out.println("this is the DECLARATION!!!");
	}
}

class DefA implements IDefault {
}

class DefB implements IDefault {

	@Override
	public String getName() {
		return "DefB's name";
	}
}

class Car {

	public Car() {
		System.out.println("Car constructor is invoked.");
	}

	public static Car create(final Supplier<Car> supplier) {
		return supplier.get();
	}

	public static void collide(final Car car) {
		System.out.println("Collided " + car.toString());
	}

	public void follow(final Car car) {
		System.out.println("Following the " + car.toString());
	}

	public void repair() {
		System.out.println("Repaired " + this.toString());
	}

}
