package com.parsleyj.toolparser.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: doc
 * A {@link Tokenizer}'s job is to return a list of {@link Token}, generated
 * from the given {@link String} input, with the given list of {@link TokenCategory}es.
 */
public class Tokenizer {
    private List<? extends LexicalPattern> patterns;

    public Tokenizer(List<? extends LexicalPattern> patterns){

        this.patterns = patterns;
    }


    public List<Token> tokenize(String input){
        return tokenize(input, 0);
    }

    private List<Token> tokenize(String input, int tcIndex){
        if(tcIndex >= patterns.size()) throw new UnscannableSubstringException(input);

        ArrayList<Token> tempResult = new ArrayList<>();
        LexicalPattern pattern = patterns.get(tcIndex);
        Matcher matcher = Pattern.compile(pattern.getPattern()).matcher(input);
        int lastEnd = 0;
        while(matcher.find()){
            if(lastEnd<matcher.start()){
                tempResult.add(new UnscannedTempToken(input.substring(lastEnd, matcher.start())));
            }
            lastEnd = matcher.end();
            String foundString =input.substring(matcher.start(), matcher.end());
            TokenCategory tokenCategory = pattern.generateTokenCategory(foundString);
            Token found = new Token(foundString, tokenCategory.getTokenClassName());
            if(!tokenCategory.isIgnorable()) {
                tempResult.add(found);
            }
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
