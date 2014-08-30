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
package org.samples.apache.stratos.event.model.instance.notifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for InstanceCleanupClusterEvent
 */

@XmlRootElement(name = "InstanceCleanupClusterEvent")
public class InstanceCleanupClusterEvent extends InstanceNotifierEvent {
    private static final Log log = LogFactory.getLog(InstanceCleanupClusterEvent.class);
    private String clusterId;

    public InstanceCleanupClusterEvent() {

    }

    public InstanceCleanupClusterEvent(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public String toString() {
        return String.format("[cluster] %s", clusterId);
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupClusterEvent
                instanceCleanupClusterEvent = new org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupClusterEvent(clusterId);

        instanceNotifierPublisher.publish(instanceCleanupClusterEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }

}
