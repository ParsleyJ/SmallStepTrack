package com.parsleyj.smallsteptrack.semanticsconverter;

import com.parsleyj.smallsteptrack.parser.ParseTreeNode;

import java.util.HashMap;
import java.util.List;

/**
 * A {@link SemanticsConverter} has the job to convert single nodes
 * of a parse tree in {@link SemanticObject}s, following the rules defined
 * in the lists of {@link CaseConverter}s and {@link TokenConverter}.
 */
public class SemanticsConverter {

    private HashMap<String, CaseConverter> caseResolvers;
    private HashMap<String, TokenConverter> tokenResolvers;

    public SemanticsConverter(
            List<? extends CaseConverter> caseResolvers,
            List<? extends TokenConverter> tokenResolvers) {
        this.caseResolvers = new HashMap<>();
        for (CaseConverter cr : caseResolvers) {
            this.caseResolvers.put(cr.getCasE().getCaseName(), cr);
        }
        this.tokenResolvers = new HashMap<>();
        for (TokenConverter tr : tokenResolvers) {
            this.tokenResolvers.put(tr.getTokenCategory().getTokenClassName(), tr);
        }
    }

    public <T extends SemanticObject> T convert(ParseTreeNode node){
        try {
            if (node.isTerminal()) {
                return (T) resolveToken(node.getParsedToken().getTokenClassName(), node.getParsedToken().getGeneratingString());
            } else {
                return (T) resolveCase(node.getSyntaxCase().getCaseName(), node);
            }
        } catch (ClassCastException t){
            throw new InvalidParseTreeException();
        }
    }

    public SemanticObject resolveCase(String syntaxCase, ParseTreeNode node) {
        CaseConverter c = caseResolvers.get(syntaxCase);
        if (c != null) {
            return c.convert(node, this);
        } else throw new NoConverterFoundForSyntaxCaseException();
    }

    public SemanticObject resolveToken(String tokenClass, String generatingString) {
        TokenConverter r = tokenResolvers.get(tokenClass);
        if (r != null) {
            return r.convert(generatingString, this);
        } else throw new NoConverterFoundForTokenCategoryException();
    }




}
