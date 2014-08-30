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

package org.samples.apache.stratos.event.model.instance.notifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class for ArtifactUpdatedEvent.
 */

@XmlRootElement(name = "ArtifactUpdatedEvent")
public class ArtifactUpdatedEvent extends InstanceNotifierEvent {
    private String clusterId;
    private String status;
    private String repoUserName;
    private String repoPassword;
    private String repoURL;
    private String tenantId;
    private boolean commitEnabled;

    private static final Log log = LogFactory.getLog(ArtifactUpdatedEvent.class);

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getRepoUserName() {
		return repoUserName;
	}

	public void setRepoUserName(String repoUserName) {
		this.repoUserName = repoUserName;
	}

	public String getRepoPassword() {
		return repoPassword;
	}

	public void setRepoPassword(String repoPassword) {
		this.repoPassword = repoPassword;
	}

	public String getRepoURL() {
		return repoURL;
	}

	public void setRepoURL(String repoURL) {
		this.repoURL = repoURL;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

    @Override
    public String toString() {
        return String.format("[cluster] %s , [repo-url] %s , [repo-username] %s , [tenant] %s",
                clusterId, repoURL, repoUserName, tenantId);
    }

    public boolean isCommitEnabled() {
        return commitEnabled;
    }

    public void setCommitEnabled(boolean commitEnabled) {
        this.commitEnabled = commitEnabled;
    }

    @Override
    public void process(){
        org.apache.stratos.messaging.event.instance.notifier.ArtifactUpdatedEvent
                artifactUpdatedEvent = new org.apache.stratos.messaging.event.instance.notifier.ArtifactUpdatedEvent();
        artifactUpdatedEvent.setClusterId(clusterId);
        artifactUpdatedEvent.setCommitEnabled(commitEnabled);
        artifactUpdatedEvent.setRepoUserName(repoUserName);
        artifactUpdatedEvent.setRepoPassword(repoPassword);
        artifactUpdatedEvent.setRepoURL(repoURL);
        artifactUpdatedEvent.setStatus(status);
        artifactUpdatedEvent.setTenantId(tenantId);

        instanceNotifierPublisher.publish(artifactUpdatedEvent);
        if (log.isInfoEnabled()){
            log.info(this.getClass().toString() + " Event published: " + this);
        }
    }
}
