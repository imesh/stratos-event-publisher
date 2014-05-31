package org.imesh.tools.stratos.event.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.domain.topology.*;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.topology.CompleteTopologyEvent;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.UUID;

/**
 * Topology event generator.
 */
public class TopologyEventGenerator implements Runnable{
    private static final Log log = LogFactory.getLog(TopologyEventGenerator.class);
    private static long TIME_INTERVAL = 10;

    private String topicName;
    private int count;

    public TopologyEventGenerator(String topicName, int count) {
        this.topicName = topicName;
        this.count = count;
    }

    @Override
    public void run() {
        EventPublisher publisher = EventPublisherPool.getPublisher(topicName);

        for(int i = 0; i < count; i++) {
            try {
                log.info("Generating sample topology event...");
                Event event = new CompleteTopologyEvent(TopologyUtil.getTopology());
                log.info("Publishing topology event...");
                publisher.publish(event);
                Thread.sleep(TIME_INTERVAL);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
