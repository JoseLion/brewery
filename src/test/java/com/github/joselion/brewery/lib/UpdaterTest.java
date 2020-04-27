package com.github.joselion.brewery.lib;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

import com.github.joselion.brewery.helpers.UnitTest;
import com.github.joselion.brewery.helpers.animal.Animal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
public class UpdaterTest {

  @Nested
  class setField {

    @Test
    void returns_a_new_instance_adding_a_field_entry() {
      assertThat(
        Updater.of(Animal.class)
          .setField(Animal::age, 3)
          .setField(Animal::kind, "Feline")
      )
      .extracting("fieldsMap", map(String.class, Object.class))
      .hasSize(2)
      .containsEntry("age", 3)
      .containsEntry("kind", "Feline");
    }
  }
}