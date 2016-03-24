package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;

/**
 * Created by Giuseppe on 23/03/16.
 * TODO: javadoc
 */
public class TokenConverter {
    private TokenClass tokenClass;
    private TokenConverterMethod method;

    public TokenConverter(TokenClass tokenClass, TokenConverterMethod method) {
        this.tokenClass = tokenClass;
        this.method = method;
    }

    public TokenClass getTokenClass() {
        return tokenClass;
    }

    public  SmallStepSemanticObject convert(String generatingString, Semantics s){
        return method.convert(generatingString, s);
    }

}
