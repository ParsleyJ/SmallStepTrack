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

    private Grammar grammar = getTestGrammar();

    /* 9 + 3 - ( 4 * 7 - ( 12 + 2 ) ) * 2 */

    public List<ASTObject> buildParseInput(List<Token> tokens){
        List<ASTObject> result = new ArrayList<>();
        for (Token t: tokens) {
            TokenClass tokenClass = grammar.getTokenClass(t);
            if(tokenClass == null) throw new InvalidTokenFoundException(); //todo: add token info to exception
            ASTObject ast = new ASTObject();
            ast.setParsedToken(t);
            ast.setTokenClass(tokenClass);
            ast.setTerminal(true);
            result.add(ast);
        }
        return result;
    }

    public ASTObject parse(List<ASTObject> subtrees){
        if(subtrees.isEmpty()) return null;
        if(subtrees.size()==1){
            ASTObject asto = subtrees.get(0);
            Pair<SyntaxClass, SyntaxCase> lookupResult =
                    grammar.lookup(Collections.singletonList(
                            asto.isTerminal() ? asto.getTokenClass() : asto.getSyntaxClass()));
            if(lookupResult == null) throw new InvalidAbstractSyntaxSubtreeFoundException();
            asto.setSyntaxClass(lookupResult.getFirst());
            asto.setSyntaxCase(lookupResult.getSecond());
            return asto;
        }
        //(work in progress...)
        return null;
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

    private class InvalidAbstractSyntaxSubtreeFoundException extends RuntimeException {
    }
}
