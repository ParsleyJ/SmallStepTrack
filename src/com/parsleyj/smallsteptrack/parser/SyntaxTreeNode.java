package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Giuseppe on 19/03/16.
 * TODO: javadoc
 */
public class SyntaxTreeNode {
    private int id;
    private List<SyntaxTreeNode> children;
    // for terminals
    private Token parsedToken;
    private TokenClass tokenClass;
    //for more complex objects
    private SyntaxCase syntaxCase;
    private SyntaxClass syntaxClass;

    private boolean isTerminal = false;

    public SyntaxTreeNode(int id, SyntaxTreeNode... children) {
        this.id = id;
        this.children = Arrays.asList(children);
    }

    public SyntaxTreeNode(int id) {
        this.children = new ArrayList<>();
        this.id = id;
    }

    public List<SyntaxTreeNode> getChildren(){
        return children;
    }

    public SyntaxTreeNode get(int i) {
        return children.get(i);
    }

    public SyntaxTreeNode addLast(SyntaxTreeNode ast) {
        children.add(ast);
        return this;
    }

    public SyntaxTreeNode addFirst(SyntaxTreeNode ast) {
        children.add(0, ast);
        return this;
    }

    public SyntaxTreeNode add(int index, SyntaxTreeNode ast) {
        children.add(index, ast);
        return this;
    }

    public Token getParsedToken() {
        return parsedToken;
    }

    public void setParsedToken(Token parsedToken) {
        this.parsedToken = parsedToken;
    }

    public TokenClass getTokenClass() {
        return tokenClass;
    }

    public void setTokenClass(TokenClass tokenClass) {
        this.tokenClass = tokenClass;
    }

    public SyntaxCase getSyntaxCase() {
        return syntaxCase;
    }

    public void setSyntaxCase(SyntaxCase syntaxCase) {
        this.syntaxCase = syntaxCase;
    }

    public SyntaxClass getSyntaxClass() {
        return syntaxClass;
    }

    public void setSyntaxClass(SyntaxClass syntaxClass) {
        this.syntaxClass = syntaxClass;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public int getId() {
        return id;
    }

    public void printTree(){
        printTree(this, 0);
    }

    private static void printTree(SyntaxTreeNode tree, int indentStart){
        StringBuilder sb = new StringBuilder("");
        IntStream.range(0, indentStart).forEach(i -> {
            if (i != indentStart-1) sb.append("  ");
            else sb.append("|-");
        });
        System.out.println(sb.toString()+": "+
                (tree.isTerminal()?
                        ("TOKEN:"+tree.getTokenClass().getSyntaxComponentName()+": \""+tree.getParsedToken().getGeneratingString()+"\""):
                        (tree.getSyntaxClass().getSyntaxComponentName()+": "+tree.getSyntaxCase().getCaseName())
                )
        );
        for(SyntaxTreeNode child: tree.getChildren()){
            printTree(child, indentStart+1);
        }
    }
}