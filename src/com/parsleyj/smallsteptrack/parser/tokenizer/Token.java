package com.parsleyj.smallsteptrack.parser.tokenizer;

/**
 * This is a data structure used to carry information about a
 * portion of program around the tokenizer and the parser.
 */
public class Token {
    public static final String UNSCANNED = "UNSCANNED";

    private String generatingString;
    private String tokenClass;

    public Token(String generatingString, String tokenClass) {
        this.generatingString = generatingString;
        this.tokenClass = tokenClass;
    }

    /**
     * @return the string that matched the {@code TokenClass}' pattern.
     */
    public String getGeneratingString() {
        return generatingString;
    }

    /**
     * @return a string name of the corresponding {@code TokenClass}.
     */
    public String getTokenClassName() {
        return tokenClass;
    }

}
