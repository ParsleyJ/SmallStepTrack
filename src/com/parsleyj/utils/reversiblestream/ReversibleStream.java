package com.parsleyj.utils.reversiblestream;

import com.parsleyj.utils.NoEnoughElementsException;

import java.util.List;

/**
 * Created by Giuseppe on 22/04/16.
 * TODO: javadoc
 */
public interface ReversibleStream<T> {
    void checkPoint();

    void rollback();

    void commit();

    void pushLeft(T element);

    T popLeft() throws NoEnoughElementsException;

    List<T> popLeft(int howMany) throws NoEnoughElementsException;

    T peekLeft();

    T peekLeft(int offset);



    boolean isEmpty();

    int remainingCount();

    ReversibleStream<T> leftSubStreamUntilPredicate(Predicate<T> predicate);

    List<T> toList();

    @FunctionalInterface
    interface Predicate<T>{
        boolean evaluate(T arg1);
    }
}
