package com.parsleyj.toolparser.parser;

/**
 * {@link SyntaxCaseComponent}s are used to create a {@link SyntaxCase}.
 */
public interface SyntaxCaseComponent {
    /**
     * @return an unique String name identifying this {@link SyntaxCaseComponent}
     */
    String getSyntaxComponentName();

    boolean isTerminal();

    default boolean matches(SyntaxCaseComponent x){
        return this.getSyntaxComponentName().equals(x.getSyntaxComponentName());
    }

}
