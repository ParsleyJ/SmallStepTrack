package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.configurations.Configuration;
import com.parsleyj.smallsteptrack.configurations.IntegerStore;
import com.parsleyj.smallsteptrack.parser.SpecificCaseComponent;
import com.parsleyj.smallsteptrack.parser.SyntaxClass;
import com.parsleyj.smallsteptrack.program.Program;
import com.parsleyj.smallsteptrack.program.ProgramGenerator;
import com.parsleyj.smallsteptrack.program.SyntaxCaseDefinition;
import com.parsleyj.smallsteptrack.program.TokenCategoryDefinition;
import com.parsleyj.smallsteptrack.semanticsconverter.CBOConverterMethod;
import com.parsleyj.smallsteptrack.semanticsconverter.UBOConverterMethod;
import com.parsleyj.smallsteptrack.utils.SimpleWrapConverterMethod;
import com.parsleyj.smallsteptrack.whilesemantics.booleanexpr.*;
import com.parsleyj.smallsteptrack.whilesemantics.command.*;
import com.parsleyj.smallsteptrack.whilesemantics.integerexpr.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //oldFact();
        newTest();
    }


    /*
        y := x; a := 1; while y > 0 do (a := a * y; y := y - 1)
     */
    public static void newTest(){
        String storeName = "S";

        //LEXICON ---------------------------------------------------------------
        TokenCategoryDefinition skipToken = new TokenCategoryDefinition("SKIP_KEYWORD", "\\Qskip\\E",
                (g, s) -> new Skip());
        TokenCategoryDefinition trueToken = new TokenCategoryDefinition("TRUE_KEYWORD", "\\Qtrue\\E",
                (g, s) -> new True());
        TokenCategoryDefinition falseToken = new TokenCategoryDefinition("FALSE_KEYWORD", "\\Qfalse\\E",
                (g, s) -> new False());
        TokenCategoryDefinition whileToken = new TokenCategoryDefinition("WHILE_KEYWORD", "\\Qwhile\\E");
        TokenCategoryDefinition doToken = new TokenCategoryDefinition("DO_KEYWORD", "\\Qdo\\E");
        TokenCategoryDefinition ifToken = new TokenCategoryDefinition("IF_KEYWORD", "\\Qif\\E");
        TokenCategoryDefinition thenToken = new TokenCategoryDefinition("THEN_KEYWORD", "\\Qthen\\E");
        TokenCategoryDefinition elseToken = new TokenCategoryDefinition("ELSE_KEYWORD", "\\Qelse\\E");
        TokenCategoryDefinition identifierToken = new TokenCategoryDefinition("IDENTIFIER", "[_a-zA-Z][_a-zA-Z0-9]*",
                (g, s) -> new Variable(storeName, g));
        TokenCategoryDefinition plusToken = new TokenCategoryDefinition("PLUS", "\\Q+\\E");
        TokenCategoryDefinition minusToken = new TokenCategoryDefinition("MINUS", "\\Q-\\E");
        TokenCategoryDefinition asteriskToken = new TokenCategoryDefinition("ASTERISK", "\\Q*\\E");
        TokenCategoryDefinition assignmentOperatorToken = new TokenCategoryDefinition("ASSIGNMENT_OPERATOR", "\\Q:=\\E");
        TokenCategoryDefinition equalsOperatorToken = new TokenCategoryDefinition("EQUALS_OPERATOR", "\\Q=\\E");
        TokenCategoryDefinition greaterOperatorToken = new TokenCategoryDefinition("GREATER_OPERATOR", "\\Q>\\E");
        TokenCategoryDefinition lessOperatorToken = new TokenCategoryDefinition("LESS_OPERATOR", "\\Q<\\E");
        TokenCategoryDefinition semicolonToken = new TokenCategoryDefinition("SEMICOLON", "\\Q;\\E");
        TokenCategoryDefinition openBracketToken = new TokenCategoryDefinition("OPEN_ROUND_BRACKET", "\\Q(\\E");
        TokenCategoryDefinition closedBracketToken = new TokenCategoryDefinition("CLOSED_ROUND_BRACKET", "\\Q)\\E");
        TokenCategoryDefinition numeralToken = new TokenCategoryDefinition("NUMERAL", "(?<=\\s|^)[-+]?\\d+(?=\\s|$)",
                (g, s) -> new Numeral(Integer.decode(g)));
        TokenCategoryDefinition blankToken = new TokenCategoryDefinition("BLANK", " ", true);//rejectable

        TokenCategoryDefinition[] lexicon = new TokenCategoryDefinition[]{
                skipToken, trueToken, falseToken, whileToken, doToken, ifToken, thenToken, elseToken,
                identifierToken, plusToken, minusToken, asteriskToken, assignmentOperatorToken,
                equalsOperatorToken, greaterOperatorToken, lessOperatorToken, semicolonToken,
                openBracketToken, closedBracketToken, numeralToken,
                blankToken
        };

        //GRAMMAR ---------------------------------------------------------------
        SyntaxClass exp = new SyntaxClass("Exp");
        SyntaxClass comm = new SyntaxClass("Comm");
        SyntaxClass bool = new SyntaxClass("Bool");

        //Exp :=
        // x |
        SyntaxCaseDefinition variable = new SyntaxCaseDefinition(exp, "variable",
                new SimpleWrapConverterMethod(),
                identifierToken);
        // n |
        SyntaxCaseDefinition numeral = new SyntaxCaseDefinition(exp, "numeral",
                new SimpleWrapConverterMethod(),
                numeralToken);
        // ( E ) |
        SyntaxCaseDefinition expressionBetweenRoundBrackets = new SyntaxCaseDefinition(exp, "expressionBetweenRoundBrackets",
                (node, s) -> new ExpressionBetweenRoundBrackets(s.convert(node.get(1))),
                openBracketToken, exp, closedBracketToken);
        // E + E |
        SyntaxCaseDefinition sum =new SyntaxCaseDefinition(exp, "sum",
                new CBOConverterMethod<IntegerExpression>(Sum::new),
                exp, plusToken, exp);
        // E - E |
        SyntaxCaseDefinition subtraction = new SyntaxCaseDefinition(exp, "subtraction",
                new CBOConverterMethod<IntegerExpression>(Subtraction::new),
                exp, minusToken, exp);
        // E * E
        SyntaxCaseDefinition multiplication = new SyntaxCaseDefinition(exp, "multiplication",
                new CBOConverterMethod<IntegerExpression>(Multiplication::new),
                exp, asteriskToken, exp);

        //Comm :=
        // skip |
        SyntaxCaseDefinition skip = new SyntaxCaseDefinition(comm, "skip",
                new SimpleWrapConverterMethod(),
                skipToken);
        // ( C ) |
        SyntaxCaseDefinition commandBetweenRoundBrackets = new SyntaxCaseDefinition(comm, "commandBetweenRoundBrackets",
                (node, s) -> new CommandBetweenRoundBrackets(s.convert(node.get(1))),
                openBracketToken, comm, closedBracketToken);
        // x := E |
        SyntaxCaseDefinition assignment = new SyntaxCaseDefinition(comm, "assignment",
                new UBOConverterMethod<Command, Variable, IntegerExpression>((var, expr)-> new Assignment(storeName, var, expr)),
                new SpecificCaseComponent(exp, variable), assignmentOperatorToken, exp);
        //  C ; C |
        SyntaxCaseDefinition sequentialComposition = new SyntaxCaseDefinition(comm, "sequentialComposition",
                new CBOConverterMethod<Command>(SequentialComposition::new),
                comm, semicolonToken, comm);
        // if B then C1 else C2 |
        SyntaxCaseDefinition ifThenElseStatement = new SyntaxCaseDefinition(comm, "ifThenElseStatement",
                (node, s) -> new IfThenElseCommand(s.convert(node.get(1)), s.convert(node.get(3)), s.convert(node.get(5))),
                ifToken, bool, thenToken, comm, elseToken, comm);
        // while B do C
        SyntaxCaseDefinition whileStatement = new SyntaxCaseDefinition(comm, "whileStatement",
                (node, s) -> new WhileCommand(s.convert(node.get(1)), s.convert(node.get(3))),
                whileToken, bool, doToken, comm);

        //Bool :=
        // true |
        SyntaxCaseDefinition trueVal = new SyntaxCaseDefinition(bool, "trueVal",
                new SimpleWrapConverterMethod(),
                trueToken);
        // false |
        SyntaxCaseDefinition falseVal = new SyntaxCaseDefinition(bool, "falseVal",
                new SimpleWrapConverterMethod(),
                falseToken);
        // ( B ) |
        SyntaxCaseDefinition booleanExpressionBetweenRoundBrackets = new SyntaxCaseDefinition(bool, "booleanExpressionBetweenRoundBrackets",
                (node, s)-> new BooleanExpressionBetweenRoundBrackets(s.convert(node.get(1))),
                openBracketToken, bool, closedBracketToken);
        // E = E |
        SyntaxCaseDefinition equalIntegerComparison = new SyntaxCaseDefinition(bool, "equalIntegerComparison",
                new UBOConverterMethod<BooleanExpression, IntegerExpression, IntegerExpression>(EqualIntegerComparison::new),
                exp, equalsOperatorToken, exp);
        // E > E |
        SyntaxCaseDefinition greaterIntegerComparison = new SyntaxCaseDefinition(bool, "greaterIntegerComparison",
                new UBOConverterMethod<BooleanExpression, IntegerExpression, IntegerExpression>(GreaterIntegerComparison::new),
                exp, greaterOperatorToken, exp);
        // E < E |
        SyntaxCaseDefinition lessIntegerComparison = new SyntaxCaseDefinition(bool, "lessIntegerComparison",
                new UBOConverterMethod<BooleanExpression, IntegerExpression, IntegerExpression>(LessIntegerComparison::new),
                exp, lessOperatorToken, exp);

        SyntaxCaseDefinition[] grammar = new SyntaxCaseDefinition[]{
                variable, numeral, trueVal, falseVal, skip,
                expressionBetweenRoundBrackets, booleanExpressionBetweenRoundBrackets, commandBetweenRoundBrackets,
                multiplication, sum, subtraction, equalIntegerComparison, greaterIntegerComparison, lessIntegerComparison,
                assignment, ifThenElseStatement, whileStatement, sequentialComposition
        };

        ProgramGenerator programGenerator = new ProgramGenerator(lexicon, grammar);

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
