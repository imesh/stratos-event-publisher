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
import java.util.HashSet;
import java.util.Set;

/**
 * Model class for SubscriptionDomainsRemovedEvent
 */
@XmlRootElement(name = "SubscriptionDomainsRemovedEvent")
public class SubscriptionDomainsRemovedEvent extends TenantEvent {

    private static final Log log = LogFactory.getLog(SubscriptionDomainsRemovedEvent.class);
    private int tenantId;
    private String serviceName;
    private Set<String> clusterIds;
    private Set<String> domains;

    public SubscriptionDomainsRemovedEvent() {

    }

    public SubscriptionDomainsRemovedEvent(int tenantId, String serviceName, Set<String> clusterIds, Set<String> domains) {
        this.tenantId = tenantId;
        this.serviceName = serviceName;
        this.clusterIds = clusterIds;
        this.domains = (domains != null) ? domains : new HashSet<String>();
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

    public Set<String> getDomains() {
        return Collections.unmodifiableSet(domains);
    }

    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }

    @Override
    public String toString() {
        return String.format("[tenant] %d , [service] %s , [cluster-set] %s , [domains] %s",
                tenantId, serviceName, clusterIds, domains);
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.event.tenant.SubscriptionDomainsRemovedEvent
                subscriptionDomainsRemovedEvent = new org.apache.stratos.messaging.event.tenant.SubscriptionDomainsRemovedEvent(
                tenantId, serviceName, clusterIds, domains);

        tenantPublisher.publish(subscriptionDomainsRemovedEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
