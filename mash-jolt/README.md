# Mash Jolt Data Mediation #
Wraps [Jolt](https://github.com/bazaarvoice/jolt) to provide a configuration-driven json mediation capability.  

This builds off the general configuration, with the following differences:

# Configuring Mediators to Runtime Lookup #
As previously mentioned, a key aspect to this approach is that mediators are not hard-coded into your codebase.  You must first configure each mediator to a specific input and output type.  This is done by specifying a simple json file(s) that is located within the directory specified in your mediation-definition-location Krausening properties.

**Be sure to add a *jolt-specification* property to your mediator configuration to specify the jolt transformation schema to apply within the mediator.**

```
#!json
[{
	"inputType": "xyz-json-v1",
	"outputType": "abc-json-v5",
	"className": "org.bitbucket.cpointe.mash.jolt.JoltMediator"
	"properties":[
		{
			"key":"jolt-specification",
			"value":"xzy-v1-to-abc-v5-jolt-transform.json"
		}
	]
}, {
	"inputType": "geopoint-json-v20",
	"outputType": "esri-json-v3",
	"className": "org.bitbucket.cpointe.mash.jolt.JoltMediator"
	"properties":[
		{
			"key":"jolt-specification",
			"value":"geopoint-v20-to-esri-v3-jolt-transform.json"
		}
	]
}]
```
# Maven Configuration #
```
#!xml
<dependency>
    <groupId>org.bitbucket.cpointe.mash</groupId>
    <artifactId>mash-jolt</artifactId>
    <version>LATEST-MASH-VERSION</version>
</dependency>
```