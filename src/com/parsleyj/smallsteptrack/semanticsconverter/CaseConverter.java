package com.parsleyj.smallsteptrack.semanticsconverter;

import com.parsleyj.smallsteptrack.parser.ParseTreeNode;
import com.parsleyj.smallsteptrack.parser.SyntaxCase;

/**
 * Contains the method and the metadata to convert a node
 * representing a syntax case in a {@link SemanticObject}.
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

    public SemanticObject convert(ParseTreeNode node, SemanticsConverter s){
        return method.convert(node, s);
    }

}
