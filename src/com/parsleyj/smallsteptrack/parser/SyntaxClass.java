package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Giuseppe on 19/03/16.
 * TODO: javadoc
 */
public class SyntaxClass implements SyntaxCaseComponent {

    private String name;
    private List<SyntaxCase> cases;

    public SyntaxClass(String name) {
        this.name = name;
        cases = new ArrayList<>();
    }

    public SyntaxClass(String name, List<SyntaxCase> cases) {
        this.name = name;
        this.cases = cases;
    }

    public SyntaxClass(Class<? extends SmallStepSemanticObject> semanticClass){
        this.name = semanticClass.getName();
        cases = new ArrayList<>();
    }

    public SyntaxClass(Class<? extends SmallStepSemanticObject> semanticClass, List<SyntaxCase> cases) {
        this.name = semanticClass.getName();
        this.cases = cases;
    }

    public List<SyntaxCase> getSyntaxCases() {
        return cases;
    }

    public void setCases(SyntaxCase... cases) {
        this.cases = Arrays.asList(cases);
    }

    @Override
    public String getSyntaxComponentName() {
        return name;
    }

}
