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

package org.samples.apache.stratos.event.model.tenant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.Set;

/**
 * Model class for TenantSubscribedEvent
 */
@XmlRootElement(name = "TenantSubscribedEvent")
public class TenantSubscribedEvent extends TenantEvent {

    private static final Log log = LogFactory.getLog(TenantSubscribedEvent.class);
    private int tenantId;
    private String serviceName;
    private Set<String> clusterIds;

    public TenantSubscribedEvent() {

    }

    public TenantSubscribedEvent(int tenantId, String serviceName, Set<String> clusterIds) {
        this.tenantId = tenantId;
        this.serviceName = serviceName;
        this.clusterIds = clusterIds;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Set<String> getClusterIds() {
        return Collections.unmodifiableSet(clusterIds);
    }

    public void setClusterIds(Set<String> clusterIds) {
        this.clusterIds = clusterIds;
    }

    @Override
    public String toString() {
        return String.format("[tenant-id] %s , [service] %s , [cluster-id-set] %s",
                tenantId, serviceName, clusterIds);
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.tenant.TenantSubscribedEvent
                tenantSubscribedEvent = new org.apache.stratos.messaging.event.tenant.TenantSubscribedEvent(tenantId, serviceName, clusterIds);

        tenantPublisher.publish(tenantSubscribedEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
