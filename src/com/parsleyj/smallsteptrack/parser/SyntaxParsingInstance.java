package com.parsleyj.smallsteptrack.parser;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class SyntaxParsingInstance implements SyntaxCaseComponent {
    private final SyntaxClass syntaxClass;
    private final SyntaxCase syntaxCase;

    public SyntaxParsingInstance(SyntaxClass syntaxClass, SyntaxCase syntaxCase) {
        this.syntaxClass = syntaxClass;
        this.syntaxCase = syntaxCase;
    }

    @Override
    public String getSyntaxComponentName() {
        return syntaxClass.getSyntaxComponentName();
    }

    public String getSyntaxCaseName(){
        return syntaxCase.getCaseName();
    }
}
