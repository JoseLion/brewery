package com.github.joselion.brewery.lib.exceptions;

public class BreweryException extends RuntimeException {

  private static final long serialVersionUID = -4205633150544206061L;

  public BreweryException() {
    super("Something went wrong while brewing 🤔");
  }

  public BreweryException(Throwable cause) {
    super(cause);
  }

  public BreweryException(String message) {
    super(message);
  }

  public BreweryException(String message, Throwable cause) {
    super(message, cause);
  }
}