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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for ClusterRemovedEvent.
 */

@XmlRootElement(name = "ClusterRemovedEvent")
public class ClusterRemovedEvent extends TopologyEvent {

    private String serviceName;
    private String clusterId;
    private String deploymentPolicy;
    private boolean isLbCluster;

    private static final Log log = LogFactory.getLog(ClusterRemovedEvent.class);

    public ClusterRemovedEvent(){

    }

    public ClusterRemovedEvent(String serviceName, String clusterId, String deploymentPolicy, boolean isLbCluster) {
        this.serviceName = serviceName;
        this.clusterId = clusterId;
        this.isLbCluster = isLbCluster;
        this.deploymentPolicy = deploymentPolicy;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getClusterId() {
        return clusterId;
    }

    public boolean isLbCluster() {
        return isLbCluster;
    }

	public String getDeploymentPolicy() {
		return deploymentPolicy;
	}

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setDeploymentPolicy(String deploymentPolicy) {
        this.deploymentPolicy = deploymentPolicy;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public void setLbCluster(boolean isLbCluster) {
        this.isLbCluster = isLbCluster;
    }

    @Override
    public String toString() {
        return String.format("[service] %s , [cluster-id] %s , [deployment-policy] %s , [is-LB-cluster] %s",
                serviceName, clusterId, deploymentPolicy, isLbCluster);
    }

    @Override
    public void process(){

        org.apache.stratos.messaging.event.topology.ClusterRemovedEvent
                clusterRemovedEvent = new org.apache.stratos.messaging.event.topology.ClusterRemovedEvent(
                serviceName, clusterId, deploymentPolicy, isLbCluster);

        topologyPublisher.publish(clusterRemovedEvent);
        if (log.isInfoEnabled()){
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
