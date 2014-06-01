package org.imesh.tools.stratos.event.generator;

import org.apache.stratos.messaging.domain.topology.*;
import org.apache.stratos.messaging.event.topology.CompleteTopologyEvent;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.UUID;

/**
 * Topology utility methods.
 */
public class TopologyUtil {
    public static Topology getTopology() {
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

        return topology;
    }

    private static Service generateService(Topology topology, String serviceName) {
        Service service = new Service(serviceName, ServiceType.SingleTenant);
        service.addPort(new Port("https", 9448, 8243));
        topology.addService(service);
        return service;
    }

    private static Service generateService(Topology topology) {
        return generateService(topology, "service-" + UUID.randomUUID().toString());
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
        member.setMemberIp("127.0.0.1");
        member.setMemberPublicIp("127.0.0.1");
        member.addPort(new Port("https", 9448, 8243));
        member.setStatus(MemberStatus.Activated);
        cluster.addMember(member);
        return member;
    }
}
