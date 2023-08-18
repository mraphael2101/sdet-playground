package com.company.sdet_programs.algorithms;
import java.util.ArrayList;

public class DynamicBoundaryValueArrayGenerator {

  public static void main(String[] args) {
    ArrayList<Integer> boundaries = createBoundaryValueArrayList(300);
    System.out.println(boundaries);
  }

  // A method that takes a max value parameter and returns an array list of values
  public static ArrayList<Integer> createBoundaryValueArrayList(int max) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    list.add(10);
    list.add(20);
//    list.add(50);
//    list.add(100);
//    list.add(150);
//    list.add(200);
    // Add increments of 50 until the max value is reached or exceeded
//    int value = 250;
    int value = 50;
    while (value <= max) {
      list.add(value);
      value += 50;
    }
    return list;
  }

}





