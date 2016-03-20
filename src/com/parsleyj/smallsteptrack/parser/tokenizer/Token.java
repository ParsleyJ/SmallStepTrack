package com.parsleyj.smallsteptrack.parser.tokenizer;

/**
 * todo: javadoc
 */
public class Token {
    public static final String UNSCANNED = "UNSCANNED";
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

    public String getTokenClassName() {
        return tokenClass;
    }

}
