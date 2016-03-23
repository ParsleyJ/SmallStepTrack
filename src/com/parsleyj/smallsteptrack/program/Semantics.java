package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.SyntaxTreeNode;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Giuseppe on 23/03/16.
 * TODO: javadoc
 */
public class Semantics {

    private HashMap<String, CaseConverter> caseResolvers;
    private HashMap<String, TokenConverter> tokenResolvers;

    public Semantics(
            List<? extends CaseConverter> caseResolvers,
            List<? extends TokenConverter> tokenResolvers) {
        this.caseResolvers = new HashMap<>();
        for (CaseConverter cr : caseResolvers) {
            this.caseResolvers.put(cr.getCasE().getCaseName(), cr);
        }
        this.tokenResolvers = new HashMap<>();
        for (TokenConverter tr : tokenResolvers) {
            this.tokenResolvers.put(tr.getTokenClass().getTokenClassName(), tr);
        }
    }

    public <T extends SmallStepSemanticObject> T resolve(SyntaxTreeNode node){
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

    public SmallStepSemanticObject resolveCase(String syntaxCase, SyntaxTreeNode node) {
        CaseConverter c = caseResolvers.get(syntaxCase);
        if (c != null) {
            return c.convert(node, this);
        } else throw new NoResolverFoundForSyntaxCaseException();
    }

    public SmallStepSemanticObject resolveToken(String tokenClass, String generatingString) {
        TokenConverter r = tokenResolvers.get(tokenClass);
        if (r != null) {
            return r.convert(generatingString, this);
        } else throw new NoResolverFoundForTokenClassException();
    }


}
