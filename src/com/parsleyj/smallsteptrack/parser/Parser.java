package com.parsleyj.smallsteptrack.parser;

import com.parsleyj.smallsteptrack.tokenizer.Token;
import com.parsleyj.smallsteptrack.tokenizer.TokenCategory;
import com.parsleyj.smallsteptrack.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link Parser} class. A parser object implements the mechanics to parse
 * a sequence of {@link Token}s in order to create a parse tree, using
 * the grammar rules defined in the {@link Grammar} instance assigned
 * to that parser with the constructor.
 */
public class Parser {

    private Grammar grammar;

    /**
     * Creates a new {@link Parser} with the specified grammar rules.
     * @param grammar
     */
    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    private List<ParseTreeNode> generateListFromToken(List<Token> tokens, ParseTreeNodeFactory stf){
        List<ParseTreeNode> treeList = new ArrayList<>();
        for (Token t: tokens) {
            TokenCategory tokenCategory = grammar.getTokenClass(t);
            if(tokenCategory == null) throw new InvalidTokenFoundException(); //todo: add token info to exception
            ParseTreeNode ast = stf.newNodeTree();
            ast.setParsedToken(t);
            ast.setTokenCategory(tokenCategory);
            ast.setTerminal(true);
            treeList.add(ast);
        }
        return treeList;
    }


    @SuppressWarnings("Duplicates")
    public ParseTreeNode parse(List<Token> tokens){
        ParseTreeNodeFactory stf = new ParseTreeNodeFactory();

        List<ParseTreeNode> treeList = generateListFromToken(tokens, stf);

        List<Integer> windows = grammar.getCaseSizes();
        while(true){
            boolean lastIterationFailed = true;
            if(treeList.size() <= 1) break;
            for(Integer window: windows){
                List<ParseTreeNode> tempList = new ArrayList<>();
                int start;
                for(start = 0; start <= treeList.size() - window; ++start){
                    int end = start + window;
                    List<ParseTreeNode> currentSubList = treeList.subList(start, end);
                    Pair<SyntaxClass, SyntaxCase> lookupResult = grammar.lookup(
                            currentSubList.stream()
                                    .map(a -> a.isTerminal()?a.getTokenCategory():new SyntaxParsingInstance(a.getSyntaxClass(), a.getSyntaxCase()))
                                    .collect(Collectors.toList())
                    );
                    if (lookupResult != null) {
                        lastIterationFailed = false;
                        ParseTreeNode nst = stf.newNodeTree(currentSubList.toArray(new ParseTreeNode[currentSubList.size()]));
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
            ParseTreeNode errorNode = stf.newNodeTree(treeList.toArray(new ParseTreeNode[treeList.size()]));
            errorNode.setSyntaxCase(new SyntaxCase("PARSE FAILED"));
            errorNode.setSyntaxClass(new SyntaxClass("###"));
            throw new ParseFailedException(errorNode);
        }
    }


    /**
     * Iterative parsing algorithm. At each iteration, it tries to find a sub-sequence
     * of {@link ParseTreeNode}s that matches a {@link SyntaxCase}, using the
     * {@link Grammar#caseMatch(SyntaxCase, SyntaxCase)} method. This algorithm tries
     * to find the {@link SyntaxCase}s in the same order defined in the {@link Grammar},
     * in this way it can respect the defined precedence rules. When, in a iteration,
     * the parser finds a matching candidate sub-sequence, creates a new
     * {@link ParseTreeNode} with the members of the sub-sequence as children and inserts
     * it in the original sequence instead of that subsequence. If the parsing operation
     * is successful, the result will be a {@link ParseTreeNode} containing the whole parse
     * tree. The algorithm fails throwing a {@link ParseFailedException} when during an
     * iteration all the attempts to find a successful match fail and there are more than
     * one {@link ParseTreeNode}s in the sequence.
     * @param tokens the list of tokens
     * @return a {@link ParseTreeNode} which is the root of the parse tree.
     */
    @SuppressWarnings("Duplicates")
    public ParseTreeNode priorityBasedParse(List<Token> tokens){
        List<Pair<SyntaxClass, SyntaxCase>> caseList = grammar.getPriorityCaseList();
        ParseTreeNodeFactory stf = new ParseTreeNodeFactory();

        List<ParseTreeNode> treeList = generateListFromToken(tokens, stf);


        while(true){
            boolean lastIterationFailed = true;
            if(treeList.size() <= 1) break;
            for(Pair<SyntaxClass, SyntaxCase> currentCase: caseList){
                List<ParseTreeNode> tempList = new ArrayList<>();
                int window = currentCase.getSecond().getStructure().size();
                int start;
                for(start = 0; start <= treeList.size() - window; ++start){
                    int end = start + window;
                    List<ParseTreeNode> currentSubList = treeList.subList(start, end);

                    SyntaxCase instanceCase = new SyntaxCase("", currentSubList.stream()
                            .map(a -> a.isTerminal()?a.getTokenCategory():new SyntaxParsingInstance(a.getSyntaxClass(), a.getSyntaxCase()))
                            .collect(Collectors.toList()).toArray(new SyntaxCaseComponent[currentSubList.size()]));
                    boolean caseMatch = Grammar.caseMatch(instanceCase, currentCase.getSecond());
                    if (caseMatch) {
                        lastIterationFailed = false;
                        ParseTreeNode nst = stf.newNodeTree(currentSubList.toArray(new ParseTreeNode[currentSubList.size()]));
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
            ParseTreeNode errorNode = stf.newNodeTree(treeList.toArray(new ParseTreeNode[treeList.size()]));
            errorNode.setSyntaxCase(new SyntaxCase("PARSE FAILED"));
            errorNode.setSyntaxClass(new SyntaxClass("###"));
            throw new ParseFailedException(errorNode);
        }
    }


}
