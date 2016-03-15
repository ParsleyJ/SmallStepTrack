package com.parsleyj.smallsteptrack.syntax;

import java.util.*;

/**
 * Syntax definition class (EXPERIMENTING)
 */
public class Syntax {

    public Grammar getGrammar() {
        SyntaxClass exp = new SyntaxClass("Exp");
        SyntaxClass num = new SyntaxClass("Num");
        SyntaxClass comm = new SyntaxClass("Comm");
        SyntaxClass bool = new SyntaxClass("Bool");
        SyntaxConstant add = new SyntaxConstant("add", "+");
        SyntaxConstant sub = new SyntaxConstant("sub", "-");
        SyntaxConstant mul = new SyntaxConstant("mul", "*");
        SyntaxConstant openBracket = new SyntaxConstant("openBracket", "(");
        SyntaxConstant closedBracket = new SyntaxConstant("closedBracket", ")");

        exp.setComponents(
                new SyntaxComponent(num),
                new SyntaxComponent(openBracket, exp, closedBracket),
                new SyntaxComponent(exp, add, exp),
                new SyntaxComponent(exp, sub, exp),
                new SyntaxComponent(exp, mul, exp)
        );

        return new Grammar(exp, num, comm, bool);
    }

    public class Grammar {
        public Grammar(SyntaxClass... list) {

        }
    }

    public class SyntaxClass implements SyntaxEntity{

        private String name;
        private List<SyntaxComponent> components;

        public SyntaxClass(String name){
            this.name = name;
            components = null;
        }

        public SyntaxClass(String name, List<SyntaxComponent> components) {
            this.name = name;
            this.components = components;
        }

        public List<SyntaxComponent> getSyntaxComponents(){
            return components;
        }

        public void setComponents(SyntaxComponent... components) {
            this.components = Arrays.asList(components);
        }

        @Override
        public String getName() {
            return name;
        }

    }

    public class SyntaxTerminalClass extends SyntaxClass{

        public SyntaxTerminalClass(String name) {
            super(name);
        }


    }

    public class SyntaxConstant implements SyntaxEntity{

        private String name;
        private String generatingString;

        public SyntaxConstant(String name, String generatingString) {
            this.name = name;
            this.generatingString = generatingString;
        }

        @Override
        public String getName() {
            return name;
        }

        public String getGeneratingString() {
            return generatingString;
        }
    }

    public interface SyntaxEntity {
        //TODO add some more useful metadata
        String getName();
    }

    public class SyntaxComponent {
        private List<SyntaxEntity> structure;

        public SyntaxComponent(SyntaxEntity... structure) {
            this.structure = Arrays.asList(structure);
        }

        List<SyntaxEntity> getStructure(){
            return structure;
        }
    }
}
