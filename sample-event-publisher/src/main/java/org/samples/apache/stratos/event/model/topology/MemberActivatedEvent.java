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
import org.samples.apache.stratos.event.domain.topology.Port;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for MemberActivated Event
 */

@XmlRootElement(name = "MemberActivatedEvent")
public class MemberActivatedEvent extends TopologyEvent {
    private static final Log log = LogFactory.getLog(MemberActivatedEvent.class);
    private String serviceName;
    private String clusterId;
    private String networkPartitionId;
    private String partitionId;
    private String memberId;
    private Map<Integer, Port> portMap;
    private String memberIp;

    public MemberActivatedEvent() {

    }

    public MemberActivatedEvent(String serviceName, String clusterId, String networkPartitionId, String partitionId, String memberId) {
        this.serviceName = serviceName;
        this.clusterId = clusterId;
        this.networkPartitionId = networkPartitionId;
        this.partitionId = partitionId;
        this.memberId = memberId;
        this.portMap = new HashMap<Integer, Port>();
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

    public String getNetworkPartitionId() {
        return networkPartitionId;
    }

    public void setNetworkPartitionId(String networkPartitionId) {
        this.networkPartitionId = networkPartitionId;
    }

    public String getPartitionId() {
        return this.partitionId;
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

    public Collection<Port> getPorts() {
        return Collections.unmodifiableCollection(portMap.values());
    }

    public Port getPort(int proxy) {
        if (portMap.containsKey(proxy)) {
            return portMap.get(proxy);
        }
        return null;
    }

    public void addPort(Port port) {
        this.portMap.put(port.getProxy(), port);
    }

    public void addPorts(Collection<Port> ports) {
        for (Port port : ports) {
            addPort(port);
        }
    }

    public void removePort(Port port) {
        this.portMap.remove(port.getProxy());
    }

    public boolean portExists(Port port) {
        return this.portMap.containsKey(port.getProxy());
    }

    public String getMemberIp() {
        return memberIp;
    }

    public void setMemberIp(String memberIp) {
        this.memberIp = memberIp;
    }

    public Map<Integer, Port> getPortMap() {
        return portMap;
    }

    public void setPortMap(Map<Integer, Port> portMap) {
        this.portMap = portMap;
    }

    @Override
    public String toString() {
        return String.format("[service] %s , [cluster-id] %s , [partition] %s , [network-partition] %s , [member] %s ",
                serviceName, clusterId, partitionId, networkPartitionId, memberId);
    }

    @Override
    public void process() {

        org.apache.stratos.messaging.event.topology.MemberActivatedEvent
                memberActivatedEvent = new org.apache.stratos.messaging.event.topology.MemberActivatedEvent(
                serviceName, clusterId, networkPartitionId, partitionId, memberId);
        memberActivatedEvent.setMemberIp(memberIp);

        if (portMap != null)
            for (Map.Entry<Integer, Port> portEntry : portMap.entrySet()) {
                org.apache.stratos.messaging.domain.topology.Port port = new org.apache.stratos.messaging.domain.topology.Port(
                        portEntry.getValue().getProtocol(), portEntry.getValue().getValue(), portEntry.getValue().getValue());
                memberActivatedEvent.addPort(port);
            }

        topologyPublisher.publish(memberActivatedEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
