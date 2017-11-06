package org.bitbucket.cpointe.mash;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains {@link MediationContext} information in addition to the class name of the mediator that will provide the
 * implementation to mediate from input type to output type.
 */
public class MediationConfiguration extends MediationContext {

    @JsonProperty
    private String className;
    
    @JsonProperty
    private List<MediationProperty> properties;    

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<MediationProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<MediationProperty> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
