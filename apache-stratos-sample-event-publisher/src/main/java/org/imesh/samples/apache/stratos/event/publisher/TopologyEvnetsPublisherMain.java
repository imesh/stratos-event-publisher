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

package org.imesh.samples.apache.stratos.event.publisher;

import org.apache.stratos.messaging.domain.topology.*;
import org.apache.stratos.messaging.event.topology.*;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.UUID;

/**
 * Run this main class to send a set of sample topology events.
 */
public class TopologyEvnetsPublisherMain {
    private static String TOPIC_NAME = "topology";
    private static long TIME_INTERVAL = 4000; // 4 Seconds

    public static void main(String[] args) {
        TopicPublisher publisher = new TopicPublisher(TOPIC_NAME);
        try {
            publisher.connect();
            sendTopologyEvents(publisher);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                publisher.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendTopologyEvents(TopicPublisher publisher) throws JMSException, NamingException, IOException, InterruptedException {
        Topology topology = new Topology();

        Service service1 = generateService(topology);
        Cluster cluster = generateCluster(service1, "host1", " dep-pol1", "auto-scl-pol1");
        for(int i=0 ; i < 10; i++)    {
            generateMember(cluster, "network-partition1", "cloud-partition1");
        }

        // Send complete topology event
        CompleteTopologyEvent event = new CompleteTopologyEvent(topology);
        publisher.publish(event);
        Thread.sleep(TIME_INTERVAL);
    }

    private static Service generateService(Topology topology) {
        Service service = new Service("service-" + UUID.randomUUID().toString(), ServiceType.SingleTenant);
        service.addPort(new Port("http", 9280, 8280));
        service.addPort(new Port("https", 9282, 8282));
        topology.addService(service);
        return service;
    }

    private static Cluster generateCluster(Service service, String hostName, String deploymentPolicy, String autoscalingPolicy) {
        int instance = service.getClusters().size() + 1;
        Cluster cluster = new Cluster(service.getServiceName(), service.getServiceName() + "-cluster" + instance, deploymentPolicy, autoscalingPolicy);
        cluster.addHostName(hostName);
        cluster.setTenantRange("1-*");
        service.addCluster(cluster);
        return cluster;
    }

    private static Member generateMember(Cluster cluster, String networkPartitionId, String partitionId) {
        int instance = cluster.getMembers().size() + 1;
        Member member = new Member(cluster.getServiceName(), cluster.getClusterId(), networkPartitionId, partitionId, cluster.getClusterId() + "-member-" + instance);
        member.setMemberIp("10.0.0." + instance);
        member.setStatus(MemberStatus.Activated);
        cluster.addMember(member);
        return member;
    }
}
