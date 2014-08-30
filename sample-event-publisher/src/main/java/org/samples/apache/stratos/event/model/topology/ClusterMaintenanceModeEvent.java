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
import org.samples.apache.stratos.event.domain.topology.ClusterStatus;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ClusterMaintenanceModeEvent")
public class ClusterMaintenanceModeEvent extends TopologyEvent {

    private static final Log log = LogFactory.getLog(ClusterMaintenanceModeEvent.class);
    private String serviceName;
    private String clusterId;
    private ClusterStatus status;

    public ClusterMaintenanceModeEvent() {

    }

    public ClusterMaintenanceModeEvent(String serviceName, String clusterId) {
        this.serviceName = serviceName;
        this.clusterId = clusterId;
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

    public ClusterStatus getStatus() {
        return status;
    }

    public void setStatus(ClusterStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("[service] %s , [cluster-id] %s , [cluster-status] %s",
                serviceName, clusterId, status);
    }

    @Override
    public void process() {

        org.apache.stratos.messaging.event.topology.ClusterMaintenanceModeEvent
                clusterMaintenanceModeEvent = new org.apache.stratos.messaging.event.topology.ClusterMaintenanceModeEvent(serviceName, clusterId);

        org.apache.stratos.messaging.domain.topology.ClusterStatus
                clusterStatus = org.apache.stratos.messaging.domain.topology.ClusterStatus.valueOf(status.name());
        clusterMaintenanceModeEvent.setStatus(clusterStatus);

        topologyPublisher.publish(clusterMaintenanceModeEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
