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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.util.Constants;
import org.samples.apache.stratos.event.util.SampleConstants;

import java.io.File;
import java.net.URL;

/**
 * Run this main class to send a set of sample topology events.
 */
public class Main {

    private static final Log log = LogFactory.getLog(Main.class);
    private static SampleEventPublisher sampleEventPublisher = null;

    private static void configure() {
        URL path = Main.class.getResource("/");
        if (System.getProperty(SampleConstants.JNDI_PROPERTIES_SYSTEM_PROPERTY) == null ||
                System.getProperty(SampleConstants.JNDI_PROPERTIES_SYSTEM_PROPERTY).equals("")) {
            System.setProperty(SampleConstants.JNDI_PROPERTIES_SYSTEM_PROPERTY, path.getFile());
        }

        if (System.getProperty(SampleConstants.EVENT_USER_DATA_PATH) == null ||
                System.getProperty(SampleConstants.EVENT_USER_DATA_PATH).equals("")) {
            System.setProperty(SampleConstants.EVENT_USER_DATA_PATH, path.getFile() + File.separator + "SampleEvents.xml");
        }
    }

    public static void main(String[] args) {
        try {
            // Add shutdown hook
            final Thread mainThread = Thread.currentThread();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        // Close event publisher connections to message broker
                        EventPublisherPool.close(Constants.INSTANCE_STATUS_TOPIC);
                        EventPublisherPool.close(Constants.HEALTH_STAT_TOPIC);
                        EventPublisherPool.close(Constants.TENANT_TOPIC);
                        EventPublisherPool.close(Constants.TOPOLOGY_TOPIC);
                        EventPublisherPool.close(Constants.INSTANCE_NOTIFIER_TOPIC);
                        mainThread.join();
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            });

            // set system properties
            configure();

            if (sampleEventPublisher == null) {
                sampleEventPublisher = new SampleEventPublisher();
                if (log.isDebugEnabled()) {
                    log.debug("Starting Sample Event Publisher...");
                }
            }
            // start sample event publisher
            Thread thread = new Thread(sampleEventPublisher);
            thread.start();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e);
            }
            if (sampleEventPublisher != null) {
                sampleEventPublisher.terminate();
            }
        }

    }
}
