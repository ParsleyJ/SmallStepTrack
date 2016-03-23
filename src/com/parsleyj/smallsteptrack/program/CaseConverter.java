package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.SyntaxCase;
import com.parsleyj.smallsteptrack.parser.SyntaxTreeNode;

/**
 * Created by Giuseppe on 23/03/16.
 * TODO: javadoc
 */
public abstract class CaseConverter {
    private SyntaxCase casE;

    public CaseConverter(SyntaxCase casE){
        this.casE = casE;
    }

    public SyntaxCase getCasE() {
        return casE;
    }

    public abstract SmallStepSemanticObject convert(SyntaxTreeNode node, Semantics s);
}
