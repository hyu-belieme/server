package com.belieme.apiserver.util;

import java.util.ArrayList;
import java.util.List;

public class FakeDao<T> {

  private final List<T> dummies;

  public FakeDao(List<T> dummies) {
    this.dummies = new ArrayList<>(dummies);
  }

  public List<T> getAll() {
    return dummies;
  }

  public List<T> getAllByCondition(FilterMethod<T> filter) {
    List<T> output = new ArrayList<>();
    for (T dummy : dummies) {
      if (filter.checkCondition(dummy)) {
        output.add(dummy);
      }
    }
    return output;
  }

  public T getFirstByCondition(FilterMethod<T> filter) {
    T output = null;
    for (T dummy : dummies) {
      if (filter.checkCondition(dummy)) {
        output = dummy;
        break;
      }
    }
    return output;
  }

  public List<T> dummyStatusAfterCreate(T newRecord) {
    List<T> output = new ArrayList<>(dummies);
    output.add(newRecord);

    return output;
  }

  public List<T> dummyStatusAfterUpdate(T targetRecord, T newRecord) {
    List<T> output = new ArrayList<>(dummies);
    output.removeIf(record -> record.equals(targetRecord));
    output.add(newRecord);

    return output;
  }

  public List<T> dummyStatusAfterDelete(T targetRecord) {
    List<T> output = new ArrayList<>(dummies);
    output.removeIf(record -> record.equals(targetRecord));

    return output;
  }

  public interface FilterMethod<T> {

    boolean checkCondition(T target);
  }
}