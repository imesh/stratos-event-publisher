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

package org.samples.apache.stratos.event.model.topology;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.samples.apache.stratos.event.domain.topology.Port;
import org.samples.apache.stratos.event.domain.topology.ServiceType;
import org.samples.apache.stratos.event.util.PropertiesAdaptor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * Model class for ServiceCreatedEvent.
 */

@XmlRootElement(name = "ServiceCreatedEvent")
public class ServiceCreatedEvent extends TopologyEvent {

    private static final Log log = LogFactory.getLog(ServiceCreatedEvent.class);
    private String serviceName;
    private ServiceType serviceType;
    // Key: Port.proxy
    private Map<Integer, Port> portMap;
    private Properties properties;

    public ServiceCreatedEvent() {

    }

    public ServiceCreatedEvent(String serviceName, ServiceType serviceType) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.portMap = new HashMap<Integer, Port>();
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
    public String toString() {
        return String.format("[service] %s , [service-type] %s , [portMap] %s , [properties] %s",
                serviceName, serviceType, portMap, properties);
    }

    @Override
    public void process() {
        org.apache.stratos.messaging.domain.topology.ServiceType
                serviceType1 = org.apache.stratos.messaging.domain.topology.ServiceType.valueOf(serviceType.name());

        org.apache.stratos.messaging.event.topology.ServiceCreatedEvent
                serviceCreatedEvent = new org.apache.stratos.messaging.event.topology.ServiceCreatedEvent(
                serviceName, serviceType1);

        if (portMap != null)
            for (Map.Entry<Integer, Port> portEntry : portMap.entrySet()) {
                org.apache.stratos.messaging.domain.topology.Port port = new org.apache.stratos.messaging.domain.topology.Port(
                        portEntry.getValue().getProtocol(), portEntry.getValue().getValue(), portEntry.getValue().getValue());
                serviceCreatedEvent.addPort(port);
            }

        if (properties != null)
            serviceCreatedEvent.setProperties(properties);

        topologyPublisher.publish(serviceCreatedEvent);
        if (log.isInfoEnabled()) {
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
