package com.parsleyj.smallsteptrack.syntax.tokenizer;

/**
 * Created by Giuseppe on 18/03/16.
 */
class UnscannedTempToken extends Token {

    UnscannedTempToken(String generatingString) {
        super(generatingString, UNSCANNED);
    }
}
