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

package org.samples.apache.stratos.event.util;

import org.samples.apache.stratos.event.domain.tenant.Subscription;
import org.samples.apache.stratos.event.domain.tenant.SubscriptionDomain;
import org.samples.apache.stratos.event.domain.tenant.Tenant;
import org.samples.apache.stratos.event.domain.topology.*;
import org.samples.apache.stratos.event.model.Wrapper;
import org.samples.apache.stratos.event.model.action.SleepEvent;
import org.samples.apache.stratos.event.model.health.stat.MemberFaultEvent;
import org.samples.apache.stratos.event.model.instance.notifier.ArtifactUpdatedEvent;
import org.samples.apache.stratos.event.model.instance.notifier.InstanceCleanupClusterEvent;
import org.samples.apache.stratos.event.model.instance.notifier.InstanceCleanupMemberEvent;
import org.samples.apache.stratos.event.model.instance.status.InstanceActivatedEvent;
import org.samples.apache.stratos.event.model.instance.status.InstanceMaintenanceModeEvent;
import org.samples.apache.stratos.event.model.instance.status.InstanceReadyToShutdownEvent;
import org.samples.apache.stratos.event.model.instance.status.InstanceStartedEvent;
import org.samples.apache.stratos.event.model.tenant.*;
import org.samples.apache.stratos.event.model.topology.*;

public class SampleConstants {
    public static final String JNDI_PROPERTIES_SYSTEM_PROPERTY = "jndi.properties.dir";
    public static final String EVENT_USER_DATA_PATH = "event.user.data.path";
    public static final int THREAD_WAIT_TIME = 20000;
    public static final Class eventClassArray[] = {
            Wrapper.class,

            MemberFaultEvent.class,


            ArtifactUpdatedEvent.class,
            InstanceCleanupClusterEvent.class,
            InstanceCleanupMemberEvent.class,


            InstanceActivatedEvent.class,
            InstanceMaintenanceModeEvent.class,
            InstanceReadyToShutdownEvent.class,
            InstanceStartedEvent.class,


            CompleteTenantEvent.class,
            SubscriptionDomainAddedEvent.class,
            SubscriptionDomainRemovedEvent.class,
            SubscriptionDomainsAddedEvent.class,
            SubscriptionDomainsRemovedEvent.class,
            TenantCreatedEvent.class,
            TenantRemovedEvent.class,
            TenantSubscribedEvent.class,
            TenantUnSubscribedEvent.class,
            TenantUpdatedEvent.class,


            ClusterCreatedEvent.class,
            ClusterMaintenanceModeEvent.class,
            ClusterRemovedEvent.class,
            CompleteTopologyEvent.class,
            InstanceSpawnedEvent.class,
            MemberActivatedEvent.class,
            MemberMaintenanceModeEvent.class,
            MemberReadyToShutdownEvent.class,
            MemberStartedEvent.class,
            MemberSuspendedEvent.class,
            MemberTerminatedEvent.class,
            ServiceCreatedEvent.class,
            ServiceRemovedEvent.class,


            Subscription.class,
            SubscriptionDomain.class,
            Tenant.class,

            Cluster.class,
            ClusterStatus.class,
            Member.class,
            MemberStatus.class,
            Port.class,
            Scope.class,
            Service.class,
            ServiceType.class,
            Topology.class,

            SleepEvent.class,
    };
}
