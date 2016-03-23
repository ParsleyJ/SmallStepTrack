package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;

/**
 * Created by Giuseppe on 23/03/16.
 * TODO: javadoc
 */
public abstract class TokenConverter {
    private TokenClass tokenClass;

    protected TokenConverter(TokenClass tokenClass) {
        this.tokenClass = tokenClass;
    }

    public TokenClass getTokenClass() {
        return tokenClass;
    }

    public abstract SmallStepSemanticObject convert(String generatingString, Semantics s);
}
