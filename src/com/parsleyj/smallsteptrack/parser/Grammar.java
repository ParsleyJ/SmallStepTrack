package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenCategory;
import com.parsleyj.smallsteptrack.program.SyntaxCaseDefinition;
import com.parsleyj.smallsteptrack.utils.Pair;

import java.util.*;

/**
 * A {@link Grammar} object carries two important infos: the list
 * of the {@link SyntaxClass}es and the ordered list used to get
 * determine a precedence rule between all the {@link SyntaxCase}
 * of the grammar.
 */
public class Grammar {
    private List<SyntaxClass> classList;
    private List<Pair<SyntaxClass,SyntaxCase>> priorityCaseList;

    /**
     * @param syntaxCaseDefinitions a list of {@link SyntaxCaseDefinition} used to generate
     *                              the Grammar. The order is important: the first syntax cases will be
     *                              searched first by the parser, so it can be used to set precedence rules.
     */
    public Grammar(SyntaxCaseDefinition... syntaxCaseDefinitions) {
        HashSet<SyntaxClass> classHashSet = new HashSet<>();
        this.priorityCaseList = new ArrayList<>();
        for(SyntaxCaseDefinition definition: syntaxCaseDefinitions){
            definition.getBelongingClass().addCase(definition);
            classHashSet.add(definition.getBelongingClass());
            this.priorityCaseList.add(new Pair<>(definition.getBelongingClass(),definition));
        }
        this.classList = new ArrayList<>(classHashSet);
    }

    /**
     * @return a list of pairs of {@link SyntaxClass}, and {@link SyntaxCase},
     *          in the order given with the constructor.
     */
    public List<Pair<SyntaxClass, SyntaxCase>> getPriorityCaseList() {
        return priorityCaseList;
    }



    /**
     * Finds all syntax cases in all classes in which {@code component} is contained.
     * @param component the component
     * @return  list of pairs of {@link SyntaxClass}, and {@link SyntaxCase}, in which
     *          the component is contained.
     */
    public List<Pair<SyntaxClass, SyntaxCase>> findCases(SyntaxCaseComponent component){
        List<Pair<SyntaxClass, SyntaxCase>> result = new ArrayList<>();
        for (SyntaxClass clas : classList) {
            for (SyntaxCase cas: clas.getSyntaxCases()){
                for(SyntaxCaseComponent comp: cas.getStructure()){
                    if(comp.getSyntaxComponentName().equals(component.getSyntaxComponentName())) {
                        result.add(new Pair<>(clas, cas));
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Searches a {@link SyntaxCase} that has exactly the same components and returns
     * it in a pair with the corresponding {@link SyntaxClass}.
     * @param components the list of case components that should match the SyntaxCase
     * @return a pair of {@link SyntaxClass} and {@link SyntaxCase} of the found case.
     */
    public Pair<SyntaxClass, SyntaxCase> lookup(List<SyntaxCaseComponent> components){
        return lookup(components, classList);
    }

    public static Pair<SyntaxClass, SyntaxCase> lookup(List<SyntaxCaseComponent> components, List<SyntaxClass> list) {
        SyntaxCase tempCase = new SyntaxCase("", components.toArray(new SyntaxCaseComponent[components.size()]));
        for (SyntaxClass clas : list) {
            for(SyntaxCase cas : clas.getSyntaxCases()){
                if(caseMatch(tempCase, cas)){
                    return new Pair<>(clas, cas);
                }
            }
        }
        return null;
    }

    /*public Boolean isPriorityGreaterOrEqual(List<SyntaxCaseComponent> a, List<SyntaxCaseComponent> b){
        SyntaxCase tempCaseA = new SyntaxCase("", a.toArray(new SyntaxCaseComponent[a.size()]));
        SyntaxCase tempCaseB = new SyntaxCase("", b.toArray(new SyntaxCaseComponent[b.size()]));
        for (SyntaxClass clas : list) {
            for(SyntaxCase cas : clas.getSyntaxCases()){
                if(caseMatch(tempCaseA, cas)){
                    return true;
                } else if (caseMatch(tempCaseB, cas)){
                    return false;
                }
            }
        }
        return null;
    }*/

    /**
     * Used to confront two {@link SyntaxCase}s and to find out if they have the same structure.
     * @return true if the two {@link SyntaxCase}s have same structure, false otherwise.
     */
    public static boolean caseMatch(SyntaxCase instanceCase, SyntaxCase ruledCase) {
        if(ruledCase.getStructure().size() != instanceCase.getStructure().size()) return false;
        for(int i = 0; i < ruledCase.getStructure().size(); ++i){
            SyntaxCaseComponent ruledComponent = ruledCase.getStructure().get(i);
            SyntaxCaseComponent instanceComponent = instanceCase.getStructure().get(i);

            if(ruledComponent instanceof  SpecificCaseComponent && !(instanceComponent instanceof TokenCategory)){
                if (!ruledComponent.getSyntaxComponentName().equals(instanceComponent.getSyntaxComponentName() + ":" + ((SyntaxParsingInstance) instanceComponent).getSyntaxCaseName()))
                    return false;
            }else if(!ruledComponent.getSyntaxComponentName().equals(instanceComponent.getSyntaxComponentName())) {
                return false;
            }
        }
        return true;
    }


    /**
     * Searches in the {@link SyntaxCaseComponent}s of each syntax case, for a {@link TokenCategory} corresponding to the given token.
     * @param t the Token
     * @return the {@link TokenCategory} if found, {@code null} otherwise.
     */
    public TokenCategory getTokenClass(Token t){
        for(SyntaxClass syntaxClass: classList) {
            for(SyntaxCase syntaxCase: syntaxClass.getSyntaxCases()) {
                for(SyntaxCaseComponent syntaxCaseComponent: syntaxCase.getStructure()) {
                    if (syntaxCaseComponent instanceof TokenCategory && syntaxCaseComponent.getSyntaxComponentName().equals(t.getTokenClassName())) {
                        return (TokenCategory) syntaxCaseComponent;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Gets a list of all the sizes of the {@link SyntaxCase}s,
     * appearing only one time in the list.
     */
    public List<Integer> getCaseSizes() {
        HashSet<Integer> hs= new HashSet<>();
        for(SyntaxClass clas: classList)
            //noinspection Convert2streamapi
            for(SyntaxCase cas: clas.getSyntaxCases())
                if(!cas.getStructure().isEmpty()) hs.add(cas.getStructure().size());
        List<Integer> sortedList = new ArrayList<>(hs);
        Collections.sort(sortedList, (o1, o2) -> o1-o2);
        return sortedList;
    }


}
