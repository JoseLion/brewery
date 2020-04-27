package com.github.joselion.brewery.helpers.animal;

public class Animal {

  private final String kind;

  private final Integer age;

  public Animal() {
    this.kind = null;
    this.age = null;
  }

  public Animal(String kind, Integer age) {
    this.kind = kind;
    this.age = age;
  }

  public String kind() {
    return kind;
  }

  public Integer age() {
    return age;
  }
}
