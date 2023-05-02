package org.technologybrewery.mash;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Provides contextual information about a mediation that is to be performed.
 */
public class MediationContext {

    /**
     * Default constructor.
     */
    public MediationContext() {

    }

    /**
     * New mediation context.
     * 
     * @param inputType
     *            input type to represent
     * @param outputType
     *            output type to represent
     */
    public MediationContext(String inputType, String outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    @JsonProperty
    private String inputType;

    @JsonProperty
    private String outputType;

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
