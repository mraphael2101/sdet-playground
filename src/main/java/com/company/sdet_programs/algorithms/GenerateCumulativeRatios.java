package com.company.sdet_programs.algorithms;

import java.util.ArrayList;
import java.util.Arrays;

public class GenerateCumulativeRatios {
    /*
    | Level   | Ratio | Cumulative Ratio |
    |---------|-------|------------------|
    | 1-9     | 0.05  | 0.05             |
    | 10-19   | 0.05  | 0.1              |
    | 20-49   | 0.15  | 0.25             |
    | 50-99   | 0.25  | 0.5              |
    | 100-149 | 0.25  | 0.75             |
    | 150-199 | 0.25  | 1                |
    */
    public static void main(String[] args) {
        ArrayList<Integer> boundaries = createBoundaryValues(300);
        System.out.println(boundaries);

        double[] cumulativeRatios = generateCumulativeRatios(boundaries);
        System.out.println(Arrays.toString(cumulativeRatios));
    }

    // A method that takes a max value parameter and returns an array list of values
    public static ArrayList<Integer> createBoundaryValues(int max) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        // Add increments of 50 until the max value is reached or exceeded
        int value = 50;
        while (value <= max) {
            list.add(value);
            value += 50;
        }
        return list;
    }

    public static double[] generateCumulativeRatios(ArrayList<Integer> boundaries) {
        double[] cumulativeRatios = new double[boundaries.size() + 1];
        // Set the first element to zero
        cumulativeRatios[0] = 0.0;
        // Initialize a variable to store the sum of the boundary differences
        double differenceSum = getDifferenceSum(boundaries);
        // Loop through the boundaries array list and calculate the cumulative ratios
        for (int i = 0; i < boundaries.size(); i++) {
            double difference = getDifference(boundaries, i);
            // Calculate the cumulative ratio for the current level and store it in the array
            cumulativeRatios[i + 1] = cumulativeRatios[i] + (difference / differenceSum);
        }

        return cumulativeRatios;
    }

    private static double getDifference(ArrayList<Integer> boundaries, int i) {
        int boundary = boundaries.get(i);
        double difference;
        // Assign the difference based on the boundary value and the previous boundary value
        if (i == 0) {
            // For the first boundary, use the boundary value itself
            difference = boundary;
        } else {
            // For any other boundary, use the difference between the current and the previous boundary values
            difference = boundary - boundaries.get(i - 1);
        }
        return difference;
    }

    private static double getDifferenceSum(ArrayList<Integer> boundaries) {
        double differenceSum = 0.0;
        // Loop through the boundaries array list and calculate the difference sum
        for (int i = 0; i < boundaries.size(); i++) {
            // Get the current boundary value
            double difference = getDifference(boundaries, i);
            // Add the difference to the sum
            differenceSum += difference;
        }
        return differenceSum;
    }

}

