package com.parsleyj.utils.reversiblestream;

import com.parsleyj.utils.NoEnoughElementsException;

import java.util.List;

/**
 * Created by Giuseppe on 23/04/16.
 * TODO: javadoc
 */
public interface DoubleSidedReversibleStream <T> extends ReversibleStream<T> {

    void pushRight(T element);

    T popRight() throws NoEnoughElementsException;

    List<T> popRight(int howMany) throws NoEnoughElementsException;

    T peekRight();

    T peekRight(int offset);

    ReversibleStream<T> rightSubStreamUntilPredicate(Predicate<T> predicate);
}
