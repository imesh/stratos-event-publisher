package org.imesh.tools.stratos.event.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.subscribe.EventSubscriber;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.instance.status.InstanceStartedEvent;
import org.apache.stratos.messaging.listener.instance.status.InstanceStartedEventListener;
import org.apache.stratos.messaging.message.receiver.instance.status.*;

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
        EventSubscriber topicSubscriber = new EventSubscriber(topicName, new org.apache.stratos.messaging.broker.subscribe.MessageListener() {
            public void messageReceived(org.apache.stratos.messaging.domain.Message message) {

                    log.info("Message received");
                    log.info(message.getEventClassName());

                       }
        });

        Thread subscriberThread = new Thread(topicSubscriber);
        subscriberThread.start();
        if (log.isDebugEnabled()) {
            log.debug("Topology event message receiver thread started");
        }
    }
}
