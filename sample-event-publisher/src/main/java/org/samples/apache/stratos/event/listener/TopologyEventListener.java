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

package org.samples.apache.stratos.event.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.domain.topology.Service;
import org.apache.stratos.messaging.domain.topology.Topology;
import org.apache.stratos.messaging.message.receiver.topology.TopologyManager;
import org.apache.stratos.messaging.util.Constants;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.lang.reflect.Type;
import java.util.Collection;


/**
 * TopologyEventListener
 */
public class TopologyEventListener implements MessageListener {
    private static final Log log = LogFactory.getLog(TopologyEventListener.class);
    private static final Gson gson = new Gson();
    private static final Type serviceType = new TypeToken<Collection<Service>>() {
    }.getType();

    @Override
    public void onMessage(Message message) {
        try {
            if (TopologyManager.getTopology().isInitialized()) {
                Topology topology = TopologyManager.getTopology();
                String completeTopology = gson.toJson(topology.getServices(), serviceType);
                log.info(String.format("Complete Topology: %s", completeTopology));
            }
            log.info(String.format("Message received [TOPIC]: %s  [MESSAGE] %s", Constants.TOPOLOGY_TOPIC, ((TextMessage) message).getText()));

        } catch (JMSException e) {
            log.error(e);
        }
    }

    @Override
    public String toString(){
        return "TopologyEventListener";
    }
}
