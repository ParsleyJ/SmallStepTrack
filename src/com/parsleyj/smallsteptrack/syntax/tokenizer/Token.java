package com.parsleyj.smallsteptrack.syntax.tokenizer;

/**
 * Assigned to Wyvilo
 * (note: you can add things here if you want to)
 */
public class Token {
    public static final String NUMERAL = "NUMERAL";
    public static final String TERMINAL_SYMBOL = "TERMINAL_SYMBOL";
    public static final String IDENTIFIER = "IDENTIFIER";

    private String generatingString;
    private String tokenClass;

    public Token(String generatingString, String tokenClass) {
        this.generatingString = generatingString;
        this.tokenClass = tokenClass;
    }

    public String getGeneratingString() {
        return generatingString;
    }

    public String getTokenClass() {
        return tokenClass;
    }
}
