package com.parsleyj.smallsteptrack.parser;

/**
 * Thrown by the Parser when the parsing operation with a non valid tree.
 */
public class ParseFailedException extends RuntimeException {
    private ParseTreeNode failureTree;

    /**
     * Creates a new instance of this exception
     * @param failureTree the non valid tree, that can be printed by catchers for debugging.
     */
    ParseFailedException(ParseTreeNode failureTree) {
        this.failureTree = failureTree;
    }

    public ParseTreeNode getFailureTree() {
        return failureTree;
    }
}
