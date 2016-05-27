package com.parsleyj.smallsteptrack.configurations;

import com.parsleyj.toolparser.configuration.ConfigurationElement;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Giuseppe on 30/03/16.
 * TODO: javadoc
 */
public class DirectInputStream implements ConfigurationElement {
    private String configurationElementName;
    private InputStream inputStream = System.in;
    private Scanner scanner = new Scanner(inputStream);

    public DirectInputStream(String configurationElementName) {
        this.configurationElementName = configurationElementName;
    }

    public DirectInputStream(String configurationElementName, InputStream inputStream){
        this.configurationElementName = configurationElementName;
        this.inputStream = inputStream;
        this.scanner = new Scanner(inputStream);
    }



    @Override
    public String getConfigurationElementName() {
        return configurationElementName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.scanner = new Scanner(inputStream);
    }

    public Scanner getScanner() {
        return scanner;
    }

    @Override
    public boolean toBePrinted() {
        return false;
    }
}
