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
import org.apache.stratos.messaging.message.TopologyEventMessage;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;

/**
 * Run this main class to send a set of sample topology events.
 */
public class TopologyEvnetsPublisherMain {
    private static String TOPIC_NAME = "topology-topic";
    private static long TIME_INTERVAL = 10000; // 10 Seconds

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

        Service service1 = new Service();
        service1.setServiceName("AppServer");
        topology.addService(service1);

        Cluster cluster1 = new Cluster();
        cluster1.setClusterId("c1");
        cluster1.setHostName("appserver.foo.org");
        cluster1.setTenantRange("1-*");

        service1.addCluster(cluster1);

        Member member1 = new Member();
        member1.setServiceName("AppServer");
        member1.setClusterId("c1");
        member1.setMemberId("m1");
        member1.setHostName("192.168.1.112");
        member1.addPort(new Port("http", 80, 8280));
        member1.addPort(new Port("https", 90, 8290));
        member1.setStatus(MemberStatus.Starting);
        cluster1.addMember(member1);

        CompleteTopologyEvent event = new CompleteTopologyEvent();
        event.setTopology(topology);
        TopologyEventMessage message = new TopologyEventMessage(event);
        publisher.publish(message);
        Thread.sleep(TIME_INTERVAL);

        ServiceCreatedEvent event1 = new ServiceCreatedEvent();
        event1.setServiceName("ESB");
        message = new TopologyEventMessage(event1);
        publisher.publish(message);
        Thread.sleep(TIME_INTERVAL);

        ClusterCreatedEvent event2 = new ClusterCreatedEvent();
        event2.setServiceName("ESB");
        event2.setClusterId("c1");
        event2.setHostName("esb.foo.org");
        event2.setTenantRange("1-*");
        message = new TopologyEventMessage(event2);
        publisher.publish(message);
        Thread.sleep(TIME_INTERVAL);

        MemberStartedEvent event3 = new MemberStartedEvent();
        event3.setServiceName("ESB");
        event3.setClusterId("c1");
        event3.setMemberId("m1");
        event3.setHostName("192.168.1.112");
        event3.addPort(new Port("http", 80, 8280));
        event3.addPort(new Port("https", 90, 8290));
        message = new TopologyEventMessage(event3);
        publisher.publish(message);
        Thread.sleep(TIME_INTERVAL);

        MemberStartedEvent event4 = new MemberStartedEvent();
        event4.setServiceName("ESB");
        event4.setClusterId("c1");
        event4.setMemberId("m2");
        event4.setHostName("192.168.1.112");
        event4.addPort(new Port("http", 80, 8280));
        event4.addPort(new Port("https", 90, 8290));
        message = new TopologyEventMessage(event4);
        publisher.publish(message);
        Thread.sleep(TIME_INTERVAL);

        MemberActivatedEvent event5 = new MemberActivatedEvent();
        event5.setServiceName("ESB");
        event5.setClusterId("c1");
        event5.setMemberId("m1");
        message = new TopologyEventMessage(event5);
        publisher.publish(message);
        Thread.sleep(TIME_INTERVAL);
    }
}
