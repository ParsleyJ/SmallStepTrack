package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.parser.tokenizer.TokenCategory;

/**
 * Helper class used to define a {@link TokenCategory} and a {@link TokenConverter}, using
 * the given {@link TokenConverterMethod}.
 */
public class TokenCategoryDefinition extends TokenCategory {
    private TokenConverter converter;

    public TokenCategoryDefinition(String tokenClassName, String pattern, TokenConverterMethod method) {
        super(tokenClassName, pattern);
        converter = new TokenConverter(this, method);
    }

    public TokenCategoryDefinition(String tokenClassName, String pattern){
        super(tokenClassName, pattern);
        converter = null;
    }

    public TokenCategoryDefinition(String tokenClassName, String pattern, boolean ignorable){
        super(tokenClassName, pattern, ignorable);
        converter = null;
    }


    public TokenConverter getConverter() {
        return converter;
    }
}
