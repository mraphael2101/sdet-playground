package com.company.sdet_programs.oop.functional_interfaces._03_function;

/** Function<T, R> : Apply an operation to an object of type T and return the result
 *                   as an object of type R. Its method is called apply().
 *                   It is a functional interface. */

@FunctionalInterface
interface MyInterface {
    void invoke();
}

public class _01_Function_Example {

    public static void main(String[] args) {
        _01_Function_Example.myMethod();
    }

    static void myMethod() {
        MyInterface x = () -> MyFunc();
        x.invoke();
    }

    private static void MyFunc() {
        System.out.println("Do something");
    }
}




