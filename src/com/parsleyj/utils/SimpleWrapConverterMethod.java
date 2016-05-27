package com.parsleyj.utils;

import com.parsleyj.toolparser.parser.ParseTreeNode;
import com.parsleyj.toolparser.semanticsconverter.CaseConverterMethod;
import com.parsleyj.toolparser.semanticsconverter.InvalidParseTreeException;
import com.parsleyj.toolparser.semanticsconverter.SemanticObject;
import com.parsleyj.toolparser.semanticsconverter.SemanticsConverter;

/**
 * Helpful class used to defineParameter a converter method that simply takes the
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
