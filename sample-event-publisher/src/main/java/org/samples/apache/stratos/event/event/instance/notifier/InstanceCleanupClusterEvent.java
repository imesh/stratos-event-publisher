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
package org.samples.apache.stratos.event.event.instance.notifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for InstanceCleanupClusterEvent
 */

@XmlRootElement(name = "InstanceCleanupClusterEvent")
public class InstanceCleanupClusterEvent extends InstanceNotifierEvent implements SampleEventInterface {
    private static final Log log = LogFactory.getLog(InstanceCleanupClusterEvent.class);
    private String clusterId;
    private String clusterInstanceId;

    public InstanceCleanupClusterEvent() {

    }

    public InstanceCleanupClusterEvent(String clusterId, String clusterInstanceId) {
        this.clusterId = clusterId;
        this.clusterInstanceId = clusterInstanceId;
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

    @Override
    public void process() {
        org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupClusterEvent
                instanceCleanupClusterEvent = new org.apache.stratos.messaging.event.instance.notifier
                .InstanceCleanupClusterEvent(clusterId, clusterInstanceId);

        Utils.publishEvent(instanceCleanupClusterEvent);
    }

}
