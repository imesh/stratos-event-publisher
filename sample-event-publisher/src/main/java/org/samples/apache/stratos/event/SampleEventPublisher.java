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

package org.samples.apache.stratos.event;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.domain.tenant.Tenant;
import org.apache.stratos.messaging.domain.topology.Cluster;
import org.apache.stratos.messaging.domain.topology.Member;
import org.apache.stratos.messaging.domain.topology.MemberStatus;
import org.apache.stratos.messaging.domain.topology.Service;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.instance.notifier.ArtifactUpdatedEvent;
import org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupMemberEvent;
import org.apache.stratos.messaging.event.tenant.*;
import org.apache.stratos.messaging.event.topology.*;
import org.apache.stratos.messaging.listener.instance.notifier.ArtifactUpdateEventListener;
import org.apache.stratos.messaging.listener.instance.notifier.InstanceCleanupClusterEventListener;
import org.apache.stratos.messaging.listener.instance.notifier.InstanceCleanupMemberEventListener;
import org.apache.stratos.messaging.listener.tenant.*;
import org.apache.stratos.messaging.listener.topology.*;
import org.apache.stratos.messaging.message.receiver.instance.notifier.InstanceNotifierEventReceiver;
import org.apache.stratos.messaging.message.receiver.tenant.TenantEventReceiver;
import org.apache.stratos.messaging.message.receiver.tenant.TenantManager;
import org.apache.stratos.messaging.message.receiver.topology.TopologyEventReceiver;
import org.apache.stratos.messaging.message.receiver.topology.TopologyManager;
import org.samples.apache.stratos.event.model.SampleEventInterface;
import org.samples.apache.stratos.event.util.EventFileReader;
import org.samples.apache.stratos.event.util.SampleConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sample Event Publisher for Apache Stratos
 */
public class SampleEventPublisher implements Runnable {

    private static final Log log = LogFactory.getLog(SampleEventPublisher.class);
    private static final Type memberType = new TypeToken<Collection<Member>>() {
    }.getType();
    private static final Type tenantType = new TypeToken<Collection<Tenant>>() {
    }.getType();
    private static final Type serviceType = new TypeToken<Collection<Service>>() {
    }.getType();
    private static Gson gson = new Gson();
    private boolean terminated;

    public SampleEventPublisher() {

    }

    List<SampleEventInterface> loadSampleEventData() {
        List<SampleEventInterface> sampleEventInterfaceList = new ArrayList<SampleEventInterface>();
        try {
            EventFileReader eventFileReader = EventFileReader.getInstance();
            List<Object> sampleEventList = eventFileReader.readSampleEvents();

            for (Object sampleEventObj : sampleEventList) {
                if (sampleEventObj instanceof SampleEventInterface) {
                    SampleEventInterface sampleEvent = (SampleEventInterface) sampleEventObj;
                    sampleEventInterfaceList.add(sampleEvent);
                } else {
                    log.info("Unknown Sample Event read: " + sampleEventObj);
                }
            }
        } catch (Exception ex) {
            log.error("Error reading user data events: " + ex.getMessage());
            log.error(ex.toString());
            ex.printStackTrace();
        }
        return sampleEventInterfaceList;
    }

    private void startSampleEventPublisher() {
        final List<SampleEventInterface> sampleEventInterfaceList = loadSampleEventData();

        Thread sampleEventPublisher = new Thread(new Runnable() {
            @Override
            public void run() {
                for (SampleEventInterface sampleEvent : sampleEventInterfaceList) {
                    try {
                        sampleEvent.process();
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        log.error("Error processing Sample Event: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
            }
        });
        sampleEventPublisher.start();
    }

    @Override
    public void run() {
        if (log.isInfoEnabled()) {
            log.info("Sample Event Publisher started");
        }

        addTopologyEventListeners();
        addTenantEventListerners();
        addInstanceNotifierEventListeners();

        //TODO: Fix this properly - wait until listeners start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Error in Sample Event Publisher main thread", e);
        }

        startSampleEventPublisher();

        // Keep the thread live until terminated
        while (!terminated) {
            try {
                TopologyManager.acquireReadLock();
                List<Member> memberList = getMembersFromTopology();
                if (memberList != null) {
                    log.info("Member list: " + gson.toJson(memberList, memberType));
                }
                log.info("Complete Topology Task: " + gson.toJson(TopologyManager.getTopology().getServices(), serviceType));
                Thread.sleep(SampleConstants.THREAD_WAIT_TIME);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Error in Sample Event Publisher main thread", e);
                }
            } finally {
                TopologyManager.releaseReadLock();
            }
        }
    }


    private List<Member> getMembersFromTopology() {
        int activeCount = 0, inMaintenance = 0, startingCount = 0, createdCount = 0,
                terminated = 0, readyToShutdown = 0, shuttingDown = 0;

        if (TopologyManager.getTopology().isInitialized()) {
            List<Member> memberList = new ArrayList<Member>();
            Collection<Service> serviceCollection = TopologyManager.getTopology().getServices();
            for (Service service : serviceCollection) {
                Collection<Cluster> clusterCollection = service.getClusters();
                for (Cluster cluster : clusterCollection) {
                    Collection<Member> memberCollection = cluster.getMembers();
                    for (Member member : memberCollection) {
                        memberList.add(member);
                        if (MemberStatus.Activated.equals(member.getStatus())) {
                            activeCount++;
                        } else if (MemberStatus.Created.equals(member.getStatus())) {
                            createdCount++;
                        } else if (MemberStatus.Starting.equals(member.getStatus())) {
                            startingCount++;
                        } else if (MemberStatus.In_Maintenance.equals(member.getStatus())) {
                            inMaintenance++;
                        } else if (MemberStatus.Terminated.equals(member.getStatus())) {
                            terminated++;
                        } else if (MemberStatus.ReadyToShutDown.equals(member.getStatus())) {
                            readyToShutdown++;
                        } else if (MemberStatus.ShuttingDown.equals(member.getStatus())) {
                            shuttingDown++;
                        }
                    }
                }
            }
            log.info("Member counts [activated] " + activeCount + ", [starting] " + startingCount + ", [creating] " + createdCount
                    + ", [maintenance] " + inMaintenance + ", [terminated] " + terminated + ", [ready-to-shutdown] " + readyToShutdown
                    + ", [shutting-down] " + shuttingDown);
            return memberList;
        }
        return null;
    }

    private void addInstanceNotifierEventListeners() {
        if (log.isDebugEnabled()) {
            log.debug("Starting instance notifier event message receiver thread...");
        }

        final InstanceNotifierEventReceiver instanceNotifierEventReceiver = new InstanceNotifierEventReceiver();
        instanceNotifierEventReceiver.addEventListener(new ArtifactUpdateEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    log.info("Artifact updated event received");
                    ArtifactUpdatedEvent artifactUpdatedEvent = (ArtifactUpdatedEvent) event;
                    log.info("Artifact updated event: " + gson.toJson(artifactUpdatedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing artifact updated event", e);
                    }
                }
            }
        });

        instanceNotifierEventReceiver.addEventListener(new InstanceCleanupMemberEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    log.info("Instance cleanup member event received");
                    InstanceCleanupMemberEvent instanceCleanupMemberEvent = (InstanceCleanupMemberEvent) event;
                    log.info("Instance cleanup member event: " + gson.toJson(instanceCleanupMemberEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing instance cleanup member event", e);
                    }
                }

            }
        });

        instanceNotifierEventReceiver.addEventListener(new InstanceCleanupClusterEventListener() {
            @Override
            protected void onEvent(Event event) {
                log.info("Instance cleanup cluster event received");
                InstanceCleanupMemberEvent instanceCleanupMemberEvent = (InstanceCleanupMemberEvent) event;
                log.info("Instance cleanup member event: " + gson.toJson(instanceCleanupMemberEvent));
            }
        });

        Thread instanceNotifierEventReceiverThread = new Thread(instanceNotifierEventReceiver);
        instanceNotifierEventReceiverThread.start();
        if (log.isInfoEnabled()) {
            log.info("Instance notifier event message receiver thread started");
        }
    }

    private void addTenantEventListerners() {
        if (log.isDebugEnabled()) {
            log.debug("Starting tenant event message receiver thread...");
        }

        final TenantEventReceiver tenantEventReceiver = new TenantEventReceiver();
        tenantEventReceiver.addEventListener(new SubscriptionDomainsAddedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    log.info("Subscription domain added event received");
                    SubscriptionDomainAddedEvent subscriptionDomainAddedEvent = (SubscriptionDomainAddedEvent) event;
                    log.debug("Subscription domain added event: " + gson.toJson(subscriptionDomainAddedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing subscription domains added event", e);
                    }
                } finally {
                    TenantManager.releaseReadLock();
                }

            }
        });

        tenantEventReceiver.addEventListener(new SubscriptionDomainsRemovedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    log.info("Subscription domain removed event received");
                    SubscriptionDomainRemovedEvent subscriptionDomainRemovedEvent = (SubscriptionDomainRemovedEvent) event;
                    log.info("Subscription domain removed event: " + gson.toJson(subscriptionDomainRemovedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing subscription domains removed event", e);
                    }
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        tenantEventReceiver.addEventListener(new CompleteTenantEventListener() {
            private boolean initialized;

            @Override
            protected void onEvent(Event event) {
                if (!initialized) {
                    try {
                        TenantManager.acquireReadLock();
                        log.info("Complete tenant event received");
                        CompleteTenantEvent completeTenantEvent = (CompleteTenantEvent) event;
                        log.info("Complete tenant event: " + gson.toJson(completeTenantEvent.getTenants(), tenantType));
                        initialized = true;
                    } catch (Exception e) {
                        if (log.isErrorEnabled()) {
                            log.error("Error processing complete tenant event", e);
                        }
                    } finally {
                        TenantManager.releaseReadLock();
                    }

                } else {
                    if (log.isInfoEnabled()) {
                        log.info("Complete tenant event updating task disabled");
                    }
                }
            }
        });

        tenantEventReceiver.addEventListener(new TenantSubscribedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    log.info("Tenant subscribed event received");
                    TenantSubscribedEvent tenantSubscribedEvent = (TenantSubscribedEvent) event;
                    log.info("Tenant subscribed event: " + gson.toJson(tenantSubscribedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing tenant subscribed event", e);
                    }
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        tenantEventReceiver.addEventListener(new TenantUnSubscribedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    log.info("Tenant unsubscribed event received");
                    TenantUnSubscribedEvent tenantUnSubscribedEvent = (TenantUnSubscribedEvent) event;
                    log.info("Tenant unsubscribed event: " + gson.toJson(tenantUnSubscribedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing tenant unsubscribed event", e);
                    }
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        tenantEventReceiver.addEventListener(new TenantCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    log.info("Tenant created event received");
                    TenantCreatedEvent tenantCreatedEvent = (TenantCreatedEvent) event;
                    log.info("Tenant created event: " + gson.toJson(tenantCreatedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing tenant created event", e);
                    }
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        tenantEventReceiver.addEventListener(new TenantRemovedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    log.info("Tenant removed event received");
                    TenantRemovedEvent tenantRemovedEvent = (TenantRemovedEvent) event;
                    log.info("Tenant removed event: " + gson.toJson(tenantRemovedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing tenant removed event", e);
                    }
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        tenantEventReceiver.addEventListener(new TenantUpdatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    log.info("Tenant updated event received");
                    TenantUpdatedEvent tenantUpdatedEvent = (TenantUpdatedEvent) event;
                    log.info("Tenant updated event: " + gson.toJson(tenantUpdatedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing tenant updated event", e);
                    }
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        Thread tenantEventReceiverThread = new Thread(tenantEventReceiver);
        tenantEventReceiverThread.start();
        if (log.isInfoEnabled()) {
            log.info("Tenant event message receiver thread started");
        }
    }

    private void addTopologyEventListeners() {
        if (log.isDebugEnabled()) {
            log.debug("Starting topology event message receiver thread...");
        }
        final TopologyEventReceiver topologyEventReceiver = new TopologyEventReceiver();
        topologyEventReceiver.addEventListener(new CompleteTopologyEventListener() {
            private boolean initialized;

            @Override
            protected void onEvent(Event event) {
                if (!initialized) {
                    try {
                        TopologyManager.acquireReadLock();
                        log.info("Complete topology event received");
                        CompleteTopologyEvent completeTopologyEvent = (CompleteTopologyEvent) event;
                        log.info("Complete topology event: " + gson.toJson(completeTopologyEvent.getTopology().getServices(), serviceType));
                        initialized = true;
                    } catch (Exception e) {
                        if (log.isErrorEnabled()) {
                            log.error("Error processing complete topology event", e);
                        }
                    } finally {
                        TopologyManager.releaseReadLock();
                    }
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberTerminatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Member terminated event received");
                    MemberTerminatedEvent memberTerminatedEvent = (MemberTerminatedEvent) event;
                    log.info("Member terminated event: " + gson.toJson(memberTerminatedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing member terminated event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberSuspendedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Member suspended event received");
                    MemberSuspendedEvent memberSuspendedEvent = (MemberSuspendedEvent) event;
                    log.info("Member suspended event: " + gson.toJson(memberSuspendedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing member suspended event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberStartedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Member started event received");
                    MemberStartedEvent memberStartedEvent = (MemberStartedEvent) event;
                    log.info("Member started event: " + gson.toJson(memberStartedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing member started event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });


        topologyEventReceiver.addEventListener(new MemberActivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Member activated event received");
                    MemberActivatedEvent memberActivatedEvent = (MemberActivatedEvent) event;
                    log.info("Member activated event: " + gson.toJson(memberActivatedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing member activated event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberMaintenanceListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Member maintenance event received");
                    MemberMaintenanceModeEvent memberMaintenanceModeEvent = (MemberMaintenanceModeEvent) event;
                    log.info("Member maintenance event: " + gson.toJson(memberMaintenanceModeEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing member maintenance event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberReadyToShutdownEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Member ready to shutdown event received");
                    MemberReadyToShutdownEvent memberReadyToShutdownEvent = (MemberReadyToShutdownEvent) event;
                    log.info("Member ready to shutdown event: " + gson.toJson(memberReadyToShutdownEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing member ready to shutdown event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Cluster created event received");
                    ClusterCreatedEvent clusterCreatedEvent = (ClusterCreatedEvent) event;
                    log.info("Cluster created event: " + gson.toJson(clusterCreatedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing cluster created event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterRemovedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Cluster removed event received");
                    ClusterRemovedEvent clusterRemovedEvent = (ClusterRemovedEvent) event;
                    log.info("Cluster removed event: " + gson.toJson(clusterRemovedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing cluster removed event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new InstanceSpawnedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Instance spawned event received");
                    InstanceSpawnedEvent instanceSpawnedEvent = (InstanceSpawnedEvent) event;
                    log.info("Instance spawned event: " + gson.toJson(instanceSpawnedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing instance spawned event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ServiceCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Service created event received");
                    ServiceCreatedEvent serviceCreatedEvent = (ServiceCreatedEvent) event;
                    log.info("Service created event: " + gson.toJson(serviceCreatedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing service created event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ServiceRemovedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    log.info("Service removed event received");
                    ServiceRemovedEvent serviceRemovedEvent = (ServiceRemovedEvent) event;
                    log.info("Service removed event: " + gson.toJson(serviceRemovedEvent));
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error processing service removed event", e);
                    }
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        Thread thread = new Thread(topologyEventReceiver);
        thread.start();
        if (log.isDebugEnabled()) {
            log.info("Sample Event Publisher topology receiver thread started");
        }
    }

    public void terminate() {
        terminated = true;
    }
}
