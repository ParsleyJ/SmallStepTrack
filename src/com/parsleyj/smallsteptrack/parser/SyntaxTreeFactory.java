package com.parsleyj.smallsteptrack.parser;

/**
 * Created by Giuseppe on 22/03/16.
 * TODO: javadoc
 */
public class SyntaxTreeFactory {
    private final IDGenerator idGenerator;

    public SyntaxTreeFactory(){
        this.idGenerator = new IDGenerator();
    }

    public SyntaxTree newSyntaxTree(){
        return new SyntaxTree(idGenerator.getNext());
    }

    public SyntaxTree newSyntaxTree(SyntaxTree... children){
        return new SyntaxTree(idGenerator.getNext(), children);
    }

    private static class IDGenerator {
        static int counter = 0;
        public int getNext(){
            return counter++;
        }
    }
}
