package org.technologybrewery.mash.jolt;

import org.technologybrewery.mash.MediationConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JoltConfiguration extends MediationConfiguration {

    @JsonIgnore
    public String joltSpecification;
    
}
