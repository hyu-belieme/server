package com.belieme.apiserver.util;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;

public class TestHelper {

  public static <T> void objectCompareTest(ObjectReturnMethod<T> routine, T expected) {
    T result;
    try {
      result = routine.method();
    } catch (Exception e) {
      e.printStackTrace();
      assert false;
      return;
    }
    Assertions.assertThat(result).isEqualTo(expected);
  }

  public static <T> void listCompareTest(ListReturnMethod<T> routine, List<T> expected) {
    List<T> result;
    try {
      result = routine.method();
      System.out.println("result " + result);
      System.out.println("expected " + expected);
    } catch (Exception e) {
      e.printStackTrace();
      assert false;
      return;
    }

    assertThatAllElementIsEqual(result, expected);
  }

  public static void exceptionTest(ThrowableAssert.ThrowingCallable routine, Class<?> type) {
    Assertions.assertThatThrownBy(routine).isInstanceOf(type);
  }

  private static <T> void assertThatAllElementIsEqual(List<T> result, List<T> expected) {
    Assertions.assertThat(result.size()).isEqualTo(expected.size());

    for (T target : result) {
      Assertions.assertThat(expected).contains(target);
    }
  }

  public interface ObjectReturnMethod<T> {

    T method();
  }

  public interface ListReturnMethod<T> {

    List<T> method();
  }
}
