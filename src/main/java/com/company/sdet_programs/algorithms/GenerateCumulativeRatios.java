package com.company.sdet_programs.algorithms;

import java.util.ArrayList;
import java.util.Arrays;

public class GenerateCumulativeRatios {

    public static void main(String[] args) {
        ArrayList<Integer> boundaries = createArray(300);
        System.out.println(boundaries);

        double[] cumulativeRatios = generateCumulativeRatios(boundaries);
        System.out.println(Arrays.toString(cumulativeRatios));
    }

    // A method that takes a max value parameter and returns an array list of values
    public static ArrayList<Integer> createArray(int max) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(10);
        list.add(20);
        list.add(50);
        list.add(100);
        list.add(150);
        list.add(200);
        // Add increments of 50 until the max value is reached or exceeded
        int value = 250;
        while (value <= max) {
            list.add(value);
            value += 50;
        }
        return list;
    }

    public static double[] generateCumulativeRatios(ArrayList<Integer> boundaries) {
        // Initialize an array to store the cumulative ratios
        double[] cumulativeRatios = new double[boundaries.size() + 1];
        // Set the first element to zero
        cumulativeRatios[0] = 0.0;
        // Initialize a variable to store the sum of the divisors
        double divisorSum = 0.0;
        // Loop through the boundaries array list and calculate the divisors
        for (int i = 0; i < boundaries.size(); i++) {
            int boundary = boundaries.get(i);
            double divisor;
            // Assign the divisor based on the boundary value
            if (boundary == 10) {
                divisor = 0.05;
            } else if (boundary >= 10 && boundary <= 20) {
                divisor = 0.05;
            } else if (boundary > 20 && boundary <= 50) {
                divisor = 0.15;
            } else if (boundary > 50 && boundary <= 100) {
                divisor = 0.25;
            } else if (boundary > 100 && boundary <= 150) {
                divisor = 0.25;
            } else if (boundary > 150 && boundary <= 200) {
                divisor = 0.25;
            } else {
                // For any other boundary value, use increments of 0.25
                divisor = 0.25;
            }
            // Add the divisor to the sum
            divisorSum += divisor;
            // Calculate the cumulative ratio for the current level and store it in the array
            cumulativeRatios[i + 1] = divisorSum;
        }
        // Set the last element to one
        cumulativeRatios[cumulativeRatios.length - 1] = 1.0;
        return cumulativeRatios;
    }

}

