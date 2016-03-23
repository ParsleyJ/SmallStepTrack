package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.configuration.Configuration;
import com.parsleyj.smallsteptrack.parser.*;
import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;
import com.parsleyj.smallsteptrack.parser.tokenizer.Tokenizer;

import java.util.List;

/**
 * Created by Giuseppe on 23/03/16.
 * TODO: javadoc
 */
public class ProgramGenerator {

    private Grammar grammar;
    private List<TokenClass> tokenClasses;
    private Semantics semantics;

    public ProgramGenerator(List<TokenClass> tokenClasses, Grammar grammar, Semantics semantics) {
        this.grammar = grammar;
        this.tokenClasses = tokenClasses;
        this.semantics = semantics;
    }

    private SmallStepSemanticObject generateRootSemanticObject(SyntaxTreeNode tree){
        if (tree.isTerminal()) {
            return semantics.resolveToken(tree.getParsedToken().getTokenClassName(), tree.getParsedToken().getGeneratingString());
        }else{
            return semantics.resolveCase(tree.getSyntaxCase().getCaseName(), tree);
        }
    }

    public Program generate(String name, String inputProgram, final ProgramStepMethod stepMethod){
        Tokenizer tokenizer = new Tokenizer(tokenClasses);
        List<Token> tokenList = tokenizer.tokenize(inputProgram);

        Parser parser = new Parser(grammar);
        SyntaxTreeNode tree = parser.parse(tokenList);
        return new Program(name, generateRootSemanticObject(tree)){
            @Override
            public boolean step(Configuration configuration) {
                return stepMethod.step(this, configuration);
            }
        };
    }

}
