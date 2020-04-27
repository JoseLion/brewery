package com.github.joselion.brewery.lib.exceptions;

import java.io.Serial;

public class BrewDefinitionException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -2111367880366754202L;

  public BrewDefinitionException(String message) {
    super(message);
  }

  public BrewDefinitionException(String message, Throwable cause) {
    super(message, cause);
  }
}