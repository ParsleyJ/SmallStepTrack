package com.parsleyj.smallsteptrack.utils;

import com.parsleyj.smallsteptrack.parser.ParseTreeNode;
import com.parsleyj.smallsteptrack.semanticsconverter.CaseConverterMethod;
import com.parsleyj.smallsteptrack.semanticsconverter.InvalidParseTreeException;
import com.parsleyj.smallsteptrack.semanticsconverter.SemanticObject;
import com.parsleyj.smallsteptrack.semanticsconverter.SemanticsConverter;

/**
 * Helpful class used to define a converter method that simply takes the
 * only child node of the current node and returns it, after a conversion.
 */
public class SimpleWrapConverterMethod implements CaseConverterMethod {

    @Override
    public SemanticObject convert(ParseTreeNode node, SemanticsConverter s) {
        if(node.getChildren().size() == 1){
            return s.convert(node.get(0));
        }else throw new InvalidParseTreeException();
    }
}
