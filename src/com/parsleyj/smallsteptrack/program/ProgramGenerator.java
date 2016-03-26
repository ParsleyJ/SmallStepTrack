package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.configuration.Configuration;
import com.parsleyj.smallsteptrack.parser.*;
import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;
import com.parsleyj.smallsteptrack.parser.tokenizer.Tokenizer;

import java.util.ArrayList;
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

    public ProgramGenerator(TokenClassDefinition[] tokenClassDefinitions, SyntaxCaseDefinition[] definitions){
        this.tokenClasses = new ArrayList<>();
        List<TokenConverter> tokenConverters = new ArrayList<>();
        for(TokenClassDefinition tokenClassDefinition: tokenClassDefinitions){
            tokenClasses.add(tokenClassDefinition);
            if (tokenClassDefinition.getConverter() != null) {
                tokenConverters.add(tokenClassDefinition.getConverter());
            }
        }
        this.grammar = new Grammar(definitions);
        List<CaseConverter> caseConverters = new ArrayList<>();
        for(SyntaxCaseDefinition syntaxCaseDefinition: definitions){
            caseConverters.add(syntaxCaseDefinition.getConverter());
        }
        this.semantics = new Semantics(caseConverters , tokenConverters);
    }

    private SmallStepSemanticObject generateRootSemanticObject(ParseTreeNode tree){
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
        ParseTreeNode tree = null;
        try{
            tree = parser.priorityBasedParse(tokenList);
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
