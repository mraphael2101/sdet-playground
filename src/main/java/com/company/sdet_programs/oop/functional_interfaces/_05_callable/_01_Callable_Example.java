package com.company.sdet_programs.oop.functional_interfaces._05_callable;

public class _01_Callable_Example implements Callable<String>{

    @Override
    public String call() {
        return "Hello World";
    }

    public static void main(String[] args) {
        System.out.println(new _01_Callable_Example().call());
    }

}

