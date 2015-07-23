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

package org.samples.apache.stratos.event.event.health.stat;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Model class for MemberFaultEvent
 */

@XmlRootElement(name = "MemberFaultEvent")
public class MemberFaultEvent extends HealthStatEvent {
    private String clusterId;
    private String clusterInstanceId;
    private String memberId;
    private String partitionId;
    private String networkPartitionId;
    private float value;

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

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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

    @Override
    public void process() {
        Map<String, Object> memberFaultEventMap = new HashMap<String, Object>();
        Map<String, Object> memberFaultEventMessageMap = new HashMap<String, Object>();

        org.apache.stratos.messaging.event.health.stat.MemberFaultEvent memberFaultEvent =
                new org.apache.stratos.messaging.event.health.stat.MemberFaultEvent(clusterId, clusterInstanceId,
                        memberId, partitionId, networkPartitionId, 0);

        memberFaultEventMessageMap.put("message", memberFaultEvent);
        memberFaultEventMap.put(memberFaultEvent.getClass().getName(), memberFaultEventMessageMap);
        healthStatPublisher.publish(memberFaultEventMap, true);
            logger.info(memberFaultEvent.getClass().toString() + " Event published: " + memberFaultEventMap);
    }
}
