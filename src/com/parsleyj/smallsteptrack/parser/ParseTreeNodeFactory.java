package com.parsleyj.smallsteptrack.parser;

/**
 * Factory used to create {@link ParseTreeNode}s with an automatically
 * generated unique id.
 */
public class ParseTreeNodeFactory {
    private final IDGenerator idGenerator;

    public ParseTreeNodeFactory(){
        this.idGenerator = new IDGenerator();
    }

    /**
     * Creates a new node with an automatically generated ID and no
     * child.
     * @return the new node
     */
    public ParseTreeNode newNodeTree(){
        return new ParseTreeNode(idGenerator.getNext());
    }

    /**
     * Creates a new node with an automatically generated ID and a list
     * of children.
     * @param children the children nodes.
     * @return the new node
     */
    public ParseTreeNode newNodeTree(ParseTreeNode... children){
        return new ParseTreeNode(idGenerator.getNext(), children);
    }

    /**
     * Private helper class used to generate unique IDs.
     */
    private static class IDGenerator {
        private int counter = 0;

        public int getNext(){
            return counter++;
        }
    }
}
