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

package org.samples.apache.stratos.event.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.util.Constants;
import org.samples.apache.stratos.event.event.MemberFaultEventMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Health Statistics Generator
 */
public class HealthStatEventGenerator implements Runnable {


    private static final Log log = LogFactory.getLog(HealthStatEventGenerator.class);
    private static long TIME_INTERVAL = 5000;
    private int count;
    private List<Event> events;

    public HealthStatEventGenerator(List<Event> events) {
        this.events = events;
    }


    @Override
    public void run() {
        EventPublisher healthStatPublisher = EventPublisherPool.getPublisher(Constants.HEALTH_STAT_TOPIC);


        for (int i = 0; i < count; i++) {
            try {
                log.info("Generating sample HealthStat event...");
                String clusterId = "appserver.appserver.cloudstagi";
                String partitionId = "P1";
                String memberId = "appserver.appserver.cloudstagi2c2bc5f4-aeb7-4551-9852-5a7e55e17ef6";
                float value = (float) 0.0;

                Map<String, Object> MemberFaultEventMap = new HashMap<String, Object>();
                MemberFaultEventMessage memberFaultEvent = new MemberFaultEventMessage(clusterId, memberId, partitionId, value);
                MemberFaultEventMap.put("org.apache.stratos.messaging.event.health.stat.MemberFaultEvent", memberFaultEvent);

                Properties headers = new Properties();
                headers.put(Constants.EVENT_CLASS_NAME, memberFaultEvent.getMemberFaultEvent().getClass().getName());

                healthStatPublisher.publish(MemberFaultEventMap, headers, true);

                Thread.sleep(TIME_INTERVAL);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
