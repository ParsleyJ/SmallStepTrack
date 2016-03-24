package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.SyntaxCase;
import com.parsleyj.smallsteptrack.parser.SyntaxTreeNode;

/**
 * Created by Giuseppe on 23/03/16.
 * TODO: javadoc
 */
public class CaseConverter {
    private SyntaxCase casE;
    private CaseConverterMethod method;

    public CaseConverter(SyntaxCase casE, CaseConverterMethod method){
        this.casE = casE;
        this.method = method;
    }

    public SyntaxCase getCasE() {
        return casE;
    }

    public SmallStepSemanticObject convert(SyntaxTreeNode node, Semantics s){
        return method.convert(node, s);
    }

}
