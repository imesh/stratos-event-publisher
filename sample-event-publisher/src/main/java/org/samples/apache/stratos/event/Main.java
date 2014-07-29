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

package org.samples.apache.stratos.event;

import org.apache.stratos.messaging.util.Constants;
import org.samples.apache.stratos.event.generator.HealthStatEventGenerator;
import org.samples.apache.stratos.event.generator.TenantEventGenerator;
import org.samples.apache.stratos.event.generator.TopologyEventGenerator;
import org.samples.apache.stratos.event.listener.HealthStatListener;
import org.samples.apache.stratos.event.listener.TopologyEventListener;
import org.samples.apache.stratos.event.receiver.EventReceiver;

import java.net.URL;

/**
 * Run this main class to send a set of sample topology events.
 */
public class Main {

    public static void main(String[] args) {
        URL path = Main.class.getResource("/");
        System.setProperty("jndi.properties.dir", path.getFile());
        initMessageProcessor();
        initReceivers();
        initGenerators();
    }

    private static void initGenerators() {
        TopologyEventGenerator topologyEventGenerator = new TopologyEventGenerator(0);
        Thread topologyGeneratorThread = new Thread(topologyEventGenerator);
        topologyGeneratorThread.start();

        TenantEventGenerator tenantEventGenerator = new TenantEventGenerator(0);
        Thread tenantGeneratorThread = new Thread(tenantEventGenerator);
        tenantGeneratorThread.start();

        HealthStatEventGenerator healthStatEventGenerator = new HealthStatEventGenerator(0);
        Thread healthStatGeneratorThread = new Thread(healthStatEventGenerator);
        healthStatGeneratorThread.start();
    }

    private static void initReceivers() {
        EventReceiver topologyReceiver = new EventReceiver(Constants.TOPOLOGY_TOPIC);
        Thread topologyReceiverThread = new Thread(topologyReceiver);
        topologyReceiverThread.start();

        EventReceiver instanceNotifierReceiver = new EventReceiver(Constants.INSTANCE_NOTIFIER_TOPIC);
        Thread instanceNotifierReceiverThread = new Thread(instanceNotifierReceiver);
        instanceNotifierReceiverThread.start();

        EventReceiver instanceStatusReceiver = new EventReceiver(Constants.INSTANCE_STATUS_TOPIC);
        Thread instanceStatusReceiverThread = new Thread(instanceStatusReceiver);
        instanceStatusReceiverThread.start();

        EventReceiver healthStatReceiver = new EventReceiver(Constants.HEALTH_STAT_TOPIC);
        Thread healthStatReceiverThread = new Thread(healthStatReceiver);
        healthStatReceiverThread.start();

        EventReceiver tenantReceiver = new EventReceiver(Constants.TENANT_TOPIC);
        Thread tenantReceiverThread = new Thread(tenantReceiver);
        tenantReceiverThread.start();
    }

    private static void initMessageProcessor() {
        MessageProcessor.addMessageListener(Constants.TOPOLOGY_TOPIC, new TopologyEventListener());
        MessageProcessor.addMessageListener(Constants.HEALTH_STAT_TOPIC, new HealthStatListener());
    }


}
