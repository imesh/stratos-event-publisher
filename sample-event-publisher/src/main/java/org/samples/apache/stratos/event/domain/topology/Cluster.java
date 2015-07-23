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

package org.samples.apache.stratos.event.domain.topology;

import org.samples.apache.stratos.event.domain.instance.ClusterInstance;
import org.samples.apache.stratos.event.util.PropertiesAdaptor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * Defines a cluster of a service.
 * Key: serviceName, clusterId
 */

@XmlType(name = "Cluster")
public class Cluster {

    private String serviceName;
    private String clusterId;
    private String autoscalePolicyName;
    private String deploymentPolicyName;

    private List<String> hostNames;
    private String tenantRange;
    private boolean isLbCluster;

    private boolean isKubernetesCluster;

    private Map<String, Member> memberMap;

    private String appId;
    private String parentId;
    private Map<String, ClusterInstance> instanceIdToInstanceContextMap;
    private List<String> accessUrls;
    private List<KubernetesService> kubernetesServices;

    private String loadBalanceAlgorithmName;

    private Properties properties;

    public Cluster() {

    }

    public Cluster(String serviceName, String clusterId, String deploymentPolicyName, String autoscalePolicyName) {
        this.serviceName = serviceName;
        this.clusterId = clusterId;
        this.deploymentPolicyName = deploymentPolicyName;
        this.autoscalePolicyName = autoscalePolicyName;
        this.hostNames = new ArrayList<String>();
        this.memberMap = new HashMap<String, Member>();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getAutoscalePolicyName() {
        return autoscalePolicyName;
    }

    public void setAutoscalePolicyName(String autoscalePolicyName) {
        this.autoscalePolicyName = autoscalePolicyName;
    }

    public String getDeploymentPolicyName() {
        return deploymentPolicyName;
    }

    public void setDeploymentPolicyName(String deploymentPolicyName) {
        this.deploymentPolicyName = deploymentPolicyName;
    }

    public List<String> getHostNames() {
        return hostNames;
    }

    public void setHostNames(List<String> hostNames) {
        this.hostNames = hostNames;
    }

    public String getTenantRange() {
        return tenantRange;
    }

    public void setTenantRange(String tenantRange) {
        this.tenantRange = tenantRange;
    }

    public boolean isLbCluster() {
        return isLbCluster;
    }

    public void setLbCluster(boolean isLbCluster) {
        this.isLbCluster = isLbCluster;
    }

    public Map<String, Member> getMemberMap() {
        return memberMap;
    }

    public void setMemberMap(Map<String, Member> memberMap) {
        this.memberMap = memberMap;
    }

    public String getLoadBalanceAlgorithmName() {
        return loadBalanceAlgorithmName;
    }

    public void setLoadBalanceAlgorithmName(String loadBalanceAlgorithmName) {
        this.loadBalanceAlgorithmName = loadBalanceAlgorithmName;
    }

    public void setIsLbCluster(boolean isLbCluster) {
        this.isLbCluster = isLbCluster;
    }

    public boolean isKubernetesCluster() {
        return isKubernetesCluster;
    }

    public void setIsKubernetesCluster(boolean isKubernetesCluster) {
        this.isKubernetesCluster = isKubernetesCluster;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Map<String, ClusterInstance> getInstanceIdToInstanceContextMap() {
        return instanceIdToInstanceContextMap;
    }

    public void setInstanceIdToInstanceContextMap(
            Map<String, ClusterInstance> instanceIdToInstanceContextMap) {
        this.instanceIdToInstanceContextMap = instanceIdToInstanceContextMap;
    }

    public List<String> getAccessUrls() {
        return accessUrls;
    }

    public void setAccessUrls(List<String> accessUrls) {
        this.accessUrls = accessUrls;
    }

    public List<KubernetesService> getKubernetesServices() {
        return kubernetesServices;
    }

    public void setKubernetesServices(
            List<KubernetesService> kubernetesServices) {
        this.kubernetesServices = kubernetesServices;
    }

    @XmlElement
    @XmlJavaTypeAdapter(PropertiesAdaptor.class)
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}

