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

import org.samples.apache.stratos.event.domain.instance.GroupInstance;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This event is fired by cartridge agent when it has started the server and
 * applications are ready to serve the incoming requests.
 */
@XmlRootElement(name = "GroupInstanceCreatedEvent")
public class GroupInstanceCreatedEvent extends ApplicationEvent implements SampleEventInterface {
    private String groupId;
    private String appId;
    private GroupInstance groupInstance;

    public GroupInstanceCreatedEvent() {
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getAppId() {
        return appId;
    }

    public GroupInstance getGroupInstance() {
        return groupInstance;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setGroupInstance(GroupInstance groupInstance) {
        this.groupInstance = groupInstance;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.instance.GroupInstance groupInstance1 = new org.apache.stratos.messaging
                .domain.instance.GroupInstance(groupInstance.getAlias(), groupInstance.getInstanceId());
        org.apache.stratos.messaging.event.application.GroupInstanceCreatedEvent groupInstanceCreatedEvent = new org
                .apache.stratos.messaging.event.application.GroupInstanceCreatedEvent(appId, groupId, groupInstance1);
        Utils.publishEvent(groupInstanceCreatedEvent);
    }
}
