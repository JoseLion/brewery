package com.github.joselion.brewery.lib.helpers;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.github.joselion.brewery.lib.exceptions.BreweryException;
import com.github.joselion.maybe.Maybe;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public interface ReflectionUtils {

  public static <T, V> String getFieldName(final Class<T> type, final Function<T, V> getter) {
    final CompletableFuture<String> futureField = new CompletableFuture<>();
    ProxyFactory.onlyPublicMethods = false;
    final ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setSuperclass(type);

    final Class<?>[] paramTypes = {};
    final Object[] paramArgs = {};
    final MethodHandler handler = (self, thisMethod, proceed, args) -> {
      futureField.complete(thisMethod.getName());
      return proceed.invoke(self, args);
    };

    final T proxy = Maybe.resolve(() -> proxyFactory.create(paramTypes, paramArgs, handler))
      .catchError(NoSuchMethodException.class, error -> {
        final String message = String.format("A zero args contructor is required in the %s type", type.getSimpleName());
        throw new BreweryException(message, error);
      })
      .and()
      .cast(type)
      .toOptional()
      .orElseThrow();

    getter.apply(proxy);
    return futureField.join();
  }

  public static <T, V> T setOnField(T instance, String name, V value) {
    Maybe.runEffect(() -> {
      final Field field = instance.getClass().getDeclaredField(name);
      field.setAccessible(true);
      field.set(instance, value);
    })
    .catchError(NoSuchFieldException.class, error -> {
      final String message = String.format(
        "The field '%s' does not exist in '%s'",
        name,
        instance.getClass().getSimpleName()
      );
      throw new BreweryException(message, error);
    })
    .onErrorThrow(BreweryException::new);

    return instance;
  }
}
