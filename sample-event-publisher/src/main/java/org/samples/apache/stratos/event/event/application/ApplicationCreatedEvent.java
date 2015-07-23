/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This event will be fired upon the application created is detected.
 */
@XmlRootElement(name = "ApplicationCreatedEvent")
public class ApplicationCreatedEvent extends ApplicationEvent implements SampleEventInterface {
    private Application application;

    public ApplicationCreatedEvent() {
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.application.Application application1 = new org.apache.stratos.messaging
                .domain.application.Application(application.getId());
        org.apache.stratos.messaging.event.application.ApplicationCreatedEvent applicationCreatedEvent = new org
                .apache.stratos.messaging.event.application.ApplicationCreatedEvent(application1);
        Utils.publishEvent(applicationCreatedEvent);
    }
}
