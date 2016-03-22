package com.parsleyj.smallsteptrack.parser;

/**
 * Created by Giuseppe on 22/03/16.
 * TODO: javadoc
 */
public class ASTFactory {
    private final IDGenerator idGenerator;

    public ASTFactory(){
        this.idGenerator = new IDGenerator();
    }

    public ASTObject newASTObject(){
        return new ASTObject(idGenerator.getNext());
    }

    public ASTObject newASTObject(ASTObject... children){
        return new ASTObject(idGenerator.getNext(), children);
    }

    private static class IDGenerator {
        static int counter = 0;
        public int getNext(){
            return counter++;
        }
    }
}
