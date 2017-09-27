package com.cpointeinc.mediation;

/**
 * Defines the contract for a class that provides mediation capabilities.
 */
public abstract class Mediator {

	/**
	 * Performs mediation based on the input and associated mediation context.
	 * @param input values to mediate
	 * @return the mediated result
	 */
	public Object mediate(Object input) {
		try {
			return performMediation(input);
		} catch (Throwable t) {
			throw new MediationException("Issue encountered while performing mediation: ", t);
		}
	}
	
	protected abstract Object performMediation(Object input);
	
}
