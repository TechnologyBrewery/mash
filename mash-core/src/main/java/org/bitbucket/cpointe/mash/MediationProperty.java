package org.bitbucket.cpointe.mash;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Allows key value pairs to be added to mediator configurations. These are especially useful adding additional
 * information, such as configuration option information, into a specific mediator.
 */
public class MediationProperty {

    @JsonProperty
    private String key;

    @JsonProperty
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
