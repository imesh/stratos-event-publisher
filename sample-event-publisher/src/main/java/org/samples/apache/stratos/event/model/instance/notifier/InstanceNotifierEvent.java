/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
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
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.util.Constants;
import org.samples.apache.stratos.event.model.SampleEventInterface;

/**
 * Instance notifier event
 */
public class InstanceNotifierEvent extends Event implements SampleEventInterface {

    protected static final EventPublisher instanceNotifierPublisher = EventPublisherPool.getPublisher(Constants.INSTANCE_NOTIFIER_TOPIC);
    private static final Log log = LogFactory.getLog(InstanceNotifierEvent.class);

    @Override
    public void process() {
        instanceNotifierPublisher.publish(this);
        if (log.isInfoEnabled()){
            log.info(this.getClass().getSimpleName() + " Event published: " + this);
        }
    }
}
