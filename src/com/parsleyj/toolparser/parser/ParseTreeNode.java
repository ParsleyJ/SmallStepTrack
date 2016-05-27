package com.parsleyj.toolparser.parser;

import com.parsleyj.toolparser.tokenizer.Token;
import com.parsleyj.toolparser.tokenizer.TokenCategory;

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
    private TokenCategory tokenCategory;
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
     * todo doc
     * @param children
     */
    public void setChildren(List<ParseTreeNode> children) {
        this.children = children;
    }

    /**
     * todo doc
     */
    public ParseTreeNode getFirstChild(){
        return children.get(0);
    }

    /**
     * todo doc
     */
    public ParseTreeNode getLastChild(){
        return children.get(children.size()-1);
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

    /**
     * @return the token assigned to this terminal node
     */
    public Token getParsedToken() {
        return parsedToken;
    }

    /**
     * @param parsedToken the token assigned to this terminal node
     */
    public void setParsedToken(Token parsedToken) {
        this.parsedToken = parsedToken;
    }

    /**
     * @return the token category of the token assigned to this terminal node
     */
    public TokenCategory getTokenCategory() {
        return tokenCategory;
    }

    /**
     * @param tokenCategory the token category of the token assigned to this
     *                      terminal node
     */
    public void setTokenCategory(TokenCategory tokenCategory) {
        this.tokenCategory = tokenCategory;
    }

    /**
     * @return the syntax case that this non-terminal node corresponds to.
     */
    public SyntaxCase getSyntaxCase() {
        return syntaxCase;
    }

    /**
     * @param syntaxCase the syntax case that this non-terminal node
     *                   corresponds to.
     */
    public void setSyntaxCase(SyntaxCase syntaxCase) {
        this.syntaxCase = syntaxCase;
    }

    /**
     * @return the syntax class that this non-terminal node corresponds to.
     */
    public SyntaxClass getSyntaxClass() {
        return syntaxClass;
    }

    /**
     * @param syntaxClass the syntax class that this non-terminal node
     *                    corresponds to.
     */
    public void setSyntaxClass(SyntaxClass syntaxClass) {
        this.syntaxClass = syntaxClass;
    }

    /**
     * @return true if this node is a terminal class (i.e. assigned to a token
     *          and token class), false otherwise (i.e. assigned to a syntax case
     *          and a syntax class).
     */
    public boolean isTerminal() {
        return isTerminal;
    }

    /**
     * @param terminal true if this node is a terminal class (i.e. assigned to a token
     *                 and token class), false otherwise (i.e. assigned to a syntax case
     *                 and syntax class).
     */
    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    /**
     * @return the unique id of this node
     */
    public int getId() {
        return id;
    }

    /**
     * Prints the tree to {@link System}{@code .out} using this node as root.
     */
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
                        ("TOKEN:"+tree.getTokenCategory().getSyntaxComponentName()+": \""+tree.getParsedToken().getGeneratingString()+"\""):
                        (tree.getSyntaxClass().getSyntaxComponentName()+": "+tree.getSyntaxCase().getCaseName())
                )
        );
        for(ParseTreeNode child: tree.getChildren()){
            printTree(child, indentStart+1);
        }
    }
}
