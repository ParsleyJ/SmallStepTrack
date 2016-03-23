package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.RejectableTokenClass;
import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;
import com.parsleyj.smallsteptrack.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser definition class (EXPERIMENTING)
 */
public class Parser {

    private Grammar grammar;

    /* 9 + 3 - ( 4 * 7 - ( 12 + 2 ) ) * 2 */

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    //iterative attempt
    public ASTObject parse(List<Token> tokens){
        ASTFactory astf = new ASTFactory();

        List<ASTObject> treeList = new ArrayList<>();
        for (Token t: tokens) {
            TokenClass tokenClass = grammar.getTokenClass(t);
            if(tokenClass == null) throw new InvalidTokenFoundException(); //todo: add token info to exception
            ASTObject ast = astf.newASTObject();
            ast.setParsedToken(t);
            ast.setTokenClass(tokenClass);
            ast.setTerminal(true);
            treeList.add(ast);
        }

        List<Integer> windows = grammar.getCaseSizes();
        while(true){
            System.err.println("size"+treeList.size());
            boolean lastIterationFailed = true;
            if(treeList.size() <= 1) break;
            for(Integer window: windows){
                System.err.println("window"+window);
                List<ASTObject> tempList = new ArrayList<>();
                int start;
                for(start = 0; start <= treeList.size() - window; ++start){
                    int end = start + window;
                    System.err.println("start"+start+" end"+end);
                    List<ASTObject> currentSubList = treeList.subList(start, end);
                    Pair<SyntaxClass, SyntaxCase> lookupResult = grammar.lookup(
                            currentSubList.stream()
                                    .map(a -> a.isTerminal()?a.getTokenClass():a.getSyntaxClass())
                                    .collect(Collectors.toList())
                    );
                    if (lookupResult != null) {
                        System.err.println("Lookup Success");
                        lastIterationFailed = false;
                        ASTObject asto = astf.newASTObject(currentSubList.toArray(new ASTObject[currentSubList.size()]));
                        asto.setSyntaxClass(lookupResult.getFirst());
                        asto.setSyntaxCase(lookupResult.getSecond());
                        asto.setTerminal(false);
                        tempList.add(asto);
                        start += window - 1;

                    } else {
                        System.err.println("Lookup Failed");
                        tempList.add(currentSubList.get(0));
                    }
                }

                //there are no more subLists with this window at this start index
                //we need to add the remaining elements to tempList
                for(int i = start; i < treeList.size(); ++i)
                    tempList.add(treeList.get(i));

                treeList = tempList;
                System.err.println("TempListSize:"+tempList.size());
            }
            if(lastIterationFailed) break;
        }
        if(treeList.size() == 1){
            return treeList.get(0);
        } else {
            throw new ParseFailedException();
        }
    }


    public static Grammar getTestGrammar() {


        TokenClass string = new TokenClass("STRING_CONSTANT", "([\"'])(?:(?=(\\\\?))\\2.)*?\\1");
        TokenClass identifier = new TokenClass("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]*");
        TokenClass add = new TokenClass("ADD_OPERATOR", "(\\+)");
        TokenClass sub = new TokenClass("SUB_OPERATOR", "(\\-)");
        TokenClass mul = new TokenClass("MUL_OPERATOR", "(\\*)");
        TokenClass openBracket = new TokenClass("OPEN_ROUND_BRACKET", "(\\()");
        TokenClass closedBracket = new TokenClass("CLOSED_ROUND_BRACKET", "(\\))");
        TokenClass numeral = new TokenClass("NUMERAL", "(?<=\\s|^)[-+]?\\d+(?=\\s|$)");
        TokenClass blank = new RejectableTokenClass("BLANK", " ");
        SyntaxClass exp = new SyntaxClass("Exp");
        SyntaxClass comm = new SyntaxClass("Comm");
        SyntaxClass bool = new SyntaxClass("Bool");
        exp.setCases(
                new SyntaxCase("identifier", identifier),
                new SyntaxCase("numeral", numeral),
                new SyntaxCase("bracketedExpression", openBracket, exp, closedBracket),
                new SyntaxCase("sum", exp, add, exp),
                new SyntaxCase("subtraction", exp, sub, exp),
                new SyntaxCase("multiplication", exp, mul, exp)
        );

        //...(EXPERIMENTING)...

        return new Grammar(exp, comm, bool);
    }


    private class InvalidTokenFoundException extends RuntimeException {
    }

    private class ParseFailedException extends RuntimeException {
    }
}
