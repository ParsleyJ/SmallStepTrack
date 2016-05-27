package com.parsleyj.toolparser.program;

import com.parsleyj.toolparser.parser.Associativity;
import com.parsleyj.toolparser.parser.SyntaxCase;
import com.parsleyj.toolparser.parser.SyntaxCaseComponent;
import com.parsleyj.toolparser.parser.SyntaxClass;
import com.parsleyj.toolparser.semanticsconverter.CaseConverter;
import com.parsleyj.toolparser.semanticsconverter.CaseConverterMethod;
import com.parsleyj.toolparser.semanticsconverter.CheckedCaseConverter;

/**
 * Helper class used to defineParameter a {@link SyntaxCase} with the belonging {@link SyntaxClass}
 * and a {@link CaseConverter}, using the given {@link CaseConverterMethod}.
 */
public class SyntaxCaseDefinition extends SyntaxCase {


    private final SyntaxClass belongingClass;
    private final CheckedCaseConverter converter;

    public SyntaxCaseDefinition(SyntaxClass belongingClass, String caseName, CaseConverterMethod method, SyntaxCaseComponent... structure) {
        super(caseName, belongingClass, structure);
        this.belongingClass = belongingClass;
        this.converter = new CheckedCaseConverter(this, method);
    }

    public SyntaxClass getBelongingClass() {
        return belongingClass;
    }

    public CheckedCaseConverter getConverter() {
        return converter;
    }

    public SyntaxCaseDefinition parsingDirection(Associativity associativity){
        setAssociativity(associativity);
        return this;
    }


}
