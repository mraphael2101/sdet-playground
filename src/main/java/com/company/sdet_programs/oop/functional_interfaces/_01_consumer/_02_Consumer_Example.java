package com.company.sdet_programs.oop.functional_interfaces._01_consumer;

import java.util.Arrays;
import java.util.List;

/** Consumer<T> : Apply an operation on an object of type T. Its method
 *                is called accept().
 *                It accepts a single input and returns no output.
 *                It is a functional interface.


 @FunctionalInterface
 interface Consumer<T> {
 void accept(T t);
 }
 */

public class _02_Consumer_Example {

    static List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
    public static void main(String[] args) {

        // implementation of the Consumer's accept method
        Consumer<Integer> consumer = (Integer x) -> System.out.println(x);
        forEach(list, consumer);
    }

    private static <T> void forEach(List<T> list, Consumer<T> consumer) {
        for (T t : list) {
            consumer.accept(t);
        }
    }

}