package com.company.sdet_programs.oop.overload;

import java.util.HashSet;

public class _01_Overloading_Equals {

    private final int clemonte;

    _01_Overloading_Equals(int x) {
        clemonte = x;
    }

    public boolean equals(_01_Overloading_Equals other) {
        return clemonte == other.clemonte;
    }

    @Override
    public int hashCode() {
        return clemonte;
    }

    @Override
    public String toString() {
        return String.format("clemonte = %d", clemonte);
    }

    public static void main(String... args) {
        var clemonte = new _01_Overloading_Equals(5);
        var piet = new _01_Overloading_Equals(5);
        var set = new HashSet<_01_Overloading_Equals>();
        set.add(clemonte);
        System.out.format("clemonte equals piet? %b%n", clemonte.equals(piet));
        System.out.format("hashcode clemonte: %d%n", clemonte.hashCode());
        System.out.format("hashcode piet: %d%n", piet.hashCode());
        System.out.format("set contains clemonte? %b%n", set.contains(clemonte));
        System.out.format("set contains piet? %b%n", set.contains(piet));
    }

}
