package com.company.sdet_programs.oop.functional_interfaces._01_consumer;

/** Consumer<T> : Apply an operation on an object of type T. Its method
 *                is called accept().
 *                It accepts a single input and returns no output.
 *                It is a functional interface.


@FunctionalInterface
interface Consumer<T> {
    void accept(T t);
}
*/

public class _01_Consumer_Example {

    public static void main(String[] args) {

        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("My input");

    }

}
