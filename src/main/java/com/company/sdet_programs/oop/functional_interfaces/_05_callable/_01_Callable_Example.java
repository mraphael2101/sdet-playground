package com.company.sdet_programs.oop.functional_interfaces._05_callable;

/**The Callable interface is found in the package java.util.concurrent. It is a functional interface that defines
 * a task that returns a result and may throw an exception123 The Callable interface contains a single method
 * called call(), which returns a result of type V and can throw an exception13

 The Callable interface is similar to the Runnable interface, but it allows the task to return a value and
 throw an exception123 The Callable object returns a Future object which provides methods to monitor the progress
 of a task being executed by a thread1

 In Java 8, the Callable interface is a functional interface and can therefore be used as the assignment target
 for a lambda expression or method reference34 */

public class _01_Callable_Example implements Callable<String>{

    @Override
    public String call() {
        return "Hello World";
    }

    public static void main(String[] args) {
        System.out.println(new _01_Callable_Example().call());
    }

}

