package com.parsleyj.smallsteptrack.parser;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a syntax case in a syntax class. For example, in this simple
 * grammar:
 *  {@code Exp:= n | (E) | (E + E) | (E * E)}
 * Each one of the expressions separated by the {@code | } is represented
 * by a {@link SyntaxCase}.
 */
public class SyntaxCase{
    private String caseName;
    private List<SyntaxCaseComponent> structure;

    /**
     * Creates a new syntax case with the given name and structure.
     * @param caseName the name of this case.
     * @param structure a varargs list of case components.
     */
    public SyntaxCase(String caseName, SyntaxCaseComponent... structure) {
        this.caseName = caseName;
        this.structure = Arrays.asList(structure);
    }

    /**
     * @return the structure of this syntax case.
     */
    public List<SyntaxCaseComponent> getStructure() {
        return structure;
    }

    /**
     * @return the name of this case
     */
    public String getCaseName() {
        return caseName;
    }

    /**
     * @param caseName the the name of this case.
     */
    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}
