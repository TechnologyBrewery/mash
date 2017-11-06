package org.bitbucket.cpointe.mash.example;

import java.util.Properties;

import org.bitbucket.cpointe.mash.Mediator;

public class LowercaseMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        String inputAsString = input.toString();
        return inputAsString.toLowerCase();
    }

}
