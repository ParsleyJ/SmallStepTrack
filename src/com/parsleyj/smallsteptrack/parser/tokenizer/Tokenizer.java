package com.parsleyj.smallsteptrack.parser.tokenizer;

import com.parsleyj.smallsteptrack.parser.SyntaxTree;
import com.parsleyj.smallsteptrack.parser.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link Tokenizer}'s job is to return a list of {@link Token}, generated
 * from the given {@link String} input, with the given list of {@link TokenClass}es.
 */
public class Tokenizer {
    private List<TokenClass> tokenClasses;

    public Tokenizer(List<TokenClass> tokenClasses){

        this.tokenClasses = tokenClasses;
    }

    public List<Token> tokenize(String input){
        return tokenize(input, 0);
    }

    private List<Token> tokenize(String input, int tcIndex){
        if(tcIndex >= tokenClasses.size()) throw new UnscannableSubstringException(input);

        ArrayList<Token> tempResult = new ArrayList<>();
        TokenClass tokenClass = tokenClasses.get(tcIndex);
        Matcher matcher = Pattern.compile(tokenClass.getPattern()).matcher(input);
        int lastEnd = 0;
        while(matcher.find()){
            if(lastEnd<matcher.start()){
                tempResult.add(new UnscannedTempToken(input.substring(lastEnd, matcher.start())));
            }
            lastEnd = matcher.end();
            Token found = new Token(input.substring(matcher.start(), matcher.end()),tokenClass.getTokenClassName());
            if(!tokenClass.isIgnorable())
                tempResult.add(found);
        }
        if(lastEnd < input.length()){
            tempResult.add(new UnscannedTempToken(input.substring(lastEnd)));
        }
        ArrayList<Token> result = new ArrayList<>();
        for(Token t: tempResult){
            if(t.getTokenClassName().equals(Token.UNSCANNED)){
                //recursively calls tokenize with the next token class pattern and the unscanned substring
                List<Token> tmp = tokenize(t.getGeneratingString(), tcIndex+1);
                result.addAll(tmp);
            }else{
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Test main
     */
    public static void main(String[] argv){
        Scanner sc = new Scanner(System.in);
        List<Token> tokens = null;

        String input = sc.nextLine();
        Tokenizer tokenizer = new Tokenizer(Arrays.asList(
                new TokenClass("STRING_CONSTANT", "([\"'])(?:(?=(\\\\?))\\2.)*?\\1"),
                new TokenClass("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]*"),
                //new TokenClass("OPERATOR", "[-!$%^&*_+|~=:;<>?,.\\/]"),
                new TokenClass("ADD_OPERATOR", "(\\+)"),
                new TokenClass("SUB_OPERATOR", "(\\-)"),
                new TokenClass("MUL_OPERATOR", "\\Q*\\E"),
                new TokenClass("OPEN_ROUND_BRACKET", "\\Q(\\E"),
                new TokenClass("CLOSED_ROUND_BRACKET", "\\Q)\\E"),
                new TokenClass("NUMERAL", "(?<=\\s|^)[-+]?\\d+(?=\\s|$)"),
                //new TokenClass("NUMERAL2", "^(0|[1-9][0-9]*)$"),
                //new TokenClass("MUL_OPERATOR", "\\*"),
                //new TokenClass("SUB_OPERATOR", "\\-"),
                new RejectableTokenClass("BLANK", " ")
        ));
        tokens = tokenizer.tokenize(input);
        tokens.forEach((t) -> {
            System.out.println("Token = " + t.getGeneratingString());
            System.out.println(" Type = " + t.getTokenClassName());
            System.out.println("--------------------------------------");
        });

        if(tokens != null && !tokens.isEmpty()){
            Parser parser = new Parser(Parser.getTestGrammar());
            SyntaxTree result = parser.parse(tokens);
            if (result != null){
                result.printTree();
            }else{
                System.out.println("PARSER RESULT IS NULL");
            }
        }

    }



    public class UnscannableSubstringException extends RuntimeException{
        public UnscannableSubstringException(String str){
            super("Unscannable input found: \""+str+"\"");
        }
    }
}
