package com.parsleyj.toolparser.tokenizer;

import java.util.List;

/**
 * Created by Giuseppe on 16/05/16.
 * TODO: javadoc
 */
public interface LexicalPattern {
    public String getPattern();
    public TokenCategory generateTokenCategory(String matchedString);
    public List<TokenCategory> declaredTokenCategories();
}
