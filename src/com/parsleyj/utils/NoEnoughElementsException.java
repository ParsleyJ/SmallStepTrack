package com.parsleyj.utils;

/**
 * Created by Giuseppe on 22/04/16.
 * TODO: javadoc
 */
public class NoEnoughElementsException extends Exception {
    public NoEnoughElementsException(int size, int howManyRequested){
        super("Stream size: "+size+" - elements requested: "+howManyRequested);
    }
}
