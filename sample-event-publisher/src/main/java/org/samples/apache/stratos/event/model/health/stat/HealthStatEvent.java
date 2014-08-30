package org.samples.apache.stratos.event.model.health.stat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.util.Constants;
import org.samples.apache.stratos.event.model.SampleEventInterface;

/**
 * Created by akila on 8/29/14.
 */
public class HealthStatEvent extends Event implements SampleEventInterface {
    protected static final EventPublisher healthStatPublisher = EventPublisherPool.getPublisher(Constants.HEALTH_STAT_TOPIC);
    private static final Log log = LogFactory.getLog(HealthStatEvent.class);

    @Override
    public void process() {
        healthStatPublisher.publish(this);
        if (log.isInfoEnabled()){
            log.info(this.getClass().getSimpleName() + " Event published: " + this);
        }
    }
}
