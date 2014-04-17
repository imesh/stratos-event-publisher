package org.imesh.samples.apache.stratos.event.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.subscribe.TopicSubscriber;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: imesh
 * Date: 4/17/14
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventReceiver implements Runnable{
    private static final Log log = LogFactory.getLog(EventReceiver.class);

    private String topicName;

    public EventReceiver(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public void run() {
        TopicSubscriber topicSubscriber = new TopicSubscriber(topicName);
        topicSubscriber.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    log.info("Message received");
                    log.debug("Message content: "+ ((TextMessage)message).getText());
                } catch (JMSException e) {
                    log.error(e);
                }
            }
        });
        Thread subscriberThread = new Thread(topicSubscriber);
        subscriberThread.start();
        if (log.isDebugEnabled()) {
            log.debug("Topology event message receiver thread started");
        }
    }
}
