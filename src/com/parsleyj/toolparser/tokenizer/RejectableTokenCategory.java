package com.parsleyj.toolparser.tokenizer;

/**
 * Created by Giuseppe on 18/03/16.
 */
public class RejectableTokenCategory extends TokenCategory {
    public RejectableTokenCategory(String tokenClassName, String pattern) {
        super(tokenClassName, pattern, true);
    }
}
