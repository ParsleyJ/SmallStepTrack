package com.parsleyj.toolparser.parser;

import com.parsleyj.toolparser.tokenizer.Token;
import com.parsleyj.toolparser.tokenizer.TokenCategory;
import com.parsleyj.utils.Lol;
import com.parsleyj.utils.NoEnoughElementsException;
import com.parsleyj.utils.PJ;
import com.parsleyj.utils.Pair;
import com.parsleyj.utils.reversiblestream.DoubleSidedReversibleStream;
import com.parsleyj.utils.reversiblestream.ReversibleStream;
import com.parsleyj.utils.reversiblestream.StackedReversibleStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Giuseppe on 17/04/16.
 * TODO: javadoc
 */
public class RecursiveParser implements Parser {


    public static ParseTreeNodeFactory PARSE_TREE_NODE_FACTORY;

    private Grammar grammar;
    private SyntaxClass rootClass;


    public RecursiveParser(Grammar grammar, SyntaxClass rootClass) {
        this.grammar = grammar;
        this.rootClass = rootClass;
    }

    @Override
    public ParseTreeNode parse(List<Token> tokens) {
        PARSE_TREE_NODE_FACTORY = new ParseTreeNodeFactory();
        StackedReversibleStream<ParseTreeNode> ts = new StackedReversibleStream<>(
                promoteHighPriorityTerminals(
                        generateListFromToken(tokens)));
        //.setDebug(System.err);
        return recursiveDescentParse(ts, null, null, rootClass);
    }

    public class InvalidSyntaxCaseDefinitionException extends RuntimeException {
    }

    private ParseTreeNode recursiveDescentParse(
            DoubleSidedReversibleStream<ParseTreeNode> ts,
            TokenCategory leftDelimiter,
            TokenCategory rightDelimiter,
            SyntaxClass rootClass) {
        boolean permissiveMode = rootClass == null;
        if (leftDelimiter != null && rightDelimiter != null)
            throw newParseFailedException(ts.toList());

        ts.checkPoint();
        boolean foundSomething = true;
        boolean enteredWhileFirstTime = false;
        while (foundSomething && !ts.isEmpty() && delimitersCheck(ts, leftDelimiter, rightDelimiter)) {
            enteredWhileFirstTime = true;
            if (oneNodeInStream(ts, leftDelimiter, rightDelimiter)) {
                if ((leftDelimiter == null && rightDelimiter == null
                        && (permissiveMode || ts.peekLeft().getSyntaxClass().isOrExtends(rootClass))) ||
                        (leftDelimiter != null ? (
                                !ts.peekRight().isTerminal() && (permissiveMode || ts.peekRight().getSyntaxClass().isOrExtends(rootClass)))
                                : (!ts.peekLeft().isTerminal() && (permissiveMode || ts.peekLeft().getSyntaxClass().isOrExtends(rootClass))))) {
                    try {
                        ParseTreeNode ptn;
                        if (leftDelimiter != null) ptn = ts.popRight();
                        else ptn = ts.popLeft();
                        ts.commit();
                        return ptn;
                    } catch (NoEnoughElementsException e) {
                        ts.rollback();
                        throw newParseFailedException(Collections.emptyList());
                    }
                }
            }

            foundSomething = false;
            List<SyntaxCase> candidates = findCandidates(ts);
            Lol.v("Candidates found:"+candidates.size());
            for (SyntaxCase candidate : candidates) {
                Lol.v("\tCandidate: "+ candidate.getCaseName());
                if (candidate.getAssociativity() == Associativity.LeftToRight) {
                    foundSomething = leftToRightParse(ts, leftDelimiter, foundSomething, candidate);

                } else if (candidate.getAssociativity() == Associativity.RightToLeft) {
                    foundSomething = rightToLeftParse(ts, rightDelimiter, foundSomething, candidate);
                }
            }

        }
        if (foundSomething && enteredWhileFirstTime) {
            return recursiveDescentParse(ts, leftDelimiter, rightDelimiter, rootClass);
        } else {
            ts.rollback();
            List<ParseTreeNode> l = ts.toList();
            if (l != null) throw newParseFailedException(ts.toList());
            else throw newParseFailedException(new ArrayList<>());
        }
    }

    private boolean delimitersCheck(DoubleSidedReversibleStream<ParseTreeNode> ts, TokenCategory leftDelimiter, TokenCategory rightDelimiter) {
        return (rightDelimiter == null || !ts.peekLeft().isTerminal() || !ts.peekLeft().getTokenCategory().matches(rightDelimiter)) &&
                (leftDelimiter == null || !ts.peekRight().isTerminal() || !ts.peekRight().getTokenCategory().matches(leftDelimiter));
    }

    private boolean rightToLeftParse(DoubleSidedReversibleStream<ParseTreeNode> ts, TokenCategory rightDelimiter, boolean foundSomething, SyntaxCase candidate) {
        List<ParseTreeNode> tmpSequence = new ArrayList<>();
        boolean found = true;
        ts.checkPoint();
        ParseTreeNode overlappedNode = null;
        try {
            List<SyntaxCaseComponent> structure = candidate.getStructure();
            if (structure.size() == 1) {
                found = findUniqueComponent(ts, tmpSequence, structure.get(0));
            } else for (int i = 0; i < structure.size(); i++) {
                SyntaxCaseComponent component = structure.get(i);
                boolean firstComponent = i == 0;
                boolean lastComponent = i == structure.size() - 1;
                if (firstComponent) {
                    if (component.isTerminal()) {
                        ParseTreeNode terminalNode = ts.popLeft();
                        if (terminalNode.isTerminal() && terminalNode.getTokenCategory().matches(component)) {
                            tmpSequence.add(terminalNode);
                        } else {
                            found = false;
                            break;
                        }
                    } else {
                        SyntaxCaseComponent nextComponent = structure.get(i + 1);
                        if (nextComponent.isTerminal()) {
                            try {
                                DoubleSidedReversibleStream<ParseTreeNode> pns = getLeftSubStream(ts, nextComponent);
                                if (pns == null) {
                                    found = false;
                                    break;
                                }

                                ParseTreeNode node;
                                node = recursiveDescentParse(pns, null, null, (SyntaxClass) component);
                                tmpSequence.add(node);
                            } catch (ParseFailedException pfe) {
                                found = false;
                                break;
                            }
                        } else {
                            throw new InvalidSyntaxCaseDefinitionException();
                        }
                    }
                } else if (lastComponent) {
                    if (component.isTerminal()) {
                        ParseTreeNode terminalNode = ts.popLeft();
                        if (terminalNode.isTerminal() && terminalNode.getTokenCategory().matches(component)) {
                            tmpSequence.add(terminalNode);
                        } else {
                            found = false;
                            break;
                        }
                    } else {
                        try {
                            ParseTreeNode node = recursiveDescentParse(ts, null, rightDelimiter, null);
                            ParseTreeNode node2 = leftPriorityOverlap(candidate, tmpSequence, node);
                            if(node2 == null){
                                tmpSequence.add(node);
                            }else{
                                overlappedNode = node2;
                            }
                        } catch (ParseFailedException | OverlapFailedException e) {
                            found = false;
                            break;
                        }
                    }
                } else {
                    if (component.isTerminal()) {
                        ParseTreeNode terminalNode = ts.popLeft();
                        if (terminalNode.isTerminal() && terminalNode.getTokenCategory().matches(component)) {
                            tmpSequence.add(terminalNode);
                        } else {
                            found = false;
                            break;
                        }
                    } else {
                        SyntaxCaseComponent nextComponent = structure.get(i + 1);
                        SyntaxCaseComponent prevComponent = structure.get(i - 1);
                        if (nextComponent.isTerminal()) {
                            try {

                                ParseTreeNode node;
                                DoubleSidedReversibleStream<ParseTreeNode> subExpression = generateSubExpressionStream(ts, Associativity.RightToLeft, (TokenCategory) prevComponent, (TokenCategory) nextComponent);
                                node = recursiveDescentParse(subExpression, null, null, (SyntaxClass) component);
                                tmpSequence.add(node);
                            } catch (ParseFailedException pfe) {
                                found = false;
                                break;
                            }
                        } else {
                            throw new InvalidSyntaxCaseDefinitionException();
                        }
                    }
                }
            }
        } catch (NoEnoughElementsException e) {
            found = false;
        }
        if (found) {
            foundSomething = true;
            ts.commit();
            if(overlappedNode == null){
                ParseTreeNode ptn = newNonTerminalNode(candidate.getBelongingClass(), candidate, tmpSequence);
                ts.pushLeft(ptn);
            }else{
                ts.pushLeft(overlappedNode);
            }
        } else {
            ts.rollback();
        }
        return foundSomething;
    }

    public ParseTreeNode leftPriorityOverlap(SyntaxCase candidate, List<ParseTreeNode> previousNodes, ParseTreeNode justParsedNode) throws OverlapFailedException {
        if(candidate.getLastComponent().isTerminal()) throw new RuntimeException("leftPriorityOverlap() called with a candidate which has a terminal last component");
        ParseTreeNode tmp = justParsedNode;
        ParseTreeNode parent = null;
        while(!tmp.isTerminal()){
            if(tmp.getFirstChild().isTerminal() ||
                    tmp.getSyntaxCase().getCaseName().equals(candidate.getCaseName()) ||
                    tmp.getSyntaxCase().hasHigherPriority(candidate, grammar)) {
                if(tmp.getSyntaxClass().isOrExtends((SyntaxClass)candidate.getLastComponent())){
                    if(parent == null) return null;
                    List<ParseTreeNode> newChildren = new ArrayList<>();
                    ParseTreeNode n = newNonTerminalNode(candidate.getBelongingClass(), candidate, PJ.tempConcat(previousNodes, tmp));
                    newChildren.add(n);
                    newChildren.addAll(parent.getChildren().subList(1, parent.getChildren().size()));
                    parent.setChildren(newChildren);
                    return justParsedNode;
                }else{
                    throw new OverlapFailedException();
                }

            }else{
                parent = tmp;
                tmp = tmp.getFirstChild();
            }
        }
        throw new OverlapFailedException();
    }

    private boolean leftToRightParse(DoubleSidedReversibleStream<ParseTreeNode> ts, TokenCategory leftDelimiter, boolean foundSomething, SyntaxCase candidate) {
        List<ParseTreeNode> tmpSequence = new ArrayList<>();
        boolean found = true;
        ts.checkPoint();
        ParseTreeNode overlappedNode = null;
        try {
            List<SyntaxCaseComponent> structure = candidate.getStructure();
            if (structure.size() == 1)
                found = findUniqueComponent(ts, tmpSequence, structure.get(0));//TODO: check direction ambivalence
            else for (int i = structure.size() - 1; i >= 0; --i) {
                SyntaxCaseComponent component = structure.get(i);
                boolean firstComponent = i == structure.size() - 1;
                boolean lastComponent = i == 0;

                if (firstComponent) {
                    if (component.isTerminal()) {
                        ParseTreeNode terminalNode = ts.popRight();
                        if (terminalNode.isTerminal() && terminalNode.getTokenCategory().matches(component)) {
                            tmpSequence.add(0, terminalNode);
                        } else {
                            found = false;
                            break;
                        }
                    } else {
                        SyntaxCaseComponent nextComponent = structure.get(i - 1);
                        if (nextComponent.isTerminal()) {
                            try {
                                DoubleSidedReversibleStream<ParseTreeNode> pns = getRightSubStream(ts, nextComponent);
                                if (pns == null) {
                                    found = false;
                                    break;
                                }
                                ParseTreeNode node;
                                node = recursiveDescentParse(pns, null, null, (SyntaxClass) component);
                                tmpSequence.add(0, node);
                            } catch (ParseFailedException pfe) {
                                found = false;
                                break;
                            }
                        } else {
                            throw new InvalidSyntaxCaseDefinitionException();
                        }
                    }
                } else if (lastComponent) {
                    if (component.isTerminal()) {
                        ParseTreeNode terminalNode = ts.popRight();
                        if (terminalNode.isTerminal() && terminalNode.getTokenCategory().matches(component)) {
                            tmpSequence.add(0, terminalNode);
                        } else {
                            found = false;
                            break;
                        }
                    } else {
                        try {

                            ParseTreeNode node = recursiveDescentParse(ts, leftDelimiter, null, null);
                            ParseTreeNode node2 = rightPriorityOverlap(candidate, tmpSequence, node);
                            if(node2 == null) {
                                tmpSequence.add(0, node);
                            }else{
                                overlappedNode = node2;
                            }
                        } catch (ParseFailedException | OverlapFailedException e) {
                            found = false;
                            break;
                        }
                    }
                } else {
                    if (component.isTerminal()) {
                        ParseTreeNode terminalNode = ts.popRight();
                        if (terminalNode.isTerminal() && terminalNode.getTokenCategory().matches(component)) {
                            tmpSequence.add(0, terminalNode);
                        } else {
                            found = false;
                            break;
                        }
                    } else {
                        SyntaxCaseComponent nextComponent = structure.get(i - 1);
                        SyntaxCaseComponent prevComponent = structure.get(i + 1);
                        if (nextComponent.isTerminal()) {
                            try {

                                ParseTreeNode node;
                                DoubleSidedReversibleStream<ParseTreeNode> subExpression = generateSubExpressionStream(ts, Associativity.LeftToRight, (TokenCategory) prevComponent, (TokenCategory) nextComponent);
                                node = recursiveDescentParse(subExpression, null, null, (SyntaxClass) component);
                                tmpSequence.add(0, node);
                            } catch (ParseFailedException pfe) {
                                found = false;
                                break;
                            }
                        } else {
                            throw new InvalidSyntaxCaseDefinitionException();
                        }
                    }
                }
            }
        } catch (NoEnoughElementsException e) {
            found = false;
        }
        if (found) {
            foundSomething = true;
            ts.commit();

            if(overlappedNode == null) {
                ParseTreeNode ptn = newNonTerminalNode(candidate.getBelongingClass(), candidate, tmpSequence);
                ts.pushRight(ptn);
            }else{
                ts.pushRight(overlappedNode);
            }
        } else {
            ts.rollback();
        }
        return foundSomething;
    }

    public ParseTreeNode rightPriorityOverlap(SyntaxCase candidate, List<ParseTreeNode> previousNodes, ParseTreeNode justParsedNode) throws OverlapFailedException {
        if(candidate.getFirstComponent().isTerminal()) throw new RuntimeException("leftPriorityOverlap() called with a candidate which has a terminal last component");
        ParseTreeNode tmp = justParsedNode;
        ParseTreeNode parent = null;
        while(!tmp.isTerminal()){
            if(tmp.getLastChild().isTerminal() ||
                    tmp.getSyntaxCase().getCaseName().equals(candidate.getCaseName()) ||
                    tmp.getSyntaxCase().hasHigherPriority(candidate, grammar)) {
                if(tmp.getSyntaxClass().isOrExtends((SyntaxClass)candidate.getFirstComponent())){
                    if(parent == null) return null;
                    List<ParseTreeNode> newChildren = new ArrayList<>();
                    ParseTreeNode n = newNonTerminalNode(candidate.getBelongingClass(), candidate, PJ.tempConcat(PJ.list(tmp), previousNodes));
                    newChildren.addAll(parent.getChildren().subList(0, parent.getChildren().size()-1));
                    newChildren.add(n);
                    parent.setChildren(newChildren);
                    return justParsedNode;
                }else{
                    throw new OverlapFailedException();
                }

            }else{
                parent = tmp;
                tmp = tmp.getLastChild();
            }
        }
        throw new OverlapFailedException();
    }

    private List<ParseTreeNode> generateListFromToken(List<Token> tokens) {
        List<ParseTreeNode> treeList = new ArrayList<>();
        for (Token t : tokens) {
            TokenCategory tokenCategory = grammar.getTokenCategory(t);
            if (tokenCategory == null) throw new InvalidTokenFoundException(t);
            ParseTreeNode ast = newTerminalNode(t);
            treeList.add(ast);
        }
        return treeList;
    }

    @SuppressWarnings("Duplicates")
    private List<SyntaxCase> candidatesStartingWith(List<SyntaxCaseComponent> leftComponents, List<SyntaxCaseComponent> rightComponents) {
        Lol.v("----candidatesStartingWith()----");
        Lol.vl("Left components: ");
        leftComponents.forEach(x -> Lol.vl("<"+x.getSyntaxComponentName()+"> "));
        Lol.v("");
        Lol.vl("Right components: ");
        rightComponents.forEach(x -> Lol.v("<"+x.getSyntaxComponentName()+"> "));
        Lol.v("");

        //FIXME: sequentialComposition of two assignments is never taken because lExp is on the left and rExp is on the right, while
        //FIXME:    sequentialComposition is composed by "comm, comm"

        return grammar.getPriorityCaseList().stream().map(Pair::getSecond).collect(Collectors.toList()); //FIXME: temp solution with NO OPTIMIZATION.
                /*.stream()
                .map(Pair::getSecond)
                .filter(candidate -> {
                    Lol.v("checking: "+candidate.getCaseName());
                    if (candidate.getAssociativity() == Associativity.RightToLeft) {
                        return candidate.potentiallyStartsWith(leftComponents.get(0), grammar);
                    } else {
                        return candidate.potentiallyEndsWith(rightComponents.get(0), grammar);
                    }
                }).collect(Collectors.toList());*/
    }

    private boolean isOnlyTerminals(SyntaxCase sc) {
        for (SyntaxCaseComponent scc : sc.getStructure())
            if (!scc.isTerminal()) return false;
        return true;
    }

    private ParseTreeNode newTerminalNode(Token t) {
        ParseTreeNode ptn = PARSE_TREE_NODE_FACTORY.newNodeTree();
        ptn.setParsedToken(t);
        ptn.setTokenCategory(grammar.getTokenCategory(t));
        ptn.setTerminal(true);
        return ptn;
    }

    private ParseTreeNode newNonTerminalNode(SyntaxClass cl, SyntaxCase cs, List<ParseTreeNode> children) {
        ParseTreeNode ptn = PARSE_TREE_NODE_FACTORY.newNodeTree(children.toArray(new ParseTreeNode[children.size()]));
        ptn.setTerminal(false);
        ptn.setSyntaxCase(cs);
        ptn.setSyntaxClass(cl);
        return ptn;
    }

    private List<ParseTreeNode> promoteHighPriorityTerminals(List<ParseTreeNode> nodes) {
        List<ParseTreeNode> result = nodes;
        List<SyntaxCase> syntaxCases = new ArrayList<>();
        for (SyntaxCase sc : grammar.getPriorityCaseList().stream().map(Pair::getSecond).collect(Collectors.toList())) {
            if (!isOnlyTerminals(sc)) break;
            syntaxCases.add(sc);
        }
        for (SyntaxCase s : syntaxCases) {
            List<ParseTreeNode> tmp = new ArrayList<>();
            int window = s.getStructure().size();
            int start = 0;
            while (start <= result.size() - window) {
                int end = start + window;
                List<ParseTreeNode> currentSubList = result.subList(start, end);
                SyntaxCase instanceCase = new SyntaxCase("", null, currentSubList.stream()
                        .map(a -> a.isTerminal() ? a.getTokenCategory() : new SyntaxParsingInstance(a.getSyntaxClass(), a.getSyntaxCase()))
                        .collect(Collectors.toList())
                        .toArray(new SyntaxCaseComponent[currentSubList.size()]));
                boolean caseMatch = Grammar.caseMatch(instanceCase, s);
                if (caseMatch) {
                    tmp.add(newNonTerminalNode(s.getBelongingClass(), s, currentSubList));
                    start += window - 1;
                } else tmp.add(currentSubList.get(0));
                ++start;
            }
            for (int i = start; i < result.size(); ++i)
                tmp.add(result.get(i));

            result = tmp;
        }

        if(Lol.VERBOSE){
            ParseTreeNode ptn = PARSE_TREE_NODE_FACTORY.newNodeTree(result.toArray(new ParseTreeNode[result.size()]));
            SyntaxClass sc = new SyntaxClass("Preparser");
            ptn.setSyntaxClass(sc);
            ptn.setSyntaxCase(new SyntaxCase("promoteHighPriorityTerminals() result", sc));
            ptn.printTree();
        }

        return result;


    }

    private DoubleSidedReversibleStream<ParseTreeNode> getLeftSubStream(DoubleSidedReversibleStream<ParseTreeNode> ts, SyntaxCaseComponent nextComponent) {
        return (DoubleSidedReversibleStream<ParseTreeNode>) ts.leftSubStreamUntilPredicate((arg1) -> arg1.isTerminal() ?
                arg1.getTokenCategory().matches(nextComponent) :
                arg1.getSyntaxClass().matches(nextComponent));
    }

    private DoubleSidedReversibleStream<ParseTreeNode> getRightSubStream(DoubleSidedReversibleStream<ParseTreeNode> ts, SyntaxCaseComponent nextComponent) {
        return (DoubleSidedReversibleStream<ParseTreeNode>) ts.rightSubStreamUntilPredicate(
                (arg1) -> arg1.isTerminal() ?
                        arg1.getTokenCategory().matches(nextComponent) :
                        arg1.getSyntaxClass().matches(nextComponent));
    }

    private boolean oneNodeInStream(DoubleSidedReversibleStream<ParseTreeNode> ts, TokenCategory leftDelimiter, TokenCategory rightDelimiter) {
        return ts.remainingCount() == 1 || (
                rightDelimiter != null &&
                        ts.remainingCount() > 1 &&
                        ts.peekLeft(1).isTerminal() &&
                        ts.peekLeft(1).getTokenCategory().matches(rightDelimiter)
        ) || (
                leftDelimiter != null &&
                        ts.remainingCount() > 1 &&
                        ts.peekRight(1).isTerminal() &&
                        ts.peekRight(1).getTokenCategory().matches(leftDelimiter)
        );
    }

    private List<SyntaxCase> findCandidates(DoubleSidedReversibleStream<ParseTreeNode> ts) {
        ParseTreeNode firstLeft = ts.peekLeft();
        ParseTreeNode firstRight = ts.peekRight();
        List<SyntaxCase> candidates = PJ.reverse(candidatesStartingWith(
                firstLeft.isTerminal() ? PJ.list(firstLeft.getTokenCategory()) : PJ.list(firstLeft.getSyntaxClass()),
                firstRight.isTerminal() ? PJ.list(firstRight.getTokenCategory()) : PJ.list(firstRight.getSyntaxClass())));
        return candidates.stream().filter(c -> streamContainsTerminals(c.getTerminalSymbols(), ts.toList())).collect(Collectors.toList());
    }

    private boolean streamContainsTerminals(List<? extends SyntaxCaseComponent> terminals, List<ParseTreeNode> sequence) {
        //todo (OOO) other improvements can be done:
        //todo  - by checking the exact number of instances of the terminal
        //todo  - by checking the exact order in which the terminals appear
        //todo  - by concurrently calling this method in multithreading
        for (SyntaxCaseComponent scc : terminals) {
            if (!scc.isTerminal())
                throw new RuntimeException("streamContainsTerminals accepts a list of terminals only as first parameter");
            boolean isPresent = false;
            for (ParseTreeNode ptn : sequence)
                if (ptn.isTerminal() && ptn.getTokenCategory().matches(scc)) {
                    isPresent = true;
                    break;
                }
            if (!isPresent) return false;
        }
        return true;
    }

    private boolean findUniqueComponent(ReversibleStream<ParseTreeNode> ts, List<ParseTreeNode> tmpSequence, SyntaxCaseComponent component) throws NoEnoughElementsException {
        if (component.isTerminal()) {
            ParseTreeNode terminalNode = ts.popLeft();
            if (terminalNode.isTerminal() && !terminalNode.getTokenCategory().matches(component)) {
                return false;
            } else {
                tmpSequence.add(terminalNode);
                return true;
            }
        } else {
            throw new InvalidSyntaxCaseDefinitionException();
        }
    }

    private ParseFailedException newParseFailedException(List<ParseTreeNode> foundSequence) {
        ParseTreeNode errorNode = PARSE_TREE_NODE_FACTORY.newNodeTree(foundSequence.toArray(new ParseTreeNode[foundSequence.size()]));
        errorNode.setSyntaxCase(new SyntaxCase("PARSE FAILED", rootClass));
        errorNode.setSyntaxClass(rootClass);
        return new ParseFailedException(errorNode);
    }

    private DoubleSidedReversibleStream<ParseTreeNode> generateSubExpressionStream(DoubleSidedReversibleStream<ParseTreeNode> ts, Associativity associativity, TokenCategory prevDelimiter, TokenCategory nextDelimiter) {
        switch (associativity) {
            case LeftToRight: {
                List<ParseTreeNode> onErrorList = ts.toList();
                final Integer[] delimiterCounter = {1};
                DoubleSidedReversibleStream<ParseTreeNode> subStream =
                        (DoubleSidedReversibleStream<ParseTreeNode>) ts.rightSubStreamUntilPredicate(arg1 -> {
                            if (arg1.isTerminal() && arg1.getTokenCategory().matches(prevDelimiter))
                                delimiterCounter[0]++;
                            else if (arg1.isTerminal() && arg1.getTokenCategory().matches(nextDelimiter))
                                delimiterCounter[0]--;
                            return delimiterCounter[0] <= 0;
                        });
                if (delimiterCounter[0] > 0)
                    throw newParseFailedException(onErrorList); //TODO: add missing delimiter info
                return subStream;
            }
            case RightToLeft: {
                List<ParseTreeNode> onErrorList = ts.toList();
                final Integer[] delimiterCounter = {1};
                DoubleSidedReversibleStream<ParseTreeNode> subStream =
                        (DoubleSidedReversibleStream<ParseTreeNode>) ts.leftSubStreamUntilPredicate(arg1 -> {
                            if (arg1.isTerminal() && arg1.getTokenCategory().matches(prevDelimiter))
                                delimiterCounter[0]++;
                            else if (arg1.isTerminal() && arg1.getTokenCategory().matches(nextDelimiter))
                                delimiterCounter[0]--;
                            return delimiterCounter[0] <= 0;
                        });
                if (delimiterCounter[0] > 0)
                    throw newParseFailedException(onErrorList); //TODO: addMissing delimiter info
                return subStream;
            }
            default:
                return null;
        }
    }

    private class OverlapFailedException extends Exception {
    }
}
