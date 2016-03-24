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
    private boolean printDebugMessages = false;

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

        if(printDebugMessages){
            System.out.println("TOKENIZER RESULT:");
            System.out.println();
            tokenList.forEach((t) -> {
                System.out.println("Token = " + t.getGeneratingString());
                System.out.println(" Type = " + t.getTokenClassName());
                System.out.println("--------------------------------------");
            });
        }

        Parser parser = new Parser(grammar);
        SyntaxTreeNode tree = null;
        try{
            tree = parser.parse(tokenList);
            if(printDebugMessages){
                System.out.println();
                System.out.println("PARSER RESULT:");
                System.out.println();
                tree.printTree();
                System.out.println();
            }
            return new Program(name, generateRootSemanticObject(tree)){
                @Override
                public boolean step(Configuration configuration) {
                    return stepMethod.step(this, configuration);
                }
            };
        } catch (ParseFailedException e){
            System.out.println();
            System.out.println("FAILED TREE:");
            System.out.println();
            e.getFailureTree().printTree();
            System.out.println();
            throw e;
        }
    }

    public boolean isPrintDebugMessages() {
        return printDebugMessages;
    }

    public void setPrintDebugMessages(boolean printDebugMessages) {
        this.printDebugMessages = printDebugMessages;
    }
}
