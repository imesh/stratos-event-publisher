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

        // Application server service
        Service service1 = new Service("AppServer");
        topology.addService(service1);

        // Application server cluster 1
        Cluster cluster1 = new Cluster(service1.getServiceName(), "c1");
        cluster1.setHostName("appserver.foo.org");
        cluster1.setTenantRange("1-*");
        service1.addCluster(cluster1);

        // Application server cluster 1 members
        Member member1 = new Member(cluster1.getServiceName(), cluster1.getClusterId(), "m1");
        member1.setMemberIp("10.100.5.8");
        member1.addPort(new Port("http", 9769, 8280));
        member1.addPort(new Port("https", 9449, 8290));
        member1.setStatus(MemberStatus.Activated);
        cluster1.addMember(member1);

        Member member2 = new Member(cluster1.getServiceName(), cluster1.getClusterId(), "m2");
        member2.setMemberIp("10.100.5.8");
        member2.addPort(new Port("http", 9770, 8280));
        member2.addPort(new Port("https", 9450, 8290));
        member2.setStatus(MemberStatus.Activated);
        cluster1.addMember(member2);

        Member member3 = new Member(cluster1.getServiceName(), cluster1.getClusterId(), "m3");
        member3.setMemberIp("10.100.5.8");
        member3.addPort(new Port("http", 9771, 8280));
        member3.addPort(new Port("https", 9451, 8290));
        member3.setStatus(MemberStatus.Activated);
        cluster1.addMember(member3);

        // Send complete topology event
        CompleteTopologyEvent event = new CompleteTopologyEvent();
        event.setTopology(topology);
        publisher.publish(event);
        Thread.sleep(TIME_INTERVAL);

        // Send ESB service created event
        ServiceCreatedEvent event1 = new ServiceCreatedEvent("ESB");
        publisher.publish(event1);
        Thread.sleep(TIME_INTERVAL);

        // Send ESB cluster c1 created event
        ClusterCreatedEvent event2 = new ClusterCreatedEvent("ESB", "c1");
        event2.setHostName("esb.foo.org");
        event2.setTenantRange("1-*");
        publisher.publish(event2);
        Thread.sleep(TIME_INTERVAL);

        // Send ESB cluster c1 member m1 spawned event
        InstanceSpawnedEvent event3 = new InstanceSpawnedEvent("ESB", "c1", "m1");
        publisher.publish(event3);
        Thread.sleep(TIME_INTERVAL);

        // Send ESB cluster c1 member m1 started event
        MemberStartedEvent event4 = new MemberStartedEvent("ESB", "c1", "m1");
        publisher.publish(event4);
        Thread.sleep(TIME_INTERVAL);

        // Send ESB cluster c1 member m1 activated event
        MemberActivatedEvent event5 = new MemberActivatedEvent("ESB", "c1", "m1");
        event5.setMemberIp("192.168.1.112");
        event5.addPort(new Port("http", 80, 8280));
        event5.addPort(new Port("https", 90, 8290));
        publisher.publish(event5);
        Thread.sleep(TIME_INTERVAL);
    }
}
