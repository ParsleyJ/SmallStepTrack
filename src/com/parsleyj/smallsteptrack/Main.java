package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.booleanexpr.GreaterIntegerComparison;
import com.parsleyj.smallsteptrack.command.*;
import com.parsleyj.smallsteptrack.configuration.Configuration;
import com.parsleyj.smallsteptrack.configuration.IntegerStore;
import com.parsleyj.smallsteptrack.integerexpr.*;
import com.parsleyj.smallsteptrack.parser.*;
import com.parsleyj.smallsteptrack.parser.tokenizer.RejectableTokenClass;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;
import com.parsleyj.smallsteptrack.program.*;
import com.parsleyj.smallsteptrack.program.InvalidTokenFoundException;
import com.parsleyj.smallsteptrack.utils.SimpleTokenClassWrapConverter;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //oldFact();
        newTest();
    }

    public static void newTest(){
        String storeName = "S";

        SyntaxClass exp = new SyntaxClass("Exp");
        SyntaxClass comm = new SyntaxClass("Comm");
        SyntaxClass bool = new SyntaxClass("Bool");

        TokenClass skipToken = new TokenClass("SKIP_KEYWORD", "\\Qskip\\E");
        TokenClass identifierToken = new TokenClass("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]*");
        TokenClass plusToken = new TokenClass("PLUS", "\\Q+\\E");
        TokenClass minusToken = new TokenClass("MINUS", "\\Q-\\E");
        TokenClass asteriskToken = new TokenClass("ASTERISK", "\\Q*\\E");
        TokenClass assignmentOperatorToken = new TokenClass("ASSIGNMENT_OPERATOR", "\\Q:=\\E");
        TokenClass semicolonToken = new TokenClass("SEMICOLON", "\\Q;\\E");
        TokenClass openBracketToken = new TokenClass("OPEN_ROUND_BRACKET", "\\Q(\\E");
        TokenClass closedBracketToken = new TokenClass("CLOSED_ROUND_BRACKET", "\\Q)\\E");
        TokenClass numeralToken = new TokenClass("NUMERAL", "(?<=\\s|^)[-+]?\\d+(?=\\s|$)");
        TokenClass blankToken = new RejectableTokenClass("BLANK", " ");

        //Exp :=
        SyntaxCase variable = new SyntaxCase("variable", identifierToken); // x |
        SyntaxCase numeral = new SyntaxCase("numeral", numeralToken); // n |
        SyntaxCase expressionBetweenRoundBrackets = new SyntaxCase("expressionBetweenRoundBrackets", openBracketToken, exp, closedBracketToken); // ( E ) |
        SyntaxCase sum =new SyntaxCase("sum", exp, plusToken, exp); // E + E |
        SyntaxCase subtraction = new SyntaxCase("subtraction", exp, minusToken, exp); // E - E |
        SyntaxCase multiplication = new SyntaxCase("multiplication", exp, asteriskToken, exp); // E * E
        exp.setCases(variable, numeral, expressionBetweenRoundBrackets, sum, subtraction, multiplication);

        //Comm :=
        SyntaxCase skip = new SyntaxCase("skip", skipToken); // skip |
        SyntaxCase assignment = new SyntaxCase("assignment", new SpecificCaseComponent(exp, variable), assignmentOperatorToken, exp); // x := E |
        SyntaxCase sequentialComposition = new SyntaxCase("sequentialComposition", comm, semicolonToken, comm); //  C ; C |
        comm.setCases(skip, assignment, sequentialComposition);
        //...

        //Bool :=
        //...


        //TODO: complete with a not ambiguous grammar

        ProgramGenerator programGenerator =
                new ProgramGenerator(
                        Arrays.asList(
                                skipToken, identifierToken, plusToken, minusToken,
                                asteriskToken, assignmentOperatorToken, semicolonToken,
                                openBracketToken, closedBracketToken, numeralToken,
                                blankToken
                        ),
                        new Grammar(
                                exp,
                                comm,
                                bool),
                        new Semantics(//TODO: improve this part with a easier and less verbose converter definition mechanism
                                Arrays.asList(
                                        new SimpleTokenClassWrapConverter(numeral),
                                        new SimpleTokenClassWrapConverter(variable),
                                        new CaseConverter(expressionBetweenRoundBrackets, (node, s) ->{
                                            if(node.getChildren().size() == 3){
                                                return new ExpressionBetweenRoundBrackets(s.resolve(node.get(1)));
                                            }else throw new InvalidParseTreeException();
                                        }),
                                        new CaseConverter(sum, (node, s) -> {
                                            if(node.getChildren().size() == 3){
                                                return new Sum(s.resolve(node.get(0)), s.resolve(node.get(2)));
                                            }else throw new InvalidParseTreeException();
                                        }),
                                        new SimpleTokenClassWrapConverter(skip),
                                        new CaseConverter(assignment, (node, s)->{
                                            if(node.getChildren().size() == 3){
                                                return new Assignment(storeName, s.resolve(node.get(0)), s.resolve(node.get(2)));
                                            }else throw new InvalidParseTreeException();
                                        }),
                                        new CaseConverter(sequentialComposition, (node, s) -> {
                                            if(node.getChildren().size() == 3){
                                                return new SequentialComposition(s.resolve(node.get(0)), s.resolve(node.get(2)));
                                            }else throw new InvalidParseTreeException();
                                        })

                                        //TODO: add all semantics converters
                                ), Arrays.asList(
                                        new TokenConverter(numeralToken, (generatingString, s) -> {
                                            try{
                                                Integer i =Integer.decode(generatingString);
                                                return new Numeral(i);
                                            }catch (NumberFormatException e){
                                                throw new InvalidTokenFoundException();
                                            }
                                        }),
                                        new TokenConverter(identifierToken, ((generatingString, s) -> new Variable(storeName, generatingString))),
                                        new TokenConverter(skipToken, ((generatingString, s) -> new Skip()))
                                        //TODO: add all semantics converters
                                )
                        )
                );

        Scanner sc = new Scanner(System.in);
        System.out.println("Insert program: ");
        String input = sc.nextLine();
        System.out.println(" ");


        programGenerator.setPrintDebugMessages(true);
        Program p = programGenerator.generate("test", input, (program, configuration) -> {
            Command c = (Command) program.getRootSemanticObject();
            //IntegerExpression c = (IntegerExpression) program.getRootSemanticObject();
            if (!c.isTerminal()) {
                program.setRootSemanticObject(c.step(configuration));
                return false;
            } else {
                return true;
            }
        });
        // creates a new store
        IntegerStore store = new IntegerStore(storeName);
        store.write("x", 3);
        System.out.println("Execution Track:");
        System.out.println(" ");
        p.executeProgram(store);
    }

    public static void oldFact(){
        String sName = "S"; //the name of the IntegerStore, used by Variable and Assignment classes to access the store.

        /*
        y := x; a := 1;
        while y > 0 do
        a := a * y;
        y := y - 1
         */
        Program program = new Program(
                "Factorial",
                new SequentialComposition(
                        new SequentialComposition(
                                new Assignment(sName, new Variable(sName, "y"),
                                        new Variable(sName, "x")), // y := x;
                                new Assignment(sName, new Variable(sName, "a"),
                                        new Numeral(1)) // a := 1;
                        ),
                        new WhileCommand(new GreaterIntegerComparison(new Variable(sName, "y"), new Numeral(0)), // while y > 0 do
                                new SequentialComposition(
                                        new Assignment(sName, new Variable(sName, "a"),
                                                new Multiplication(new Variable(sName, "a"), new Variable(sName, "y"))), // a := a * y;
                                        new Assignment(sName, new Variable(sName, "y"),
                                                new Subtraction(new Variable(sName, "y"), new Numeral(1))) // y := y - 1
                                )
                        )
                )
        ){

            @Override
            public boolean step(Configuration configuration) {
                Command c = (Command) this.getRootSemanticObject();
                if (!c.isTerminal()) {
                    setRootSemanticObject(c.step(configuration));
                    return false;
                } else {
                    return true;
                }
            }
        };

        // creates a new store
        IntegerStore store = new IntegerStore(sName);
        // writes 3 to x in the store
        store.write("x", 3);

        //starts the execution
        program.executeProgram(store);
    }


}
