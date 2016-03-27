package com.parsleyj.smallsteptrack.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link Tokenizer}'s job is to return a list of {@link Token}, generated
 * from the given {@link String} input, with the given list of {@link TokenCategory}es.
 */
public class Tokenizer {
    private List<TokenCategory> tokenCategories;

    public Tokenizer(List<TokenCategory> tokenCategories){

        this.tokenCategories = tokenCategories;
    }

    public List<Token> tokenize(String input){
        return tokenize(input, 0);
    }

    private List<Token> tokenize(String input, int tcIndex){
        if(tcIndex >= tokenCategories.size()) throw new UnscannableSubstringException(input);

        ArrayList<Token> tempResult = new ArrayList<>();
        TokenCategory tokenCategory = tokenCategories.get(tcIndex);
        Matcher matcher = Pattern.compile(tokenCategory.getPattern()).matcher(input);
        int lastEnd = 0;
        while(matcher.find()){
            if(lastEnd<matcher.start()){
                tempResult.add(new UnscannedTempToken(input.substring(lastEnd, matcher.start())));
            }
            lastEnd = matcher.end();
            Token found = new Token(input.substring(matcher.start(), matcher.end()), tokenCategory.getTokenClassName());
            if(!tokenCategory.isIgnorable())
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
