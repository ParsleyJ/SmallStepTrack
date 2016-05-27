package com.parsleyj.utils.reversiblestream;

import com.parsleyj.utils.NoEnoughElementsException;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Created by Giuseppe on 22/04/16.
 * TODO: javadoc
 */
public class SimpleReversibleStream<T> implements ReversibleStream<T> {



    private List<T> elements;
    private ArrayDeque<Integer> checkPoints = new ArrayDeque<>();
    private int index = 0;

    public SimpleReversibleStream(List<T> elements) {
        this.elements = elements;
    }

    @Override
    public void checkPoint() {
        checkPoints.addFirst(index);
    }

    @Override
    public void rollback() {
        index = checkPoints.pop();
    }

    @Override
    public void commit() {
        checkPoints.pop();
    }

    @Override
    public T popLeft() throws NoEnoughElementsException {
        if (index >= elements.size()) throw new NoEnoughElementsException(remainingCount(), index-elements.size());
        return elements.get(index++);
    }



    @Override
    public List<T> popLeft(int howMany) throws NoEnoughElementsException {
        if (index + howMany >= elements.size()) throw new NoEnoughElementsException(remainingCount(), howMany);
        List<T> result = elements.subList(index, index + howMany);
        index += howMany;
        return result;
    }

    @Override
    public void pushLeft(T element) {
        elements.add(0, element);
    }


    @Override
    public T peekLeft() {
        return elements.get(index);
    }

    @Override
    public T peekLeft(int offset) {
        return elements.get(index + offset);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean isEmpty() {
        return index >= elements.size();
    }

    @Override
    public int remainingCount() {
        return elements.size() - index;
    }

    @Override
    public ReversibleStream<T> leftSubStreamUntilPredicate(Predicate<T> predicate) {
        if (predicate == null) {
            SimpleReversibleStream<T> pns = new SimpleReversibleStream<>(elements.subList(index, elements.size()));
            index = elements.size();
            return pns;
        }
        for (int i = index; i < elements.size(); ++i) {
            if (predicate.evaluate(elements.get(i))) {
                SimpleReversibleStream<T> pns = new SimpleReversibleStream<>(elements.subList(index, i));
                index = i;
                return pns;
            }
        }
        return null;
    }

    @Override
    public List<T> toList() {
        return elements.subList(index, elements.size());
    }

}
