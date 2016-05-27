package com.parsleyj.toolparser.program;

import com.parsleyj.toolparser.configuration.Configuration;
import com.parsleyj.toolparser.parser.*;
import com.parsleyj.toolparser.semanticsconverter.CaseConverter;
import com.parsleyj.toolparser.semanticsconverter.SemanticObject;
import com.parsleyj.toolparser.semanticsconverter.SemanticsConverter;
import com.parsleyj.toolparser.semanticsconverter.TokenConverter;
import com.parsleyj.toolparser.tokenizer.LexicalPattern;
import com.parsleyj.toolparser.tokenizer.Token;
import com.parsleyj.toolparser.tokenizer.TokenCategory;
import com.parsleyj.toolparser.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpful class used to defineParameter a {@link Tokenizer}, a {@link RecursiveParser}
 * and a {@link SemanticsConverter}, which inputs and outputs are chained
 * in order to interpret a {@link Program} object from the input {@link String}
 * program.
 */
public class Interpreter {

    private final List<LexicalPattern> patterns;
    private Grammar grammar;
    private List<TokenCategory> tokenCategories;
    private SemanticsConverter semanticsConverter;
    private boolean printDebugMessages = false;

    /**
     * TODO: doc
     * Creates a program generator with the given {@link TokenCategoryDefinition}s and the
     * {@link SyntaxCaseDefinition}s.
     * The {@code Definition}s objects are used to create the {@link Interpreter}'s
     * internal {@link Tokenizer}, {@link RecursiveParser} and {@link SemanticsConverter}. The order
     * of both arrays is meaningful: see params descriptions for more.
     * @param tokenCategories  an ordered array of the token categories with eventual method converters.
*                              The tokenizer searches the patterns in the input program string following
*                              the order of this array.
     * @param definitions an ordered array of the syntax cases with method converters. The parser
     *                    searches the cases in the input token list following the order of this array.
     */
    public Interpreter(LexicalPatternDefinition[] patterns, SyntaxCaseDefinition[] definitions){
        this.tokenCategories = new ArrayList<>();
        this.patterns = new ArrayList<>();
        List<TokenConverter> tokenConverters = new ArrayList<>();

        for(LexicalPatternDefinition pattern:patterns){
            this.patterns.add(pattern);
            this.tokenCategories.addAll(pattern.declaredTokenCategories());
            tokenConverters.addAll(pattern.getDeclaredTokenConverters());
        }

        this.grammar = new Grammar(definitions);
        List<CaseConverter> caseConverters = new ArrayList<>();
        for(SyntaxCaseDefinition syntaxCaseDefinition: definitions){
            caseConverters.add(syntaxCaseDefinition.getConverter());
        }
        this.semanticsConverter = new SemanticsConverter(caseConverters , tokenConverters);
    }


    private SemanticObject generateRootSemanticObject(ParseTreeNode tree){
        if (tree.isTerminal()) {
            return semanticsConverter.resolveToken(tree.getParsedToken().getTokenClassName(), tree.getParsedToken().getGeneratingString());
        }else{
            return semanticsConverter.resolveCase(tree.getSyntaxCase().getCaseName(), tree);
        }
    }

    /**
     * Generates the {@link Program} object with the semantics of the input string program,
     * by tokenizing it, parsing it and converting the parse tree.
     * @param name the name of the program
     * @param inputProgram the program input string
     * @param executionMethod the method used to let the program make a computational execute.
     * @return the Program object.
     */
    public Program interpret(String name, String inputProgram, SyntaxClass rootClass, final ProgramExecutionMethod executionMethod){
        Tokenizer tokenizer = new Tokenizer(patterns); //TODO: move in init method
        List<Token> tokenList = tokenizer.tokenize(inputProgram);

        if(printDebugMessages){//TODO use lol class
            System.out.println("TOKENIZER RESULT:");
            System.out.println();
            tokenList.forEach((t) -> {
                System.out.println("Token = " + t.getGeneratingString());
                System.out.println(" Type = " + t.getTokenClassName());
                System.out.println("--------------------------------------");
            });
        }

        Parser parser = new RecursiveParser(grammar, rootClass); //TODO: move in init method
        ParseTreeNode tree;
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
                public boolean execute(Configuration configuration) {
                    return executionMethod.execute(this, configuration);
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

    /**
     * @return the state of the flag used to determine if the generator
     * should print debug messages.
     */
    public boolean isPrintDebugMessages() {
        return printDebugMessages;
    }

    /**
     * @param printDebugMessages the state of the flag used to determine if the generator
     * should print debug messages.
     */
    public void setPrintDebugMessages(boolean printDebugMessages) {
        this.printDebugMessages = printDebugMessages;
    }
}
