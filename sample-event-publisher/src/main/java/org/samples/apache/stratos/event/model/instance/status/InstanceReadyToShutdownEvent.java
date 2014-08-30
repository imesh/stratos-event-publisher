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
package org.samples.apache.stratos.event.model.instance.status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for InstanceReadyToShutdownEvent.
 */
@XmlRootElement(name = "InstanceReadyToShutdownEvent")
public class InstanceReadyToShutdownEvent extends InstanceStatusEvent {
    private static final Log log = LogFactory.getLog(InstanceReadyToShutdownEvent.class);
    private String serviceName;
    private String clusterId;
    private String networkPartitionId;
    private String partitionId;
    private String memberId;

    public InstanceReadyToShutdownEvent() {

    }

    public InstanceReadyToShutdownEvent(String serviceName, String clusterId,
                                        String networkPartitionId, String partitionId, String memberId) {
        this.serviceName = serviceName;
        this.clusterId = clusterId;
        this.networkPartitionId = networkPartitionId;
        this.partitionId = partitionId;
        this.memberId = memberId;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getNetworkPartitionId() {
        return networkPartitionId;
    }

    public void setNetworkPartitionId(String networkPartitionId) {
        this.networkPartitionId = networkPartitionId;
    }

    @Override
    public String toString() {
        return String.format("[cluster] %s , [service] %s , [network-partition] %s , [partition] %s , [member] %s",
                clusterId, serviceName, networkPartitionId, partitionId, memberId);
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.instance.status.InstanceReadyToShutdownEvent
                instanceReadyToShutdownEvent = new org.apache.stratos.messaging.event.instance.status.InstanceReadyToShutdownEvent(
                serviceName, clusterId, networkPartitionId, partitionId, memberId);

        instanceStatusPublisher.publish(instanceReadyToShutdownEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
