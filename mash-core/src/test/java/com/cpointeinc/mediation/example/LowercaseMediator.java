package com.cpointeinc.mediation.example;

import java.util.Properties;

import com.cpointeinc.mediation.Mediator;

public class LowercaseMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        String inputAsString = input.toString();
        return inputAsString.toLowerCase();
    }

}
