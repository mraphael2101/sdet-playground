package com.company.sdet_programs.oop.functional_interfaces._04_predicate;

import java.util.function.Predicate;

/** Predicate<T> : Determine if an object of type T fulfills some constraint. Return
 *                 a boolean value that indicates the outcome. Its method is
 *                 called test().
 *                 It is a functional interface. */

public class _01_Predicate_Example {

    public static void main(String[] args)
    {
        Predicate<String> predicate = s -> { return s.equals("Hello"); };

        System.out.println(predicate.test("Hello"));  // returns true
        System.out.println(predicate.test("False"));  // returns false
    }

}
