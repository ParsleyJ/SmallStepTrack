package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Giuseppe on 19/03/16.
 * TODO: javadoc
 */
public class ASTObject {
    private int id;
    private List<ASTObject> children;
    // for terminals
    private Token parsedToken;
    private TokenClass tokenClass;
    //for more complex objects
    private SyntaxCase syntaxCase;
    private SyntaxClass syntaxClass;

    private boolean isTerminal = false;

    public ASTObject(int id, ASTObject... children) {
        this.id = id;
        this.children = Arrays.asList(children);
    }

    public ASTObject(int id) {
        this.children = new ArrayList<>();
        this.id = id;
    }

    public List<ASTObject> getChildren(){
        return children;
    }

    public ASTObject get(int i) {
        return children.get(i);
    }

    public ASTObject addLast(ASTObject ast) {
        children.add(ast);
        return this;
    }

    public ASTObject addFirst(ASTObject ast) {
        children.add(0, ast);
        return this;
    }

    public ASTObject add(int index, ASTObject ast) {
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
