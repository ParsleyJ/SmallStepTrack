package com.parsleyj.utils;

/**
 * Created by Giuseppe on 01/05/16.
 * TODO: javadoc
 */
public class Lol {
    public static final boolean DEBUG = true;
    public static final boolean VERBOSE = false;

    public static void v(String x){
        if(VERBOSE) System.out.println(x);
    }

    public static void vl(String x){
        if(VERBOSE) System.out.print(x);
    }

    public static void d(String x){
        if(DEBUG) System.out.println(x);
    }

    public static void dl(String x){
        if(DEBUG) System.out.print(x);
    }
}
