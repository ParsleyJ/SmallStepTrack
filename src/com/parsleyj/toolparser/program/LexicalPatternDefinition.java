package com.parsleyj.toolparser.program;

import com.parsleyj.toolparser.semanticsconverter.TokenConverter;
import com.parsleyj.toolparser.tokenizer.LexicalPattern;

import java.util.List;

/**
 * Created by Giuseppe on 16/05/16.
 * TODO: javadoc
 */
public interface LexicalPatternDefinition extends LexicalPattern{
    List<TokenConverter> getDeclaredTokenConverters();
}
