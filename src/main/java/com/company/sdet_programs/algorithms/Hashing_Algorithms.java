package com.company.sdet_programs.algorithms;

import java.util.HashSet;

/** Hashing is a fundamental concept of computer science.
 * In Java, efficient hashing algorithms stand behind some of the most popular
 * collections, such as the HashMap.
 * In hashing, the informational content of a key is used to determine a unique value,
 * called its hash code. The hash code is then used as the index at which the data
 * associated with the key is stored. The advantage of hashing is that it allows the
 * execution time of .add(), .contains(), .remove(), and size() to remain constant even
 * for large data sets */

public class Hashing_Algorithms {

    public static void main(String[] args) {
        demoOverridingHashCode();
    }

    private static void demoBuiltinHashSet() {
        // The process does not lend itself to the creation of sorted sets
        HashSet<String> hs = new HashSet<>(); // Initialises a default hashset
        hs.add("Beta");
        hs.add("Alpha");
        hs.add("Eta");
        hs.add("Gamma");
        hs.add("Epsilon");
        hs.add("Omega");

        System.out.println(hs);
    }

    private static void demoOverridingHashCode() {
        User u1 = new User("mr","mr@msn.com");
        User u2 = new User("mr","mr@msn.com");

        if(u1.equals(u2)) {
            System.out.println("Both objects are equal");
        } else {
            System.out.println("Both objects are not equal");
        }
        System.out.println("u1 Hashcode " + u1.hashCode());
        System.out.println("u2 Hashcode " + u2.hashCode());
    }

}

class User {
    private final String name, email;

    User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // This is not necessary. However, you can override hashcode algorithm and the better it is the faster the indexing
    @Override
    public int hashCode() {
        return name.hashCode() * email.hashCode();
    }

    /** Overriding equals is necessary as the default implementation of Java will consider
     * that two objects are different even if they have the same values. The only exception
     * to this rule is if you are utilising a String class which either has a single character
     * or multiple chars which equals another string either by reassignment or by declaration/initialisation
     * which will return true */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && email.equals(user.email);
    }

}