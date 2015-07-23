/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.samples.apache.stratos.event.event.topology;

import org.samples.apache.stratos.event.domain.instance.ClusterInstance;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Cluster activated event will be sent by Autoscaler
 */
@XmlRootElement(name = "ClusterInstanceCreatedEvent")
public class ClusterInstanceCreatedEvent extends TopologyEvent implements SampleEventInterface {
    private String serviceName;
    private String clusterId;
    private String partitionId;
    private String networkPartitionId;
    private ClusterInstance clusterInstance;

    public ClusterInstanceCreatedEvent() {

    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getNetworkPartitionId() {
        return networkPartitionId;
    }

    public void setNetworkPartitionId(String networkPartitionId) {
        this.networkPartitionId = networkPartitionId;
    }

    public ClusterInstance getClusterInstance() {
        return clusterInstance;
    }

    public void setClusterInstance(ClusterInstance clusterInstance) {
        this.clusterInstance = clusterInstance;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.instance.ClusterInstance clusterInstance1 = new org.apache.stratos
                .messaging.domain.instance.ClusterInstance(clusterInstance.getAlias(), clusterId, clusterInstance
                .getInstanceId());
        org.apache.stratos.messaging.event.topology.ClusterInstanceCreatedEvent clusterInstanceCreatedEvent = new
                org.apache.stratos.messaging.event.topology.ClusterInstanceCreatedEvent(serviceName, clusterId,
                clusterInstance1);
        clusterInstanceCreatedEvent.setNetworkPartitionId(networkPartitionId);
        clusterInstanceCreatedEvent.setPartitionId(partitionId);
        Utils.publishEvent(clusterInstanceCreatedEvent);
    }
}
