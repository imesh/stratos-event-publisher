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


import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.PropertiesAdaptor;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Properties;


/**
 * This event is fired by Cloud Controller once the member initialization process is done.
 */
@XmlRootElement(name = "MemberInitializedEvent")
public class MemberInitializedEvent extends TopologyEvent implements SampleEventInterface {
    private String serviceName;
    private String clusterId;
    private String clusterInstanceId;
    private String networkPartitionId;
    private String partitionId;
    private String memberId;
    private String instanceId;
    private List<String> memberPublicIPs;
    private String defaultPublicIP;
    private List<String> memberPrivateIPs;
    private String defaultPrivateIP;
    private Properties properties;

    public MemberInitializedEvent() {
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

    public String getClusterInstanceId() {
        return clusterInstanceId;
    }

    public void setClusterInstanceId(String clusterInstanceId) {
        this.clusterInstanceId = clusterInstanceId;
    }

    public String getNetworkPartitionId() {
        return networkPartitionId;
    }

    public void setNetworkPartitionId(String networkPartitionId) {
        this.networkPartitionId = networkPartitionId;
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @XmlElement
    @XmlJavaTypeAdapter(PropertiesAdaptor.class)
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.topology.MemberInitializedEvent memberInitializedEvent = new org.apache
                .stratos.messaging.event.topology.MemberInitializedEvent(serviceName, clusterId, clusterInstanceId,
                memberId, networkPartitionId, partitionId, instanceId);
        Utils.publishEvent(memberInitializedEvent);
    }
}
