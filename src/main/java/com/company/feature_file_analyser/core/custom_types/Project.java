package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Project {

    @Getter
    private Set<String> setOfDistinctPathsString = null;

    public void initialiseSetOfDistinctPathsString(List<String> value) {
        this.setOfDistinctPathsString = new HashSet<>(value);
    }

}
