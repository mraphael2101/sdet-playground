package com.company.sdet_programs.oop.functional_interfaces._02_supplier;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Supplier<T> : Return an object of type T. Its method is called get()
 *  It takes no arguments and returns a result.
 *  It is a functional interface.
 *
 * @FunctionalInterface
 * public interface Supplier<T> {
 *     T get();
 * }*/

public class _01_Supplier_Example {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {

        // This example uses Supplier to return a current date-time

        Supplier<LocalDateTime> s = () -> LocalDateTime.now();
        LocalDateTime timeObj = s.get();
        System.out.println(timeObj);

        Supplier<String> s1 = () -> dtf.format(LocalDateTime.now());
        String timeAsStringLiteral = s1.get();
        System.out.println(timeAsStringLiteral);

    }

}