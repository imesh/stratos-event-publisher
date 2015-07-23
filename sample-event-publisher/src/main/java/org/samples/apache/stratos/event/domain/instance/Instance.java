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

package org.samples.apache.stratos.event.domain.instance;

import org.samples.apache.stratos.event.domain.topology.lifecycle.LifeCycleState;
import org.samples.apache.stratos.event.domain.topology.lifecycle.LifeCycleStateManager;
import org.samples.apache.stratos.event.util.PropertiesAdaptor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Properties;

public abstract class Instance<T extends LifeCycleState> implements Serializable {
    private String alias;
    private String instanceId;
    private Properties instanceProperties;
    private LifeCycleStateManager<T> lifeCycleStateManager;
    private String parentId;
    private String networkPartitionId;
    private String partitionId;

    public Instance() {

    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @XmlElement
    @XmlJavaTypeAdapter(PropertiesAdaptor.class)
    public Properties getInstanceProperties() {
        return instanceProperties;
    }

    public void setInstanceProperties(Properties instanceProperties) {
        this.instanceProperties = instanceProperties;
    }

    public LifeCycleStateManager<T> getLifeCycleStateManager() {
        return lifeCycleStateManager;
    }

    public void setLifeCycleStateManager(
            LifeCycleStateManager<T> lifeCycleStateManager) {
        this.lifeCycleStateManager = lifeCycleStateManager;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getNetworkPartitionId() {
        return networkPartitionId;
    }

    public void setNetworkPartitionId(String networkPartitionId) {
        this.networkPartitionId = networkPartitionId;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }
}

