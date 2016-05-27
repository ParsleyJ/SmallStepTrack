package com.parsleyj.toolparser.program;

import com.parsleyj.toolparser.configuration.Configuration;
import com.parsleyj.toolparser.configuration.ConfigurationElement;
import com.parsleyj.toolparser.semanticsconverter.SemanticObject;


/**
 * This class represents a Program written in While language.
 */
public abstract class Program {
    public static boolean VERBOSE = true;

    private String programName;

    private SemanticObject rootSemObject;

    /**
     * Creates a new Program class, wrapped around the specified "root" Command.
     * @param name the program's name
     * @param rootSemObject the root semantic object of this program
     */
    public Program(String name, SemanticObject rootSemObject) {
        programName = name;
        this.rootSemObject = rootSemObject;
    }

    /**
     * Prints a separator between steps.
     */
    public static void printStepSeparator(){
        if(VERBOSE)System.out.println("------------------------------");
    }

    /**
     * Prints the state of the execution.
     */
    public static void printState(Program program, Configuration configuration){
        if(VERBOSE)System.out.println("P  =  " + program);
        if(VERBOSE)configuration.printState();
    }

    /**
     * Prints a separator followed by the state of program and configuration.
     */
    public static void printSepAndState(Program program, Configuration configuration){
        printStepSeparator();
        printState(program, configuration);
    }

    /**
     * Executes the program and prints the track of execution and the configuration state at each execute.
     * @param configurationElements the configuration elements
     */
    public void executeProgram(ConfigurationElement... configurationElements){
        Configuration configuration = new Configuration(configurationElements);
        printState(this, configuration);

        while(!this.execute(configuration)){
            printSepAndState(this, configuration);
        }

        printState(this, configuration);

    }

    /**
     * Makes a computational execute.
     *
     * @param configuration the configuration
     * @return true if the program reached a "ended" state; false otherwise.
     */
    public abstract boolean execute(Configuration configuration);

    public String getProgramName() {
        return programName;
    }

    public SemanticObject getRootSemanticObject() {
        return rootSemObject;
    }

    public void setRootSemanticObject(SemanticObject rootSemObject) {
        this.rootSemObject = rootSemObject;
    }

    /**
     * Returns the string representation of the Program object.
     * This is done by returning the string representation of the
     * Command object initially specified via {@link #Program(String, SemanticObject)}
     * @return the string representation of the Program object.
     */
    @Override
    public String toString() {
        return "" + rootSemObject.toString();
    }
}
