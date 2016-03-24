package com.parsleyj.smallsteptrack.parser;

/**
 * Created by Giuseppe on 24/03/16.
 * TODO: javadoc
 */
public class ParseFailedException extends RuntimeException {
    private SyntaxTreeNode failureTree;

    ParseFailedException(SyntaxTreeNode failureTree) {
        this.failureTree = failureTree;
    }

    public SyntaxTreeNode getFailureTree() {
        return failureTree;
    }
}
