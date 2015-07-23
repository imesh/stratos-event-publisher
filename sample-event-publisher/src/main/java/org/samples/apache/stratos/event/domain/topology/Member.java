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

package org.samples.apache.stratos.event.domain.topology;


import org.samples.apache.stratos.event.domain.LoadBalancingIPType;
import org.samples.apache.stratos.event.domain.topology.lifecycle.LifeCycleStateManager;
import org.samples.apache.stratos.event.util.PropertiesAdaptor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * Defines a member node in a cluster.
 * Key: serviceName, clusterId, memberId
 */

@XmlType(name = "Member")
public class Member {
    private String serviceName;
    private String clusterId;
    private String networkPartitionId;
    private String partitionId;
    private String memberId;
    private String clusterInstanceId;
    private long initTime;
    private Map<Integer, Port> portMap;
    private List<String> memberPublicIPs;
    private String defaultPublicIP;
    private List<String> memberPrivateIPs;
    private String defaultPrivateIP;
    private Properties properties;
    private String lbClusterId;
    private LifeCycleStateManager<MemberStatus> memberStateManager;
    private LoadBalancingIPType loadBalancingIPType;

    public Member() {

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

    public void removePort(org.apache.stratos.messaging.domain.topology.Port port) {
        this.portMap.remove(port.getProxy());
    }

    public boolean portExists(Port port) {
        return this.portMap.containsKey(port.getProxy());
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

    public String getLbClusterId() {
        return lbClusterId;
    }

    public void setLbClusterId(String lbClusterId) {
        this.lbClusterId = lbClusterId;
    }

    public String getNetworkPartitionId() {
        return networkPartitionId;
    }

    public void setNetworkPartitionId(String networkPartitionId) {
        this.networkPartitionId = networkPartitionId;
    }

    public Map<Integer, Port> getPortMap() {
        return portMap;
    }

    public void setPortMap(Map<Integer, Port> portMap) {
        this.portMap = portMap;
    }

    public String getClusterInstanceId() {
        return clusterInstanceId;
    }

    public void setClusterInstanceId(String clusterInstanceId) {
        this.clusterInstanceId = clusterInstanceId;
    }

    public long getInitTime() {
        return initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    public List<String> getMemberPublicIPs() {
        return memberPublicIPs;
    }

    public void setMemberPublicIPs(List<String> memberPublicIPs) {
        this.memberPublicIPs = memberPublicIPs;
    }

    public String getDefaultPublicIP() {
        return defaultPublicIP;
    }

    public void setDefaultPublicIP(String defaultPublicIP) {
        this.defaultPublicIP = defaultPublicIP;
    }

    public List<String> getMemberPrivateIPs() {
        return memberPrivateIPs;
    }

    public void setMemberPrivateIPs(List<String> memberPrivateIPs) {
        this.memberPrivateIPs = memberPrivateIPs;
    }

    public String getDefaultPrivateIP() {
        return defaultPrivateIP;
    }

    public void setDefaultPrivateIP(String defaultPrivateIP) {
        this.defaultPrivateIP = defaultPrivateIP;
    }

    public LifeCycleStateManager<MemberStatus> getMemberStateManager() {
        return memberStateManager;
    }

    public void setMemberStateManager(
            LifeCycleStateManager<MemberStatus> memberStateManager) {
        this.memberStateManager = memberStateManager;
    }

    public LoadBalancingIPType getLoadBalancingIPType() {
        return loadBalancingIPType;
    }

    public void setLoadBalancingIPType(LoadBalancingIPType loadBalancingIPType) {
        this.loadBalancingIPType = loadBalancingIPType;
    }
}

