package com.parsleyj.utils.reversiblestream;

import com.parsleyj.utils.NoEnoughElementsException;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Giuseppe on 22/04/16.
 * TODO: javadoc
 */
public class StackedReversibleStream <T> implements DoubleSidedReversibleStream<T> {

    private static int idgen = 0;

    private ArrayDeque<ArrayDeque<T>> streams = new ArrayDeque<>();
    private PrintStream debugStream = null;
    private final int id;

    public StackedReversibleStream(List<T> elements){
        this.id = idgen++;
        streams.add(new ArrayDeque<>(elements));
    }


    public StackedReversibleStream<T> setDebug(PrintStream debugStream){
        this.debugStream = debugStream;
        return this;
    }


    @Override
    public void checkPoint() {
        if (debugStream != null) {
            debugStream.println("STREAM:"+id+" - CHECKPOINT");
        }
        streams.push(new ArrayDeque<>(streams.peek()));
    }

    @Override
    public void rollback() {
        if (debugStream != null) {
            debugStream.println("STREAM:"+id+" - ROLLBACK");
        }
        streams.pop();
    }

    @Override
    public void commit() {
        if (debugStream != null) {
            debugStream.println("STREAM:"+id+" - COMMIT");
        }
        ArrayDeque<T> stream = streams.pop();
        streams.pop();
        streams.push(stream);
    }

    @Override
    public T popLeft() throws NoEnoughElementsException {
        if(streams.peek().isEmpty()) throw new NoEnoughElementsException(streams.peek().size(), 1);
        return streams.peek().removeFirst();
    }




    @Override
    public List<T> popLeft(int howMany) throws NoEnoughElementsException {
        if(streams.peek().size()<howMany) throw new NoEnoughElementsException(streams.peek().size(), howMany);
        List<T> result = new ArrayList<>();
        for(int  i = 0; i < howMany; ++i){
            result.add(streams.peek().removeFirst());
        }
        return result;
    }

    @Override
    public void pushLeft(T element) {
        streams.peek().addFirst(element);
    }


    @Override
    public T peekLeft() {
        return streams.peek().peekFirst();
    }

    @Override
    public T peekLeft(int offset) {
        int i = 0;
        for(T t: streams.peek()){
            if(i == offset) return t;
            ++i;
        }
        throw new IndexOutOfBoundsException("Stream size: "+streams.peek().size()+" - searched offset: "+offset);
    }

    @Override
    public boolean isEmpty() {
        return streams.peek().isEmpty();
    }

    @Override
    public int remainingCount() {
        return streams.peek().size();
    }

    @Override
    public ReversibleStream<T> leftSubStreamUntilPredicate(Predicate<T> predicate) {
        if(predicate == null) try {
            return new StackedReversibleStream<>(this.popLeft(remainingCount())).setDebug(debugStream);
        } catch (NoEnoughElementsException e) {
            e.printStackTrace(); //this never happens
        }else {
            for(int i = 0; i < remainingCount(); ++i){
                if(predicate.evaluate(this.peekLeft(i))){
                    try {
                        return new StackedReversibleStream<>(this.popLeft(i)).setDebug(debugStream);
                    } catch (NoEnoughElementsException e) {
                        e.printStackTrace(); //this never happens
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ReversibleStream<T> rightSubStreamUntilPredicate(Predicate<T> predicate) {//let this make return the same object with a new stream on the stack?
        if(predicate == null) try {
            return new StackedReversibleStream<>(this.popRight(remainingCount())).setDebug(debugStream);
        } catch (NoEnoughElementsException e) {
            e.printStackTrace(); //this never happens
        } else {
            for(int i = 0; i < remainingCount(); ++i){
                if(predicate.evaluate(this.peekRight(i))){
                    try {
                        return new StackedReversibleStream<>(this.popRight(i)).setDebug(debugStream);
                    } catch (NoEnoughElementsException e) {
                        e.printStackTrace(); //this never happens
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<T> toList() {
        if(streams.peek()==null) return null;
        return streams.peek().stream().collect(Collectors.toList());
    }

    @Override
    public void pushRight(T element) {
        streams.peek().addLast(element);
    }

    @Override
    public T popRight() throws NoEnoughElementsException {
        if(streams.peek().isEmpty()) throw new NoEnoughElementsException(streams.peek().size(), 1);
        return streams.peek().removeLast();
    }

    @Override
    public List<T> popRight(int howMany) throws NoEnoughElementsException {
        if(streams.peek().size()<howMany) throw new NoEnoughElementsException(streams.peek().size(), howMany);
        List<T> result = new ArrayList<>();
        for(int  i = howMany-1; i >= 0; --i){
            result.add(0, streams.peek().removeLast());
        }
        return result;
    }

    @Override
    public T peekRight() {
        return streams.peek().peekLast();
    }

    @Override
    public T peekRight(int offset) {
        int i = 0;
        for (Iterator<T> iterator = streams.peek().descendingIterator(); iterator.hasNext(); ) {
            T t = iterator.next();
            if (i == offset) return t;
            ++i;
        }
        throw new IndexOutOfBoundsException("Stream size: "+streams.peek().size()+" - searched offset: "+offset);
    }
}
