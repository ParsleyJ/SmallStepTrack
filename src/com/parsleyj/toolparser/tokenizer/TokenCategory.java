package com.parsleyj.toolparser.tokenizer;

import com.parsleyj.toolparser.parser.SyntaxCaseComponent;

import java.util.Collections;
import java.util.List;

/**
 * Class representing a category of the lexicon. For example, the numerals
 * can be seen as a {@link TokenCategory}.
 */
public class TokenCategory implements SyntaxCaseComponent, LexicalPattern {
    private String tokenClassName;
    private String pattern;
    private boolean ignoreToken = false;

    /**
     * @param tokenClassName the unique name of this {@link TokenCategory}
     * @param pattern the regex pattern
     */
    public TokenCategory(String tokenClassName, String pattern) {
        this.tokenClassName = tokenClassName;
        this.pattern = pattern;
    }

    /**
     * @param tokenClassName the unique name of this {@link TokenCategory}
     * @param pattern the regex pattern
     * @param ignoreToken true if the found token must be rejected, false otherwise
     */
    public TokenCategory(String tokenClassName, String pattern, boolean ignoreToken) {
        this.tokenClassName = tokenClassName;
        this.pattern = pattern;
        this.ignoreToken = ignoreToken;
    }

    /**
     * Called by the {@link Tokenizer} to determine if the found token corresponding
     * this category must be discarded instead of inserted to the resulting token list.
     * @return true if this {@link TokenCategory} must be ignored by the {@link Tokenizer}, false otherwise.
     */
    public boolean isIgnorable() {
        return ignoreToken;
    }

    /**
     * @return an unique String name identifying this {@link TokenCategory}.
     */
    public String getTokenClassName() {
        return tokenClassName;
    }

    /**
     * @return the regex pattern used by the {@link Tokenizer} to find instances of this {@link TokenCategory}.
     */
    @Override
    public String getPattern() {
        return pattern;
    }

    //TODO: doc
    @Override
    public TokenCategory generateTokenCategory(String matchedString) {
        return this;
    }

    //TODO: doc
    @Override
    public List<TokenCategory> declaredTokenCategories() {
        return Collections.singletonList(this);
    }

    @Override
    public String getSyntaxComponentName() {
        return tokenClassName;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }
}
