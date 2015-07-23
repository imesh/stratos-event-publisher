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

package org.samples.apache.stratos.event.event.cluster.status;

import org.apache.stratos.messaging.event.Event;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This event is fired by cartridge agent when it has started the server and
 * applications are ready to serve the incoming requests.
 */
@XmlRootElement(name = "ClusterStatusClusterCreatedEvent")
public class ClusterStatusClusterCreatedEvent extends Event implements SampleEventInterface {
    private String serviceName;
    private String clusterId;
    private String appId;
    private String autosScalePolicyName;
    private String deploymentPolicyName;
    private List<String> hostNames;
    private String tenantRange;

    public ClusterStatusClusterCreatedEvent() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getClusterId() {
        return clusterId;
    }

    public String getAppId() {
        return appId;
    }

    public String getAutosScalePolicyName() {
        return autosScalePolicyName;
    }

    public String getDeploymentPolicyName() {
        return deploymentPolicyName;
    }

    public List<String> getHostNames() {
        return hostNames;
    }

    public void setHostNames(List<String> hostNames) {
        this.hostNames = hostNames;
    }

    public String getTenantRange() {
        return tenantRange;
    }

    public void setTenantRange(String tenantRange) {
        this.tenantRange = tenantRange;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAutosScalePolicyName(String autosScalePolicyName) {
        this.autosScalePolicyName = autosScalePolicyName;
    }

    public void setDeploymentPolicyName(String deploymentPolicyName) {
        this.deploymentPolicyName = deploymentPolicyName;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.cluster.status.ClusterStatusClusterCreatedEvent
                clusterStatusClusterCreatedEvent = new org.apache.stratos.messaging.event.cluster.status
                .ClusterStatusClusterCreatedEvent(appId, serviceName, clusterId, autosScalePolicyName,
                deploymentPolicyName);
        Utils.publishEvent(clusterStatusClusterCreatedEvent);
    }
}
