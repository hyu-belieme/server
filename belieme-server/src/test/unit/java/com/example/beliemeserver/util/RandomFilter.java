package com.example.beliemeserver.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RandomFilter<T> {
    private final Stream<T> stream;
    private final long size;

    public static <T> RandomFilter<T> makeInstance(List<T> list, Predicate<? super T> predicate) {
        long size = list.stream().filter(predicate).count();
        return new RandomFilter<>(list.stream().filter(predicate), size);
    }

    private RandomFilter(Stream<T> stream, long size) {
        this.stream = stream;
        this.size = size;
    }

    public Optional<T> get() {
        return stream.skip((long) (size * Math.random())).findAny();
    }
}