package com.cpointeinc.mediation.example;

import com.cpointeinc.mediation.Mediator;

public class LowercaseMediator extends Mediator {

	@Override
	protected Object performMediation(Object input) {
		String inputAsString = input.toString();
		return inputAsString.toLowerCase();
	}
	
}
