package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.RejectableTokenClass;
import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;
import com.parsleyj.smallsteptrack.utils.Pair;

import java.util.ArrayList;
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
    public SyntaxTree parse(List<Token> tokens){
        SyntaxTreeFactory stf = new SyntaxTreeFactory();

        List<SyntaxTree> treeList = new ArrayList<>();
        for (Token t: tokens) {
            TokenClass tokenClass = grammar.getTokenClass(t);
            if(tokenClass == null) throw new InvalidTokenFoundException(); //todo: add token info to exception
            SyntaxTree ast = stf.newSyntaxTree();
            ast.setParsedToken(t);
            ast.setTokenClass(tokenClass);
            ast.setTerminal(true);
            treeList.add(ast);
        }

        List<Integer> windows = grammar.getCaseSizes();
        while(true){
            boolean lastIterationFailed = true;
            if(treeList.size() <= 1) break;
            for(Integer window: windows){
                List<SyntaxTree> tempList = new ArrayList<>();
                int start;
                for(start = 0; start <= treeList.size() - window; ++start){
                    int end = start + window;
                    List<SyntaxTree> currentSubList = treeList.subList(start, end);
                    Pair<SyntaxClass, SyntaxCase> lookupResult = grammar.lookup(
                            currentSubList.stream()
                                    .map(a -> a.isTerminal()?a.getTokenClass():a.getSyntaxClass())
                                    .collect(Collectors.toList())
                    );
                    if (lookupResult != null) {
                        lastIterationFailed = false;
                        SyntaxTree nst = stf.newSyntaxTree(currentSubList.toArray(new SyntaxTree[currentSubList.size()]));
                        nst.setSyntaxClass(lookupResult.getFirst());
                        nst.setSyntaxCase(lookupResult.getSecond());
                        nst.setTerminal(false);
                        tempList.add(nst);
                        start += window - 1;

                    } else {
                        tempList.add(currentSubList.get(0));
                    }
                }

                //there are no more subLists with this window at this start index
                //we need to add the remaining elements to tempList
                for(int i = start; i < treeList.size(); ++i)
                    tempList.add(treeList.get(i));

                treeList = tempList;
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

        //TODO: complete with a not ambiguous grammar

        return new Grammar(exp, comm, bool);
    }


    private class InvalidTokenFoundException extends RuntimeException {
    }

    private class ParseFailedException extends RuntimeException {
    }
}
