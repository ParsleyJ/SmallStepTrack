package com.parsleyj.smallsteptrack.parser;

/**
 * {@link SyntaxCaseComponent}s are used to create a {@link SyntaxCase}.
 */
public interface SyntaxCaseComponent {
    /**
     * @return an unique String name identifying this {@link SyntaxCaseComponent}
     */
    String getSyntaxComponentName();
}
