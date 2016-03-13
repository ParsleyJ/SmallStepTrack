package com.parsleyj.smallsteptrack;

import com.parsleyj.smallsteptrack.booleanexpr.GreaterIntegerComparison;
import com.parsleyj.smallsteptrack.command.Assignment;
import com.parsleyj.smallsteptrack.command.SequentialComposition;
import com.parsleyj.smallsteptrack.command.WhileCommand;
import com.parsleyj.smallsteptrack.configuration.Configuration;
import com.parsleyj.smallsteptrack.configuration.ConfigurationElement;
import com.parsleyj.smallsteptrack.configuration.IntegerStore;
import com.parsleyj.smallsteptrack.integerexpr.Multiplication;
import com.parsleyj.smallsteptrack.integerexpr.Numeral;
import com.parsleyj.smallsteptrack.integerexpr.Subtraction;
import com.parsleyj.smallsteptrack.integerexpr.Variable;

public class Main {

    public static void main(String[] args) {

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
        );

        // creates a new store
        IntegerStore store = new IntegerStore(sName);
        // writes 3 to x in the store
        store.write("x", 3);

        //starts the execution
        executeProgram(program, store);
    }

    /**
     * Prints a separator between steps.
     */
    public static void printStepSeparator(){
        System.out.println("------------------------------");
    }

    /**
     * Prints the state of the execution.
     */
    public static void printState(Program program, Configuration configuration){
        System.out.println("P  =  " + program);
        configuration.printState();
    }

    /**
     * Prints a separator followed by the state of program and configuration.
     */
    public static void printSepAndState(Program program, Configuration configuration){
        printStepSeparator();
        printState(program, configuration);
    }

    /**
     * Executes the program and prints the track of execution and the configuration state at each step.
     * @param program the program
     * @param configurationElements the configuration elements
     */
    public static void executeProgram(Program program, ConfigurationElement... configurationElements){
        Configuration configuration = new Configuration(configurationElements);
        printState(program, configuration);
        while(!program.step(configuration)){
            printSepAndState(program, configuration);
        }

    }


}
