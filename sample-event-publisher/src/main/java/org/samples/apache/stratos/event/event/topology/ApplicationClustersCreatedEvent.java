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

import org.samples.apache.stratos.event.domain.topology.Cluster;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ApplicationClustersCreatedEvent")
public class ApplicationClustersCreatedEvent extends TopologyEvent implements SampleEventInterface {
    private List<Cluster> clusterList;
    private String appId;

    public ApplicationClustersCreatedEvent() {
    }

    public List<Cluster> getClusterList() {
        return clusterList;
    }

    public void setClusterList(List<Cluster> clusterList) {
        this.clusterList = clusterList;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public void process() {
        List<org.apache.stratos.messaging.domain.topology.Cluster> clusterList1 = new ArrayList<>();
        for (Cluster cluster : clusterList) {
            clusterList1.add(new org.apache.stratos.messaging.domain.topology.Cluster(cluster.getServiceName(),
                    cluster.getClusterId(), cluster.getDeploymentPolicyName(), cluster.getAutoscalePolicyName(),
                    cluster.getAppId()));
        }
        org.apache.stratos.messaging.event.topology.ApplicationClustersCreatedEvent applicationClustersCreatedEvent =
                new org.apache.stratos.messaging.event.topology.ApplicationClustersCreatedEvent(clusterList1, appId);
        Utils.publishEvent(applicationClustersCreatedEvent);
    }
}