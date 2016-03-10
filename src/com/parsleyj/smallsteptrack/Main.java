package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.booleanexpr.GreaterIntegerComparison;
import com.parsleyj.smallsteptrack.command.Assignment;
import com.parsleyj.smallsteptrack.command.Command;
import com.parsleyj.smallsteptrack.command.SequentialComposition;
import com.parsleyj.smallsteptrack.command.WhileCommand;
import com.parsleyj.smallsteptrack.integerexpr.Multiplication;
import com.parsleyj.smallsteptrack.integerexpr.Numeral;
import com.parsleyj.smallsteptrack.integerexpr.Subtraction;
import com.parsleyj.smallsteptrack.integerexpr.Variable;

public class Main {

    private static Program program = null;
    private static Store store = null;

    /**
     * Prints a separator between steps.
     */
    public static void printStepSeparator(){
        System.out.println("------------------------------");
    }

    /**
     * Prints the state of the execution. P is the program, S is the store.
     */
    public static void printState(){
        System.out.println("P  =  " + program);
        System.out.println("S  =  " + store);
    }

    /**
     * Prints a separator followed by the state.
     */
    public static void printSepAndState(){
        printStepSeparator();
        printState();
    }


    public static void main(String[] args) {

        /*
        y := x; a := 1;
        while y > 0 do
        a := a * y;
        y := y - 1
         */
        program = new Program(
                new SequentialComposition(
                        new SequentialComposition(
                                new Assignment(new Variable("y"), new Variable("x")),
                                new Assignment(new Variable("a"), new Numeral(1))
                        ),
                        new WhileCommand(new GreaterIntegerComparison(new Variable("y"), new Numeral(0)),
                                new SequentialComposition(
                                        new Assignment(new Variable("a"), new Multiplication(new Variable("a"), new Variable("y"))),
                                        new Assignment(new Variable("y"), new Subtraction(new Variable("y"), new Numeral(1)))
                                )
                        )
                )
        );

        // writes 3 to x in the store
        store = new Store();
        store.write("x", 3);

        printState();

        while(!program.step(store)){
            printSepAndState();
        }

    }


}
