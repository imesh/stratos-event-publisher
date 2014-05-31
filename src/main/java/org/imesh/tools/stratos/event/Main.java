/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.imesh.tools.stratos.event;

import org.apache.log4j.PropertyConfigurator;
import org.imesh.tools.stratos.event.generator.TenantEventGenerator;
import org.imesh.tools.stratos.event.generator.TopologyEventGenerator;
import org.imesh.tools.stratos.event.receiver.EventReceiver;

import java.io.File;

/**
 * Run this main class to send a set of sample topology events.
 */
public class Main {

    public static void main(String[] args) {
        // Configure log4j properties
        PropertyConfigurator.configure(System.getProperty("log4j.properties.file.path", "src/main/conf/log4j.properties"));
        System.setProperty("jndi.properties.dir", System.getProperty("jndi.properties.dir", "src/main/conf"));

        TopologyEventGenerator topologyEventGenerator = new TopologyEventGenerator("topology", 1);
        Thread topologyEventGeneratorThread = new Thread(topologyEventGenerator);
        topologyEventGeneratorThread.start();

        EventReceiver receiver = new EventReceiver("topology");
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        TenantEventGenerator tenantEventGenerator = new TenantEventGenerator("tenant", 1);
        Thread tenantEventGeneratorThread = new Thread(tenantEventGenerator);
        tenantEventGeneratorThread.start();
    }
}
