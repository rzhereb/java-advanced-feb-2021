package com.oktenweb.decjavaadv.exceptions;

public class ItemNotFoundException extends RuntimeException {

  public ItemNotFoundException(String entity, String field, String value) {
    super(String.format("No item found for %s with field %s value = %s", entity, field, value));
  }
}
