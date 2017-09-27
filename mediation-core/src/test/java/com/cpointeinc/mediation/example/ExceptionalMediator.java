package com.cpointeinc.mediation.example;

import com.cpointeinc.mediation.Mediator;

public class ExceptionalMediator extends Mediator {

	@Override
	protected Object performMediation(Object input) {
		throw new RuntimeException("***BOOM*** You asked for this exception!");
	}

}
