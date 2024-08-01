package com.company.sdet_programs.countPojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

record StepKeyConstituents(String path, String stepName, int rowIndex) {}

public class CountCustomPojo {
    public static ArrayList<StepKeyConstituents> data = new ArrayList<>();

    static {
        data.add(new StepKeyConstituents("dir/myfile1", "mark", 23));
        data.add(new StepKeyConstituents("dir/myfile2", "mark", 23));
        data.add(new StepKeyConstituents("dir/myfile3", "john", 27));
        data.add(new StepKeyConstituents("dir/myfile3", "alex", 29));

    }

    public static Map<String, Integer> countNames(ArrayList<StepKeyConstituents> data) {
        Map<String, Integer> nameCounts = new HashMap<>();
        for (StepKeyConstituents item : data) {
            String name = item.stepName();
            nameCounts.put(name, nameCounts.getOrDefault(name, 0) + 1);
        }
        return nameCounts;
    }

    public static void main(String[] args) {
        Map<String, Integer> nameCounts = countNames(data);

        System.out.println("Name occurrences:");
        for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
