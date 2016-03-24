package com.parsleyj.smallsteptrack.parser;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
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
}
