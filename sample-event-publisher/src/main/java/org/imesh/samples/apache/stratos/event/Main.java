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

package org.imesh.samples.apache.stratos.event;

import org.apache.stratos.messaging.util.Constants;
import org.imesh.samples.apache.stratos.event.generator.TenantEventGenerator;
import org.imesh.samples.apache.stratos.event.generator.TopologyEventGenerator;
import org.imesh.samples.apache.stratos.event.receiver.EventReceiver;

import java.net.URL;

/**
 * Run this main class to send a set of sample topology events.
 */
public class Main {

    public static void main(String[] args) {

        URL path = Main.class.getResource("/");
        System.setProperty("jndi.properties.dir", path.getFile());

        TopologyEventGenerator topologyEventGenerator = new TopologyEventGenerator(1);
        Thread generatorThread = new Thread(topologyEventGenerator);
        generatorThread.start();

        TenantEventGenerator tenantEventGenerator = new TenantEventGenerator(1);
        Thread tenantGeneratorThread = new Thread(tenantEventGenerator);
        tenantGeneratorThread.start();

        EventReceiver receiver = new EventReceiver(Constants.TOPOLOGY_TOPIC);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        receiver = new EventReceiver(Constants.INSTANCE_NOTIFIER_TOPIC);
        receiverThread = new Thread(receiver);
        receiverThread.start();

        receiver = new EventReceiver(Constants.INSTANCE_STATUS_TOPIC);
        receiverThread = new Thread(receiver);
        receiverThread.start();

        receiver = new EventReceiver(Constants.HEALTH_STAT_TOPIC);
        receiverThread = new Thread(receiver);
      //  receiverThread.start();

        receiver = new EventReceiver(Constants.TENANT_TOPIC);
        receiverThread = new Thread(receiver);
        receiverThread.start();
    }
}
