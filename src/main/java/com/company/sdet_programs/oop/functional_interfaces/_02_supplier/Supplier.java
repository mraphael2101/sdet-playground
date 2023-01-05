package com.company.sdet_programs.oop.functional_interfaces._02_supplier;

// There is no need to declare it as it is built into Java

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
