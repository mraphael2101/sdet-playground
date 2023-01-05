package com.company.sdet_programs.oop.functional_interfaces._02_supplier;

import java.math.BigDecimal;
import java.time.LocalDate;

public class _03_Supplier_Example {

    public static void main(String[] args) {
        // The Supplier is Developer::new
        Developer obj = factory(Developer::new);
        System.out.println(obj.getName() + "\nStart date: " + obj.start);

        // The Supplier is () -> new Developer("Mark")
        Developer obj2 = factory(() -> new Developer("Mark"));
        System.out.println(obj2.getName() + "\nStart date: " + obj2.start);
    }

    public static Developer factory(Supplier<? extends Developer> s) {
        Developer developer = s.get();
        if (developer.getName() == null || "".equals(developer.getName())) {
            developer.setName("Default -> A person");
        }
        developer.setSalary(BigDecimal.ONE);
        developer.setStart(LocalDate.of(2017, 8, 8));
        return developer;
    }
}



class Developer {

    String name;
    BigDecimal salary;
    LocalDate start;

    // for factory(Developer::new);
    public Developer() {
        System.out.println("Class was instantiated using a default constructor");
    }

    // for factory(() -> new Developer("firstname"));
    public Developer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String val) {
        this.name = val;
    }

    public void setSalary(BigDecimal val) {
        this.salary = val;
    }

    public void setStart(LocalDate val) {
        this.start = val;
    }

}