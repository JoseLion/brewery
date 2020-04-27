package com.github.joselion.brewery.lib.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@interface BrewDefinitions {

  BrewDefinition[] value();
}