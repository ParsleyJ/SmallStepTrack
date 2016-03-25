package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.parser.tokenizer.RejectableTokenClass;
import com.parsleyj.smallsteptrack.parser.tokenizer.Token;
import com.parsleyj.smallsteptrack.parser.tokenizer.TokenClass;
import com.parsleyj.smallsteptrack.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser definition class (EXPERIMENTING)
 */
public class Parser {

    private Grammar grammar;

    /* 9 + 3 - ( 4 * 7 - ( 12 + 2 ) ) * 2 */

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    private List<SyntaxTreeNode> generateListFromToken(List<Token> tokens, SyntaxTreeNodeFactory stf){
        List<SyntaxTreeNode> treeList = new ArrayList<>();
        for (Token t: tokens) {
            TokenClass tokenClass = grammar.getTokenClass(t);
            if(tokenClass == null) throw new InvalidTokenFoundException(); //todo: add token info to exception
            SyntaxTreeNode ast = stf.newSyntaxTree();
            ast.setParsedToken(t);
            ast.setTokenClass(tokenClass);
            ast.setTerminal(true);
            treeList.add(ast);
        }
        return treeList;
    }

    //iterative attempt
    @SuppressWarnings("Duplicates")
    public SyntaxTreeNode parse(List<Token> tokens){
        SyntaxTreeNodeFactory stf = new SyntaxTreeNodeFactory();

        List<SyntaxTreeNode> treeList = generateListFromToken(tokens, stf);

        List<Integer> windows = grammar.getCaseSizes();
        while(true){
            boolean lastIterationFailed = true;
            if(treeList.size() <= 1) break;
            for(Integer window: windows){
                List<SyntaxTreeNode> tempList = new ArrayList<>();
                int start;
                for(start = 0; start <= treeList.size() - window; ++start){
                    int end = start + window;
                    List<SyntaxTreeNode> currentSubList = treeList.subList(start, end);
                    Pair<SyntaxClass, SyntaxCase> lookupResult = grammar.lookup(
                            currentSubList.stream()
                                    .map(a -> a.isTerminal()?a.getTokenClass():new SyntaxParsingInstance(a.getSyntaxClass(), a.getSyntaxCase()))
                                    .collect(Collectors.toList())
                    );
                    if (lookupResult != null) {
                        lastIterationFailed = false;
                        SyntaxTreeNode nst = stf.newSyntaxTree(currentSubList.toArray(new SyntaxTreeNode[currentSubList.size()]));
                        nst.setSyntaxClass(lookupResult.getFirst());
                        nst.setSyntaxCase(lookupResult.getSecond());
                        nst.setTerminal(false);
                        tempList.add(nst);
                        start += window - 1;

                    } else {
                        tempList.add(currentSubList.get(0));
                    }
                }

                //there are no more subLists with this window at this start index
                //we need to add the remaining elements to tempList
                for(int i = start; i < treeList.size(); ++i)
                    tempList.add(treeList.get(i));

                treeList = tempList;
            }
            if(lastIterationFailed) break;
        }
        if(treeList.size() == 1){
            return treeList.get(0);
        } else {
            SyntaxTreeNode errorNode = stf.newSyntaxTree(treeList.toArray(new SyntaxTreeNode[treeList.size()]));
            errorNode.setSyntaxCase(new SyntaxCase("PARSE FAILED"));
            errorNode.setSyntaxClass(new SyntaxClass("###"));
            throw new ParseFailedException(errorNode);
        }
    }


    //a more efficient version of parse.
    @SuppressWarnings("Duplicates")
    public SyntaxTreeNode priorityBasedParse(List<Token> tokens){
        List<Pair<SyntaxClass, SyntaxCase>> caseList = grammar.getPriorityCaseList();
        SyntaxTreeNodeFactory stf = new SyntaxTreeNodeFactory();

        List<SyntaxTreeNode> treeList = generateListFromToken(tokens, stf);


        while(true){
            boolean lastIterationFailed = true;
            if(treeList.size() <= 1) break;
            for(Pair<SyntaxClass, SyntaxCase> currentCase: caseList){
                List<SyntaxTreeNode> tempList = new ArrayList<>();
                int window = currentCase.getSecond().getStructure().size();
                int start;
                for(start = 0; start <= treeList.size() - window; ++start){
                    int end = start + window;
                    List<SyntaxTreeNode> currentSubList = treeList.subList(start, end);

                    SyntaxCase instanceCase = new SyntaxCase("", currentSubList.stream()
                            .map(a -> a.isTerminal()?a.getTokenClass():new SyntaxParsingInstance(a.getSyntaxClass(), a.getSyntaxCase()))
                            .collect(Collectors.toList()).toArray(new SyntaxCaseComponent[currentSubList.size()]));
                    boolean caseMatch = Grammar.caseMatch(instanceCase, currentCase.getSecond());
                    if (caseMatch) {
                        lastIterationFailed = false;
                        SyntaxTreeNode nst = stf.newSyntaxTree(currentSubList.toArray(new SyntaxTreeNode[currentSubList.size()]));
                        nst.setSyntaxClass(currentCase.getFirst());
                        nst.setSyntaxCase(currentCase.getSecond());
                        nst.setTerminal(false);
                        tempList.add(nst);
                        start += window - 1;
                    } else {
                        tempList.add(currentSubList.get(0));
                    }
                }

                //there are no more subLists with this window at this start index
                //we need to add the remaining elements to tempList
                for(int i = start; i < treeList.size(); ++i)
                    tempList.add(treeList.get(i));

                treeList = tempList;
            }
            if(lastIterationFailed) break;
        }
        if(treeList.size() == 1){
            return treeList.get(0);
        } else {
            SyntaxTreeNode errorNode = stf.newSyntaxTree(treeList.toArray(new SyntaxTreeNode[treeList.size()]));
            errorNode.setSyntaxCase(new SyntaxCase("PARSE FAILED"));
            errorNode.setSyntaxClass(new SyntaxClass("###"));
            throw new ParseFailedException(errorNode);
        }
    }


}
