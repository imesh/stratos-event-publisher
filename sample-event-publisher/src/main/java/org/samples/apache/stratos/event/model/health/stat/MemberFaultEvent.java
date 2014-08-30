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

package org.samples.apache.stratos.event.model.health.stat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.util.Constants;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Model class for MemberFaultEvent
 */

@XmlRootElement(name = "MemberFaultEvent")
public class MemberFaultEvent extends HealthStatEvent {
    private java.lang.String clusterId;
    private java.lang.String memberId;
    private java.lang.String partitionId;
    private float value;

    private static final Log log = LogFactory.getLog(MemberFaultEvent.class);

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

    @Override
    public String toString() {
        return String.format("[cluster-id] %s , [partition] %s , [member] %s ",
                clusterId, partitionId, memberId);
    }

    @Override
    public void process() {
        Map<String, Object> memberFaultEventMap = new HashMap<String, Object>();
        Map<String, Object> memberFaultEventMessageMap = new HashMap<String, Object>();

        org.apache.stratos.messaging.event.health.stat.MemberFaultEvent memberFaultEvent =
                new org.apache.stratos.messaging.event.health.stat.MemberFaultEvent(clusterId,
                        memberId, partitionId, 0);

        memberFaultEventMessageMap.put("message", memberFaultEvent);
        memberFaultEventMap.put(memberFaultEvent.getClass().getName(), memberFaultEventMessageMap);

        Properties headers = new Properties();
        headers.put(Constants.EVENT_CLASS_NAME, memberFaultEvent.getClass().getName());

        healthStatPublisher.publish(memberFaultEventMap, headers, true);
        if (log.isInfoEnabled()) {
            log.info(memberFaultEvent.getClass().toString() + " Event published: " + memberFaultEventMap);
        }
    }
}
