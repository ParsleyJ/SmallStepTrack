package com.parsleyj.smallsteptrack.parser;

/**
 * Helper class used by the parser to override the normal
 * case match mechanism in order to match {@link SpecificCaseComponent}s.
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
