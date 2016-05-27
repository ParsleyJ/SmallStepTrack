package com.parsleyj.toolparser.parser;

/**
 * A special case component that overrides the normal mechanism that
 * the parser uses when confronts two cases. This class can be used to use
 * a specific syntax case as a case component.
 */
public class SpecificCaseComponent implements SyntaxCaseComponent {

    private final SyntaxClass clas;
    private final SyntaxCase cas;

    public SpecificCaseComponent(SyntaxClass clas, SyntaxCase cas){

        this.clas = clas;
        this.cas = cas;
    }


    @Override
    public String getSyntaxComponentName() {
        return clas.getSyntaxComponentName()+":"+cas.getCaseName();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    public SyntaxClass getSyntaxClass() {
        return clas;
    }

    public SyntaxCase getSyntaxCase() {
        return cas;
    }


}
