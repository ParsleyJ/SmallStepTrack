package com.parsleyj.smallsteptrack.syntax.tokenizer;

/**
 * Assigned to Wyvilo
 * (note: you can add things here if you want to)
 */
public class TokenClass {
    private String tokenClassName;
    private String pattern;

    public TokenClass(String tokenClassName, String pattern) {
        this.tokenClassName = tokenClassName;
        this.pattern = pattern;
    }

    public String getTokenClassName() {
        return tokenClassName;
    }

    public String getPattern() {
        return pattern;
    }
}
