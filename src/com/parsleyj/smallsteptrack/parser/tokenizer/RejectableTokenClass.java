package com.parsleyj.smallsteptrack.parser.tokenizer;

/**
 * Created by Giuseppe on 18/03/16.
 */
public class RejectableTokenClass extends TokenClass{
    public RejectableTokenClass(String tokenClassName, String pattern) {
        super(tokenClassName, pattern, true);
    }
}