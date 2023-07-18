package com.company.sdet_programs.oop.functional_interfaces._05_callable;

// There is no need to declare it as it is built into Java

@FunctionalInterface
public interface Callable<T> {
    T call();
}

