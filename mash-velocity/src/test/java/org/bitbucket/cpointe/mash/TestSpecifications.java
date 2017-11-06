package org.bitbucket.cpointe.mash;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Test harness to run Gherkin Business Driven Development specifications.
 * 
 * This needs to be left at the mediation package level to reuse mediation-core steps.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/specifications", plugin = "json:target/cucumber-html-reports/cucumber.json")
public class TestSpecifications {

}