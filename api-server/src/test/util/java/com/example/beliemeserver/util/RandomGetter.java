package com.example.beliemeserver.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RandomGetter<T> {
    private final List<T> list;

    public RandomGetter(List<T> list) {
        this.list = new ArrayList<>(list);
    }

    public RandomGetter<T> filter(Predicate<? super T> predicate) {
        return new RandomGetter<>(list.stream().filter(predicate).toList());
    }

    public T randomSelect() {
        return list.stream().skip((long) (list.size() * Math.random())).findAny().orElse(null);
    }

    public T getFirst() {
        return list.stream().findAny().orElse(null);
    }
}
