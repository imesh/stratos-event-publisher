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
import org.apache.stratos.messaging.event.tenant.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for TenantRemovedEvent
 */
@XmlRootElement(name = "TenantRemovedEvent")
public class TenantRemovedEvent extends TenantEvent {

    private int tenantId;
    private static final Log log = LogFactory.getLog(TenantRemovedEvent.class);

    public TenantRemovedEvent(){

    }

    public TenantRemovedEvent(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return String.format("[tenant-id] %s",  tenantId);
    }

    @Override
    public void process(){
        org.apache.stratos.messaging.event.tenant.TenantRemovedEvent
                tenantRemovedEvent = new org.apache.stratos.messaging.event.tenant.TenantRemovedEvent(tenantId);

        tenantPublisher.publish(tenantRemovedEvent);
        if (log.isInfoEnabled()){
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
