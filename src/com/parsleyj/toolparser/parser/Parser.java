package com.parsleyj.toolparser.parser;

import com.parsleyj.toolparser.tokenizer.Token;

import java.util.List;

/**
 * Created by Giuseppe on 17/04/16.
 * TODO: javadoc
 */
public interface Parser {
    ParseTreeNode parse(List<Token> tokens);
}
