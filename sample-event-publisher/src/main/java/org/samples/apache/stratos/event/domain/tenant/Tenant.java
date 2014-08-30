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

package org.samples.apache.stratos.event.domain.tenant;

import org.apache.stratos.messaging.domain.tenant.Subscription;

import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Tenant definition.
 */

@XmlType(name = "Tenant")
public class Tenant {

    private int tenantId;
    private String tenantDomain;
    // Map<ServiceName, Subscription>
    private Map<String, Subscription> serviceNameSubscriptionMap;

    public Tenant() {
    }

    public Tenant(int tenantId, String tenantDomain) {
        this.tenantId = tenantId;
        this.tenantDomain = tenantDomain;
        this.serviceNameSubscriptionMap = new HashMap<String, Subscription>();
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantDomain() {
        return tenantDomain;
    }

    public void setTenantDomain(String tenantDomain) {
        this.tenantDomain = tenantDomain;
    }

    public Subscription getSubscription(String serviceName) {
        if (serviceNameSubscriptionMap.containsKey(serviceName)) {
            return serviceNameSubscriptionMap.get(serviceName);
        }
        return null;
    }

    public Collection<Subscription> getSubscriptions() {
        return serviceNameSubscriptionMap.values();
    }

    public boolean isSubscribed(String serviceName) {
        return serviceNameSubscriptionMap.containsKey(serviceName);
    }

    public void addSubscription(Subscription subscription) {
        serviceNameSubscriptionMap.put(subscription.getServiceName(), subscription);
    }

    public void removeSubscription(String serviceName) {
        serviceNameSubscriptionMap.remove(serviceName);
    }

    public Map<String, Subscription> getServiceNameSubscriptionMap() {
        return serviceNameSubscriptionMap;
    }

    public void setServiceNameSubscriptionMap(Map<String, Subscription> serviceNameSubscriptionMap) {
        this.serviceNameSubscriptionMap = serviceNameSubscriptionMap;
    }
}
