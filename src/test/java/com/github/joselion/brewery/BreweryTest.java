package com.github.joselion.brewery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.joselion.brewery.helpers.UnitTest;
import com.github.joselion.brewery.helpers.animal.Animal;
import com.github.joselion.brewery.lib.Updater;
import com.github.joselion.brewery.lib.exceptions.BrewDefinitionException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


@UnitTest
public class BreweryTest {

  private final Brewery brewery = new Brewery("com.github.joselion.brewery");

  @Nested
  class brew {
    
    @Nested
    class when_brew_definition_is_found {

      @Test
      void returns_an_instance_of_the_object() {
        assertThat(brewery.brew(Animal.class))
          .isExactlyInstanceOf(Animal.class)
          .isEqualToComparingFieldByField(new Animal("feline", 9));

        assertThat(
          brewery.brew(
            Animal.class,
            Updater.of(Animal.class)
              .setField(Animal::age, 3)
              .setField(Animal::kind, "canine")
          )
        )
        .isExactlyInstanceOf(Animal.class)
        .isEqualToComparingFieldByField(new Animal("canine", 3));

        assertThat(
          brewery.brew(
            Animal.class,
            updater -> updater
              .setField(Animal::age, 3)
              .setField(Animal::kind, "canine")
          )
        )
        .isExactlyInstanceOf(Animal.class)
        .isEqualToComparingFieldByField(new Animal("canine", 3));
      }
    }

    @Nested
    class when_brew_definition_is_NOT_found {

      @Test
      void throws_a_runtime_exception() {
        assertThat(
          assertThrows(BrewDefinitionException.class, () -> brewery.brew(Person.class))
        )
        .hasMessage(
          "Unable to locate Brew defination for the type Person.class. " +
          "Do you have a definition class annotated with @BrewDefinition " +
          "and implementing Brewable<Person> in your package?"
        );
      }
    }
  }

  public class Person {

    private String name;

    public Person(String name) {
      this.name = name;
    }

    public String name() {
      return name;
    }
  }
}