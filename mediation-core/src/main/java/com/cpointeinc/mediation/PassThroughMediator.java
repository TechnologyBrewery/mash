package com.cpointeinc.mediation;

public class PassThroughMediator extends Mediator {

	@Override
	protected Object performMediation(Object input) {
		return input;
	}

}
