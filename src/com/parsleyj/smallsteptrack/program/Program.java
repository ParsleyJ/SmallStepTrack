package com.parsleyj.smallsteptrack.program;

import com.parsleyj.smallsteptrack.SmallStepSemanticObject;
import com.parsleyj.smallsteptrack.configuration.Configuration;
import com.parsleyj.smallsteptrack.configuration.ConfigurationElement;


/**
 * This class represents a Program written in While language.
 */
public abstract class Program {
    private String programName;

    private SmallStepSemanticObject rootSemObject;

    /**
     * Creates a new Program class, wrapped around the specified "root" Command.
     * @param name the program's name
     * @param rootSemObject the root semantic object of this program
     */
    public Program(String name, SmallStepSemanticObject rootSemObject) {
        programName = name;
        this.rootSemObject = rootSemObject;
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
     * @param configurationElements the configuration elements
     */
    public void executeProgram(ConfigurationElement... configurationElements){
        Configuration configuration = new Configuration(configurationElements);
        printState(this, configuration);
        while(!this.step(configuration)){
            printSepAndState(this, configuration);
        }

    }

    /**
     * Makes a computational step.
     *
     * @param configuration the configuration
     * @return true if the program reached a "ended" state; false otherwise.
     */
    public abstract boolean step(Configuration configuration);

    public String getProgramName() {
        return programName;
    }

    public SmallStepSemanticObject getRootSemanticObject() {
        return rootSemObject;
    }

    public void setRootSemanticObject(SmallStepSemanticObject rootSemObject) {
        this.rootSemObject = rootSemObject;
    }

    /**
     * Returns the string representation of the Program object.
     * This is done by returning the string representation of the
     * Command object initially specified via {@link #Program(String,SmallStepSemanticObject)}
     * @return the string representation of the Program object.
     */
    @Override
    public String toString() {
        return "" + rootSemObject.toString();
    }
}
