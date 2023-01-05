package com.company.sdet_programs.oop.override;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class _01_Overriding_Equals {

    @Override
    public boolean equals(Object object) {
        // File file = (File) object;
        // Check some condition here
        return object instanceof File;
    }

    public static void main(String[] args) {
        List<File> files = new ArrayList<>();

        files.add(new File("/usr/local/bin/chromedriver"));
        files.add(new File("/usr/local/bin/chromedriver"));


        files.remove(new File("/usr/local/bin/chromedriver"));

        for(File file : files)
            System.out.println(file);
    }
}
