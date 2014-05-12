package org.imesh.samples.apache.stratos.event.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.publish.EventPublisher;
import org.apache.stratos.messaging.broker.publish.EventPublisherPool;
import org.apache.stratos.messaging.domain.tenant.Subscription;
import org.apache.stratos.messaging.domain.tenant.Tenant;
import org.apache.stratos.messaging.domain.topology.*;
import org.apache.stratos.messaging.event.Event;
import org.apache.stratos.messaging.event.tenant.CompleteTenantEvent;
import org.apache.stratos.messaging.event.tenant.SubscriptionDomainAddedEvent;
import org.apache.stratos.messaging.event.tenant.SubscriptionDomainRemovedEvent;
import org.apache.stratos.messaging.event.tenant.TenantCreatedEvent;
import org.apache.stratos.messaging.event.topology.*;
import org.apache.stratos.messaging.message.receiver.tenant.TenantManager;
import org.apache.stratos.messaging.util.Constants;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.*;

/**
 * Event generator.
 */
public class TenantEventGenerator implements Runnable {
    private static final Log log = LogFactory.getLog(TenantEventGenerator.class);
    private static long TIME_INTERVAL = 5000;
    private Topology topology = new Topology();
    private int count;

    public TenantEventGenerator(int count) {
        this.count = count;
    }

    public void run() {

        EventPublisher tenantPublisher = EventPublisherPool.getPublisher(Constants.TENANT_TOPIC);
        List<Tenant> tenants = new ArrayList<Tenant>();
        CompleteTenantEvent event = new CompleteTenantEvent(tenants);
        tenantPublisher.publish(event);

        Subscription subscription = new Subscription("apistore", new HashSet<String>(1));
        Tenant t = new Tenant(12345, "myTenant");
        t.addSubscription(subscription);
        TenantManager.getInstance().addTenant(t);
        tenantPublisher.publish(new TenantCreatedEvent(t));



        for (int i = 0; i < count; i++) {
            try {
                log.info("Generating sample event...");


                SubscriptionDomainAddedEvent subscriptionDomainAddedEvent = new SubscriptionDomainAddedEvent(12345, "apistore",
                        new HashSet<String>(1), "myDomainName", "myAppContext");
                tenantPublisher.publish(subscriptionDomainAddedEvent);

                SubscriptionDomainRemovedEvent subscriptionDomainRemovedEvent = new SubscriptionDomainRemovedEvent(12345, "apistore",
                        new HashSet<String>(1), "myDomainName");
                tenantPublisher.publish(subscriptionDomainRemovedEvent);
                Thread.sleep(TIME_INTERVAL);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private Service generateService(Topology topology, String serviceName) {
        Service service = new Service(serviceName, ServiceType.SingleTenant);
        service.addPort(new Port("http", 8080, 9080));
        topology.addService(service);
        return service;
    }

}