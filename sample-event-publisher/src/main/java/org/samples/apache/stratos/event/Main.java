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
import org.apache.stratos.messaging.util.MessagingUtil;
import org.samples.apache.stratos.event.util.SampleConstants;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

/**
 * Run this main class to send a set of sample topology events.
 */
public class Main {

    private static final Log logger = LogFactory.getLog(Main.class);
    private static SampleEventPublisher sampleEventPublisher = null;

    private static void configure() {
        URL path = Main.class.getResource("/");
        if (System.getProperty(SampleConstants.JNDI_PROPERTIES_SYSTEM_PROPERTY) == null ||
                System.getProperty(SampleConstants.JNDI_PROPERTIES_SYSTEM_PROPERTY).equals("")) {
            System.setProperty(SampleConstants.JNDI_PROPERTIES_SYSTEM_PROPERTY, path.getFile());
        }

        if (System.getProperty(SampleConstants.EVENT_USER_DATA_PATH) == null ||
                System.getProperty(SampleConstants.EVENT_USER_DATA_PATH).equals("")) {
            System.setProperty(SampleConstants.EVENT_USER_DATA_PATH,
                    path.getFile() + File.separator + "SampleEvents.xml");
        }
    }

    public static void main(String[] args) {
        init(args);
    }

    private static void init(String[] args) {
        try {
            // Add shutdown hook
            final Thread mainThread = Thread.currentThread();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        // Close event publisher connections to message broker
                        EventPublisherPool.close(MessagingUtil.Topics.INSTANCE_STATUS_TOPIC.getTopicName());
                        EventPublisherPool.close(MessagingUtil.Topics.HEALTH_STAT_TOPIC.getTopicName());
                        EventPublisherPool.close(MessagingUtil.Topics.TENANT_TOPIC.getTopicName());
                        EventPublisherPool.close(MessagingUtil.Topics.TOPOLOGY_TOPIC.getTopicName());
                        EventPublisherPool.close(MessagingUtil.Topics.INSTANCE_NOTIFIER_TOPIC.getTopicName());
                        mainThread.join();
                    }
                    catch (Exception e) {
                        logger.error(e);
                    }
                }
            });

            // set system properties
            configure();

            if (sampleEventPublisher == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Starting Sample Event Publisher...");
                }
                sampleEventPublisher = new SampleEventPublisher();
                if (Arrays.asList(args).contains(SampleConstants.ENABLE_PUBLISHER)) {
                    logger.info("Event publisher is enabled.");
                    sampleEventPublisher.setIsPublisherEnabled(true);
                }
            }
            // start sample event publisher
            Thread thread = new Thread(sampleEventPublisher);
            thread.start();
        }
        catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(e);
            }
            if (sampleEventPublisher != null) {
                sampleEventPublisher.terminate();
            }
        }
    }

}


