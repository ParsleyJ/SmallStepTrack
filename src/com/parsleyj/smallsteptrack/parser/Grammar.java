package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;
import com.parsleyj.smallsteptrack.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Giuseppe on 19/03/16.
 * TODO: javadoc
 */
public class Grammar {
    private List<SyntaxClass> list;

    public Grammar(SyntaxClass... list) {
        this.list = Arrays.asList(list);
    }



    /**
     * Finds all cases in all classes in which {@code component} is contained.
     * @param component
     * @return
     */
    public List<Pair<SyntaxClass, SyntaxCase>> findCases(SyntaxCaseComponent component){
        List<Pair<SyntaxClass, SyntaxCase>> result = new ArrayList<>();
        for (SyntaxClass clas : list) {
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

    public Pair<SyntaxClass, SyntaxCase> lookup(List<SyntaxCaseComponent> components){
        return lookup(components, list);
    }

    public Pair<SyntaxClass, SyntaxCase> lookup(List<SyntaxCaseComponent> components, List<SyntaxClass> list) {
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

    public Boolean isPriorityGreaterOrEqual(List<SyntaxCaseComponent> a, List<SyntaxCaseComponent> b){
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
    }

    public static boolean caseMatch(SyntaxCase sc1, SyntaxCase sc2) {
        if(sc1.getStructure().size() != sc2.getStructure().size()) return false;
        for(int i = 0; i < sc1.getStructure().size(); ++i){
            if(!sc1.getStructure().get(i).getSyntaxComponentName().equals(sc2.getStructure().get(i).getSyntaxComponentName()))
                return false;
        }
        return true;
    }

    public TokenClass getTokenClass(Token t){
        for(SyntaxClass syntaxClass: list) {
            for(SyntaxCase syntaxCase: syntaxClass.getSyntaxCases()) {
                for(SyntaxCaseComponent syntaxCaseComponent: syntaxCase.getStructure()) {
                    if (syntaxCaseComponent instanceof TokenClass && syntaxCaseComponent.getSyntaxComponentName().equals(t.getTokenClassName())) {
                        return (TokenClass) syntaxCaseComponent;
                    }
                }
            }
        }
        return null;
    }

    public List<Integer> getCaseSizes() {
        HashSet<Integer> hs= new HashSet<>();
        for(SyntaxClass clas: list)
            //noinspection Convert2streamapi
            for(SyntaxCase cas: clas.getSyntaxCases())
                if(!cas.getStructure().isEmpty()) hs.add(cas.getStructure().size());
        List<Integer> sortedList = new ArrayList<>(hs);
        Collections.sort(sortedList, (o1, o2) -> o1-o2);
        return sortedList;
    }
}
