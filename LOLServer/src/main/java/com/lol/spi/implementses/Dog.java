package com.lol.spi.implementses;

import com.lol.fwk.spi.Spi;
import com.lol.fwk.spi.interfaces.Animal;

@Spi(order = 2)
public class Dog implements Animal {
	@Override
	public void eat() {
		System.out.println("Dog eating...");
	}
}