package org.bitbucket.cpointe.mash.jolt;

import org.bitbucket.cpointe.mash.MediationConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JoltConfiguration extends MediationConfiguration {

    @JsonIgnore
    public String joltSpecification;
    
}
