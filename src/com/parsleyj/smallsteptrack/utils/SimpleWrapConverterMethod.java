package com.parsleyj.smallsteptrack.utils;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.SyntaxCase;
import com.parsleyj.smallsteptrack.parser.SyntaxTreeNode;
import com.parsleyj.smallsteptrack.program.CaseConverter;
import com.parsleyj.smallsteptrack.program.CaseConverterMethod;
import com.parsleyj.smallsteptrack.program.InvalidParseTreeException;
import com.parsleyj.smallsteptrack.program.Semantics;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class SimpleWrapConverterMethod implements CaseConverterMethod {

    @Override
    public SmallStepSemanticObject convert(SyntaxTreeNode node, Semantics s) {
        if(node.getChildren().size() == 1){
            return s.resolve(node.get(0));
        }else throw new InvalidParseTreeException();
    }
}
