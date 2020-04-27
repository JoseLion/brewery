package com.github.joselion.brewery.lib;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.joselion.brewery.lib.helpers.ReflectionUtils;

public class Updater<T> {

  private final Class<T> type;

  private final Map<String, Object> fieldsMap;

  private Updater(final Class<T> type) {
    this.type = type;
    this.fieldsMap = Map.of();
  }

  private Updater(final Updater<T> self, final Entry<String, Object> fieldEntry) {
    this.type = self.type;
    this.fieldsMap = Stream.concat(
      self.fieldsMap.entrySet().stream(),
      Stream.of(fieldEntry)
    )
    .collect(Collectors.toUnmodifiableMap(
      Entry::getKey,
      Entry::getValue
    ));
  }

  public static <T> Updater<T> of(final Class<T> defClass) {
    return new Updater<>(defClass);
  }

  public <V> Updater<T> setField(final Function<T, V> fieldGetter, V value) {
    final String fieldName = ReflectionUtils.getFieldName(type, fieldGetter);

    return new Updater<>(this, Map.entry(fieldName, value));
  }

  public Map<String, Object> fieldsMap() {
    return fieldsMap;
  }
}