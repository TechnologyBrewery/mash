package com.cpointeinc.mediation;

/**
 * All {@link Throwable} that are encountered during the execution of a mediator will be wrapped in this exception.
 */
public class MediationException extends RuntimeException {

    private static final long serialVersionUID = -7364801681015246787L;

    /**
     * {@inheritDoc}
     */
    public MediationException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public MediationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public MediationException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public MediationException(Throwable cause) {
        super(cause);
    }

}
