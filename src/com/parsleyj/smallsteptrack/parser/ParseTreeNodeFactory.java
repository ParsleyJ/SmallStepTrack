package com.parsleyj.smallsteptrack.parser;

/**
 * Created by Giuseppe on 22/03/16.
 * TODO: javadoc
 */
public class ParseTreeNodeFactory {
    private final IDGenerator idGenerator;

    public ParseTreeNodeFactory(){
        this.idGenerator = new IDGenerator();
    }

    public ParseTreeNode newSyntaxTree(){
        return new ParseTreeNode(idGenerator.getNext());
    }

    public ParseTreeNode newSyntaxTree(ParseTreeNode... children){
        return new ParseTreeNode(idGenerator.getNext(), children);
    }

    private static class IDGenerator {
        private int counter = 0;

        public int getNext(){
            return counter++;
        }
    }
}
