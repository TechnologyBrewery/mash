# Velocity Data Mediation #
Wraps [Velocity](http://velocity.apache.org/) to provide a configuration-driven generic -to-string mediation capability.  

This builds off the general configuration, with the following differences:

# Configuring Mediators to Runtime Lookup #
As previously mentioned, a key aspect to this approach is that mediators are not hard-coded into your codebase.  You must first configure each mediator to a specific input and output type.  This is done by specifying a simple json file(s) that is located within the directory specified in your mediation-definition-location Krausening properties.

**Be sure to add a *velocity-template* property to your mediator configuration to specify the velocity template to apply within the mediator.**

Additionally, if you want to validate that the input type is of a specified class (or one of its sub-classes), you can add a velocity-input-class property with the fully qualified class name to use in this validation step.  If none is specified, the value will default to java.lang.Object, effectively allowing anything to pass the check.

```
#!json
[{
	"inputType": "Foo.class",
	"outputType": "foo-v1.xml",
	"className": "org.bitbucket.cpointe.mash.velocity.VelocityMediator",
	"properties": [{
		"key": "velocity-template",
		"value": "templates/to-xml.vm"
	}]
}, {
	"inputType": "TestProperty",
	"outputType": "foo-v1.json",
	"className": "org.bitbucket.cpointe.mash.velocity.VelocityMediator",
	"properties": [{
		"key": "velocity-template",
		"value": "templates/to-json.vm"
	}, {
		"key": "velocity-input-class",
		"value": "org.bitbucket.cpointe.mash.velocity.TestProperty"
	}]
}}]
```
# Maven Configuration #
```
#!xml
<dependency>
    <groupId>org.bitbucket.cpointe.mash</groupId>
    <artifactId>mash-velocity</artifactId>
    <version>LATEST-MASH-VERSION</version>
</dependency>
```