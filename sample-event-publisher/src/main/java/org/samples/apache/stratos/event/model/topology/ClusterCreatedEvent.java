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

package org.samples.apache.stratos.event.model.topology;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.samples.apache.stratos.event.domain.topology.Cluster;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for ClusterCreatedEvent.
 */

@XmlRootElement(name = "ClusterCreatedEvent")
public class ClusterCreatedEvent extends TopologyEvent {

	private String serviceName;
	private String clusterId;
    private Cluster cluster;

    private static final Log log = LogFactory.getLog(ClusterCreatedEvent.class);

    public ClusterCreatedEvent(){

    }

    public ClusterCreatedEvent(String serviceName, String clusterId, Cluster cluster) {
        this.serviceName = serviceName;
        this.clusterId = clusterId;
        this.cluster = cluster;
    }

    public Cluster getCluster() {
        return cluster;
    }
    
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public String getClusterId() {
        return clusterId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public String toString() {
        return String.format("[service] %s , [cluster-id] %s , [cluster] %s",
                serviceName, clusterId, cluster);
    }

    @Override
    public void process(){

        org.apache.stratos.messaging.domain.topology.Cluster
                cluster1 = new org.apache.stratos.messaging.domain.topology.Cluster(
                serviceName, clusterId, cluster.getDeploymentPolicyName(), cluster.getAutoscalePolicyName());
        org.apache.stratos.messaging.event.topology.ClusterCreatedEvent
                clusterCreatedEvent = new org.apache.stratos.messaging.event.topology.ClusterCreatedEvent(serviceName, clusterId, cluster1);

        topologyPublisher.publish(clusterCreatedEvent);
        if (log.isInfoEnabled()){
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
