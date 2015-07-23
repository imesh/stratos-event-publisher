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

import org.samples.apache.stratos.event.domain.LoadBalancingIPType;
import org.samples.apache.stratos.event.domain.application.*;
import org.samples.apache.stratos.event.domain.application.signup.ApplicationSignUp;
import org.samples.apache.stratos.event.domain.application.signup.ArtifactRepository;
import org.samples.apache.stratos.event.domain.application.signup.DomainMapping;
import org.samples.apache.stratos.event.domain.instance.ApplicationInstance;
import org.samples.apache.stratos.event.domain.instance.ClusterInstance;
import org.samples.apache.stratos.event.domain.instance.GroupInstance;
import org.samples.apache.stratos.event.domain.instance.Instance;
import org.samples.apache.stratos.event.domain.tenant.SubscriptionDomain;
import org.samples.apache.stratos.event.domain.tenant.Tenant;
import org.samples.apache.stratos.event.domain.topology.*;
import org.samples.apache.stratos.event.domain.topology.lifecycle.LifeCycleState;
import org.samples.apache.stratos.event.domain.topology.lifecycle.LifeCycleStateManager;
import org.samples.apache.stratos.event.domain.topology.lifecycle.LifeCycleStateTransitionBehavior;
import org.samples.apache.stratos.event.event.Wrapper;
import org.samples.apache.stratos.event.event.action.SleepEvent;
import org.samples.apache.stratos.event.event.application.*;
import org.samples.apache.stratos.event.event.application.signup.ApplicationSignUpAddedEvent;
import org.samples.apache.stratos.event.event.application.signup.ApplicationSignUpRemovedEvent;
import org.samples.apache.stratos.event.event.cluster.status.*;
import org.samples.apache.stratos.event.event.domain.mapping.DomainMappingAddedEvent;
import org.samples.apache.stratos.event.event.domain.mapping.DomainMappingRemovedEvent;
import org.samples.apache.stratos.event.event.health.stat.MemberFaultEvent;
import org.samples.apache.stratos.event.event.instance.notifier.ArtifactUpdatedEvent;
import org.samples.apache.stratos.event.event.instance.notifier.InstanceCleanupClusterEvent;
import org.samples.apache.stratos.event.event.instance.notifier.InstanceCleanupMemberEvent;
import org.samples.apache.stratos.event.event.instance.status.InstanceActivatedEvent;
import org.samples.apache.stratos.event.event.instance.status.InstanceMaintenanceModeEvent;
import org.samples.apache.stratos.event.event.instance.status.InstanceReadyToShutdownEvent;
import org.samples.apache.stratos.event.event.instance.status.InstanceStartedEvent;
import org.samples.apache.stratos.event.event.tenant.*;
import org.samples.apache.stratos.event.event.topology.*;

public class SampleConstants {
    public static final String JNDI_PROPERTIES_SYSTEM_PROPERTY = "jndi.properties.dir";
    public static final String EVENT_USER_DATA_PATH = "event.user.data.path";
    public static final String ENABLE_PUBLISHER = "publish";
    public static final int THREAD_WAIT_TIME = 20000;
    public static final Class eventClassArray[] = {
            Wrapper.class,

            SleepEvent.class,

            ApplicationSignUpAddedEvent.class,
            ApplicationSignUpRemovedEvent.class,

            ApplicationCreatedEvent.class,
            ApplicationDeletedEvent.class,
            ApplicationInstanceActivatedEvent.class,
            ApplicationInstanceCreatedEvent.class,
            ApplicationInstanceInactivatedEvent.class,
            ApplicationInstanceTerminatedEvent.class,
            ApplicationInstanceTerminatingEvent.class,
            CompleteApplicationsEvent.class,
            GroupInstanceActivatedEvent.class,
            GroupInstanceCreatedEvent.class,
            GroupInstanceInactivatedEvent.class,
            GroupInstanceTerminatedEvent.class,
            GroupInstanceTerminatingEvent.class,
            GroupMaintenanceModeEvent.class,
            GroupReadyToShutdownEvent.class,

            ClusterStatusClusterActivatedEvent.class,
            ClusterStatusClusterCreatedEvent.class,
            ClusterStatusClusterInactivateEvent.class,
            ClusterStatusClusterInstanceCreatedEvent.class,
            ClusterStatusClusterResetEvent.class,
            ClusterStatusClusterTerminatedEvent.class,
            ClusterStatusClusterTerminatingEvent.class,

            DomainMappingAddedEvent.class,
            DomainMappingRemovedEvent.class,

            MemberFaultEvent.class,

            ArtifactUpdatedEvent.class,
            InstanceCleanupClusterEvent.class,
            InstanceCleanupMemberEvent.class,

            InstanceActivatedEvent.class,
            InstanceMaintenanceModeEvent.class,
            InstanceReadyToShutdownEvent.class,
            InstanceStartedEvent.class,

            CompleteTenantEvent.class,
            TenantCreatedEvent.class,
            TenantRemovedEvent.class,
            TenantSubscribedEvent.class,
            TenantUnSubscribedEvent.class,
            TenantUpdatedEvent.class,

            ApplicationClustersCreatedEvent.class,
            ApplicationClustersRemovedEvent.class,
            ClusterCreatedEvent.class,
            ClusterInstanceActivatedEvent.class,
            ClusterInstanceCreatedEvent.class,
            ClusterInstanceInactivateEvent.class,
            ClusterInstanceTerminatingEvent.class,
            ClusterRemovedEvent.class,
            ClusterResetEvent.class,
            CompleteTopologyEvent.class,
            MemberActivatedEvent.class,
            MemberCreatedEvent.class,
            MemberInitializedEvent.class,
            MemberMaintenanceModeEvent.class,
            MemberReadyToShutdownEvent.class,
            MemberStartedEvent.class,
            MemberSuspendedEvent.class,
            MemberTerminatedEvent.class,
            ServiceCreatedEvent.class,
            ServiceRemovedEvent.class,


            ApplicationSignUp.class,
            ArtifactRepository.class,
            DomainMapping.class,

            Application.class,
            Applications.class,
            ApplicationStatus.class,
            ClusterDataHolder.class,
            DependencyOrder.class,
            DeploymentPolicy.class,
            Group.class,
            GroupStatus.class,
            ScalingDependentList.class,
            StartupOrder.class,

            ApplicationInstance.class,
            ClusterInstance.class,
            GroupInstance.class,
            Instance.class,

            SubscriptionDomain.class,
            Tenant.class,

            LifeCycleState.class,
            LifeCycleStateManager.class,
            LifeCycleStateTransitionBehavior.class,

            Cluster.class,
            ClusterStatus.class,
            KubernetesService.class,
            Member.class,
            MemberStatus.class,
            Port.class,
            Scope.class,
            Service.class,
            ServiceType.class,
            Topology.class,


            LoadBalancingIPType.class
    };
}
