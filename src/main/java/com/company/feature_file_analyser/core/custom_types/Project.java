package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Project {

    @Getter
    private Set<String> setOfDistinctFilePathsString = null;

    public void initialiseSetOfDistinctPathsString(List<String> value) {
        this.setOfDistinctFilePathsString = new HashSet<>(value);
    }

}
