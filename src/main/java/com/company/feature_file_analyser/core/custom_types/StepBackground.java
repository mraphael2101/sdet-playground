package com.company.feature_file_analyser.core.custom_types;

import lombok.Getter;
import lombok.Setter;

public class StepBackground {
    @Getter
    @Setter
    private String Path = "";
    @Getter
    @Setter
    private String stepName = "";
    @Getter
    @Setter
    private int lineNumber = 0;
    @Getter
    @Setter
    private boolean isDataDriven = false;
    @Getter
    @Setter
    private boolean isDataTableDriven = false;

    //todo could be bound to more than one outline or scenario - how to capture the scope
//    @Getter
//    @Setter
//    private boolean isBoundToOutline = false;
//    @Getter
//    @Setter
//    private boolean isBoundToScenario = false
    @Getter
    @Setter
    private DataTable dataTable = null;
}
