package com.parsleyj.smallsteptrack.parser.tokenizer;

import com.parsleyj.smallsteptrack.parser.SyntaxCaseComponent;

/**
 * todo javadoc
 */
public class TokenClass implements SyntaxCaseComponent {
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
    public String getSyntaxComponentName() {
        return tokenClassName;
    }
}
