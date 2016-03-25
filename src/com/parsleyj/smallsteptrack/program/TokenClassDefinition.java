package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;

/**
 * Created by Giuseppe on 25/03/16.
 * TODO: javadoc
 */
public class TokenClassDefinition extends TokenClass{
    private TokenConverter converter;

    public TokenClassDefinition(String tokenClassName, String pattern, TokenConverterMethod method) {
        super(tokenClassName, pattern);
        converter = new TokenConverter(this, method);
    }

    public TokenClassDefinition(String tokenClassName, String pattern){
        super(tokenClassName, pattern);
        converter = null;
    }

    public TokenClassDefinition(String tokenClassName, String pattern, boolean ignorable){
        super(tokenClassName, pattern, ignorable);
        converter = null;
    }


    public TokenConverter getConverter() {
        return converter;
    }
}
