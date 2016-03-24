package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.parser.SyntaxCase;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class CheckedCaseConverter extends CaseConverter{
    public CheckedCaseConverter(SyntaxCase casE, CaseConverterMethod method) {
        super(casE, ((node, s) -> {
            if(node.getChildren().size() == casE.getStructure().size()){
                return method.convert(node,s);
            } else throw new InvalidParseTreeException();
        }));
    }
}
