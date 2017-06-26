package com.lol.spi.implementses;

import com.lol.fwk.spi.Spi;
import com.lol.fwk.spi.interfaces.Animal;

@Spi(order = 1)
public class Pig implements Animal {
    @Override
    public void eat() {
        System.out.println("Pig eating...");
    }
}