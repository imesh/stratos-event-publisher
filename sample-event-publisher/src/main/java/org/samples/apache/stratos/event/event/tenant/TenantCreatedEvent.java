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

package org.samples.apache.stratos.event.event.tenant;

import org.samples.apache.stratos.event.domain.tenant.Tenant;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This event is fired when a tenant is created.
 */
@XmlRootElement(name = "TenantCreatedEvent")
public class TenantCreatedEvent extends TenantEvent implements SampleEventInterface {
    private Tenant tenant;

    public TenantCreatedEvent() {

    }

    public TenantCreatedEvent(Tenant tenant) {
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public String toString() {
        return String.format("[tenant] %s", tenant);
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.tenant.Tenant tenant1 = new org.apache.stratos.messaging.domain.tenant
                .Tenant(tenant.getTenantId(), tenant.getTenantDomain());
        org.apache.stratos.messaging.event.tenant.TenantCreatedEvent
                tenantCreatedEvent = new org.apache.stratos.messaging.event.tenant.TenantCreatedEvent(tenant1);
        Utils.publishEvent(tenantCreatedEvent);
    }
}
