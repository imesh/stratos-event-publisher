package org.imesh.samples.apache.stratos.event.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.domain.topology.*;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.topology.CompleteTopologyEvent;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

/**
 * Event generator.
 */
public class EventGenerator {
    private static final Log log = LogFactory.getLog(EventGenerator.class);
    private static String TOPIC_NAME = "topology";
    private static long TIME_INTERVAL = 4000; // 4 Seconds

    public void execute() {
        URL path = getClass().getResource("/");
        System.setProperty("jndi.properties.dir", path.getFile());
        EventPublisher publisher = new EventPublisher(TOPIC_NAME);

        for(int i = 0; i < 100; i++) {
            try {
                log.info("Generating sample event...");
                Event event = generateCompleteTopologyEvent();
                log.info("Publishing event...");
                publisher.publish(event);
                Thread.sleep(TIME_INTERVAL);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private CompleteTopologyEvent generateCompleteTopologyEvent() throws JMSException, NamingException, IOException, InterruptedException {
        Topology topology = new Topology();

        Service service1 = generateService(topology, "Service1");
        Cluster cluster1 = generateCluster(service1, "foo.org", " dep-pol1", "auto-scl-pol1");
        for(int i=0 ; i < 5; i++)    {
            generateMember(cluster1, "network-partition1", "cloud-partition1");
        }

        Service service2 = generateService(topology, "Service2");
        Cluster cluster2 = generateCluster(service2, "foo.org", " dep-pol1", "auto-scl-pol1");
        for(int i=0 ; i < 5; i++)    {
            generateMember(cluster2, "network-partition1", "cloud-partition1");
        }

        // Send complete topology event
        CompleteTopologyEvent event = new CompleteTopologyEvent(topology);
        return event;
    }

    private Service generateService(Topology topology, String serviceName) {
        Service service = new Service(serviceName, ServiceType.SingleTenant);
        service.addPort(new Port("http", 8080, 9080));
        topology.addService(service);
        return service;
    }

    private Service generateService(Topology topology) {
        return generateService(topology, "service-" + UUID.randomUUID().toString());
    }

    private Cluster generateCluster(Service service, String hostName, String deploymentPolicy, String autoscalingPolicy) {
        int instance = service.getClusters().size() + 1;
        Cluster cluster = new Cluster(service.getServiceName(), service.getServiceName() + "-cluster" + instance, deploymentPolicy, autoscalingPolicy);
        cluster.addHostName(hostName);
        cluster.setTenantRange("1-*");
        service.addCluster(cluster);
        return cluster;
    }

    private Member generateMember(Cluster cluster, String networkPartitionId, String partitionId) {
        int instance = cluster.getMembers().size() + 1;
        Member member = new Member(cluster.getServiceName(), cluster.getClusterId(), networkPartitionId, partitionId, cluster.getClusterId() + "-member-" + instance);
        member.setMemberIp("127.0.0.1");
        member.setMemberPublicIp("127.0.0.1");
        member.addPort(new Port("http", 8080, 9080));
        member.setStatus(MemberStatus.Activated);
        cluster.addMember(member);
        return member;
    }
}