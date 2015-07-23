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

package org.samples.apache.stratos.event.event.topology;


import org.samples.apache.stratos.event.domain.topology.Cluster;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for ClusterCreatedEvent.
 */

@XmlRootElement(name = "ClusterCreatedEvent")
public class ClusterCreatedEvent extends TopologyEvent implements SampleEventInterface {
    private Cluster cluster;

    public ClusterCreatedEvent() {
    }

    public ClusterCreatedEvent(Cluster cluster) {
        this.cluster = cluster;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.topology.Cluster
                clusterObj = new org.apache.stratos.messaging.domain.topology.Cluster(
                cluster.getServiceName(), cluster.getClusterId(), cluster.getDeploymentPolicyName(),
                cluster.getAutoscalePolicyName(), cluster.getAppId());

        org.apache.stratos.messaging.event.topology.ClusterCreatedEvent
                clusterCreatedEvent = new org.apache.stratos.messaging.event.topology.ClusterCreatedEvent(clusterObj);
        Utils.publishEvent(clusterCreatedEvent);
    }
}
