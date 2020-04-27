package com.github.joselion.brewery.helpers.animal;

import com.github.joselion.brewery.Brewable;
import com.github.joselion.brewery.lib.annotations.BrewDefinition;

@BrewDefinition
public class AnimalDefinition implements Brewable<Animal> {

  @Override
  public Animal define() {
    return new Animal("feline", 9);
  }
}