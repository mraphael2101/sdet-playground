package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.TreeMap;

@Slf4j
public class Project {
    @Getter
    @Setter
    private TreeMap<String, Boolean> mapTestTypesProjects = new TreeMap() {{
        put("web", false);
        put("mobile", false);
        put("desktop", false);
        put("api", false);
    }};
    @Getter
    @Setter
    private int numberOfPeople = 0;
    @Getter
    @Setter
    private long numberOfLinesOfCode = 0;
    @Getter
    @Setter
    private String sizeClassification = "";

}
