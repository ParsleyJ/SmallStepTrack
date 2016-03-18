package com.parsleyj.smallsteptrack.syntax.tokenizer;

import com.parsleyj.smallsteptrack.syntax.Syntax;

/**
 * todo javadoc
 */
public class TokenClass implements Syntax.SyntaxEntity{
    private String tokenClassName;
    private String pattern;
    private boolean ignoreToken = false;

    public TokenClass(String tokenClassName, String pattern) {
        this.tokenClassName = tokenClassName;
        this.pattern = pattern;
    }

    public TokenClass(String tokenClassName, String pattern, boolean ignoreToken) {
        this.tokenClassName = tokenClassName;
        this.pattern = pattern;
        this.ignoreToken = ignoreToken;
    }

    public boolean isIgnorable() {
        return ignoreToken;
    }

    public String getTokenClassName() {
        return tokenClassName;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String getName() {
        return tokenClassName;
    }
}
