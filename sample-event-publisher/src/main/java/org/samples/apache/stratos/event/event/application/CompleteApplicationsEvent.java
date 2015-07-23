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

package org.samples.apache.stratos.event.event.application;


import org.samples.apache.stratos.event.domain.application.Application;
import org.samples.apache.stratos.event.domain.application.Applications;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * This event is fired periodically with the complete topology. It would be a
 * starting point for subscribers to initialize the current state of the topology
 * before receiving other topology events.
 */
@XmlRootElement(name = "CompleteApplicationsEvent")
public class CompleteApplicationsEvent extends ApplicationEvent implements SampleEventInterface {
    private Applications applications;

    public CompleteApplicationsEvent() {
    }

    public Applications getApplications() {
        return applications;
    }

    public void setApplications(Applications applications) {
        this.applications = applications;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.application.Applications applications1 = new org.apache.stratos.messaging
                .domain.application.Applications();
        for (Map.Entry<String, Application> entry : applications.getApplicationMap().entrySet()) {
            org.apache.stratos.messaging.domain.application.Application application = new org.apache.stratos
                    .messaging.domain.application.Application(entry.getValue().getId());
            applications1.addApplication(application);
        }

        org.apache.stratos.messaging.event.application.CompleteApplicationsEvent completeApplicationsEvent = new org
                .apache.stratos.messaging.event.application.CompleteApplicationsEvent(applications1);
        Utils.publishEvent(completeApplicationsEvent);
    }
}
