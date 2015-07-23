/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Cluster activated event will be sent by Autoscaler
 */
@XmlRootElement(name = "ClusterInstanceTerminatedEvent")
public class ClusterInstanceTerminatedEvent extends TopologyEvent implements SampleEventInterface {
    private String serviceName;
    private String clusterId;
    private String appId;
    private String instanceId;

    public ClusterInstanceTerminatedEvent() {
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.topology.ClusterInstanceTerminatedEvent clusterInstanceTerminatedEvent =
                new org.apache.stratos.messaging.event.topology.ClusterInstanceTerminatedEvent(appId, serviceName,
                        clusterId, instanceId);
        Utils.publishEvent(clusterInstanceTerminatedEvent);
    }
}
