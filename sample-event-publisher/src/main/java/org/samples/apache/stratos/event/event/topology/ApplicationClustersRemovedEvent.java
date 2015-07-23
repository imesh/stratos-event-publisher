package org.samples.apache.stratos.event.event.topology;/*
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

import org.samples.apache.stratos.event.domain.application.ClusterDataHolder;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "ApplicationClustersRemovedEvent")
public class ApplicationClustersRemovedEvent extends TopologyEvent implements SampleEventInterface {
    private Set<ClusterDataHolder> clusterData;
    private String appId;

    public ApplicationClustersRemovedEvent() {
    }

    public Set<ClusterDataHolder> getClusterData() {
        return clusterData;
    }

    public void setClusterData(
            Set<ClusterDataHolder> clusterData) {
        this.clusterData = clusterData;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public void process() {

        Set<org.apache.stratos.messaging.domain.application.ClusterDataHolder> clusterDataObj = new HashSet<>();
        for (ClusterDataHolder clusterDataHolder : clusterData) {
            org.apache.stratos.messaging.domain.application.ClusterDataHolder clusterDataHolder1 = new org.apache
                    .stratos.messaging.domain.application.ClusterDataHolder(clusterDataHolder.getServiceType(),
                    clusterDataHolder.getClusterId());
            clusterDataObj.add(clusterDataHolder1);
        }
        org.apache.stratos.messaging.event.topology.ApplicationClustersRemovedEvent applicationCreatedEvent = new org
                .apache.stratos.messaging.event.topology.ApplicationClustersRemovedEvent(clusterDataObj, appId);
        Utils.publishEvent(applicationCreatedEvent);
    }

}