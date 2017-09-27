package com.cpointeinc.mediation;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains {@link MediationContext} information in addition to the class name of the mediator that will provide the
 * implementation to mediate from input type to output type.
 */
public class MediationConfiguration extends MediationContext {

    @JsonProperty
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
