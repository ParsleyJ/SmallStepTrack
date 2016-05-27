package com.parsleyj.smallsteptrack.integerexpr;

import com.parsleyj.toolparser.configuration.Configuration;
import com.parsleyj.smallsteptrack.configurations.DirectInputStream;

import java.util.Scanner;

/**
 * Created by Giuseppe on 30/03/16.
 * TODO: javadoc
 */
public class ReadCommand implements IntegerExpression {
    private String inputStreamName;

    public ReadCommand(String inputStreamName) {
        this.inputStreamName = inputStreamName;
    }

    @Override
    public IntegerExpression step(Configuration c) {
        DirectInputStream dis = (DirectInputStream) c.getConfigurationElement(inputStreamName);
        Scanner sc = dis.getScanner();
        System.out.print("read: ");
        Integer i = sc.nextInt();
        return new Numeral(i);
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return "read";
    }
}
