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

package org.samples.apache.stratos.event.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.subscribe.TopicSubscriber;
import org.samples.apache.stratos.event.MessageProcessor;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Event Receiver.
 */
public class EventReceiver implements Runnable {
    private static final Log log = LogFactory.getLog(EventReceiver.class);

    private String topicName;
    private MessageListener messageListener;


    public EventReceiver(String topicName) {
        this.topicName = topicName;
        this.messageListener = MessageProcessor.getMessageListerner(topicName);
    }

    @Override
    public void run() {
        TopicSubscriber topicSubscriber = new TopicSubscriber(topicName);

        if (messageListener == null) {
            log.debug("Message Listener is set to default");
            topicSubscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        log.info(String.format("Message received [TOPIC]: %s  [MESSAGE] %s", topicName, ((TextMessage) message).getText()));
                    } catch (JMSException e) {
                        log.error(e);
                    }
                }
            });
        } else {
            log.debug(String.format("Message Listener is set to %s", messageListener.toString()));
            topicSubscriber.setMessageListener(messageListener);
        }


        Thread subscriberThread = new Thread(topicSubscriber);
        subscriberThread.start();
        if (log.isDebugEnabled()) {
            log.debug(String.format("%s topic message receiver thread started", topicName));
        }
    }
}
