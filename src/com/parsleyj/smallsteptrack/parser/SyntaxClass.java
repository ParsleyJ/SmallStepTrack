package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.SemanticObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a specific syntax class. This class implements
 * {@link SyntaxCaseComponent}, so its instances can be used
 * as components to define syntax cases.
 */
public class SyntaxClass implements SyntaxCaseComponent {

    private String name;
    private List<SyntaxCase> cases;

    /**
     * Creates a new syntax class with the given name.
     * @param name the name of this class.
     */
    public SyntaxClass(String name) {
        this.name = name;
        cases = new ArrayList<>();
    }

    /**
     * Creates a new syntax class with the given name and
     * list of cases.
     * @param name the name of this class.
     * @param cases the cases that generate instances of this syntax class.
     */
    public SyntaxClass(String name, List<SyntaxCase> cases) {
        this.name = name;
        this.cases = cases;
    }

    /**
     * @return the cases that generate instances of this syntax class.
     */
    public List<SyntaxCase> getSyntaxCases() {
        return cases;
    }

    /**
     * @param cases the cases that generate instances of this syntax
     *              class.
     */
    public void setCases(SyntaxCase... cases) {
        this.cases = Arrays.asList(cases);
    }

    /**
     * Adds a case to the list of cases of this syntax class.
     * @param cas the cas to be added
     */
    public void addCase(SyntaxCase cas){
        if (this.cases == null) {
            cases = new ArrayList<>();
        }
        cases.add(cas);
    }

    @Override
    public String getSyntaxComponentName() {
        return name;
    }

}
