package com.parsleyj.toolparser.program;

/**
 * Created by Giuseppe on 16/05/16.
 * TODO: javadoc
 */
public abstract class MultiPatternDefinition implements LexicalPatternDefinition {
    private String pattern;

    protected MultiPatternDefinition(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}
