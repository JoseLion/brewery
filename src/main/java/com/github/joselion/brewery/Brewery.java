package com.github.joselion.brewery;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.github.joselion.brewery.lib.Updater;
import com.github.joselion.brewery.lib.annotations.BrewDefinition;
import com.github.joselion.brewery.lib.exceptions.BrewDefinitionException;
import com.github.joselion.brewery.lib.helpers.ReflectionUtils;
import com.github.joselion.maybe.Maybe;

import org.reflections.Reflections;

public class Brewery {

  private final String scanPackage;

  public Brewery(final String scanPackage) {
    this.scanPackage = scanPackage;
  }

  public <T> T brew(final Class<T> type) {
    final Reflections reflections = new Reflections(this.scanPackage);

    final Class<?> definitionClass = reflections.getTypesAnnotatedWith(BrewDefinition.class)
      .stream()
      .filter(defClass -> Optional.ofNullable(defClass.getGenericInterfaces()[0])
        .map(ParameterizedType.class::cast)
        .map(it -> it.getActualTypeArguments()[0])
        .map(type::equals)
        .orElse(false)
      )
      .findFirst()
      .orElseThrow(() -> new BrewDefinitionException(
        String.format(
          "Unable to locate Brew defination for the type %s.class. " +
          "Do you have a definition class annotated with @BrewDefinition " +
          "and implementing Brewable<%s> in your package?",
          type.getSimpleName(),
          type.getSimpleName()
        )
      ));

    final Brewable<?> brewable = Maybe.resolve(() ->
      definitionClass.getConstructor().newInstance()
    )
    .and()
    .cast(Brewable.class)
    .toOptional()
    .orElseThrow(() -> new BrewDefinitionException(
      "Unable to initialize definition. " +
      "The zero arguments constructor is required"
    ));

    return type.cast(brewable.define());
  }

  public <T> T brew(final Class<T> type, Updater<T> updater) {
    final T init = this.brew(type);

    updater.fieldsMap().forEach((name, value) ->
      ReflectionUtils.setOnField(init, name, value)
    );
    
    return init;
  }

  public <T> T brew(final Class<T> type, UnaryOperator<Updater<T>> updateFn) {
    final T init = this.brew(type);

    updateFn.apply(Updater.of(type))
      .fieldsMap().forEach((name, value) ->
        ReflectionUtils.setOnField(init, name, value)
      );

    return init;
  }
}
