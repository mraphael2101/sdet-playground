package com.company.sdet_programs.oop.override;

public class _02_Overriding_ToString {

    private String name = "Johnstone";
    private int age = 35;

    @Override
    public String toString() {
        return name + " " + age;
    }

    public static void main(String[] args) {
        String outcome = "".toString();

        // Prints nothing
        System.out.println(outcome);

        // Prints Johnstone 35
        _02_Overriding_ToString objRef = new _02_Overriding_ToString();
        System.out.println(objRef);
    }

}