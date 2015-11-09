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
import org.apache.stratos.common.threading.StratosThreadPool;
import org.apache.stratos.messaging.domain.tenant.Tenant;
import org.apache.stratos.messaging.domain.topology.Cluster;
import org.apache.stratos.messaging.domain.topology.Member;
import org.apache.stratos.messaging.domain.topology.MemberStatus;
import org.apache.stratos.messaging.domain.topology.Service;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.application.*;
import org.apache.stratos.messaging.event.application.signup.ApplicationSignUpAddedEvent;
import org.apache.stratos.messaging.event.application.signup.ApplicationSignUpRemovedEvent;
import org.apache.stratos.messaging.event.cluster.status.*;
import org.apache.stratos.messaging.event.domain.mapping.DomainMappingAddedEvent;
import org.apache.stratos.messaging.event.domain.mapping.DomainMappingRemovedEvent;
import org.apache.stratos.messaging.event.health.stat.MemberFaultEvent;
import org.apache.stratos.messaging.event.initializer.CompleteTopologyRequestEvent;
import org.apache.stratos.messaging.event.instance.notifier.ArtifactUpdatedEvent;
import org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupClusterEvent;
import org.apache.stratos.messaging.event.instance.notifier.InstanceCleanupMemberEvent;
import org.apache.stratos.messaging.event.tenant.CompleteTenantEvent;
import org.apache.stratos.messaging.event.tenant.TenantCreatedEvent;
import org.apache.stratos.messaging.event.tenant.TenantRemovedEvent;
import org.apache.stratos.messaging.event.tenant.TenantUpdatedEvent;
import org.apache.stratos.messaging.event.topology.*;
import org.apache.stratos.messaging.listener.application.*;
import org.apache.stratos.messaging.listener.application.signup.ApplicationSignUpAddedEventListener;
import org.apache.stratos.messaging.listener.application.signup.ApplicationSignUpRemovedEventListener;
import org.apache.stratos.messaging.listener.cluster.status.*;
import org.apache.stratos.messaging.listener.domain.mapping.DomainMappingAddedEventListener;
import org.apache.stratos.messaging.listener.domain.mapping.DomainMappingRemovedEventListener;
import org.apache.stratos.messaging.listener.health.stat.MemberFaultEventListener;
import org.apache.stratos.messaging.listener.initializer.CompleteTopologyRequestEventListener;
import org.apache.stratos.messaging.listener.instance.notifier.ArtifactUpdateEventListener;
import org.apache.stratos.messaging.listener.instance.notifier.InstanceCleanupClusterEventListener;
import org.apache.stratos.messaging.listener.instance.notifier.InstanceCleanupMemberEventListener;
import org.apache.stratos.messaging.listener.tenant.CompleteTenantEventListener;
import org.apache.stratos.messaging.listener.tenant.TenantCreatedEventListener;
import org.apache.stratos.messaging.listener.tenant.TenantRemovedEventListener;
import org.apache.stratos.messaging.listener.tenant.TenantUpdatedEventListener;
import org.apache.stratos.messaging.listener.topology.*;
import org.apache.stratos.messaging.message.receiver.application.ApplicationManager;
import org.apache.stratos.messaging.message.receiver.application.ApplicationsEventReceiver;
import org.apache.stratos.messaging.message.receiver.application.signup.ApplicationSignUpEventReceiver;
import org.apache.stratos.messaging.message.receiver.cluster.status.ClusterStatusEventReceiver;
import org.apache.stratos.messaging.message.receiver.domain.mapping.DomainMappingEventReceiver;
import org.apache.stratos.messaging.message.receiver.health.stat.HealthStatEventReceiver;
import org.apache.stratos.messaging.message.receiver.initializer.InitializerEventReceiver;
import org.apache.stratos.messaging.message.receiver.instance.notifier.InstanceNotifierEventReceiver;
import org.apache.stratos.messaging.message.receiver.tenant.TenantEventReceiver;
import org.apache.stratos.messaging.message.receiver.tenant.TenantManager;
import org.apache.stratos.messaging.message.receiver.topology.TopologyEventReceiver;
import org.apache.stratos.messaging.message.receiver.topology.TopologyManager;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.EventFileReader;
import org.samples.apache.stratos.event.util.SampleConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Sample Event Publisher for Apache Stratos
 */
public class SampleEventPublisher implements Runnable {

    private static final Log logger = LogFactory.getLog(SampleEventPublisher.class);
    private boolean isPublisherEnabled = false;
    private static final Type memberType = new TypeToken<Collection<Member>>() {
    }.getType();
    private static final Type tenantType = new TypeToken<Collection<Tenant>>() {
    }.getType();
    private static final Type serviceType = new TypeToken<Collection<Service>>() {
    }.getType();
    private static Gson gson = new Gson();
    private boolean terminated;
    private static final ExecutorService eventListenerExecutorService =
            StratosThreadPool.getExecutorService("sample.event.publisher.thread.pool", 10);
    private InstanceNotifierEventReceiver instanceNotifierEventReceiver;
    private TopologyEventReceiver topologyEventReceiver;
    private TenantEventReceiver tenantEventReceiver;
    private ApplicationsEventReceiver applicationsEventReceiver;
    private ApplicationSignUpEventReceiver applicationSignUpEventReceiver;
    private HealthStatEventReceiver healthStatEventReceiver;
    private ClusterStatusEventReceiver clusterStatusEventReceiver;
    private DomainMappingEventReceiver domainMappingEventReceiver;
    private InitializerEventReceiver initializerEventReceiver;

    public SampleEventPublisher() {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating event listeners...");
        }
        applicationSignUpEventReceiver = new ApplicationSignUpEventReceiver();
        applicationSignUpEventReceiver.setExecutorService(eventListenerExecutorService);
        clusterStatusEventReceiver = new ClusterStatusEventReceiver();
        clusterStatusEventReceiver.setExecutorService(eventListenerExecutorService);
        domainMappingEventReceiver = new DomainMappingEventReceiver();
        domainMappingEventReceiver.setExecutorService(eventListenerExecutorService);
        applicationsEventReceiver = new ApplicationsEventReceiver();
        applicationsEventReceiver.setExecutorService(eventListenerExecutorService);
        healthStatEventReceiver = new HealthStatEventReceiver();
        healthStatEventReceiver.setExecutorService(eventListenerExecutorService);
        topologyEventReceiver = new TopologyEventReceiver();
        topologyEventReceiver.setExecutorService(eventListenerExecutorService);
        instanceNotifierEventReceiver = new InstanceNotifierEventReceiver();
        tenantEventReceiver = new TenantEventReceiver();
        tenantEventReceiver.setExecutorService(eventListenerExecutorService);
        initializerEventReceiver = new InitializerEventReceiver();
        initializerEventReceiver.setExecutorService(eventListenerExecutorService);
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
                    logger.info("Unknown Sample Event read: " + sampleEventObj);
                }
            }
        } catch (Exception ex) {
            logger.error("Error reading user data events: " + ex.getMessage());
            logger.error(ex.toString());
            ex.printStackTrace();
        }
        return sampleEventInterfaceList;
    }

    private void startSampleEventPublisher() {
        final List<SampleEventInterface> sampleEventInterfaceList = loadSampleEventData();

        Thread sampleEventPublisher = new Thread(new Runnable() {
            public void run() {
                for (SampleEventInterface sampleEvent : sampleEventInterfaceList) {
                    try {
                        sampleEvent.process();
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                        logger.error("Error processing Sample Event: " + ex.toString());
                        ex.printStackTrace();
                    }
                }
                logger.info("Event publishing completed.");
            }
        });
        sampleEventPublisher.start();
    }

    public void run() {
        logger.info("Starting Sample Event Publisher...");
        addInitializerEventListeners();
        addTopologyEventListeners();
        addTenantEventListeners();
        addInstanceNotifierEventListeners();
        addApplicationEventListeners();
        addApplicationSignUpEventListeners();
        addHealthStatEventListeners();
        addClusterStatusEventListeners();
        addDomainMappingEventListeners();
        if (isPublisherEnabled) {
            logger.info("Starting to publish events...");
            startSampleEventPublisher();
        }
        logger.info("Sample Event Publisher started");

        // Keep the thread live until terminated
        while (!terminated) {
            try {
                TopologyManager.acquireReadLock();
                List<Member> memberList = getMembersFromTopology();
                if (memberList != null) {
                    logger.info("Member list: " + gson.toJson(memberList, memberType));
                }
                logger.info("Complete Topology Task: " +
                        gson.toJson(TopologyManager.getTopology().getServices(), serviceType));
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error in Sample Event Publisher main thread", e);
                }
            } finally {
                TopologyManager.releaseReadLock();
            }

            try {
                ApplicationManager.acquireReadLockForApplications();
                logger.info("Complete Application Task: " +
                        gson.toJson(ApplicationManager.getApplications()));
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error in Sample Event Publisher main thread", e);
                }
            } finally {
                ApplicationManager.releaseReadLockForApplications();
            }

            try {
                TenantManager.acquireReadLock();
                logger.info("Complete Tenant Task: " + gson.toJson(TenantManager.getInstance()));
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Error in Sample Event Publisher main thread", e);
                }
            } finally {
                TenantManager.releaseReadLock();
            }


            try {
                Thread.sleep(SampleConstants.THREAD_WAIT_TIME);
            } catch (Exception e) {
                logger.error("Error while sleeping", e);
            }
        }
    }

    private void addInitializerEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting CompleteTopologyRequestEvent event message receiver thread...");
        }
        initializerEventReceiver.addEventListener(new CompleteTopologyRequestEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("CompleteTopologyRequestEvent event received");
                    CompleteTopologyRequestEvent completeTopologyRequestEvent = (CompleteTopologyRequestEvent) event;
                    logger.info("CompleteTopologyRequestEvent event: " + gson.toJson(completeTopologyRequestEvent));
                } catch (Exception e) {
                    logger.error("Error processing CompleteTopologyRequestEvent event", e);
                }
            }
        });

        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                initializerEventReceiver.execute();
            }
        });
        logger.info("CompleteTopologyRequestEvent event message receiver thread started");
    }

    private List<Member> getMembersFromTopology() {
        int activeCount = 0, inMaintenance = 0, startingCount = 0, createdCount = 0,
                terminated = 0, readyToShutdown = 0, initialized = 0, suspended = 0;

        if (TopologyManager.getTopology().isInitialized()) {
            List<Member> memberList = new ArrayList<Member>();
            Collection<Service> serviceCollection = TopologyManager.getTopology().getServices();
            for (Service service : serviceCollection) {
                Collection<Cluster> clusterCollection = service.getClusters();
                for (Cluster cluster : clusterCollection) {
                    Collection<Member> memberCollection = cluster.getMembers();
                    for (Member member : memberCollection) {
                        memberList.add(member);
                        if (MemberStatus.Active.equals(member.getStatus())) {
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
                        } else if (MemberStatus.Initialized.equals(member.getStatus())) {
                            initialized++;
                        } else if (MemberStatus.Suspended.equals(member.getStatus())) {
                            suspended++;
                        }

                    }
                }
            }
            logger.info("Member counts [activated] " + activeCount + ", [starting] " + startingCount + ", [creating] " +
                    createdCount
                    + ", [maintenance] " + inMaintenance + ", [terminated] " + terminated + ", [ready-to-shutdown] " +
                    readyToShutdown
                    + ", [initialized] " + initialized + ", [suspended] " + suspended);
            return memberList;
        }
        return null;
    }

    private void addApplicationSignUpEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting ApplicationSignUp event message receiver thread...");
        }
        applicationSignUpEventReceiver.addEventListener(new ApplicationSignUpAddedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationSignUpAddedEvent event received");
                    ApplicationSignUpAddedEvent applicationSignUpAddedEvent = (ApplicationSignUpAddedEvent) event;
                    logger.info("ApplicationSignUpAddedEvent event: " + gson.toJson(applicationSignUpAddedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationSignUpAddedEvent event", e);
                }
            }
        });

        applicationSignUpEventReceiver.addEventListener(new ApplicationSignUpRemovedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationSignUpRemovedEvent event received");
                    ApplicationSignUpRemovedEvent applicationSignUpRemovedEvent = (ApplicationSignUpRemovedEvent) event;
                    logger.info("ApplicationSignUpRemovedEvent event: " + gson.toJson(applicationSignUpRemovedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationSignUpRemovedEvent event", e);
                }
            }
        });
        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                applicationSignUpEventReceiver.execute();
            }
        });
        logger.info("ApplicationSignUp event message receiver thread started");
    }

    private void addApplicationEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting Application event message receiver thread...");
        }
        applicationsEventReceiver.addEventListener(new ApplicationCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("Artifact updated event received");
                    ApplicationCreatedEvent applicationCreatedEvent = (ApplicationCreatedEvent) event;
                    logger.info("Artifact updated event: " + gson.toJson(applicationCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing artifact updated event", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new ApplicationDeletedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationDeletedEvent event received");
                    ApplicationDeletedEvent applicationDeletedEvent = (ApplicationDeletedEvent) event;
                    logger.info("ApplicationDeletedEvent event: " + gson.toJson(applicationDeletedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationDeletedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new ApplicationInstanceActivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationInstanceActivatedEvent event received");
                    ApplicationInstanceActivatedEvent applicationInstanceActivatedEvent =
                            (ApplicationInstanceActivatedEvent) event;
                    logger.info("ApplicationInstanceActivatedEvent event: " +
                            gson.toJson(applicationInstanceActivatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationInstanceActivatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new ApplicationInstanceCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationInstanceCreatedEvent event received");
                    ApplicationInstanceCreatedEvent applicationInstanceCreatedEvent =
                            (ApplicationInstanceCreatedEvent) event;
                    logger.info(
                            "ApplicationInstanceCreatedEvent event: " + gson.toJson(applicationInstanceCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationInstanceCreatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new ApplicationInstanceInactivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationInstanceInactivatedEvent event received");
                    ApplicationInstanceInactivatedEvent applicationInstanceInactivatedEvent =
                            (ApplicationInstanceInactivatedEvent) event;
                    logger.info("ApplicationInstanceInactivatedEvent event: " +
                            gson.toJson(applicationInstanceInactivatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationInstanceInactivatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new ApplicationInstanceTerminatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationInstanceTerminatedEvent event received");
                    ApplicationInstanceTerminatedEvent applicationInstanceTerminated =
                            (ApplicationInstanceTerminatedEvent) event;
                    logger.info(
                            "ApplicationInstanceTerminatedEvent event: " + gson.toJson(applicationInstanceTerminated));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationInstanceTerminatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new ApplicationInstanceTerminatingEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ApplicationInstanceTerminatingEvent event received");
                    ApplicationInstanceTerminatingEvent applicationInstanceTerminatingEvent =
                            (ApplicationInstanceTerminatingEvent) event;
                    logger.info(
                            "ApplicationInstanceTerminatingEvent event: " +
                                    gson.toJson(applicationInstanceTerminatingEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationInstanceTerminatingEvent", e);
                }
            }
        });


        applicationsEventReceiver.addEventListener(new CompleteApplicationsEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("CompleteApplicationsEvent event received");
                    CompleteApplicationsEvent completeApplicationsEvent =
                            (CompleteApplicationsEvent) event;
                    logger.info(
                            "CompleteApplicationsEvent event: " + gson.toJson(completeApplicationsEvent));
                } catch (Exception e) {
                    logger.error("Error processing CompleteApplicationsEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new GroupInstanceActivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("GroupInstanceActivatedEvent event received");
                    GroupInstanceActivatedEvent groupInstanceActivatedEvent =
                            (GroupInstanceActivatedEvent) event;
                    logger.info(
                            "GroupInstanceActivatedEvent event: " + gson.toJson(groupInstanceActivatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing GroupInstanceActivatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new GroupInstanceCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("GroupInstanceCreatedEvent event received");
                    GroupInstanceCreatedEvent groupInstanceCreatedEvent =
                            (GroupInstanceCreatedEvent) event;
                    logger.info(
                            "GroupInstanceCreatedEvent event: " + gson.toJson(groupInstanceCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing GroupInstanceCreatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new GroupInstanceInactivateEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("GroupInstanceInactivatedEvent event received");
                    GroupInstanceInactivatedEvent groupInstanceInactivatedEvent =
                            (GroupInstanceInactivatedEvent) event;
                    logger.info(
                            "GroupInstanceInactivatedEvent event: " + gson.toJson(groupInstanceInactivatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing GroupInstanceInactivatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new GroupInstanceTerminatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("GroupInstanceTerminatedEvent event received");
                    GroupInstanceTerminatedEvent groupInstanceTerminatedEvent =
                            (GroupInstanceTerminatedEvent) event;
                    logger.info(
                            "GroupInstanceTerminatedEvent event: " + gson.toJson(groupInstanceTerminatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing GroupInstanceTerminatedEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new GroupInstanceTerminatingEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("GroupInstanceTerminatingEvent event received");
                    GroupInstanceTerminatingEvent groupInstanceTerminatingEvent =
                            (GroupInstanceTerminatingEvent) event;
                    logger.info(
                            "GroupInstanceTerminatingEvent event: " + gson.toJson(groupInstanceTerminatingEvent));
                } catch (Exception e) {
                    logger.error("Error processing GroupInstanceTerminatingEvent", e);
                }
            }
        });

        applicationsEventReceiver.addEventListener(new GroupMaintenanceModeEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("GroupMaintenanceModeEvent event received");
                    GroupMaintenanceModeEvent groupMaintenanceModeEvent = (GroupMaintenanceModeEvent) event;
                    logger.info(
                            "GroupMaintenanceModeEvent event: " + gson.toJson(groupMaintenanceModeEvent));
                } catch (Exception e) {
                    logger.error("Error processing GroupMaintenanceModeEvent", e);
                }
            }
        });

        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                applicationsEventReceiver.execute();
            }
        });
        logger.info("Application event message receiver thread started");
    }


    private void addClusterStatusEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting ClusterStatus event message receiver thread...");
        }

        clusterStatusEventReceiver.addEventListener(new ClusterStatusClusterActivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ClusterStatusClusterActivatedEvent event received");
                    ClusterStatusClusterActivatedEvent clusterStatusClusterActivatedEvent =
                            (ClusterStatusClusterActivatedEvent) event;
                    logger.info("ClusterStatusClusterActivatedEvent event: " +
                            gson.toJson(clusterStatusClusterActivatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterStatusClusterActivatedEvent event", e);
                }
            }
        });

        clusterStatusEventReceiver.addEventListener(new ClusterStatusClusterInactivateEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ClusterStatusClusterInactivateEvent event received");
                    ClusterStatusClusterInactivateEvent clusterStatusClusterInactivateEvent =
                            (ClusterStatusClusterInactivateEvent) event;
                    logger.info("ClusterStatusClusterInactivateEvent event: " +
                            gson.toJson(clusterStatusClusterInactivateEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterStatusClusterInactivateEvent event", e);
                }
            }
        });

        clusterStatusEventReceiver.addEventListener(new ClusterStatusClusterInstanceCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ClusterStatusClusterInstanceCreatedEvent event received");
                    ClusterStatusClusterInstanceCreatedEvent clusterStatusClusterInstanceCreatedEvent =
                            (ClusterStatusClusterInstanceCreatedEvent) event;
                    logger.info("ClusterStatusClusterInstanceCreatedEvent event: " +
                            gson.toJson(clusterStatusClusterInstanceCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterStatusClusterInstanceCreatedEvent event", e);
                }
            }
        });

        clusterStatusEventReceiver.addEventListener(new ClusterStatusClusterResetEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ClusterStatusClusterResetEvent event received");
                    ClusterStatusClusterResetEvent clusterStatusClusterResetEvent =
                            (ClusterStatusClusterResetEvent) event;
                    logger.info("ClusterStatusClusterResetEvent event: " + gson.toJson(clusterStatusClusterResetEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterStatusClusterResetEvent event", e);
                }
            }
        });

        clusterStatusEventReceiver.addEventListener(new ClusterStatusClusterTerminatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ClusterStatusClusterTerminatedEvent event received");
                    ClusterStatusClusterTerminatedEvent clusterStatusClusterTerminatedEvent =
                            (ClusterStatusClusterTerminatedEvent) event;
                    logger.info("ClusterStatusClusterTerminatedEvent event: " +
                            gson.toJson(clusterStatusClusterTerminatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterStatusClusterTerminatedEvent event", e);
                }
            }
        });

        clusterStatusEventReceiver.addEventListener(new ClusterStatusClusterTerminatingEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("ClusterStatusClusterTerminatingEvent event received");
                    ClusterStatusClusterTerminatingEvent clusterStatusClusterTerminatingEvent =
                            (ClusterStatusClusterTerminatingEvent) event;
                    logger.info("ClusterStatusClusterTerminatingEvent event: " +
                            gson.toJson(clusterStatusClusterTerminatingEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterStatusClusterTerminatingEvent event", e);
                }
            }
        });

        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                clusterStatusEventReceiver.execute();
            }
        });
        logger.info("ClusterStatus event message receiver thread started");
    }


    private void addDomainMappingEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting DomainMapping event message receiver thread...");
        }

        domainMappingEventReceiver.addEventListener(new DomainMappingAddedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("DomainMappingAddedEvent event received");
                    DomainMappingAddedEvent domainMappingAddedEvent = (DomainMappingAddedEvent) event;
                    logger.info("DomainMappingAddedEvent event: " + gson.toJson(domainMappingAddedEvent));
                } catch (Exception e) {
                    logger.error("Error processing DomainMappingAddedEvent event", e);
                }
            }
        });

        domainMappingEventReceiver.addEventListener(new DomainMappingRemovedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("DomainMappingRemovedEvent event received");
                    DomainMappingRemovedEvent domainMappingRemovedEvent = (DomainMappingRemovedEvent) event;
                    logger.info("DomainMappingRemovedEvent event: " + gson.toJson(domainMappingRemovedEvent));
                } catch (Exception e) {
                    logger.error("Error processing DomainMappingRemovedEvent event", e);
                }
            }
        });

        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                domainMappingEventReceiver.execute();
            }
        });
        logger.info("DomainMapping event message receiver thread started");
    }

    private void addHealthStatEventListeners() {
        healthStatEventReceiver.addEventListener(new MemberFaultEventListener() {
            @Override
            protected void onEvent(org.apache.stratos.messaging.event.Event event) {
                try {
                    logger.info("MemberFaultEvent event received");
                    MemberFaultEvent memberFaultEvent = (MemberFaultEvent) event;
                    logger.info("MemberFaultEvent event: " + gson.toJson(memberFaultEvent));
                } catch (Exception e) {
                    logger.error("Error processing MemberFaultEvent event", e);
                }
            }
        });
        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                healthStatEventReceiver.execute();
            }
        });
    }

    private void addInstanceNotifierEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting instance notifier event message receiver thread...");
        }


        instanceNotifierEventReceiver.addEventListener(new ArtifactUpdateEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("Artifact updated event received");
                    ArtifactUpdatedEvent artifactUpdatedEvent = (ArtifactUpdatedEvent) event;
                    logger.info("Artifact updated event: " + gson.toJson(artifactUpdatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing artifact updated event", e);
                }
            }
        });

        instanceNotifierEventReceiver.addEventListener(new InstanceCleanupMemberEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    logger.info("Instance cleanup member event received");
                    InstanceCleanupMemberEvent instanceCleanupMemberEvent = (InstanceCleanupMemberEvent) event;
                    logger.info("Instance cleanup member event: " + gson.toJson(instanceCleanupMemberEvent));
                } catch (Exception e) {
                    logger.error("Error processing instance cleanup member event", e);
                }

            }
        });

        instanceNotifierEventReceiver.addEventListener(new InstanceCleanupClusterEventListener() {
            @Override
            protected void onEvent(Event event) {
                logger.info("Instance cleanup cluster event received");
                InstanceCleanupClusterEvent instanceCleanupClusterEvent = (InstanceCleanupClusterEvent) event;
                logger.info("Instance cleanup member event: " + gson.toJson(instanceCleanupClusterEvent));
            }
        });

        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                instanceNotifierEventReceiver.execute();
            }
        });
        logger.info("Instance notifier event message receiver thread started");
    }

    private void addTenantEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting tenant event message receiver thread...");
        }

        tenantEventReceiver.addEventListener(new CompleteTenantEventListener() {
            private boolean initialized;

            @Override
            protected void onEvent(Event event) {
                if (!initialized) {
                    try {
                        TenantManager.acquireReadLock();
                        logger.info("Complete tenant event received");
                        CompleteTenantEvent completeTenantEvent = (CompleteTenantEvent) event;
                        logger.info(
                                "Complete tenant event: " + gson.toJson(completeTenantEvent.getTenants(), tenantType));
                        initialized = true;
                    } catch (Exception e) {
                        logger.error("Error processing complete tenant event", e);
                    } finally {
                        TenantManager.releaseReadLock();
                    }

                } else {
                    logger.info("Complete tenant event updating task disabled");
                }
            }
        });

        tenantEventReceiver.addEventListener(new TenantCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    logger.info("Tenant created event received");
                    TenantCreatedEvent tenantCreatedEvent = (TenantCreatedEvent) event;
                    logger.info("Tenant created event: " + gson.toJson(tenantCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing tenant created event", e);
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
                    logger.info("Tenant removed event received");
                    TenantRemovedEvent tenantRemovedEvent = (TenantRemovedEvent) event;
                    logger.info("Tenant removed event: " + gson.toJson(tenantRemovedEvent));
                } catch (Exception e) {
                    logger.error("Error processing tenant removed event", e);
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });
/*
        tenantEventReceiver.addEventListener(new TenantSubscribedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    logger.info("TenantSubscribedEvent event received");
                    TenantSubscribedEvent tenantSubscribedEvent = (TenantSubscribedEvent) event;
                    logger.info("TenantSubscribedEvent event: " + gson.toJson(tenantSubscribedEvent));
                }
                catch (Exception e) {
                    logger.error("Error processing TenantSubscribedEvent event", e);
                }
                finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        tenantEventReceiver.addEventListener(new TenantUnSubscribedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    logger.info("TenantUnSubscribedEvent event received");
                    TenantUnSubscribedEvent tenantUnSubscribedEvent = (TenantUnSubscribedEvent) event;
                    logger.info("TenantUnSubscribedEvent event: " + gson.toJson(tenantUnSubscribedEvent));
                }
                catch (Exception e) {
                    logger.error("Error processing TenantUnSubscribedEvent event", e);
                }
                finally {
                    TenantManager.releaseReadLock();
                }
            }
        });
*/
        tenantEventReceiver.addEventListener(new TenantUpdatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TenantManager.acquireReadLock();
                    logger.info("Tenant updated event received");
                    TenantUpdatedEvent tenantUpdatedEvent = (TenantUpdatedEvent) event;
                    logger.info("Tenant updated event: " + gson.toJson(tenantUpdatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing tenant updated event", e);
                } finally {
                    TenantManager.releaseReadLock();
                }
            }
        });

        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                tenantEventReceiver.execute();
            }
        });
        logger.info("Tenant event message receiver thread started");
    }

    private void addTopologyEventListeners() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting topology event message receiver thread...");
        }

        topologyEventReceiver.addEventListener(new ApplicationClustersCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ApplicationClustersCreatedEvent event received");
                    ApplicationClustersCreatedEvent applicationClustersCreatedEvent =
                            (ApplicationClustersCreatedEvent) event;
                    logger.info(
                            "ApplicationClustersCreatedEvent event: " + gson.toJson(applicationClustersCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationClustersCreatedEvent event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ApplicationClustersRemovedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ApplicationClustersRemovedEvent event received");
                    ApplicationClustersRemovedEvent applicationClustersRemovedEvent =
                            (ApplicationClustersRemovedEvent) event;
                    logger.info(
                            "ApplicationClustersRemovedEvent event: " + gson.toJson(applicationClustersRemovedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ApplicationClustersRemovedEvent event", e);
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
                    logger.info("Cluster created event received");
                    ClusterCreatedEvent clusterCreatedEvent = (ClusterCreatedEvent) event;
                    logger.info("Cluster created event: " + gson.toJson(clusterCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing cluster created event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterInstanceActivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ClusterInstanceActivatedEvent event received");
                    ClusterInstanceActivatedEvent clusterInstanceActivatedEvent =
                            (ClusterInstanceActivatedEvent) event;
                    logger.info(
                            "ClusterInstanceActivatedEvent event: " + gson.toJson(clusterInstanceActivatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterInstanceActivatedEvent event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterInstanceCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ClusterInstanceCreatedEvent event received");
                    ClusterInstanceCreatedEvent clusterInstanceCreatedEvent =
                            (ClusterInstanceCreatedEvent) event;
                    logger.info(
                            "ClusterInstanceCreatedEvent event: " + gson.toJson(clusterInstanceCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterInstanceCreatedEvent event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterInstanceInactivateEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ClusterInstanceInactivateEvent event received");
                    ClusterInstanceInactivateEvent clusterInstanceInactivateEvent =
                            (ClusterInstanceInactivateEvent) event;
                    logger.info(
                            "ClusterInstanceInactivateEvent event: " + gson.toJson(clusterInstanceInactivateEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterInstanceInactivateEvent event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterInstanceTerminatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ClusterInstanceTerminatedEvent event received");
                    ClusterInstanceTerminatedEvent clusterInstanceTerminatedEvent =
                            (ClusterInstanceTerminatedEvent) event;
                    logger.info(
                            "ClusterInstanceTerminatedEvent event: " + gson.toJson(clusterInstanceTerminatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterInstanceTerminatedEvent event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterInstanceTerminatingEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ClusterInstanceTerminatingEvent event received");
                    ClusterInstanceTerminatingEvent clusterInstanceTerminatingEvent =
                            (ClusterInstanceTerminatingEvent) event;
                    logger.info(
                            "ClusterInstanceTerminatingEvent event: " + gson.toJson(clusterInstanceTerminatingEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterInstanceTerminatingEvent event", e);
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
                    logger.info("Cluster removed event received");
                    ClusterRemovedEvent clusterRemovedEvent = (ClusterRemovedEvent) event;
                    logger.info("Cluster removed event: " + gson.toJson(clusterRemovedEvent));
                } catch (Exception e) {
                    logger.error("Error processing cluster removed event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new ClusterResetEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("ClusterResetEvent event received");
                    ClusterResetEvent clusterResetEvent =
                            (ClusterResetEvent) event;
                    logger.info(
                            "ClusterResetEvent event: " + gson.toJson(clusterResetEvent));
                } catch (Exception e) {
                    logger.error("Error processing ClusterResetEvent event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new CompleteTopologyEventListener() {
            private boolean initialized;

            @Override
            protected void onEvent(Event event) {
                if (!initialized) {
                    try {
                        TopologyManager.acquireReadLock();
                        logger.info("Complete topology event received");
                        CompleteTopologyEvent completeTopologyEvent = (CompleteTopologyEvent) event;
                        logger.info("Complete topology event: " +
                                gson.toJson(completeTopologyEvent.getTopology().getServices(), serviceType));
                        initialized = true;
                    } catch (Exception e) {
                        logger.error("Error processing complete topology event", e);
                    } finally {
                        TopologyManager.releaseReadLock();
                    }
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberActivatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("Member activated event received");
                    MemberActivatedEvent memberActivatedEvent = (MemberActivatedEvent) event;
                    logger.info("Member activated event: " + gson.toJson(memberActivatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing member activated event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberCreatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("MemberCreatedEvent event received");
                    MemberCreatedEvent memberCreatedEvent = (MemberCreatedEvent) event;
                    logger.info("MemberCreatedEvent event: " + gson.toJson(memberCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing MemberCreatedEvent event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberInitializedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("MemberInitializedEvent event received");
                    MemberInitializedEvent memberInitializedEvent = (MemberInitializedEvent) event;
                    logger.info("MemberInitializedEvent event: " + gson.toJson(memberInitializedEvent));
                } catch (Exception e) {
                    logger.error("Error processing MemberInitializedEvent event", e);
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
                    logger.info("Member maintenance event received");
                    MemberMaintenanceModeEvent memberMaintenanceModeEvent = (MemberMaintenanceModeEvent) event;
                    logger.info("Member maintenance event: " + gson.toJson(memberMaintenanceModeEvent));
                } catch (Exception e) {
                    logger.error("Error processing member maintenance event", e);
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
                    logger.info("Member ready to shutdown event received");
                    MemberReadyToShutdownEvent memberReadyToShutdownEvent = (MemberReadyToShutdownEvent) event;
                    logger.info("Member ready to shutdown event: " + gson.toJson(memberReadyToShutdownEvent));
                } catch (Exception e) {
                    logger.error("Error processing member ready to shutdown event", e);
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
                    logger.info("Member started event received");
                    MemberStartedEvent memberStartedEvent = (MemberStartedEvent) event;
                    logger.info("Member started event: " + gson.toJson(memberStartedEvent));
                } catch (Exception e) {
                    logger.error("Error processing member started event", e);
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
                    logger.info("Member suspended event received");
                    MemberSuspendedEvent memberSuspendedEvent = (MemberSuspendedEvent) event;
                    logger.info("Member suspended event: " + gson.toJson(memberSuspendedEvent));
                } catch (Exception e) {
                    logger.error("Error processing member suspended event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        topologyEventReceiver.addEventListener(new MemberTerminatedEventListener() {
            @Override
            protected void onEvent(Event event) {
                try {
                    TopologyManager.acquireReadLock();
                    logger.info("Member terminated event received");
                    MemberTerminatedEvent memberTerminatedEvent = (MemberTerminatedEvent) event;
                    logger.info("Member terminated event: " + gson.toJson(memberTerminatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing member terminated event", e);
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
                    logger.info("Service created event received");
                    ServiceCreatedEvent serviceCreatedEvent = (ServiceCreatedEvent) event;
                    logger.info("Service created event: " + gson.toJson(serviceCreatedEvent));
                } catch (Exception e) {
                    logger.error("Error processing service created event", e);
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
                    logger.info("Service removed event received");
                    ServiceRemovedEvent serviceRemovedEvent = (ServiceRemovedEvent) event;
                    logger.info("Service removed event: " + gson.toJson(serviceRemovedEvent));
                } catch (Exception e) {
                    logger.error("Error processing service removed event", e);
                } finally {
                    TopologyManager.releaseReadLock();
                }
            }
        });

        eventListenerExecutorService.submit(new Runnable() {
            public void run() {
                topologyEventReceiver.execute();
            }
        });
        if (logger.isDebugEnabled()) {
            logger.info("Sample Event Publisher topology receiver thread started");
        }
    }

    public boolean isPublisherEnabled() {
        return isPublisherEnabled;
    }

    public void setIsPublisherEnabled(boolean isPublisherEnabled) {
        this.isPublisherEnabled = isPublisherEnabled;
    }

    public void terminate() {
        topologyEventReceiver.terminate();
        tenantEventReceiver.terminate();
        applicationsEventReceiver.terminate();
        applicationSignUpEventReceiver.terminate();
        clusterStatusEventReceiver.terminate();
        domainMappingEventReceiver.terminate();
        instanceNotifierEventReceiver.terminate();
        healthStatEventReceiver.terminate();
        terminated = true;
    }
}
