package com.parsleyj.toolparser.parser;

import com.parsleyj.utils.Lol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a specific syntax class. This class implements
 * {@link SyntaxCaseComponent}, so its instances can be used
 * as components to defineParameter syntax cases.
 */
public class SyntaxClass implements SyntaxCaseComponent {

    private String name;
    private List<SyntaxClass> extendedClasses = null;
    private List<SyntaxCase> cases;

    /**
     * Creates a new syntax class with the given name.
     * @param name the name of this class.
     */
    public SyntaxClass(String name) {
        this.name = name;
        this.extendedClasses = Collections.emptyList();
        cases = new ArrayList<>();
    }


    //TODO: doc
    public SyntaxClass(String className, SyntaxClass... extendedClasses) {
        this.name = className;
        this.extendedClasses = Arrays.asList(extendedClasses);
        this.cases = new ArrayList<>();
    }



    public List<SyntaxClass> getExtendedClasses(){
        return extendedClasses;
    }


    /**
     * @return the cases that interpret instances of this syntax class.
     */
    public List<SyntaxCase> getSyntaxCases() {
        return cases;
    }

    /**
     * @param cases the cases that interpret instances of this syntax
     *              class.
     */
    public void setCases(SyntaxCase... cases) {
        this.cases = Arrays.asList(cases);
    }

    /**
     * Adds a case to the list of cases of this syntax class.
     * @param cas the cas to be added
     */
    public void addCase(SyntaxCase cas){
        if (this.cases == null) {
            cases = new ArrayList<>();
        }
        cases.add(cas);
    }

    @Override
    public String getSyntaxComponentName() {
        return name;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    /*public boolean isOrExtends(SyntaxClass c){
        Lol.vl("<"+this.getSyntaxComponentName()+">.isOrExtends("+c.getSyntaxComponentName()+") ? ");
        SyntaxClass x = this;
        while(x != null){
            if(x.getSyntaxComponentName().equals(c.getSyntaxComponentName())) {
                Lol.v("Yes.");
                return true;
            }
            x = x.getExtendedClasses();
        }
        Lol.v("No.");
        return false;
    }*/

    public boolean isOrExtends(SyntaxClass c){
        if (this.matches(c)) return true;
        if(extendedClasses.isEmpty()) return false;
        for (SyntaxClass extendedClass : extendedClasses) {
            if(extendedClass.isOrExtends(c))
                return true;
        }
        return false;
    }
}
