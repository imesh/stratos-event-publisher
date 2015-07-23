Apache Stratos - Samples
======================

This git repo contains sample artifacts and tools for testing features in [Apache Stratos PaaS](http://stratos.apache.org/).

### Sample Event Publisher

This tool can be used to publish custom events to a message broker. Stratos uses a message broker based communication mechanism to pass messages between separate components in a loosely coupled manner. This tool uses the same messaging component in Stratos to publish events. It can be used to write integration tests by simulating various events. Also it can be used to troubleshoot issues that may arise in a Stratos PaaS deployment.

#### Building and running the tool
1. Build the product using Maven
2. Update <Sample_Publisher_Home>/conf/jndi.properties and <Sample_Publisher_Home>/conf/mqtttopic.properties file with MB URL and port.
3. Add events to be published in <Sample_Publisher_Home>/data/SampleEvents.xml 
4. Run `<Sample_Publisher_Home>/bin/run.sh publish`


#### Maven execute
1. Add events to be published in <Git_Repo>/sample-event-publisher/src/main/resources/SampleEvents.xml 
2. Update <Sample_Publisher_Home>/conf/jndi.properties and <Sample_Publisher_Home>/conf/mqtttopic.properties file with MB URL and port.
3. Run `<Git_Repo>/run.sh publish`

**You need to pass `publish` argument to publish the events provided. Otherwise it will only listen to events published by other components.**


#License

Copyright (C) 2013-2015 The Apache Software Foundation

Licensed under the Apache License, Version 2.0

