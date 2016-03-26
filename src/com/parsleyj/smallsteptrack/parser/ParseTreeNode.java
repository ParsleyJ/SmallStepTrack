package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents a node of the parse tree.
 */
public class ParseTreeNode {
    private int id;
    private List<ParseTreeNode> children;
    // for terminals
    private Token parsedToken;
    private TokenClass tokenClass;
    //for more complex objects
    private SyntaxCase syntaxCase;
    private SyntaxClass syntaxClass;

    private boolean isTerminal = false;

    /**
     * Creates a new node with the given ID and a list of children.
     * @param id the id of this node
     * @param children the children nodes.
     */
    public ParseTreeNode(int id, ParseTreeNode... children) {
        this.id = id;
        this.children = Arrays.asList(children);
    }

    /**
     * Creates a new node with the given ID and no children.
     * @param id the id of this node
     */
    public ParseTreeNode(int id) {
        this.children = new ArrayList<>();
        this.id = id;
    }

    /**
     * @return a list of all the children of this node.
     */
    public List<ParseTreeNode> getChildren(){
        return children;
    }

    /**
     * Returns the node child at the index {@code i}
     * @param i the index
     * @return the node at index {@code i}
     */
    public ParseTreeNode get(int i) {
        return children.get(i);
    }

    /**
     * Adds a node to the children list of this node, at the last position.
     * @param ast the node
     * @return this object for method chaining.
     */
    public ParseTreeNode addLast(ParseTreeNode ast) {
        children.add(ast);
        return this;
    }

    /**
     * Adds a node to the children list of this node, at the first position.
     * @param ast the node
     * @return this object for method chaining.
     */
    public ParseTreeNode addFirst(ParseTreeNode ast) {
        children.add(0, ast);
        return this;
    }
    /**
     * Adds a node to the children list of this node, at the {@code index} position.
     * @param index the index
     * @param ast the node
     * @return this object for method chaining.
     */
    public ParseTreeNode add(int index, ParseTreeNode ast) {
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

    private static void printTree(ParseTreeNode tree, int indentStart){
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
        for(ParseTreeNode child: tree.getChildren()){
            printTree(child, indentStart+1);
        }
    }
}
