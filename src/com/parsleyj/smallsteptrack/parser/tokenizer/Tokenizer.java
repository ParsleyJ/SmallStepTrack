package com.parsleyj.smallsteptrack.parser.tokenizer;

import java.util.ArrayList;
import java.util.List;
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





    public class UnscannableSubstringException extends RuntimeException{
        public UnscannableSubstringException(String str){
            super("Unscannable input found: \""+str+"\"");
        }
    }
}
