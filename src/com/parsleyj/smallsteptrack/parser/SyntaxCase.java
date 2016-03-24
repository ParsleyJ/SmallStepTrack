package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Giuseppe on 19/03/16.
 * TODO: javadoc
 */
public class SyntaxCase{
    private String caseName;
    private List<SyntaxCaseComponent> structure;

    public SyntaxCase(String caseName, SyntaxCaseComponent... structure) {
        this.caseName = caseName;
        this.structure = Arrays.asList(structure);
    }

    public SyntaxCase(Class<? extends SmallStepSemanticObject> semanticClass, SyntaxCaseComponent... structure){
        this.caseName = semanticClass.getName();
        this.structure = Arrays.asList(structure);
    }

    List<SyntaxCaseComponent> getStructure() {
        return structure;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}
