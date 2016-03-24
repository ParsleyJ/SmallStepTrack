package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.booleanexpr.*;
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
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //oldFact();
        newTest();
    }


    /*
        (y := x); ((a := 1); (while (y > 0) do ( a := (a * y) ; (y := (y - 1)))))
         */
    public static void newTest(){
        String storeName = "S";

        //LEXICON ---------------------------------------------------------------
        TokenClass skipToken = new TokenClass("SKIP_KEYWORD", "\\Qskip\\E");
        TokenClass trueToken = new TokenClass("TRUE_KEYWORD", "\\Qtrue\\E");
        TokenClass falseToken = new TokenClass("FALSE_KEYWORD", "\\Qfalse\\E");
        TokenClass whileToken = new TokenClass("WHILE_KEYWORD", "\\Qwhile\\E");
        TokenClass doToken = new TokenClass("DO_KEYWORD", "\\Qdo\\E");
        TokenClass ifToken = new TokenClass("IF_KEYWORD", "\\Qif\\E");
        TokenClass thenToken = new TokenClass("THEN_KEYWORD", "\\Qthen\\E");
        TokenClass elseToken = new TokenClass("ELSE_KEYWORD", "\\Qelse\\E");
        TokenClass identifierToken = new TokenClass("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]*");
        TokenClass plusToken = new TokenClass("PLUS", "\\Q+\\E");
        TokenClass minusToken = new TokenClass("MINUS", "\\Q-\\E");
        TokenClass asteriskToken = new TokenClass("ASTERISK", "\\Q*\\E");
        TokenClass assignmentOperatorToken = new TokenClass("ASSIGNMENT_OPERATOR", "\\Q:=\\E");
        TokenClass equalsOperatorToken = new TokenClass("EQUALS_OPERATOR", "\\Q=\\E");
        TokenClass greaterOperatorToken = new TokenClass("GREATER_OPERATOR", "\\Q>\\E");
        TokenClass lessOperatorToken = new TokenClass("LESS_OPERATOR", "\\Q<\\E");
        TokenClass semicolonToken = new TokenClass("SEMICOLON", "\\Q;\\E");
        TokenClass openBracketToken = new TokenClass("OPEN_ROUND_BRACKET", "\\Q(\\E");
        TokenClass closedBracketToken = new TokenClass("CLOSED_ROUND_BRACKET", "\\Q)\\E");
        TokenClass numeralToken = new TokenClass("NUMERAL", "(?<=\\s|^)[-+]?\\d+(?=\\s|$)");
        TokenClass blankToken = new RejectableTokenClass("BLANK", " ");

        List<TokenClass> lexicon = Arrays.asList(
                skipToken, trueToken, falseToken, whileToken, doToken, ifToken, thenToken, elseToken,
                identifierToken, plusToken, minusToken, asteriskToken, assignmentOperatorToken,
                equalsOperatorToken, greaterOperatorToken, lessOperatorToken, semicolonToken,
                openBracketToken, closedBracketToken, numeralToken,
                blankToken
        );

        //GRAMMAR ---------------------------------------------------------------
        SyntaxClass exp = new SyntaxClass("Exp");
        SyntaxClass comm = new SyntaxClass("Comm");
        SyntaxClass bool = new SyntaxClass("Bool");

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
        SyntaxCase commandBetweenRoundBrackets = new SyntaxCase("commandBetweenRoundBrackets", openBracketToken, comm, closedBracketToken); // ( C ) |
        SyntaxCase assignment = new SyntaxCase("assignment", new SpecificCaseComponent(exp, variable), assignmentOperatorToken, exp); // x := E |
        SyntaxCase sequentialComposition = new SyntaxCase("sequentialComposition", comm, semicolonToken, comm); //  C ; C |
        SyntaxCase ifThenElseStatement = new SyntaxCase("ifThenElseStatement", ifToken, bool, thenToken, comm, elseToken, comm); // if B then C1 else C2 |
        SyntaxCase whileStatement = new SyntaxCase("whileStatement", whileToken, bool, doToken, comm); // while B do C
        comm.setCases(skip, commandBetweenRoundBrackets, assignment, sequentialComposition, ifThenElseStatement, whileStatement);
        //...

        //Bool :=
        SyntaxCase trueVal = new SyntaxCase("trueVal", trueToken); // true |
        SyntaxCase falseVal = new SyntaxCase("falseVal", falseToken); // false |
        SyntaxCase booleanExpressionBetweenRoundBrackets = new SyntaxCase("booleanExpressionBetweenRoundBrackets", openBracketToken, bool, closedBracketToken); // ( B ) |
        SyntaxCase equalIntegerComparison = new SyntaxCase("equalIntegerComparison", exp, equalsOperatorToken, exp); // E = E |
        SyntaxCase greaterIntegerComparison = new SyntaxCase("greaterIntegerComparison", exp, greaterOperatorToken, exp); // E > E |
        SyntaxCase lessIntegerComparison = new SyntaxCase("lessIntegerComparison", exp, lessOperatorToken, exp); // E < E |
        bool.setCases(trueVal, booleanExpressionBetweenRoundBrackets, falseVal, equalIntegerComparison, greaterIntegerComparison, lessIntegerComparison);
        //...
        //TODO: complete with a not ambiguous grammar
        Grammar grammar = new Grammar(exp, comm, bool);

        //SEMANTICS ---------------------------------------------------------------
        Semantics semantics = new Semantics(
                Arrays.asList(
                        new SimpleTokenClassWrapConverter(numeral),
                        new SimpleTokenClassWrapConverter(variable),
                        new CheckedCaseConverter(expressionBetweenRoundBrackets, (node, s) -> new ExpressionBetweenRoundBrackets(s.resolve(node.get(1)))),
                        new ClosedBinaryOperationConverter<IntegerExpression>(sum, Sum::new),
                        new ClosedBinaryOperationConverter<IntegerExpression>(multiplication, Multiplication::new),
                        new ClosedBinaryOperationConverter<IntegerExpression>(subtraction, Subtraction::new),

                        new SimpleTokenClassWrapConverter(skip),
                        new CheckedCaseConverter(commandBetweenRoundBrackets, (node, s)-> new CommandBetweenRoundBrackets(s.resolve(node.get(1)))),
                        new UnclosedBinaryOperationConverter<Command, Variable, IntegerExpression>
                                (assignment, (var, expr) -> new Assignment(storeName, var, expr)),
                        new ClosedBinaryOperationConverter<Command>(sequentialComposition, SequentialComposition::new),
                        new CheckedCaseConverter(whileStatement, (node, s) -> new WhileCommand(s.resolve(node.get(1)), s.resolve(node.get(3)))),
                        new CheckedCaseConverter(ifThenElseStatement, (node, s) ->
                                new IfThenElseCommand(s.resolve(node.get(1)), s.resolve(node.get(3)), s.resolve(node.get(5)))),

                        new SimpleTokenClassWrapConverter(trueVal),
                        new SimpleTokenClassWrapConverter(falseVal),
                        new CheckedCaseConverter(booleanExpressionBetweenRoundBrackets, (node, s) -> new ExpressionBetweenRoundBrackets(s.resolve(node.get(1)))),
                        new UnclosedBinaryOperationConverter<BooleanExpression, IntegerExpression, IntegerExpression>
                                (equalIntegerComparison, EqualIntegerComparison::new),
                        new UnclosedBinaryOperationConverter<BooleanExpression, IntegerExpression, IntegerExpression>
                                (greaterIntegerComparison, GreaterIntegerComparison::new),
                        new UnclosedBinaryOperationConverter<BooleanExpression, IntegerExpression, IntegerExpression>
                                (lessIntegerComparison, LessIntegerComparison::new)
                        //TODO: add all semantics converters
                ), Arrays.asList(
                        new TokenConverter(numeralToken, (generatingString, s) -> new Numeral(Integer.decode(generatingString))),
                        new TokenConverter(identifierToken, ((generatingString, s) -> new Variable(storeName, generatingString))),
                        new TokenConverter(skipToken, (g, s) -> new Skip()),
                        new TokenConverter(trueToken, (g, s) -> new True()),
                        new TokenConverter(falseToken, (g, s) -> new False())
                        //TODO: add all semantics converters
                )
        );


        ProgramGenerator programGenerator = new ProgramGenerator(lexicon, grammar, semantics);

        Scanner sc = new Scanner(System.in);
        System.out.println("Insert program: ");
        String input = sc.nextLine();
        System.out.println(" ");


        //programGenerator.setPrintDebugMessages(true);
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
