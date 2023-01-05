package com.company.sdet_programs.oop.functional_interfaces._02_supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Supplier<T> : Return an object of type T. Its method is called get()
 *  It takes no arguments and returns a result.
 *  It is a functional interface.
 *  In this example, pay attention to the <T> after the class declaration
 *  which is very important or the code will not work.
 *  This example returns a Supplier. */

public class _02_Supplier_Example<T> {

    public static void main(String[] args) {
        _02_Supplier_Example<String> myInstance = new _02_Supplier_Example();
        List<String> list = myInstance.supplier().get();

        // The list was initialised by a Supplier that takes no arguments and returns a result
        System.out.println(list.size());
    }

    public Supplier<List<T>> supplier() {
        return () -> new ArrayList<>();
    }

}
