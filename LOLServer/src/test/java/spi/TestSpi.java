package spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.lol.fwk.spi.SpiLoader;
import com.lol.fwk.spi.interfaces.Animal;

public class TestSpi {
	public static void main(String[] args) {
//		ServiceLoader<Animal> serviceLoader = ServiceLoader.load(Animal.class);
//		Iterator<Animal> animalIterator = serviceLoader.iterator();
//		while (animalIterator.hasNext()) {
//			Animal animal = animalIterator.next();
//			animal.eat();
//		}
		
		//取得spi注解order最小的实例
		Animal animal = SpiLoader.load(Animal.class);
		animal.eat();
	}
}