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
import org.samples.apache.stratos.event.domain.topology.MemberStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Properties;

@XmlRootElement(name = "MemberMaintenanceModeEvent")
public class MemberMaintenanceModeEvent extends TopologyEvent {
    private String serviceName;
    private String clusterId;
    private String networkPartitionId;
    private String partitionId;
    private String memberId;
    private MemberStatus status;
    private Properties properties;

    private static final Log log = LogFactory.getLog(MemberMaintenanceModeEvent.class);

    public MemberMaintenanceModeEvent() {

    }

    public MemberMaintenanceModeEvent(String serviceName, String clusterId,
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

    public String getClusterId() {
        return clusterId;
    }

    public String getMemberId() {
        return memberId;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public String getNetworkPartitionId() {
        return networkPartitionId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public void setNetworkPartitionId(String networkPartitionId) {
        this.networkPartitionId = networkPartitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return String.format("[service] %s , [cluster-id] %s , [partition] %s , [network-partition] %s , [member] %s ",
                serviceName, clusterId, partitionId, networkPartitionId, memberId);
    }

    @Override
    public void process() {

        org.apache.stratos.messaging.event.topology.MemberMaintenanceModeEvent
                memberMaintenanceModeEvent = new org.apache.stratos.messaging.event.topology.MemberMaintenanceModeEvent(
                serviceName, clusterId, networkPartitionId, partitionId, memberId);

        org.apache.stratos.messaging.domain.topology.MemberStatus memberStatus = org.apache.stratos.messaging.domain.topology.MemberStatus.valueOf(status.name());

        memberMaintenanceModeEvent.setStatus(memberStatus);
        memberMaintenanceModeEvent.setProperties(properties);

        topologyPublisher.publish(memberMaintenanceModeEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
