package com.company.sdet_programs.countPojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

record StepPathNameAndRowIndex(String path, String stepName, int rowIndex) {}

public class CountCustomPojo {
    static ArrayList<StepPathNameAndRowIndex> data = new ArrayList<>();

    static {
        data.add(new StepPathNameAndRowIndex("dir/myfile1", "mark", 23));
        data.add(new StepPathNameAndRowIndex("dir/myfile2", "mark", 23));
        data.add(new StepPathNameAndRowIndex("dir/myfile3", "john", 27));
        data.add(new StepPathNameAndRowIndex("dir/myfile3", "alex", 29));

    }
    public static void main(String[] args) {
        Map<String, Integer> nameCounts = countNames(data);

        System.out.println("Name occurrences:");
        for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static Map<String, Integer> countNames(ArrayList<StepPathNameAndRowIndex> data) {
        Map<String, Integer> nameCounts = new HashMap<>();
        for (StepPathNameAndRowIndex item : data) {
            String name = item.stepName();
            nameCounts.put(name, nameCounts.getOrDefault(name, 0) + 1);
        }
        return nameCounts;
    }
}
