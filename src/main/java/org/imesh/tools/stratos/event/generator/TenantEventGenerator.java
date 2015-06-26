package org.imesh.tools.stratos.event.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.domain.tenant.Tenant;
import org.apache.stratos.messaging.domain.topology.*;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.tenant.CompleteTenantEvent;
import org.apache.stratos.messaging.event.topology.CompleteTopologyEvent;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.*;

/**
 * Tenant event generator.
 */
public class TenantEventGenerator implements Runnable{
    private static final Log log = LogFactory.getLog(TenantEventGenerator.class);
    private static long TIME_INTERVAL = 10;

    private String topicName;
    private int count;

    public TenantEventGenerator(String topicName, int count) {
        this.topicName = topicName;
        this.count = count;
    }

    @Override
    public void run() {
        EventPublisher publisher = EventPublisherPool.getPublisher(topicName);

        for(int i = 0; i < count; i++) {
            try {
                log.info("Generating sample tenant event...");
                Event event = new CompleteTenantEvent(TenantUtil.getTenants());
                log.info("Publishing tenant event...");
                publisher.publish(event);
                Thread.sleep(TIME_INTERVAL);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}
