package com.parsleyj.smallsteptrack.parser;

/**
 * Created by Giuseppe on 22/03/16.
 * TODO: javadoc
 */
public class SyntaxTreeNodeFactory {
    private final IDGenerator idGenerator;

    public SyntaxTreeNodeFactory(){
        this.idGenerator = new IDGenerator();
    }

    public SyntaxTreeNode newSyntaxTree(){
        return new SyntaxTreeNode(idGenerator.getNext());
    }

    public SyntaxTreeNode newSyntaxTree(SyntaxTreeNode... children){
        return new SyntaxTreeNode(idGenerator.getNext(), children);
    }

    private static class IDGenerator {
        private int counter = 0;

        public int getNext(){
            return counter++;
        }
    }
}
