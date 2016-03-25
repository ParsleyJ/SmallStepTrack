package com.parsleyj.smallsteptrack.parser.tokenizer;

import com.parsleyj.smallsteptrack.parser.SyntaxCaseComponent;

/**
 * Class representing a category of the lexicon. For example, the numerals
 * can be seen as a {@link TokenClass}.
 */
public class TokenClass implements SyntaxCaseComponent {
    private String tokenClassName;
    private String pattern;
    private boolean ignoreToken = false;

    /**
     * @param tokenClassName the unique name of this {@link TokenClass}
     * @param pattern the regex pattern
     */
    public TokenClass(String tokenClassName, String pattern) {
        this.tokenClassName = tokenClassName;
        this.pattern = pattern;
    }

    /**
     * @param tokenClassName the unique name of this {@link TokenClass}
     * @param pattern the regex pattern
     * @param ignoreToken true if the found token must be discarted, false otherwise
     */
    public TokenClass(String tokenClassName, String pattern, boolean ignoreToken) {
        this.tokenClassName = tokenClassName;
        this.pattern = pattern;
        this.ignoreToken = ignoreToken;
    }

    /**
     * Called by the {@link Tokenizer} to determine if the found token corresponding
     * this category must be discarded instead of inserted to the resulting token list.
     * @return true if this {@link TokenClass} must be ignored by the {@link Tokenizer}, false otherwise.
     */
    public boolean isIgnorable() {
        return ignoreToken;
    }

    /**
     * @return an unique String name identifying this {@link TokenClass}.
     */
    public String getTokenClassName() {
        return tokenClassName;
    }

    /**
     * @return the regex pattern used by the {@link Tokenizer} to find instances of this {@link TokenClass}.
     */
    public String getPattern() {
        return pattern;
    }


    @Override
    public String getSyntaxComponentName() {
        return tokenClassName;
    }
}
