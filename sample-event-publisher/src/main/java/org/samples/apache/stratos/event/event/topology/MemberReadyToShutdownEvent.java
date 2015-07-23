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

import org.samples.apache.stratos.event.domain.topology.MemberStatus;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.PropertiesAdaptor;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Properties;

/**
 * Model class for MemberReadyToShutdownEvent
 */

@XmlRootElement(name = "MemberReadyToShutdownEvent")
public class MemberReadyToShutdownEvent extends TopologyEvent implements SampleEventInterface {
    private String serviceName;
    private String clusterId;
    private String clusterInstanceId;
    private String memberId;
    private String networkPartitionId;
    private String partitionId;
    private MemberStatus status;
    private Properties properties;
    private String groupId;

    public MemberReadyToShutdownEvent() {
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    @XmlElement
    @XmlJavaTypeAdapter(PropertiesAdaptor.class)
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
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

    public String getClusterInstanceId() {
        return clusterInstanceId;
    }

    public void setClusterInstanceId(String clusterInstanceId) {
        this.clusterInstanceId = clusterInstanceId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.topology.MemberReadyToShutdownEvent
                memberReadyToShutdownEvent = new org.apache.stratos.messaging.event.topology.MemberReadyToShutdownEvent(
                serviceName, clusterId, clusterInstanceId, memberId, networkPartitionId, partitionId);
        org.apache.stratos.messaging.domain.topology.MemberStatus
                memberStatus = org.apache.stratos.messaging.domain.topology.MemberStatus.valueOf(status.name());
        memberReadyToShutdownEvent.setStatus(memberStatus);
        memberReadyToShutdownEvent.setProperties(properties);
        Utils.publishEvent(memberReadyToShutdownEvent);
    }
}
