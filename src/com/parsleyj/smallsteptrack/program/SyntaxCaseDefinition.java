package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.parser.SyntaxCase;
import com.parsleyj.smallsteptrack.parser.SyntaxCaseComponent;
import com.parsleyj.smallsteptrack.parser.SyntaxClass;
import com.parsleyj.smallsteptrack.program.CaseConverterMethod;
import com.parsleyj.smallsteptrack.program.CheckedCaseConverter;

/**
 * Created by Giuseppe on 25/03/16.
 * TODO: javadoc
 */
public class SyntaxCaseDefinition extends SyntaxCase {


    private final SyntaxClass belongingClass;
    private final CheckedCaseConverter converter;

    public SyntaxCaseDefinition(SyntaxClass belongingClass, String caseName, CaseConverterMethod method, SyntaxCaseComponent... structure) {
        super(caseName, structure);
        this.belongingClass = belongingClass;
        this.converter = new CheckedCaseConverter(this, method);
    }

    public SyntaxClass getBelongingClass() {
        return belongingClass;
    }

    public CheckedCaseConverter getConverter() {
        return converter;
    }
}
