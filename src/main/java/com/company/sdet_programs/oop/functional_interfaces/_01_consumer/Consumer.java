package com.company.sdet_programs.oop.functional_interfaces._01_consumer;

// There is no need to declare it as it is built into Java

@FunctionalInterface
interface Consumer<T> {
    void accept(T t);
}
