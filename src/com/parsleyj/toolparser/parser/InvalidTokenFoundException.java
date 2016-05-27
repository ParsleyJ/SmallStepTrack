package com.parsleyj.toolparser.parser;

import com.parsleyj.toolparser.tokenizer.Token;

/**
 * Exception thrown by the parser when in the input token list appears
 * an invalid token.
 */
public class InvalidTokenFoundException extends RuntimeException {
    public InvalidTokenFoundException(Token x){
        super("<"+x.getTokenClassName()+":"+x.getGeneratingString()+">");
    }

}
