package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Giuseppe on 19/03/16.
 * TODO: javadoc
 */
public class SyntaxTree {
    private int id;
    private List<SyntaxTree> children;
    // for terminals
    private Token parsedToken;
    private TokenClass tokenClass;
    //for more complex objects
    private SyntaxCase syntaxCase;
    private SyntaxClass syntaxClass;

    private boolean isTerminal = false;

    public SyntaxTree(int id, SyntaxTree... children) {
        this.id = id;
        this.children = Arrays.asList(children);
    }

    public SyntaxTree(int id) {
        this.children = new ArrayList<>();
        this.id = id;
    }

    public List<SyntaxTree> getChildren(){
        return children;
    }

    public SyntaxTree get(int i) {
        return children.get(i);
    }

    public SyntaxTree addLast(SyntaxTree ast) {
        children.add(ast);
        return this;
    }

    public SyntaxTree addFirst(SyntaxTree ast) {
        children.add(0, ast);
        return this;
    }

    public SyntaxTree add(int index, SyntaxTree ast) {
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
}
