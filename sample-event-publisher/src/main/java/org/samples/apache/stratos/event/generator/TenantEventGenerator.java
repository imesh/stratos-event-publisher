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
import org.apache.stratos.messaging.domain.tenant.Tenant;
import org.apache.stratos.messaging.domain.topology.Port;
import org.apache.stratos.messaging.domain.topology.Service;
import org.apache.stratos.messaging.domain.topology.ServiceType;
import org.apache.stratos.messaging.domain.topology.Topology;
import org.apache.stratos.messaging.event.tenant.CompleteTenantEvent;
import org.apache.stratos.messaging.util.MessagingUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Tenant Event generator.
 */
public class TenantEventGenerator implements Runnable {
    private static final Log log = LogFactory.getLog(TenantEventGenerator.class);
    private static long TIME_INTERVAL = 5000;
    private Topology topology = new Topology();
    private int count;

    public TenantEventGenerator(int count) {
        this.count = count;
    }

    public void run() {

        EventPublisher tenantPublisher = EventPublisherPool.getPublisher(MessagingUtil.Topics.TENANT_TOPIC.getTopicName());
        List<Tenant> tenants = new ArrayList<Tenant>();
        CompleteTenantEvent event = new CompleteTenantEvent(tenants);
        tenantPublisher.publish(event);


    }

    private Service generateService(Topology topology, String serviceName) {
        Service service = new Service(serviceName, ServiceType.SingleTenant);
        service.addPort(new Port("http", 8080, 9080));
        topology.addService(service);
        return service;
    }

}