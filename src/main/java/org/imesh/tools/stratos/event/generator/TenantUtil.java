package org.imesh.tools.stratos.event.generator;

import org.apache.stratos.messaging.domain.tenant.Tenant;
import org.apache.stratos.messaging.domain.topology.Cluster;
import org.apache.stratos.messaging.domain.topology.Service;
import org.apache.stratos.messaging.domain.topology.Topology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by imesh on 5/31/14.
 */
public class TenantUtil {
    public static List<Tenant> getTenants() {
        List<Tenant> tenants = new ArrayList<Tenant>();
        Topology topology = TopologyUtil.getTopology();

        Tenant tenant1 = new Tenant(-1234, "super.admin");
        tenants.add(tenant1);

        Tenant tenant2 = new Tenant(1, "foo.org");
        for(Service service : topology.getServices()) {
            Set<String> clusterIds = new HashSet<String>();
            for(Cluster cluster : service.getClusters()) {
                clusterIds.add(cluster.getClusterId());
            }

        }
        tenants.add(tenant2);

        return tenants;
    }
}
