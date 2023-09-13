package com.company.sdet_programs.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * Find the Integer that has the maximum value after iterating over the list
 * using the interface that accepts a Generic Type Comparator<T> */
public class ComparatorExample {

    private static final List<Integer> randomList = new ArrayList<>();

    private static Integer getMaxValue(List<Integer> list) {
        Comparator<Integer> countComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        return Collections.max(randomList, countComparator);
    }

    public static void main(String [] args) {
        randomList.add(1);
        randomList.add(4);
        randomList.add(3);
        System.out.println(getMaxValue(randomList));
    }
}
