package com.parsleyj.toolparser.program;

import com.parsleyj.toolparser.semanticsconverter.TokenConverter;
import com.parsleyj.toolparser.semanticsconverter.TokenConverterMethod;
import com.parsleyj.toolparser.tokenizer.TokenCategory;

import java.util.Collections;
import java.util.List;

/**
 * Helper class used to defineParameter a {@link TokenCategory} and a {@link TokenConverter}, using
 * the given {@link TokenConverterMethod}.
 */
public class TokenCategoryDefinition extends TokenCategory implements LexicalPatternDefinition{
    private TokenConverter converter;

    public TokenCategoryDefinition(String tokenClassName, String pattern, TokenConverterMethod method) {
        super(tokenClassName, pattern);
        converter = new TokenConverter(this, method);
    }

    public TokenCategoryDefinition(String tokenClassName, String pattern){
        super(tokenClassName, pattern);
        converter = null;
    }

    public TokenCategoryDefinition(String tokenClassName, String pattern, boolean ignorable){
        super(tokenClassName, pattern, ignorable);
        converter = null;
    }


    public TokenConverter getConverter() {
        return converter;
    }

    @Override
    public List<TokenConverter> getDeclaredTokenConverters() {
        return converter==null? Collections.emptyList() : Collections.singletonList(converter);
    }
}
