package com.parsleyj.smallsteptrack.syntax;

import com.parsleyj.smallsteptrack.syntax.tokenizer.RejectableTokenClass;
import com.parsleyj.smallsteptrack.syntax.tokenizer.TokenClass;

import java.util.*;

/**
 * Syntax definition class (EXPERIMENTING)
 */
public class Syntax {

    public Grammar getGrammar() {


        TokenClass string = new TokenClass("STRING_CONSTANT", "([\"'])(?:(?=(\\\\?))\\2.)*?\\1");
        TokenClass identifier = new TokenClass("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]*");
        TokenClass add = new TokenClass("ADD_OPERATOR", "(\\+)");
        TokenClass sub = new TokenClass("SUB_OPERATOR", "(\\-)");
        TokenClass mul = new TokenClass("MUL_OPERATOR", "(\\*)");
        TokenClass openBracket = new TokenClass("OPEN_ROUND_BRACKET", "(\\()");
        TokenClass closedBracket = new TokenClass("CLOSED_ROUND_BRACKET", "(\\))");
        TokenClass numeral = new TokenClass("NUMERAL", "(?<=\\s|^)[-+]?\\d+(?=\\s|$)");
        TokenClass blank = new RejectableTokenClass("BLANK", " ");
        SyntaxCase variable = new SyntaxCase(identifier);
        SyntaxClass exp = new SyntaxClass("Exp");
        SyntaxClass comm = new SyntaxClass("Comm");
        SyntaxClass bool = new SyntaxClass("Bool");
        exp.setCases(
                new SyntaxCase(identifier),
                new SyntaxCase(numeral),
                new SyntaxCase(openBracket, exp, closedBracket),
                new SyntaxCase(exp, add, exp),
                new SyntaxCase(exp, sub, exp),
                new SyntaxCase(exp, mul, exp)
        );

        return new Grammar(exp, comm, bool);
    }

    public class Grammar {
        public Grammar(SyntaxClass... list) {

        }
    }

    public class SyntaxClass implements SyntaxEntity{

        private String name;
        private List<SyntaxCase> cases;

        public SyntaxClass(String name){
            this.name = name;
            cases = null;
        }

        public SyntaxClass(String name, List<SyntaxCase> cases) {
            this.name = name;
            this.cases = cases;
        }

        public List<SyntaxCase> getSyntaxComponents(){
            return cases;
        }

        public void setCases(SyntaxCase... cases) {
            this.cases = Arrays.asList(cases);
        }

        @Override
        public String getName() {
            return name;
        }

    }


    public interface SyntaxEntity {
        //TODO add some more useful metadata
        String getName();
    }

    public class SyntaxCase {
        private List<SyntaxEntity> structure;

        public SyntaxCase(SyntaxEntity... structure) {
            this.structure = Arrays.asList(structure);
        }

        List<SyntaxEntity> getStructure(){
            return structure;
        }
    }
}
