package com.github.joselion.brewery.lib.helpers;

import static com.github.joselion.brewery.lib.helpers.ReflectionUtils.getFieldName;
import static com.github.joselion.brewery.lib.helpers.ReflectionUtils.setOnField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.joselion.brewery.helpers.UnitTest;
import com.github.joselion.brewery.helpers.animal.Animal;
import com.github.joselion.brewery.lib.exceptions.BreweryException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
public class ReflectionUtilsTest {

  @Nested
  class getFieldName {

    @Nested
    class when_a_zero_args_contructor_exists {

      @Test
      void returns_the_field_name() {
        assertThat(getFieldName(Animal.class, Animal::age))
          .isEqualTo("age");
      }
    }

    @Nested
    class when_the_contructor_does_not_exists {

      @Test
      void throws_an_error() {
        assertThat(
          assertThrows(
            BreweryException.class,
            () -> getFieldName(Person.class, Person::name)
          )
        )
        .hasMessage("A zero args contructor is required in the Person type");
      }
    }
  }

  @Nested
  class setOnField {

    @Nested
    class when_the_field_exists {

      @Test
      void sets_the_value() {
        final Animal dog = new Animal("Canine", 3);
        setOnField(dog, "age", 6);

        assertThat(dog.age()).isEqualTo(6);
      }
    }

    @Nested
    class when_the_field_does_NOT_exist {
      
      @Test
      void throws_an_exception() {
        final Animal dog = new Animal("Canine", 3);
        assertThat(
          assertThrows(
            BreweryException.class,
            () -> setOnField(dog, "name", 6)
          )
        )
        .hasMessage("The field 'name' does not exist in 'Animal'");
      }
    }
  }

  private static class Person {

    private String name;

    private Person(final String name) {
      this.name = name;
    }

    private String name() {
      return name;
    }
  }
}