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

package org.samples.apache.stratos.event.event.topology;

import org.samples.apache.stratos.event.domain.topology.Port;
import org.samples.apache.stratos.event.domain.topology.ServiceType;
import org.samples.apache.stratos.event.event.SampleEventInterface;
import org.samples.apache.stratos.event.util.PropertiesAdaptor;
import org.samples.apache.stratos.event.util.Utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Model class for ServiceCreatedEvent.
 */

@XmlRootElement(name = "ServiceCreatedEvent")
public class ServiceCreatedEvent extends TopologyEvent implements SampleEventInterface {
    private String serviceName;
    private ServiceType serviceType;
    private Map<Integer, Port> portMap;
    private Properties properties;

    public ServiceCreatedEvent() {

    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Collection<Port> getPorts() {
        return Collections.unmodifiableCollection(portMap.values());
    }

    public Port getPort(int proxy) {
        if (portMap.containsKey(proxy)) {
            return portMap.get(proxy);
        }
        return null;
    }

    public void addPort(Port port) {
        this.portMap.put(port.getProxy(), port);
    }

    public void addPorts(Collection<Port> ports) {
        for (Port port : ports) {
            addPort(port);
        }
    }

    public void removePort(Port port) {
        this.portMap.remove(port.getProxy());
    }

    public boolean portExists(Port port) {
        return this.portMap.containsKey(port.getProxy());
    }

    @XmlElement
    @XmlJavaTypeAdapter(PropertiesAdaptor.class)
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Map<Integer, Port> getPortMap() {
        return portMap;
    }

    public void setPortMap(Map<Integer, Port> portMap) {
        this.portMap = portMap;
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.topology.ServiceType
                serviceType1 = org.apache.stratos.messaging.domain.topology.ServiceType.valueOf(serviceType.name());

        org.apache.stratos.messaging.event.topology.ServiceCreatedEvent
                serviceCreatedEvent = new org.apache.stratos.messaging.event.topology.ServiceCreatedEvent(
                serviceName, serviceType1);

        if (portMap != null) {
            for (Map.Entry<Integer, Port> portEntry : portMap.entrySet()) {
                org.apache.stratos.messaging.domain.topology.Port port =
                        new org.apache.stratos.messaging.domain.topology.Port(
                                portEntry.getValue().getProtocol(), portEntry.getValue().getValue(),
                                portEntry.getValue().getValue());
                serviceCreatedEvent.addPort(port);
            }
        }

        if (properties != null) {
            serviceCreatedEvent.setProperties(properties);
        }
        Utils.publishEvent(serviceCreatedEvent);
    }
}
