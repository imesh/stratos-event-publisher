package org.imesh.samples.apache.stratos.event.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.domain.topology.*;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.topology.*;
import org.apache.stratos.messaging.util.Constants;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * Event generator.
 */
public class TopologyEventGenerator implements Runnable {
    private static final Log log = LogFactory.getLog(TopologyEventGenerator.class);
    private static long TIME_INTERVAL = 5000;
    private Topology topology = new Topology();
    private int count;

    public TopologyEventGenerator(int count) {
        this.count = count;
    }

    public void run() {
        EventPublisher topologyPublisher = EventPublisherPool.getPublisher(Constants.TOPOLOGY_TOPIC);

        try {
            for (int i = 0; i < count; i++) {
                log.info("Generating sample event...");
                Event completeTopologyEvent = generateCompleteTopologyEvent();
                topologyPublisher.publish(completeTopologyEvent);

                Service service1 = generateService(topology, "myTestService");
                Properties p = new Properties();
                p.put("mykey1", "myval1");
                service1.setProperties(p);
                Cluster cluster1 = generateCluster(service1, "myTestCluster", "foo.org", " dep-pol1", "auto-scl-pol1");
                p.put("mykey123", "myval123");
                cluster1.setProperties(p);
                ServiceCreatedEvent serviceCreatedEvent = new ServiceCreatedEvent("myTestService", ServiceType.SingleTenant);
                topologyPublisher.publish(serviceCreatedEvent);

                ClusterCreatedEvent clusterCreatedEvent = new ClusterCreatedEvent("myTestService", "myTestCluster", cluster1);
                topologyPublisher.publish(clusterCreatedEvent);

                InstanceSpawnedEvent instanceSpawnedEvent = new InstanceSpawnedEvent("mongoshard", "mongoshard123", "network-partition1", "cloud-partition1", "mongoshard123-member-2");
                topologyPublisher.publish(instanceSpawnedEvent);

                MemberActivatedEvent memberActivatedEvent = new MemberActivatedEvent("mongoshard", "mongoshard123", "network-partition1", "cloud-partition1", "mongoshard123-member-2");
                memberActivatedEvent.setMemberIp("127.0.1.1");
                memberActivatedEvent.addPort(new Port("mongo", 27017, 27017));

                topologyPublisher.publish(memberActivatedEvent);
            }
            Thread.sleep(TIME_INTERVAL);
        } catch (Exception e) {
            log.error(e);
        }

    }

    private CompleteTopologyEvent generateCompleteTopologyEvent() throws JMSException, NamingException, IOException, InterruptedException {


        Service service1 = generateService(topology, "gateway");
        Properties p = new Properties();
        p.put("mykey1weeee", "myvalweeeeeeeee1");
        service1.setProperties(p);

        Cluster cluster1 = generateCluster(service1, "gatewaygateway.am.wso2.com.dom", "foo.org", " dep-pol1", "auto-scl-pol1");
        p.put("mykey1tttttttt", "myvalweetttttttttttt1");
        cluster1.setProperties(p);
        for (int i = 0; i < 2; i++) {
            generateMember(cluster1, "network-partition1", "cloud-partition1");
        }

        Service service2 = generateService(topology, "lb");
        Cluster cluster2 = generateCluster(service2, "lbisuruh.lk.domain", "foo.org", " dep-pol1", "auto-scl-pol1");
        for (int i = 0; i < 2; i++) {
            //generateMember(cluster2, "network-partition1", "cloud-partition1");
        }
        Member lbMember = new Member("lb", "lbisuruh.lk.domain", "network-partition1", "cloud-partition1", "lb-member-id-1");
        lbMember.setMemberPublicIp("public IP of apistore");
        lbMember.setMemberIp("private IP of apistore");
        cluster1.addMember(lbMember);


        Service service3 = generateService(topology, "apistore");
        Cluster cluster3 = generateCluster(service3, "apistorestore.am.wso2.com.doma", "foo.org", " dep-pol1", "auto-scl-pol1");
        for (int i = 0; i < 2; i++) {
           // generateMember(cluster3, "network-partition1", "cloud-partition1");
        }
        Member gatewayMember = new Member("gateway", "gatewaygateway.am.wso2.com.dom", "network-partition1", "cloud-partition1", "gatewaygateway.am.wso2.com.dom5863cf8d-2746-4be1-91e9-da98c2bcb5a9");
        gatewayMember.setMemberPublicIp("public IP");
        gatewayMember.setMemberIp("private IP");
        cluster1.addMember(gatewayMember);


        Member apiStoreMember = new Member("apistore", "apistorestore.am.wso2.com.doma", "network-partition1", "cloud-partition1", "apistorestore.am.wso2.com.doma298e2959-a3b4-45a0-a11f-7671606158ed" );
        apiStoreMember.setMemberIp("ip");
        apiStoreMember.setMemberPublicIp("pub ip");
        apiStoreMember.setStatus(MemberStatus.Activated);
        apiStoreMember.setLbClusterId("lbmongolb594224131.mongolb.dom");
        cluster3.addMember(apiStoreMember);

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
        return generateService(topology, UUID.randomUUID().toString());
    }

    private Cluster generateCluster(Service service, String clusterId, String hostName, String deploymentPolicy, String autoscalingPolicy) {
        int instance = service.getClusters().size() + 1;
        Cluster cluster = new Cluster(service.getServiceName(), clusterId, deploymentPolicy, autoscalingPolicy);
        cluster.addHostName(hostName);
        cluster.setTenantRange("1-*");
        service.addCluster(cluster);
        return cluster;
    }

    private Member generateMember(Cluster cluster, String networkPartitionId, String partitionId) {
        int instance = cluster.getMembers().size() + 1;
        Member member = new Member(cluster.getServiceName(), cluster.getClusterId(), networkPartitionId, partitionId, cluster.getClusterId() + "-member-" + instance);
        member.setMemberIp("127.0.0.1 private");
        member.setMemberPublicIp("127.0.0.1 public");
        member.addPort(new Port("http", 8080, 9080));
        member.setStatus(MemberStatus.Activated);

        Properties p = new Properties();
        p.put("mykey1xxxxxxxxx", "myvalweccccccccccccccc");
        member.setProperties(p);

        cluster.addMember(member);
        return member;
    }
}